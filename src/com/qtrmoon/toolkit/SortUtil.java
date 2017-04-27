package com.qtrmoon.toolkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SortUtil {
	
	/**
	 * 将list中的对象排序，依据list元素中的param属性来排序。
	 * @param list;//要排序的数据集合
	 * @param className;//集合中的元素类或接口，需要绝对，例如:com.qtrmoon.xxx.Cxx。
	 * @param param;//要排序的属性，需要实现该属性的getter方法。
	 * @param type;//排序属性的数据类型,可以是Integer,Long,Double,String,Date
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
	 * 将list中的对象倒序排序，依据list元素中的param属性来排序。
	 * @param list;//要排序的数据集合
	 * @param className;//集合中的元素类或接口，需要绝对，例如:com.qtrmoon.xxx.Cxx。
	 * @param param;//要排序的属性，需要实现该属性的getter方法。
	 * @param type;//排序属性的数据类型,可以是Integer,Long,Double,String,Date
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
	 * 将list中的对象排序，依据list元素中的param属性来排序。list元素识别为list中的首元素类型，list中元素相同时可以使用该方法省略类名参数。
	 * @param list;//要排序的数据集合
	 * @param param;//要排序的属性，需要实现该属性的getter方法。
	 * @param type;//排序属性的数据类型,可以是Integer,Long,Double,String,Date
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
	 * 将list中的对象倒序排序，依据list元素中的param属性来排序。list元素识别为list中的首元素类型，list中元素相同时可以使用该方法省略类名参数。
	 * @param list;//要排序的数据集合
	 * @param param;//要排序的属性，需要实现该属性的getter方法。
	 * @param type;//排序属性的数据类型,可以是Integer,Long,Double,String,Date
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
