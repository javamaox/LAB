package com.qtrmoon.toolkit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransfer {
	public static Date stringToDate(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null || date.equals("") || date.equals("null")) {
			return null;
		} else {
			try {
				return sf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static Date stringToTime(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date == null || date == "") {
			return null;
		} else {
			try {
				return sf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	public static String dateToString(Date date) {
		if (date != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			return sf.format(date);
		} else {
			return null;
		}
	}
	public static String dateToString() {
		return dateToString(new Date());
	}
	
	public static String timeToString(Date date) {
		if (date != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sf.format(date);
		} else {
			return null;
		}
	}
	public static String timeToString(Date date,String fmt) {
		if (date != null) {
			SimpleDateFormat sf = new SimpleDateFormat(fmt);
			return sf.format(date);
		} else {
			return null;
		}
	}
	public static String timeToString() {
		return timeToString(new Date());
	}
	
	public static String dateToId(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		return sf.format(date);
	}
	public static String dateToId() {
		return dateToId(new Date());
	}
	
	public static String timeToId(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sf.format(date);
	}
	public static String timeToId() {
		return timeToId(new Date());
	}
	/**
	 * ��ȡ��������
	 * @param x
	 * @return
	 */
	public static String getCNWeek(int x){
		switch(x){
			case 0:return "������";
			case 1:return "����һ";
			case 2:return "���ڶ�";
			case 3:return "������";
			case 4:return "������";
			case 5:return "������";
			case 6:return "������";
		}
		return "";
	}
	
	/**
	 * ��ȡ����������
	 * @return ����һ�������¶�ʮ��
	 */
	public static String getCNDate(){
		return getCNDate(new Date());
	}
	/**
	 * ��ȡ����������
	 * @param d ʱ��
	 * @return
	 */
	public static String getCNDate(Date d){
		String res=getCNNum(d.getYear()+1900)+"��"+getCNNum(d.getMonth()+1)+"��"+getCNNum(d.getDate())+"��";
		return res;
	}
	
	private static String getCNNum(int x){
		String res="";
		if(x<10){
			res+=getx(x);
		}else if(x==10){
			res+="ʮ";
		}else if(x<20){
			res+="ʮ";
			res+=getx(x%10);
		}else if(x<40){
			res+=getx(x/10);
			res+="ʮ";
			res+=getx(x%10);
		}else{
			String xs=Integer.toString(x);
			for(int i=0;i<xs.length();i++){
				res+=getx(Integer.parseInt(xs.charAt(i)+""));
			}
		}
		return res;
	}
	
	private static String getx(int x){
		switch(x){
			case 0:return "O";
			case 1:return "һ";
			case 2:return "��";
			case 3:return "��";
			case 4:return "��";
			case 5:return "��";
			case 6:return "��";
			case 7:return "��";
			case 8:return "��";
			case 9:return "��";
		}
		return "";
	}
	
	/**
	 * ��ʽ������
	 * @param dataString
	 * @return
	 */
	public static String formatString(String dataString) {
		if(dataString!=null&&!dataString.equals("")){
			dataString=dataString.replaceAll("[-��/.��]", ",");
			dataString=dataString.replaceAll("��", ",");
			dataString=dataString.replaceAll("��", ",");
			dataString=dataString.replaceAll("��", ",");
			dataString=dataString.replaceAll("[^0-9,-]", "");
		}
		dataString=dataString.trim();
		if(dataString.endsWith(",")){
			dataString=dataString.substring(0,dataString.length()-1);
		}
		if(dataString!=null&&!dataString.equals("")){
			String arr[]=dataString.split(",");
			if(arr.length==3){
				if(arr[2].length()>2){//Ӣ��׼�������꡿������һ���ǳ��򵼳��ģ�������������Ϊ��ȫ�ġ�
					dataString=arr[2]+"-"+arr[1]+"-"+arr[0];
				}else{//������
					dataString=qnc(arr[0])+"-"+arr[1]+"-"+arr[2];
				}
			}else if(arr.length==2){//����
				dataString=qnc(arr[0])+"-"+arr[1]+"-01";
			}else if(arr[0].length()==8){//�����մ�
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-"+dataString.substring(4,6)+"-"+dataString.substring(6);
			}else if(arr[0].length()==6){//���´�
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-"+dataString.substring(4,6)+"-"+"01";
			}else if(arr[0].length()==4){//�괮
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-01-01";
			}
		}
		return dataString;
	}

	/**
	 * ˫λ��ת��λ�ꡣ
	 * @param year
	 * @return
	 */
	private static String qnc(String year) {
		String val="";
		if(year.length()<=2){
			if(Integer.parseInt(year)<30){
				val="20"+year;
			}else{
				val="19"+year;
			}
		}else{
			val=year;
		}
		return val;
	}
}
