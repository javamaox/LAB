package com.qtrmoon.toolkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SortUtil {
	
	/**
	 * ��list�еĶ�����������listԪ���е�param����������
	 * @param list;//Ҫ��������ݼ���
	 * @param className;//�����е�Ԫ�����ӿڣ���Ҫ���ԣ�����:com.qtrmoon.xxx.Cxx��
	 * @param param;//Ҫ��������ԣ���Ҫʵ�ָ����Ե�getter������
	 * @param type;//�������Ե���������,������Integer,Long,Double,String,Date
	 */
	public static void sortList(List list,String className,String param, Class type){
		if(list.size()==0)return;
		if (type.equals(String.class)) {
			sortList_str(list,className,param);
		} else if (type.equals(Double.class)||type.equals(Long.class)||type.equals(Integer.class)||type.equals(Date.class)) {
			sortList_num(list,className,param,type);
		} else if (type.equals(Date.class)) {
			
		}
	}
	/**
	 * ��list�еĶ�������������listԪ���е�param����������
	 * @param list;//Ҫ��������ݼ���
	 * @param className;//�����е�Ԫ�����ӿڣ���Ҫ���ԣ�����:com.qtrmoon.xxx.Cxx��
	 * @param param;//Ҫ��������ԣ���Ҫʵ�ָ����Ե�getter������
	 * @param type;//�������Ե���������,������Integer,Long,Double,String,Date
	 */
	public static void sortListDesc(List list,String className,String param, Class type){
		if(list.size()==0)return;
		if (type.equals(String.class)) {
			sortList_str_desc(list,className,param);
		} else if (type.equals(Double.class)||type.equals(Long.class)||type.equals(Integer.class)||type.equals(Date.class)) {
			sortList_num_desc(list,className,param,type);
		}
	}
	/**
	 * ��list�еĶ�����������listԪ���е�param����������listԪ��ʶ��Ϊlist�е���Ԫ�����ͣ�list��Ԫ����ͬʱ����ʹ�ø÷���ʡ������������
	 * @param list;//Ҫ��������ݼ���
	 * @param param;//Ҫ��������ԣ���Ҫʵ�ָ����Ե�getter������
	 * @param type;//�������Ե���������,������Integer,Long,Double,String,Date
	 */
	public static void sortList(List list,String param, Class type){
		if(list.size()==0)return;
		if (type.equals(String.class)) {
			sortList_str(list,null,param);
		} else if (type.equals(Double.class)||type.equals(Long.class)||type.equals(Integer.class)||type.equals(Date.class)) {
			sortList_num(list,null,param,type);
		} else if (type.equals(Date.class)) {
			
		}
	}
	/**
	 * ��list�еĶ�������������listԪ���е�param����������listԪ��ʶ��Ϊlist�е���Ԫ�����ͣ�list��Ԫ����ͬʱ����ʹ�ø÷���ʡ������������
	 * @param list;//Ҫ��������ݼ���
	 * @param param;//Ҫ��������ԣ���Ҫʵ�ָ����Ե�getter������
	 * @param type;//�������Ե���������,������Integer,Long,Double,String,Date
	 */
	public static void sortListDesc(List list,String param, Class type){
		if(list.size()==0)return;
		if (type.equals(String.class)) {
			sortList_str_desc(list,null,param);
		} else if (type.equals(Double.class)||type.equals(Long.class)||type.equals(Integer.class)||type.equals(Date.class)) {
			sortList_num_desc(list,null,param,type);
		}
	}
	private static void sortList_str(List list,String className,String param){
		try {
			param="get"+(param.charAt(0)+"").toUpperCase()+param.substring(1);
			Class tar;
			if(className==null){
				tar=list.get(0).getClass();
			}else{
				tar=Class.forName(className);
			}
			Method method=tar.getMethod(param);
			Object node,node1,obj;
			String order,order1;
			int index=0;
			for (int i = 0; i < list.size() - 1; i++) {
				node = list.get(i);
				order = (String)method.invoke(node);
				for (int m = i + 1; m < list.size(); m++) {
					node1 = list.get(m);
					order1=(String)method.invoke(node1);
					if (order.compareTo(order1)>0) {
						order = order1;
						index = m;
					}
				}
				if (index > 0) {
					obj = list.get(i);
					list.set(i, list.get(index));
					list.set(index, obj);
				}
				index = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void sortList_str_desc(List list,String className,String param){
		try {
			param="get"+(param.charAt(0)+"").toUpperCase()+param.substring(1);
			Class tar;
			if(className==null){
				tar=list.get(0).getClass();
			}else{
				tar=Class.forName(className);
			}
			Method method=tar.getMethod(param);
			Object node,node1,obj;
			String order,order1;
			int index=0;
			for (int i = 0; i < list.size() - 1; i++) {
				node = list.get(i);
				order = (String)method.invoke(node);
				for (int m = i + 1; m < list.size(); m++) {
					node1 = list.get(m);
					order1=(String)method.invoke(node1);
					if (order.compareTo(order1)<0) {
						order = order1;
						index = m;
					}
				}
				if (index > 0) {
					obj = list.get(i);
					list.set(i, list.get(index));
					list.set(index, obj);
				}
				index = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sortList_num(List list,String className,String param,Class type){
		try {
			param="get"+(param.charAt(0)+"").toUpperCase()+param.substring(1);
			Class tar;
			if(className==null){
				tar=list.get(0).getClass();
			}else{
				tar=Class.forName(className);
			}
			Method method=tar.getMethod(param);
			Object node,node1,obj;
			Double order,order1;
			int index=0;
			for (int i = 0; i < list.size() - 1; i++) {
				node = list.get(i);
				order = getVal(method.invoke(node),type);
				for (int m = i + 1; m < list.size(); m++) {
					node1 = list.get(m);
					order1=getVal(method.invoke(node1),type);
					if (order.compareTo(order1)>0) {
						order = order1;
						index = m;
					}
				}
				if (index > 0) {
					obj = list.get(i);
					list.set(i, list.get(index));
					list.set(index, obj);
				}
				index = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private static void sortList_num_desc(List list,String className,String param,Class type){
		try {
			param="get"+(param.charAt(0)+"").toUpperCase()+param.substring(1);
			Class tar;
			if(className==null){
				tar=list.get(0).getClass();
			}else{
				tar=Class.forName(className);
			}
			Method method=tar.getMethod(param);
			Object node,node1,obj;
			Double order,order1;
			int index=0;
			for (int i = 0; i < list.size() - 1; i++) {
				node = list.get(i);
				order = getVal(method.invoke(node),type);
				for (int m = i + 1; m < list.size(); m++) {
					node1 = list.get(m);
					order1=getVal(method.invoke(node1),type);
					if (order.compareTo(order1)<0) {
						order = order1;
						index = m;
					}
				}
				if (index > 0) {
					obj = list.get(i);
					list.set(i, list.get(index));
					list.set(index, obj);
				}
				index = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static Double getVal(Object v, Class type) {
		if(type.equals(Integer.class)){
			return ((Integer)v).doubleValue();
		}else if(type.equals(Long.class)){
			return ((Long)v).doubleValue();
		}else if(type.equals(Double.class)){
			return ((Double)v).doubleValue();
		}else if(type.equals(Date.class)){
			return Double.valueOf(((Date)v).getTime());
		}
		return null;
	}
	public static void main(String args[]) throws Exception{
		List<Cat> l=new ArrayList<Cat>();
		l.add(new Cat("1",3,new Date(554585555852l)));
		l.add(new Cat("8",5,new Date(444585854424l)));
		l.add(new Cat("11",1,new Date(335858543323l)));
		l.add(new Cat("21",2,new Date(4558585442545l)));
		l.add(new Cat("9",4,new Date(854588554235l)));
		sortList(l,"com.qtrmoon.toolkit.Cat","bir",Date.class);
		for(Cat cat:l){
			System.out.println(cat.getBir());
		}
	}
}
class Cat{
	public String name;
	private int age;
	private Date bir;
	
	public Cat(String name,int age,Date bir){
		this.name=name;
		this.age=age;
		this.bir=bir;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getBir() {
		return bir;
	}

	public void setBir(Date bir) {
		this.bir = bir;
	}
	
}
