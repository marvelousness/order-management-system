package org.marvelousness.springboot.oms.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 完整的 Restful 单元测试
 * 
 * @author 981247127@qq.com
 * @time 2020-09-12 08:42
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration("src/main/resources")
public class TestRestFulCase {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testControllerRedis() throws Exception {
		final int limit = 1000;
		int i = 0;
		int total = 0;
		while (i < limit) {
			String uri = "/test/redis/TEST:" + StringUtils.randomAlphanumeric(10);
			String value = StringUtils.randomNumeric(125);
			MvcResult result = mvc.perform(get(uri).param("value", value)).andReturn();
			MockHttpServletResponse response = result.getResponse();
			String content = response != null ? response.getContentAsString() : "";
			if (StringUtils.isNotBlank(content)) {
				System.out.println("调用接口成功！响应结果是：" + content);
				total++;
			}
		}
		System.out.println("成功【" + total + "】次，失败【" + (limit - total) + "】次");
	}
}
