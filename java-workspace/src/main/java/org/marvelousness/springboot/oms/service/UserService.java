package org.marvelousness.springboot.oms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.marvelousness.springboot.basic.exception.ServiceInvokeException;
import org.marvelousness.springboot.basic.session.SessionManager;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.AutoCompleteDto;
import org.marvelousness.springboot.oms.entity.dto.UserDto;
import org.marvelousness.springboot.oms.entity.pojo.User;
import org.marvelousness.springboot.oms.mapper.UserCustomerRelationMapper;
import org.marvelousness.springboot.oms.mapper.UserDepartmentRelationMapper;
import org.marvelousness.springboot.oms.mapper.UserDtoMapper;
import org.marvelousness.springboot.oms.mapper.UserMapper;
import org.marvelousness.springboot.oms.mapper.UserOrderRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户信息业务层
 * 
 * @author 981247127@qq.com
 */
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserDepartmentRelationMapper relationMapper;

	@Autowired
	private UserDtoMapper dtoMapper;

	@Autowired
	private UserCustomerRelationMapper customerRelationMapper;

	@Autowired
	private UserOrderRelationMapper orderRelationMapper;

	@Autowired
	private SessionManager manager;

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 用户登录
	 * 
	 * @param authenticator 登录身份，可以是登录账号，手机号，邮箱
	 * @param certification 登录凭据，也就是登录密码
	 * @param certifycode   登录验证码
	 */
	public void login(String authenticator, String certification, String certifycode) {

		// 1. 验证传递的验证码

		// 2. 验证传递的登录身份
		boolean isMobilePhone = StringUtils.isMobilePhone(authenticator);
		boolean isEmail = StringUtils.isEmail(authenticator);
		boolean isVariableName = StringUtils.isVariableName(authenticator);
		// 首先尝试通过手机号码的方式查询用户信息是否存在
		boolean exists = isMobilePhone && Boolean.TRUE.equals(userMapper.existsByMobile(authenticator));
		// 再次尝试通过电子邮箱的方式查询用户信息是否存在
		exists = !exists && isEmail && Boolean.TRUE.equals(userMapper.existsByEmail(authenticator));
		// 最后尝试通过用户名的方式查询用户信息是否存在
		// 这里认为，符合变量命名法的登录身份字符串为用户名的必要格式
		exists = !exists && isVariableName && Boolean.TRUE.equals(userMapper.existsByUserid(authenticator));

		// 断言用户信息存在

		// Assert.isTrue(exists, "用户不存在");
		if (!exists) {
			throw new ServiceInvokeException("用户不存在");
		}

		// 3. 验证传递的登录凭据
		User user = null;
		if (isMobilePhone) {
			user = userMapper.selectByMobile(authenticator, certification);
		} else if (isEmail) {
			user = userMapper.selectByEmail(authenticator, certification);
		} else if (isVariableName) {
			user = userMapper.selectByUserid(authenticator, certification);
		}

		// 断言用户信息存在

		// Assert.isTrue(user != null, "登录密码错误");
		if (user == null || user.getId() == null) {
			throw new ServiceInvokeException("登录密码错误");
		}

		if (!user.isEnabled()) {
			throw new ServiceInvokeException("用户【" + authenticator + "】已经停用，不允许登录");
		}

		// 4. 存储会话信息
		manager.setHttpSession(user);
	}

	/**
	 * 查询列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public ResponsePageEntity<UserDto> list(Integer number, Integer size) {
		int limit = size != null && size > 0 ? size : 10;
		int offset = number != null && number > 0 ? (number - 1) * limit : 0;
		List<UserDto> dtos = dtoMapper.select(offset, limit);
		Integer total = dtoMapper.count();
		return new ResponsePageEntity<UserDto>(dtos, total, number, size);
	}

	/**
	 * 为前端的 AutoComplete 提供数据支持
	 * 
	 * @return
	 */
	public List<AutoCompleteDto> getAutoCompleteOptions() {
		List<AutoCompleteDto> dtos = new ArrayList<AutoCompleteDto>();
		Integer count = dtoMapper.count();
		if (count == null || count < 1) {
			return dtos;
		}
		List<UserDto> users = dtoMapper.select(0, count);
		if (users != null) {
			for (UserDto user : users) {
				if (user != null && user.getId() != null && user.getDepartmentId() != null) {
					String value = user.getDepartmentId().toString();
					String title = user.getDepartmentName();
					if (value == null) {
						continue;
					}

					AutoCompleteDto dto = null;
					for (AutoCompleteDto _dto : dtos) {
						if (_dto != null && value.equals(_dto.getValue())) {
							dto = _dto;
							break;
						}
					}
					if (dto == null) {
						dto = new AutoCompleteDto();
						dto.setValue(value);
						dto.setTitle(title);
					}
					List<AutoCompleteDto> children = dto.getChildren();
					if (children == null) {
						children = new ArrayList<AutoCompleteDto>();
					}

					{
						AutoCompleteDto _dto = new AutoCompleteDto();
						_dto.setValue(user.getId().toString());
						_dto.setTitle(user.getName());
						children.add(_dto);
					}

					dto.setChildren(children);
					
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}

	/**
	 * 新增或修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public Long save(UserDto user) {
		if (user == null) {
			return 0L;
		}
		boolean inserted = user.getId() != null && user.getId() > 0;
		Integer affect = null;

		if (!departmentService.exists(user.getDepartmentId())) {
			throw new ServiceInvokeException("请选择该用户所属的部门");
		}

		if (inserted) {
			// 已经插入过，现在需要更新数据
			affect = userMapper.update(user);
		} else {
			// 没有插入过，现在需要插入数据

			// 1. 检测字段
			if (userMapper.existsByMobileWithoutDeleted(user.getMobile())) {
				if (userMapper.existsByMobile(user.getMobile())) {
					throw new ServiceInvokeException("该手机号码已经存在，不允许重复创建账号");
				} else {
					throw new ServiceInvokeException("该手机号码对应的账号已经被标记为删除，不允许创建重复的数据");
				}
			} else if (userMapper.existsByEmailWithoutDeleted(user.getEmail())) {
				if (userMapper.existsByMobile(user.getMobile())) {
					throw new ServiceInvokeException("该电子邮箱已经存在，不允许重复创建账号");
				} else {
					throw new ServiceInvokeException("该电子邮箱对应的账号已经被标记为删除，不允许创建重复的数据");
				}
			}

			// 2. 初始化部分字段的默认值
			// 新增用户的初始密码的均为 888888
			// 密码的处理是使用 MD5 加密两次，在新建账号的时候，由这里加密两次。
			// 其他地方，例如登录，修改密码等，全部都是前后端各加密一次
			user.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex("888888")));
			// 设置默认的头像
			if (user.getAvatar() == null) {
				user.setAvatar("default");
			}
			// 自动生成工号
			{
				// 工号的生产规则：
				// 1. 公司所在地域组为工号前缀，这里统一视为武汉地区，及前缀同意为 WH
				// 2. 地域编号后，以两位数的年份和两位数的月份为中间位
				// 3. 在工号的最后四位是员工在本月的数据创建的顺序，即，该数据属于本月的第 124 条，则最后四位是：0124
				Integer count = userMapper.countOfThisMonth();
				count = (count == null ? 0 : count) + 1;
				String jobnumber = DateFormatUtils.formatUTC(new Date(), "yyMM");
				if (count < 10) {
					jobnumber += "000" + count;
				} else if (count < 100) {
					jobnumber += "00" + count;
				} else if (count < 1000) {
					jobnumber += "0" + count;
				} else {
					jobnumber += count;
				}
				user.setUserid("WH" + jobnumber);
			}

			affect = userMapper.insert(user);

		}

		// 保存用户和部门的关系
		Long userId = user.getId();
		Long departmentId = user.getDepartmentId();
		boolean isLeader = Boolean.TRUE.equals(user.getIsLeader());
		if (isLeader) {
			// 该用户是部门领导
			Long leader = relationMapper.getLeader(departmentId);
			if (leader != null && !leader.equals(userId)) {
				throw new ServiceInvokeException("保存信息失败，原因是该部门已经存在领导，该用户不能作为该部门的领导！");
			}
		}

		// 删除该用户与部门的关系
		relationMapper.delete(userId);
		// 重新建立新的关系
		relationMapper.insert(departmentId, userId, isLeader);

		return affect != null && affect > 0 ? userId : null;
	}

	/**
	 * 修改账号的密码
	 * 
	 * @param id
	 * @param passowrd
	 * @return
	 */
	public int passwd(Long id, String passowrd) {
		return userMapper.updatePassword(id, passowrd);
	}

	/**
	 * 修改用户状态
	 * 
	 * @param id         用户的ID
	 * @param isDisabled 是否是禁用账号
	 * @return
	 */
	public Integer changeUserStatus(Long id, boolean isDisabled) {
		if (isDisabled) {
			// 禁用账户之前先查询是否存在订单或者客户信息
			if (Boolean.TRUE.equals(customerRelationMapper.exists(id))) {
				throw new ServiceInvokeException("该账户存在客户信息，不允许被停用");
			}
			if (Boolean.TRUE.equals(orderRelationMapper.exists(id))) {
				throw new ServiceInvokeException("该账户存在订单信息，不允许被停用");
			}
			return userMapper.disabled(id);
		}
		return userMapper.enable(id);
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	public Integer delete(Long id) {
		User user = userMapper.selectByPrimaryKey(id);
		if (user == null) {
			return 0;
		}
		if (user.isEnabled()) {
			throw new ServiceInvokeException("账户正在被使用时，不允许删除");
		}
		return userMapper.delete(id);
	}
}