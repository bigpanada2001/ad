package com.freeco.service.impl;

import java.util.List;
import javax.annotation.Resource;

import com.freeco.dao.ShardingUserDao;
import com.freeco.service.ShardingUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ShardingUserServiceImpl implements ShardingUserService {
	@Resource
	@Qualifier(value="shardingUserDaoImpl")
	ShardingUserDao shardingUserDaoImpl;
	
	
	@Override
	public List getUsers() {
		return shardingUserDaoImpl.getUsers();
	}

}
