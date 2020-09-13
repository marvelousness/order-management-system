package org.marvelousness.springboot.oms.service;

import java.util.List;

import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.CustomerDto;
import org.marvelousness.springboot.oms.entity.pojo.Customer;
import org.marvelousness.springboot.oms.mapper.CustomerDtoMapper;
import org.marvelousness.springboot.oms.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 23:03
 */
@Service
public class CustomerService {
	@Autowired
	private CustomerMapper mapper;
	@Autowired
	private CustomerDtoMapper dtoMapper;

	/**
	 * 保存客户信息
	 * 
	 * @param customer
	 * @return
	 */
	public Long save(Customer customer) {
		if (customer == null) {
			return 0L;
		}
		Integer affect = 0;
		if (Boolean.TRUE.equals(mapper.exists(customer.getId()))) {
			affect = mapper.update(customer);
		} else {
			affect = mapper.insert(customer);
		}
		return affect != null && affect > 0 ? customer.getId() : 0L;
	}

	/**
	 * @param id
	 * @return
	 */
	public Integer delete(Long id) {
		return mapper.delete(id);
	}

	/**
	 * 分页查询客户列表数据
	 * 
	 * @param number
	 * @param size
	 * @return
	 */
	public ResponsePageEntity<CustomerDto> list(Integer number, Integer size) {
		int limit = size != null && size > 0 ? size : 10;
		int offset = number != null && number > 0 ? (number - 1) * limit : 0;
		List<CustomerDto> dtos = dtoMapper.select(offset, limit);
		Long total = dtoMapper.count();
		return new ResponsePageEntity<CustomerDto>(dtos, total, number, size);
	}

	/**
	 * 根据主键获取客户信息
	 * 
	 * @param id
	 * @return
	 */
	public CustomerDto getCustomer(Long id) {
		return dtoMapper.selectByPrimaryKey(id);
	}
}