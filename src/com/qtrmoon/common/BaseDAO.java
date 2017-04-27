package com.qtrmoon.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.qtrmoon.toolkit.db.Condition;
import com.qtrmoon.toolkit.db.Heb3QueryUtil;

/**
 * 对实体对象操作的超类，涵盖了基本的增删改查方法。
 */
public class BaseDAO extends HibernateDaoSupport {
	protected final Log log = LogFactory.getLog(getClass());

	public void saveObject(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public Object getObject(Class clazz, Serializable id) {
		Object o = getHibernateTemplate().get(clazz, id);
		if (o == null) {
			throw new ObjectRetrievalFailureException(clazz, id);
		}
		return o;
	}

	public List getObjects(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	/** 按主键删除 */
	public void removeObject(Class clazz, Serializable id) {
		getHibernateTemplate().delete(getObject(clazz, id));
	}
	/** 删除对象 */
	public void removeObject(Object obj) {
		getHibernateTemplate().delete(obj);
	}
	/** 删除集合 */
	public void removeAll(Collection<Object> coll) {
		getHibernateTemplate().deleteAll(coll);
	}
	/**
	 * 执行sql语句，删除、修改等。
	 * @param sql
	 */
	public void execute(String sql) {
		PreparedStatement stm=null;
		try {
			stm = getConn().prepareStatement(sql);
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(stm);
		}
	}
	public Connection getConn(){
		return getSession().connection();
	}
	protected static void close(Statement dbState) {
		if(dbState!=null){
			try {
				dbState.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 使用反射机制实现的通用查询方法，
	 * @param commonForm
	 * @param className
	 * @return
	 */
	public List sch(Object commonForm,String className){
		PageForm pageForm=(PageForm)commonForm;
		Session session = getSession();
		Query query;
		int pageSize=0,firstRes=0;
		String condition = "";
		List<Condition> conditions = new ArrayList<Condition>();
		try {
			if (commonForm != null) {
				Class objCls = Class.forName(className);
				Class formCls = commonForm.getClass();
				Class paramTypeCls;
				Field[] fs = objCls.getDeclaredFields();
				String fName,upfName;
				Method method;
				Object value;
				for(Field f:fs){
					fName=f.getName();
					upfName=(fName.charAt(0)+"").toUpperCase()+fName.substring(1);
					method=formCls.getMethod("get"+upfName);
					paramTypeCls=f.getType();
					if(paramTypeCls.equals(Date.class)){
						value=formCls.getMethod("get"+upfName+"Beg").invoke(commonForm);
						if (value!= null && !value.equals("")) {
							condition += " and t."+fName+" >= to_Date(:"+fName+"Beg,'yyyy-mm-dd')";
							conditions.add(new Condition(fName+"Beg", String.class, value));
						}
						value=formCls.getMethod("get"+upfName+"End").invoke(commonForm);
						if (value!= null && !value.equals("")) {
							condition += " and t."+fName+" <= to_Date(:"+fName+"End,'yyyy-mm-dd')";
							conditions.add(new Condition(fName+"End", String.class, value));
						}
					}else{
						value=method.invoke(commonForm);
						if (value!= null&&!value.equals("")&&!value.equals(0L)) {
							condition += " and t."+fName+" = :"+fName+"";
							conditions.add(new Condition(fName,paramTypeCls,value));
						}
					}
				}
				String formCond = pageForm.getCondition();
				if (formCond != null && !formCond.equals("")) {
					condition += formCond;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pageForm!=null){
			String order="";//排序字段
			if(pageForm.getOrderCol()!=null&&!pageForm.getOrderCol().equals("")){
				order=" order by "+pageForm.getOrderCol()+" "+pageForm.getOrderType();
			}
			query = session.createQuery("select count(*) from "+className+" t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			int datasize = (Integer)query.list().get(0);
			pageForm.setDatasize(datasize);
			query = session.createQuery("from "+className+" t where 1=1 "+condition + order);
			Heb3QueryUtil.createQuery(conditions, query);
			pageSize = pageForm.getPagesize();
			firstRes = (pageForm.getCurrentPage()-1) * pageSize;
			query.setMaxResults(pageSize);
			query.setFirstResult(firstRes);
		}else{
			query = session.createQuery("from "+className+" t");
		}
		return query.list();
	}
}
