package com.freeco.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.freeco.model.common.Page;
import org.apache.commons.logging.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public interface Jdbc {


	DataSource getDataSource();

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	@Deprecated
	JdbcTemplate getJdbcTemplate();


	String printSQL(Log logger, String sql, StatementParameter param);

	String getSQL(String sql, StatementParameter param);

	boolean exist(String sql);

	boolean exist(String sql, StatementParameter param);

	int[] batchUpdate(String sql, BatchPreparedStatementSetter setter);

	<T> T query(String sql, Class<T> elementType);

	<T> T query(String sql, Class<T> elementType, StatementParameter param);

	<T> T query(String sql, Class<T> elementType, Object... params);

	List<Map<String, Object>> queryForMaps(String sql);

	<T> List<T> queryForList(String sql, Class<T> elementType);

	<T> List<T> queryForList(String sql, Class<T> elementType, int start, int size);

	<T> List<T> queryForList(String sql, Class<T> elementType, Object... params);

	<T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param);

	<T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param, int start, int size);

	List<Long> queryForLongs(String sql, StatementParameter param);

	List<Long> queryForLongs(String sql, StatementParameter param, int start, int size);

	List<Integer> queryForInts(String sql, StatementParameter param);

	List<Integer> queryForInts(String sql, StatementParameter param, int start, int size);

	List<String> queryForStrings(String sql);

	List<String> queryForStrings(String sql, int start, int size);

	List<String> queryForStrings(String sql, StatementParameter param);

	List<String> queryForStrings(String sql, StatementParameter param, int start, int size);

	Long queryForLong(String sql);

	Long queryForLong(String sql, StatementParameter param);

	Long queryForLong(String sql, Object... params);

	Integer queryForInt(String sql);
	Integer queryForInt(String sql, StatementParameter param);
	Integer queryForInt(String sql, Object... params);
	java.util.Date queryForDate(String sql);
	java.util.Date queryForDate(String sql, StatementParameter param);
	String queryForString(String sql);
	String queryForString(String sql, StatementParameter param);

	boolean insertIgnoreForBoolean(String sql, StatementParameter param);
	boolean insertForBoolean(String sql, StatementParameter param);

	boolean insertForBoolean(String sql, Object... params);

	/**
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	Long incr(String sql, StatementParameter param);

	boolean updateForBoolean(String sql, Object... params);
	boolean updateForBoolean(String sql, StatementParameter param);
	int update(String sql, StatementParameter param);
	int update(String sql);
	long insertForLastId(String sql, StatementParameter param);
	int[] batchUpdate(String[] sqls);

//	boolean insertByBean(String sql, Object bean);
//
//	boolean updateByBean(String sql, Object bean);

	<T> Page<T> queryForPage(String sql, Class<T> elementType);

	<T> Page<T> queryForPage(String sql, Class<T> elementType, int start, int size);

	<T> Page<T> queryForPage(String sql, Class<T> elementType, Object... params);

	<T> Page<T> queryForPage(String sql, Class<T> elementType, StatementParameter param);

	<T> Page<T> queryForPage(String sql, Class<T> elementType, StatementParameter param, int start, int size);
}
