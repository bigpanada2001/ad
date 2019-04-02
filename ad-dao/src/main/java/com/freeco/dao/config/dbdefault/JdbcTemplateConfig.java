package com.freeco.dao.config.dbdefault;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 默认db
 */
@Configuration
public class JdbcTemplateConfig {

	@Bean(name="defaultJdbcTemplate")
	public JdbcTemplate defaultJdbcTemplate (
	    @Qualifier("defaultDataSource")DataSource dataSource ) {
	
	    return new JdbcTemplate(dataSource);
	}
}

