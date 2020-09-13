package org.marvelousness.springboot.oms.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.marvelousness.springboot.oms.controller.TestController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 简单的 Restful 单元测试
 * 
 * @author 981247127@qq.com
 * @time 2020-09-12 08:42
 */
@SpringBootTest
public class TestRestFulStandaloneCase {

	@Test
	public void testController() throws Exception {
		MockMvc mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();

		MvcResult result = mvc.perform(get("/test").param("name", "Timor")).andReturn();
		String content = result.getResponse().getContentAsString();

		System.out.println("===================================");
		System.out.println(content);
		System.out.println("===================================");
	}
}
