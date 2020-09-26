package org.marvelousness.springboot.oms.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.marvelousness.springboot.oms.entity.dto.UserDto;
import org.marvelousness.springboot.oms.entity.pojo.Customer;
import org.marvelousness.springboot.oms.entity.pojo.Order;
import org.marvelousness.springboot.oms.entity.pojo.OrderPayment;
import org.marvelousness.springboot.oms.mapper.AreaMapper;
import org.marvelousness.springboot.oms.mapper.UserDtoMapper;
import org.marvelousness.springboot.oms.test.mapper.MySQLMapper;
import org.marvelousness.springboot.oms.test.mybatis.MyBatisBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 因为本项目中没有大量的数据来支撑首页的统计图，这里通过MyBatis来批量模拟数据
 * 
 * @author 981247127@qq.com
 * @time 2020-09-12 08:42
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration("src/main/resources")
public class TestMyBatisMockData {
	private final static Random random = new Random();
	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDtoMapper userMapper;
	@Autowired
	private AreaMapper areaMapper;

	/**
	 * 测试查询数据库
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectDatabase() throws IOException {
		SqlSessionFactory factory = new MyBatisBuilder().getSessionFactory(dataSource);
		SqlSession session = factory.openSession();
		MySQLMapper mapper = session.getMapper(MySQLMapper.class);
		log.info(mapper.database());
	}

	/**
	 * Mock data
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	public void testMockData() throws ParseException, IOException, SQLException {
		// Mock 的思路
		// 1. Mock 的时间区间：[2019-10-1 00:00:00, 2020-10-1 00:00:00]
		// 2. Mock 的用户区间：数据库中能查询到的除了管理员的用户
		// 3. Mock 的金额区间：随机，不限

		SqlSessionFactory factory = new MyBatisBuilder().getSessionFactory(dataSource);
		SqlSession session = factory.openSession(false);
		Connection connection = session.getConnection();
		connection.setAutoCommit(false);
		MySQLMapper mapper = session.getMapper(MySQLMapper.class);

		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.set(2019, 9, 1, 0, 0, 0);
		endCalendar.set(2020, 9, 1, 0, 0, 0);

		List<UserDto> users = userMapper.select(0, 10);
		List<String> areas = areaMapper.selectSecondAreaNames();
		List<String> ways = Arrays.asList("信用卡", "储蓄卡", "支付宝", "QQ支付", "微信支付", "苹果支付", "现金支付");
		int userTotal = users.size();
		int areaTotal = areas.size();
		int wayTotal = ways.size();
		int ctotal = 0;
		int ototal = 0;
		while (!DateUtils.isSameDay(startCalendar, endCalendar)) {
			// 创造当前时间
			String today = DateFormatUtils.format(startCalendar, "yyyy-MM-dd");

			// 随机获取一个用户
			UserDto user = null;
			while (user == null) {
				user = users.get(random.nextInt(userTotal - 1) + 1);
				if (user != null && user.getId() != null && user.getId().equals(1L)) {
					// 不需要 root 账户的参与
					user = null;
				}
			}

			int customerTotal = random.nextInt(51) + 5;
			int customerInsertedTotal = 0;
			int orderInsertedTotal = 0;
			while (customerInsertedTotal < customerTotal) {
				Customer customer = new Customer();
				String phone = "1" + StringUtils.randomNumeric(10);
				customer.setCreateTime(getNow(today));
				customer.setCreatorId(user.getId());
				customer.setName(StringUtils.randomAlphabetic(5));
				customer.setNick(StringUtils.randomAlphanumeric(8));
				customer.setWangwang(StringUtils.randomAlphanumeric(8));
				customer.setQq(phone);
				customer.setWechat(phone);
				customer.setPhone(phone);
				customer.setRegion(areas.get(random.nextInt(areaTotal)));
				customer.setRemark("MockData");
				try {
					mapper.insertCustomer(customer);
				} catch (Exception e) {
					e.printStackTrace();
					connection.rollback();
					break;
				}
				if (customer.getId() == null) {
					connection.rollback();
					break;
				}
				// 客户数据计数
				customerInsertedTotal++;

				// 创建订单数据
				int orderTotal = random.nextInt(30);
				int orderInserted = 0;
				while (orderInserted < orderTotal) {
					Order order = new Order();
					Date now = getNow(today);
					String number = mapper.mockNextOrderNumber(DateFormatUtils.format(now, "yyyyMMddHHmmss"));
					order.setNumber(number);
					order.setCustomerId(customer.getId());
					order.setCreatorId(user.getId());
					order.setExecutorId(user.getId());
					order.setAmount(new BigDecimal(StringUtils.randomNumeric(6)));
					order.setDealTime(now);
					order.setCreateTime(now);
					order.setRemark("MockData");

					try {
						mapper.insertOrder(order);
					} catch (Exception e) {
						e.printStackTrace();
						connection.rollback();
						break;
					}
					if (order.getId() == null) {
						connection.rollback();
						break;
					}

					BigDecimal orderAmount = order.getAmount();
					BigDecimal paidAmount = BigDecimal.ZERO;
					int paymentTotal = random.nextInt(5);
					List<OrderPayment> payments = new ArrayList<OrderPayment>();
					while (paidAmount.compareTo(orderAmount) < 0) {
						BigDecimal amount = null;
						do {
							amount = new BigDecimal(StringUtils.randomNumeric(3) + "." + StringUtils.randomNumeric(4));
						} while (paidAmount.add(amount).compareTo(orderAmount) > 0);
						paidAmount = paidAmount.add(amount);

						OrderPayment payment = new OrderPayment();
						payment.setOrderId(order.getId());
						payment.setAgentId(user.getId());
						payment.setAmount(amount);
						payment.setCreateTime(order.getCreateTime());
						payment.setPaymentTime(order.getDealTime());
						payment.setPaymentAccount(StringUtils.randomNumeric(16));
						payment.setPaymentProof("/statics/images/favicon.png");
						payment.setPaymentWay(ways.get(random.nextInt(wayTotal)));
						payments.add(payment);
						if (payments.size() > paymentTotal) {
							break;
						}
					}
					if (payments.size() > 0) {
						try {
							mapper.insertOrderPayments(payments);
						} catch (Exception e) {
							e.printStackTrace();
							connection.rollback();
							break;
						}
					}
					orderInserted++;
				}
				orderInsertedTotal += orderInserted;
			}
			log.info("mock data at " + today + ", customer total is " + customerInsertedTotal + ", order total is " + orderInsertedTotal);
			ctotal += customerInsertedTotal;
			ototal += orderInsertedTotal;
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		connection.commit();

		log.info("mock finish! customer total is " + ctotal + ", order total is " + ototal);
	}

	/**
	 * 根据 today 创造当前时间
	 * 
	 * @param today
	 * @return
	 * @throws ParseException
	 */
	Date getNow(String today) throws ParseException {
		int hour = random.nextInt(24);
		int minute = random.nextInt(60);
		int second = random.nextInt(60);
		return DateUtils.parseDate(today + " " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second), "yyyy-MM-dd HH:mm:ss");
	}

}
