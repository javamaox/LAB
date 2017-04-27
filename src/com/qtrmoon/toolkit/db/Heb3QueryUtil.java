package com.qtrmoon.toolkit.db;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

public class Heb3QueryUtil {
	/**
	 * @param conditions
	 *            ��ѯ�������б�
	 * @param q
	 *            Query
	 * 
	 * ���б��еĲ�ѯ����set����Ӧ��ռλ����ȥ
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
	 *            ռλ���������ֵ������
	 * @param key
	 *            ռλ���ı�ʾ
	 * @param value
	 *            ռλ�������ֵ
	 * @param q
	 *            Query
	 * 
	 * ���ݲ�ѯ����������,��ֵ����ͬ�����͵�����Ӧ�ķ�����ֵset��ռλ����ȥ
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
