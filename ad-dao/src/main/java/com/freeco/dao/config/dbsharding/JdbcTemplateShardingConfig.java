package com.freeco.dao.config.dbsharding;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 分表分库分片db
 */
@Configuration
public class JdbcTemplateShardingConfig {

	@Resource
	DataSource dataSource;

	@Bean(name="shardingJdbcTemplate")
	public JdbcTemplate shardingJdbcTemplate (
//	    @Qualifier("dataSource")
		) {

	    return new JdbcTemplate(dataSource);
	}
}

