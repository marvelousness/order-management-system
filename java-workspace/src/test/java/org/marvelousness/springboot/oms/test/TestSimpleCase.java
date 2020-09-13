package org.marvelousness.springboot.oms.test;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 简单的单元测试
 * 
 * @author 981247127@qq.com
 */
@RunWith(JUnit4.class)
public class TestSimpleCase {
	@Test
	public void testString() {
		System.out.println("Hello Test Case!");
	}

	/**
	 * 测试生产工号
	 */
	@Test
	public void testGenerateJobNumber() {
		int count = 1506;
		String utc = DateFormatUtils.formatUTC(new Date(), "yyyyMMdd");
		if (count < 10) {
			utc += "000" + count;
		} else if (count < 100) {
			utc += "00" + count;
		} else if (count < 1000) {
			utc += "0" + count;
		} else {
			utc += count;
		}
		System.out.println(utc);
	}

}