package com.qtrmoon.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.qtrmoon.toolkit.keyBuilder.StrKey36;

public class CommunalDao extends BaseDAO {

	private static Map<String, Key> keyMap=new HashMap<String,Key>();//�������ֵ
	/**
	 * @param table
	 * @param pk
	 * @return ���ֵ
	 * 
	 * ��ȡtable����pk�ֶε����ֵ
	 */
	public long getMaxLongId(String table, String pk) {
		Key key = keyMap.get(table);
		//��ȡ�������
		Long max=1L;
		if(key==null){
			String sql = "select max(t." + pk + ") id from " + table + " t";
			Long l = (Long) getSession().createSQLQuery(sql).addScalar("id",
					Hibernate.LONG).uniqueResult();
			if (l == null) {
				l = 1L;
			}
		}else{
			max = (Long)key.value;
		}
		//�������ֵ
		Long next = max+1;
		if(key==null){
			key = new Key();
			key.type="Long";
			key.value=next;
			keyMap.put(table, key);
		}else{
			key.value=next;
		}
		return max;
	}

	/**
	 * @param table
	 * @param pk
	 * @return ���ֵ
	 * 
	 * ��ȡtable����pk�ֶε����ֵ
	 */
	public synchronized String getMaxStringId(String table, String pk) {
		Key key = keyMap.get(table);
		//��ȡ�������
		String max="0";
		if(key==null){
			String sql = "select max(t." + pk + ") id from " + table + " t where length(t." + pk + ")=(select max (length(t." + pk + ")) id from " + table + " t)";
			max = (String) getSession().createSQLQuery(sql).addScalar("id",
					Hibernate.STRING).uniqueResult();
			if (max == null) {
				max = "0";
			}
		}else{
			max = (String)key.value;
		}
		//�������ֵ
		String next = StrKey36.getNextSeq(max);
		if(key==null){
			key = new Key();
			key.type="String";
			key.value=next;
			keyMap.put(table, key);
		}else{
			key.value=next;
		}
		return max;
	}
	
	/**
	 * �����ֶβ�ѯ�����
	 */
	public List schData(String sql, String[] cols,String[] type) {
		SQLQuery query=getSession().createSQLQuery(sql);
		for(int i=0,ii=cols.length;i<ii;i++){
			query.addScalar(cols[i],getHibernateType(type[i]));
		}
		List list=query.list();
		return list;
	}
	
	/**
	 * ִ��sql��䣬ɾ�����޸ĵȡ�
	 * @param sql
	 */
	public void execute(String sql) {
		Statement stm = null;
		try {
			Transaction tx = getSession().beginTransaction();
			stm = getSession().connection().createStatement();
			stm.execute(sql);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void updBlob(String sql,byte[] bytes) {
		PreparedStatement stm = null;
		try {
			Transaction tx = getSession().beginTransaction();
			
			stm=getSession().connection().prepareStatement(sql);
			stm.setBytes(1, bytes);
			
			stm.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Type getHibernateType(String type) {
		if(type.equalsIgnoreCase("String")){
			return Hibernate.STRING;
		}else if(type.equalsIgnoreCase("Integer")||type.equalsIgnoreCase("int")){
			return Hibernate.INTEGER;
		}else if(type.equalsIgnoreCase("Long")){
			return Hibernate.LONG;
		}else if(type.equalsIgnoreCase("Date")){
			return Hibernate.DATE;
		}else if(type.equalsIgnoreCase("Double")){
			return Hibernate.DOUBLE;
		}else if(type.equalsIgnoreCase("Float")){
			return Hibernate.FLOAT;
		}else if(type.equalsIgnoreCase("Boolean")){
			return Hibernate.BOOLEAN;
		}else if(type.equalsIgnoreCase("Blob")){
			return Hibernate.BLOB;
		}else if(type.equalsIgnoreCase("Clob")){
			return Hibernate.CLOB;
		}
		return null;
	}

	/**
	 * ��ȡĳ����ĵ�ǰ�����к�
	 * 
	 * @return
	 */
	public Long getSequences(String seq) {
		String sql = "select " + seq + ".nextval as id from dual";
		Long l = (Long) getSession().createSQLQuery(sql).addScalar("id",
				Hibernate.LONG).uniqueResult();
		return l;
	}

}
class Key{
	public String type;//��������
	public Object value;//����ֵ
}