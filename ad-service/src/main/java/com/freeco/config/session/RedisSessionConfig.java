package com.freeco.config.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 开启redis分布式session
 * @author Administrator
 *
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
}
