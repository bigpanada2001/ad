package com.freeco.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.freeco.model.common.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcShardingMysqlImpl extends JdbcDaoSupport implements Jdbc {
	 protected Log logger = LogFactory.getLog(this.getClass());

	private static boolean log = false;
	
	@Resource
	@Qualifier(value="defaultJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
	public static void setLog(boolean log) {
		JdbcShardingMysqlImpl.log = log;
	}

	// @Override
	// /**
	// * @see com.duowan.leopard.data.jdbc.Jdbc#setAutoCommit(boolean)
	// */
	// public boolean setAutoCommit(boolean autoCommit) {
	// try {
	//
	// Connection con = super.getConnection();
	// if (con == null) {
	// return false;
	// }
	// System.out.println("con:" + con + " " + this.getDataSource().getConnection());
	// con.setAutoCommit(autoCommit);
	// return true;
	// }
	// catch (CannotGetJdbcConnectionException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// catch (SQLException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// }
	//
	// @Override
	// public boolean rollback() {
	// try {
	// Connection con = super.getConnection();
	// if (con == null) {
	// return false;
	// }
	// System.out.println("con:" + con + " " + this.getDataSource().getConnection());
	// con.rollback();
	// return true;
	// }
	// catch (CannotGetJdbcConnectionException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// catch (SQLException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// }
	//
	// @Override
	// public boolean commit() {
	// try {
	// Connection con = DataSourceUtils.getConnection(getDataSource());
	// // Connection con = super.getConnection();
	// if (con == null) {
	// return false;
	// }
	// con.commit();
	// return true;
	// }
	// catch (CannotGetJdbcConnectionException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// catch (SQLException e) {
	// logger.error(e.getMessage(), e);
	// return false;
	// }
	// }

	@Override
	public String printSQL(Log logger, String sql, StatementParameter param) {
		String sql1 = this.getSQL(sql, param);
		logger.info(sql1);
		return sql1;
	}

	// protected void printStackTrace(String sql, StatementParameter param, int updatedCount) {
	// String str1 = this.getSQL(sql, param);
	// logger.info("sql:" + str1);
	// logger.info("updatedCount:" + updatedCount);
	// Exception e = new Exception();
	// logger.info(e.getMessage(), e);
	// }

	@Override
	public String getSQL(String sql, StatementParameter param) {
		return SqlUtil.getSQL(sql, param);
	}

	@Override
	public int[] batchUpdate(String[] sqls) {
		return this.getJdbcTemplate().batchUpdate(sqls);
	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
		return this.getJdbcTemplate().batchUpdate(sql, setter);
	}

	@Override
	public <T> T query(String sql, Class<T> elementType) {
		try {
			return this.getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<T>(elementType));
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> T query(String sql, Class<T> elementType, Object... params) {
		return this.query(sql, elementType, toStatementParameter(sql, params));
	}

	@Override
	public <T> T query(String sql, Class<T> elementType, StatementParameter param) {
		try {
			return this.getJdbcTemplate().queryForObject(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected void log(List<?> list, String sql, StatementParameter param) {
		int size = list.size();
		String sql1;
		if (param == null) {
			sql1 = sql;
		}
		else {
			sql1 = this.getSQL(sql, param);
		}
		this.logger.info("result size:" + size + " sql:" + sql1);
	}

	@Override
	public List<Map<String, Object>> queryForMaps(String sql) {
		try {
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
			return list;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected String appendLimitSql(String sql, int start, int size) {
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		return sql + " LIMIT " + start + "," + size + ";";
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForList(sql, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<T>(elementType));
			if (log) {
				this.log(list, sql, null);
			}
			return list;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForLongs(sql, param);
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param) {
		List<Long> list = super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int index) {
				try {
					return rs.getLong(1);
				}
				catch (SQLException e) {
					throw new InvalidParamDataAccessException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param) {
		List<Integer> list = super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int index) {
				try {
					return rs.getInt(1);
				}
				catch (SQLException e) {
					throw new InvalidParamDataAccessException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	@Override
	public List<String> queryForStrings(String sql) {
		List<String> list = super.getJdbcTemplate().query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int index) {
				try {
					return rs.getString(1);
				}
				catch (SQLException e) {
					throw new InvalidParamDataAccessException(e);
				}
			}
		});
		return list;
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param) {
		List<String> list = super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int index) {
				try {
					return rs.getString(1);
				}
				catch (SQLException e) {
					throw new InvalidParamDataAccessException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	// private String replaceH2Date(String sql) {
	// if (!h2) {
	// return sql;
	// }
	// String regex = "DATE\\((.*?)\\)";
	// Pattern p = Pattern.compile(regex);
	// Matcher m = p.matcher(sql);
	// StringBuffer sb = new StringBuffer();
	// while (m.find()) {
	// String param = m.group(1);
	// String replacement = "left(" + param + ",10)";
	// m.appendReplacement(sb, replacement);
	// }
	// sql = sql.replace("DATE(?)", "left(?,10)");
	// m.appendTail(sb);
	// return sb.toString();
	// }

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForList(sql, elementType, param);
	}

	@Override
	/**
	 * @see com.duowan.leopard.data.jdbc.Jdbc#queryForList(String, Class<T>, Object...)
	 */
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... params) {
		return this.queryForList(sql, elementType, toStatementParameter(sql, params));
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
			if (log) {
				this.log(list, sql, param);
			}
			return list;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Long queryForLong(String sql) {
		try {
			@SuppressWarnings("deprecation")
			long result = this.getJdbcTemplate().queryForObject(sql, Long.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Long queryForLong(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			@SuppressWarnings("deprecation")
			long result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, Long.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Integer queryForInt(String sql) {
		try {
			@SuppressWarnings("deprecation")
			int result = this.getJdbcTemplate().queryForObject(sql, Integer.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public boolean exist(String sql) {
		return this.queryForInt(sql) > 0;
	}

	@Override
	public boolean exist(String sql, StatementParameter param) {
		return this.queryForInt(sql, param) > 0;
	}

	@Override
	public Integer queryForInt(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			@SuppressWarnings("deprecation")
			int result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, Integer.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Date queryForDate(String sql) {
		try {
			Date result = this.getJdbcTemplate().queryForObject(sql, Date.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Date queryForDate(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			Date result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, Date.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String queryForString(String sql) {
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, String.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String queryForString(String sql, StatementParameter param) {
		// System.out.println("queryForString:" + sql + " " + this.getJdbcTemplate());
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, String.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public boolean insertIgnoreForBoolean(String sql, StatementParameter param) {
		try {
			return this.insertForBoolean(sql, param);
		}
		catch (DuplicateKeyException e) {
			logger.info(e.getMessage());
			return false;
		}
	}

	@Override
	public long insertForLastId(final String sql, final StatementParameter param) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				param.setValues(pstmt);
				return pstmt;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public boolean insertForBoolean(String sql, StatementParameter param) {
		return this.updateForBoolean(sql, param);
	}

	@Override
	public boolean updateForBoolean(String sql, StatementParameter param) {
		int updatedCount = this.update(sql, param);
		return (updatedCount > 0);
	}
	protected StatementParameter toStatementParameter(String sql, Object... params) {
		StatementParameter param = new StatementParameter();
		for (Object p : params) {
			if (p instanceof Integer) {
				param.setInt((Integer) p);
			}
			else if (p instanceof Long) {
				param.setLong((Long) p);
			}
			else if (p instanceof Boolean) {
				param.setBool((Boolean) p);
			}
			else if (p instanceof Float) {
				param.setFloat((Float) p);
			}
			else if (p instanceof Double) {
				param.setDouble((Double) p);
			}
			else if (p instanceof String) {
				param.setString((String) p);
			}
			else if (p instanceof Date) {
				param.setDate((Date) p);
			}
			else {
				throw new IllegalArgumentException("鏈煡鏁版嵁绫诲瀷[" + p.getClass().getName() + "].");
			}
		}
		return param;
	}

	@Override
	public boolean updateForBoolean(String sql, Object... params) {
		return this.updateForBoolean(sql, toStatementParameter(sql, params));
	}

	@Override
	public int update(String sql, StatementParameter param) {
		int updatedCount = this.getJdbcTemplate().update(sql, param.getParameters());
		if (log) {
			String sql1 = this.getSQL(sql, param);
			logger.info("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return updatedCount;
	}

	@Override
	public int update(String sql) {
		int updatedCount = this.getJdbcTemplate().update(sql);
		if (log) {
			logger.info("updatedCount:" + updatedCount + " sql:" + sql);
		}
		return updatedCount;
	}

	// public String beanName() {
	// return this.getClass().getName();
	// }

	@Override
	public Long incr(String sql, StatementParameter param) {
		boolean success = this.updateForBoolean(sql, param);
		if (success) {
			return 1L;
		}
		else {
			return 0L;
		}
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForInts(sql, param);
	}

	@Override
	public List<String> queryForStrings(String sql, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForStrings(sql);
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForStrings(sql, param);
	}

	@Override
	public Long queryForLong(String sql, Object... params) {
		return this.queryForLong(sql, this.toStatementParameter(sql, params));
	}

	@Override
	public Integer queryForInt(String sql, Object... params) {
		return this.queryForInt(sql, this.toStatementParameter(sql, params));
	}

	@Override
	public boolean insertForBoolean(String sql, Object... params) {
		return this.insertForBoolean(sql, toStatementParameter(sql, params));
	}

//	@Override
//	public boolean insertByBean(String sql, Object bean) {
//		return this.insertForBoolean(sql, SqlParserUtil.toInsertParameter(sql, bean));
//	}
//
//	@Override
//	public boolean updateByBean(String sql, Object bean) {
//		return this.updateForBoolean(sql, SqlParserUtil.toUpdateParameter(sql, bean));
//	}

	@Override
	public <T> Page<T> queryForPage(String sql, Class<T> elementType) {
//		List<T> list = this.queryForList(sql, elementType);
		String countSql = SqlUtil.toCountSql(sql);
		int count = this.queryForInt(countSql);
		List<T> list =new ArrayList<T>();
		if(count>0){
			list = this.queryForList(sql, elementType);
		}

		Page<T> page = new Page<T>();
		page.setData(list);
		page.setCount(count);
		return page;
	}

	@Override
	public <T> Page<T> queryForPage(String sql, Class<T> elementType, int start, int size) {
		sql = this.appendLimitSql(sql, start, size);
		return this.queryForPage(sql, elementType);
	}

	@Override
	public <T> Page<T> queryForPage(String sql, Class<T> elementType, Object... params) {
		StatementParameter param = toStatementParameter(sql, params);
//		List<T> list = this.queryForList(sql, elementType, param);
		String countSql = SqlUtil.toCountSql(sql);
		int count = this.queryForInt(countSql, param);
		List<T> list =new ArrayList<T>();
		if(count>0){
			list = this.queryForList(sql, elementType, param);
		}

		Page<T> page = new Page<T>();
		page.setData(list);
		page.setCount(count);
		return page;
	}

	@Override
	public <T> Page<T> queryForPage(String sql, Class<T> elementType, StatementParameter param) {
//		List<T> list = this.queryForList(sql, elementType, param);
		CountSqlParser countSqlParser = new CountSqlParser(sql, param);
		// String countSql = countSqlParser.getCountSql();
		// System.err.println("countSql:" + countSqlParser.getCountSql());
		int count = this.queryForInt(countSqlParser.getCountSql(), countSqlParser.getCountParam());
		List<T> list =new ArrayList<T>();
		if(count>0){
			list= this.queryForList(sql, elementType, param);
		}

		Page<T> page = new Page<T>();
		page.setData(list);
		page.setCount(count);
		return page;
	}

	@Override
	public <T> Page<T> queryForPage(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
//		List<T> list = this.queryForList(sql, elementType, param, start, size);

		String countSql = SqlUtil.toCountSql(sql);
		int count = this.queryForInt(countSql, param);
		List<T> list =new ArrayList<T>();
		if(count>0){
			list = this.queryForList(sql, elementType, param, start, size);
		}
		
		Page<T> page = new Page<T>();
		page.setData(list);
		page.setCount(count);
		return page;
	}

}