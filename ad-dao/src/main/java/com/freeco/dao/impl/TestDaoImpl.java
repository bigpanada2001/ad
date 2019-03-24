package com.freeco.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import com.freeco.dao.TestDao;
import com.freeco.jdbc.Jdbc;
import com.freeco.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TestDaoImpl implements TestDao {

	@Resource
	@Qualifier(value="jdbcMysqlImpl")
	Jdbc jdbc;
	
	@Override
	public List getUsers() {
		List<User> rst = jdbc.queryForList("select * from user", User.class);
		return rst;
	}
	
	@Override
	@Transactional	//(propagation = Propagation.REQUIRES_NEW)
	public int updateUser() throws Exception {
		int rst =jdbc.update("update user set username='xy---' where id=1");
		return rst;
	}
	@Override
	@Transactional	//(propagation = Propagation.REQUIRES_NEW)
	public int updateRole() throws Exception {
		int rst =jdbc.update("update role set role_name='超级---' where id=1");
		if(1 == 1) {
			throw new Exception();
		}
		return rst;

	}
	
}
