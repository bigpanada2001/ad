package com.freeco.jdbc;

import java.util.Date;

public class SqlUtil {

	public static String getSQL(String sql, StatementParameter param) {
		int i = 0;
		while (sql.indexOf('?') > -1) {
			if (param == null) {
				throw new InvalidParamDataAccessException("非法参数不能为null.");
			}
			if (i >= param.size()) {
				return sql;
			}
			Class<?> type = param.getType(i);
			Object obj = param.getObject(i);
			String value = getValue(type, obj);
			sql = sql.substring(0, sql.indexOf('?')) + value + sql.substring(sql.indexOf('?') + 1, sql.length());
			i++;
		}
		return sql;//
	}

	protected static String getValue(Class<?> type, Object obj) {
		String value;
		if (type.equals(String.class)) {
			value = "'" + ((String) obj) + "'";
		}

		else if (type.equals(Integer.class)) {
			value = Integer.toString(((Integer) obj));
		}
		else if (type.equals(Boolean.class)) {
			value = (((Boolean) obj) ? 1 : 0) + "";
		}
		else if (type.equals(Float.class)) {
			value = Float.toString(((Float) obj));
		}
		else if (type.equals(Double.class)) {
			value = Double.toString(((Double) obj));
		}
		else if (type.equals(Long.class)) {
			value = Long.toString(((Long) obj));
		}
		else {
			throw new InvalidParamDataAccessException("非法参数[" + type.getName() + "].");
		}
		return value;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 */
	public static String toCountSql(String sql) {
		sql = sql.replace("select * from", "select count(*) from");
		sql = sql.replace("SELECT * FROM", "SELECT count(*) FROM");
		sql = sql.replaceAll(" LIMIT.*", "");
		sql = sql.replaceAll(" limit.*", "");
		return sql;
	}

}
