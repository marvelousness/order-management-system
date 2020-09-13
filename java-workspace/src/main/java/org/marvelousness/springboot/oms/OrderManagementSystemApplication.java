package org.marvelousness.springboot.oms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author 981247127@qq.com
 */
@SpringBootApplication
@ComponentScan("org.marvelousness.springboot")
@MapperScan("org.marvelousness.springboot.oms.mapper")
public class OrderManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementSystemApplication.class, args);
	}

}