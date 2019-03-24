package com.freeco.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;

public class StatementParameter {

	private final List<Object> list = new ArrayList<Object>();
	private final List<Class<?>> type = new ArrayList<Class<?>>();

	private boolean allowNull;

	public StatementParameter() {

	}

	public StatementParameter(boolean allowNull) {
		this.allowNull = allowNull;
	}

	protected void checkNull(Object value) {
		if (allowNull) {
			return;
		}
	}

	public void setDate(Date value) {
		this.checkNull(value);
		list.add(value);
		type.add(Date.class);
	}

	public void setTimestamp(Timestamp value) {
		this.checkNull(value);
		list.add(value);
		type.add(Timestamp.class);
	}

	public void setObject(Class<?> type, Object value) {
		if (type.equals(String.class)) {
			this.setString((String) value);
		}
		else if (type.equals(Integer.class)) {
			this.setInt((Integer) value);
		}
		else if (type.equals(Long.class)) {
			this.setLong((Long) value);
		}
		else if (type.equals(Float.class)) {
			this.setFloat((Float) value);
		}
		else if (type.equals(Double.class)) {
			this.setDouble((Double) value);
		}
		else if (type.equals(Boolean.class)) {
			if (value instanceof Integer) {
				int num = (Integer) value;
				this.setBool(num == 1);
			}
			else {
				this.setBool((Boolean) value);
			}
		}
		else if (type.equals(Date.class)) {
			this.setDate((Date) value);
		}
		else {
			throw new IllegalArgumentException("非法参数类型[" + type + "].");
		}
	}

	public void setString(String value) {
		this.checkNull(value);
		list.add(value);
		type.add(String.class);
	}

	public void setBool(Boolean value) {
		this.checkNull(value);
		list.add(value);
		type.add(Boolean.class);
	}

	public void setInt(Integer value) {
		this.checkNull(value);
		list.add(value);
		type.add(Integer.class);
	}

	public void setLong(Long value) {
		this.checkNull(value);
		list.add(value);
		type.add(Long.class);
	}

	public void setDouble(Double value) {
		this.checkNull(value);
		list.add(value);
		type.add(Double.class);
	}

	public void setFloat(Float value) {
		this.checkNull(value);
		list.add(value);
		type.add(Float.class);
	}

	public Date getDate(int index) {
		Object value = this.getObject(index);
		return (Date) value;
	}

	public Timestamp getTimestamp(int index) {
		Object value = this.getObject(index);
		return (Timestamp) value;
	}

	public String getString(int index) {
		Object value = this.getObject(index);
		return (String) value;
	}

	public int getInt(int index) {
		Object value = this.getObject(index);
		return (Integer) value;
	}

	public float getFloat(int index) {
		Object value = this.getObject(index);
		return (Float) value;
	}

	public long getLong(int index) {
		Object value = this.getObject(index);
		return (Long) value;
	}

	public double getDouble(int index) {
		Object value = this.getObject(index);
		return (Double) value;
	}

	public Boolean getBool(int index) {
		Object value = this.getObject(index);
		return (Boolean) value;
	}

	public Object getObject(int index) {
		return list.get(index);
	}

	public Class<?> getType(int index) {
		return type.get(index);
	}

	// private int getTypes(int index) {
	// Object value = list.get(index);
	// return this.getTypes(value);
	// }
	public Object[] getArgs() {
		Object[] values = new Object[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = this.getArg(i);
		}
		return values;
	}

	protected Object getArg(int index) {
		Object value = list.get(index);
		if (value == null) {
			return null;
		}
		Class<?> type = this.type.get(index);
		if (type.equals(String.class)) {
			return value;
		}
		else if (type.equals(Boolean.class)) {
			Boolean flag = (Boolean) value;
			return (int) (flag ? 1 : 0);
		}

		else if (type.equals(Integer.class)) {
			return value;
		}
		else if (type.equals(Long.class)) {
			return value;
		}
		else if (type.equals(Float.class)) {
			return value;
		}
		else if (type.equals(Double.class)) {
			return value;
		}
		else if (type.equals(Date.class)) {
			Date date = (Date) value;
			return new Timestamp(date.getTime());
		}
		else if (type.equals(Timestamp.class)) {
			return value;
		}

		else {
			throw new IllegalArgumentException("非法参数[" + type.getName() + "].");
		}
	}

	protected int[] getArgTypes() {
		// { Types.CHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR };
		int[] valuesTypes = new int[type.size()];
		Object[] types = type.toArray();
		for (int i = 0; i < types.length; i++) {
			valuesTypes[i] = this.getIntType(i);
		}
		return valuesTypes;
	}

	protected int getIntType(int index) {
		Class<?> type = this.type.get(index);
		if (type.equals(String.class)) {
			return Types.VARCHAR;
		}
		else if (type.equals(Boolean.class)) {
			return Types.INTEGER;
		}
		else if (type.equals(Integer.class)) {
			return Types.INTEGER;
		}
		else if (type.equals(Long.class)) {
			return Types.BIGINT;
		}
		else if (type.equals(Float.class)) {
			return Types.FLOAT;
		}
		else if (type.equals(Double.class)) {
			return Types.DOUBLE;
		}
		else if (type.equals(Date.class)) {
			return Types.TIMESTAMP;
		}
		else if (type.equals(Timestamp.class)) {
			return Types.TIMESTAMP;
		}
		else {
			throw new IllegalArgumentException("非法参数[" + type.getName() + "].");
		}
	}

	public int size() {
		return list.size();
	}

	public PreparedStatementSetter getParameters() {
		if (list.size() == 0) {
			return null;
		}
		PreparedStatementSetter param = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstmt) {
				try {
					StatementParameter.this.setValues(pstmt);
				}
				catch (SQLException e) {
					throw new InvalidParamDataAccessException(e);
				}
			}

		};
		return param;
	}

	public void setValues(PreparedStatement pstmt) throws SQLException {
		int i = 1;
		for (Class<?> type : this.type) {
			this.setValues(pstmt, i, type);
			i++;
		}
	}

	protected void setValues(PreparedStatement pstmt, int i, Class<?> type) throws SQLException {
		Object value = this.getArg(i - 1);
		if (type.equals(String.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.VARCHAR);
			}
			else {
				pstmt.setString(i, (String) value);
			}
		}
		else if (type.equals(Boolean.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.INTEGER);
			}
			else {
				pstmt.setInt(i, (Integer) value);
			}
		}
		else if (type.equals(Integer.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.INTEGER);
			}
			else {
				pstmt.setInt(i, (Integer) value);
			}
		}
		else if (type.equals(Long.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.BIGINT);
			}
			else {
				pstmt.setLong(i, (Long) value);
			}
		}
		else if (type.equals(Float.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.FLOAT);
			}
			else {
				pstmt.setFloat(i, (Float) value);
			}
		}
		else if (type.equals(Double.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.DOUBLE);
			}
			else {
				pstmt.setDouble(i, (Double) value);
			}
		}
		else if (type.equals(Date.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(i, (Timestamp) value);
			}
		}
		else if (type.equals(Timestamp.class)) {
			if (value == null) {
				pstmt.setNull(i, Types.TIMESTAMP);
			}
			else {
				pstmt.setTimestamp(i, (Timestamp) value);
			}
		}
		else {
			throw new InvalidParamDataAccessException("非法参数[" + i + ":" + type.getName() + "].");
		}
	}
}
