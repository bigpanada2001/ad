package com.freeco.service.impl;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import com.freeco.dao.Test1Dao;
import com.freeco.dao.TestDao;
import com.freeco.dao.impl.Test1DaoImpl;
import com.freeco.service.TestService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "test")
public class TestServiceImpl implements TestService {
	@Resource
	RedisTemplate cacheRedisTemplate;
	
	@Resource
	TestDao testDaoImpl;
	@Resource
	Test1Dao test1DaoImpl;
	
	@Override
	@CachePut(value="test",key = "'cache' + #key")
	public String setCache(String key, String value) {
//		cacheRedisTemplate.opsForValue().set(key, value);
		return value;
	}

	@Cacheable(cacheNames = "test",key = "'cache' + #key")
	@Override
	public String getCache(String key) {
		int random = new Random().nextInt(10);
		System.out.println("-----------getCache="+key);
		return key + random;
	}
	
	@Override
	public List getUsers() {
		return testDaoImpl.getUsers();
	}
	
	@Override
	public void testTrans() throws Exception {
		test1DaoImpl.updateTest();
	}

}
