package org.marvelousness.springboot.oms.controller;

import static org.marvelousness.springboot.basic.enumer.AuthorizationType.Anon;
import static org.marvelousness.springboot.basic.enumer.AuthorizationType.Authc;

import org.marvelousness.springboot.basic.annotation.Authorization;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.marvelousness.springboot.oms.bean.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;;

/**
 * 测试控制器
 *
 * @author 981247127@qq.com
 * @time 2020-08-13 18:06
 */
@Slf4j
@RestController
@RequestMapping("test")
@Authorization(Anon)
public class TestController {
	@Autowired
	private RedisClient redis;

	/**
	 * 匿名可访问
	 * 
	 * @description 测试 —— 当函数上没有 @Authorization 的时候，根据类上的注解来判断是否能够被允许访问
	 * @param name
	 * @return
	 */
	@GetMapping()
	public String index(@RequestParam(required = false) String name) {
		return StringUtils.isBlank(name) ? "Hello World!" : "Hi " + name;
	}

	/**
	 * 授权可访问
	 * 
	 * @description 测试 —— 当函数上有 @Authorization 的时候，根据注解来判断是否能够被允许访问
	 * @param key   用于读取 redis 的 key
	 * @param value 用于存储 redis 的值
	 * @return
	 */
	@Authorization(Authc)
	@GetMapping("redis/{key}")
	public String redis(@PathVariable("key") String key, Long seconds, String value) {
		log.info("key = " + key + ", value = " + value);
		seconds = seconds == null || seconds < 1 || seconds > 60 ? 10 : seconds;
		redis.set(key, value, seconds);
		return redis.get(key, String.class);
	}
}