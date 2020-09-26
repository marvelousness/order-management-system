package org.marvelousness.springboot.oms.test.mybatis;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-26 09:39
 */
public class MyBatisBuilder {
	
	public SqlSessionFactory getSessionFactory(DataSource dataSource) throws IOException {
		return getSessionFactory(dataSource, null);
	}
	public SqlSessionFactory getSessionFactory(DataSource dataSource, TransactionFactory transactionFactory) throws IOException {
		XMLConfigBuilder builder = new XMLConfigBuilder(Resources.getResourceAsStream("mybatis-config.xml"), null, null);
		Configuration configuration = builder.getConfiguration();
		if (transactionFactory == null) {
			transactionFactory = new SpringManagedTransactionFactory();
		}
		if (configuration != null) {
			builder.parse();
			configuration.setEnvironment(new Environment(SqlSessionFactoryBean.class.getSimpleName(), transactionFactory, dataSource));
		}
		return new SqlSessionFactoryBuilder().build(configuration);
	}
}