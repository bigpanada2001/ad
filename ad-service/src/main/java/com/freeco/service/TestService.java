package com.freeco.service;

import java.util.List;

public interface TestService {
	public String setCache(String key, String value);
	public String getCache(String key);
	
	public List getUsers();
	
	public void testTrans() throws Exception;
}
