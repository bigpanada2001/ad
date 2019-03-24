package com.freeco.config;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class MyBatisConfig {

	@Resource
	@Qualifier(value="defaultDataSource")
	private DataSource defaultDataSource;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactory(
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(defaultDataSource);

		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLogImpl(org.apache.ibatis.logging.log4j.Log4jImpl.class);//use log4j log
		sessionFactory.setConfiguration(configuration);
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
//
//		Properties prop = new Properties();
//		prop.setProperty("supportMethodsArguments","true");
//		prop.setProperty("rowBoundsWithCount", "true");
//		prop.setProperty("params","pageNum=pageNum;pageSize=pageSize;");
//		PageInterceptor pi = new PageInterceptor();
//		pi.setProperties(prop);
//		sessionFactory.setPlugins(new Interceptor[]{pi});

		return sessionFactory;
	}

}
