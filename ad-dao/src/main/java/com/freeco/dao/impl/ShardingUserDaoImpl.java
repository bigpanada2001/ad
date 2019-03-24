package com.freeco.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import com.freeco.dao.ShardingUserDao;
import com.freeco.dao.TestDao;
import com.freeco.jdbc.Jdbc;
import com.freeco.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ShardingUserDaoImpl implements ShardingUserDao {

	@Resource
	@Qualifier(value="jdbcShardingMysqlImpl")
	Jdbc jdbc;
	
	@Override
	public List getUsers() {
		List<User> rst = jdbc.queryForList("select * from user", User.class);
		return rst;
	}
	
}
