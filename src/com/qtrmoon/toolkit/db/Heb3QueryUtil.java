package com.qtrmoon.toolkit.db;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

public class Heb3QueryUtil {
	/**
	 * @param conditions
	 *            查询条件的列表
	 * @param q
	 *            Query
	 * 
	 * 把列表中的查询条件set到相应的占位符中去
	 */
	public static void createQuery(List<Condition> conditions, Query q) {
		for (int i = 0; i < conditions.size(); i++) {
			Condition condition = conditions.get(i);
			try {
				addCondition(condition.getKey(), condition.getType(), condition
						.getValue(), q);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param type
	 *            占位符所代表的值的类型
	 * @param key
	 *            占位符的标示
	 * @param value
	 *            占位符代表的值
	 * @param q
	 *            Query
	 * 
	 * 根据查询条件的类型,把值按不同的类型调用相应的方法把值set到占位符中去
	 */
	public static void addCondition(String key, Class type, Object value,
			Query q) throws ClassCastException {
		if (type.equals(String.class)) {
			q.setString(key, (String) value);
		} else if (type.equals(Long.class)) {
			q.setLong(key, (Long) value);
		} else if (type.equals(Integer.class)) {
			q.setInteger(key, (Integer) value);
		} else if (type.equals(Date.class)) {
			q.setDate(key, (Date) value);
		} else if (type.equals(Double.class)) {
			q.setDouble(key, (Double) value);
		} else if (type.equals(Boolean.class)) {
			q.setBoolean(key, (Boolean) value);
		}
	}

}
