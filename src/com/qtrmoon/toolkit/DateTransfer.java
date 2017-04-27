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
	 * 获取中文星期
	 * @param x
	 * @return
	 */
	public static String getCNWeek(int x){
		switch(x){
			case 0:return "星期日";
			case 1:return "星期一";
			case 2:return "星期二";
			case 3:return "星期三";
			case 4:return "星期四";
			case 5:return "星期五";
			case 6:return "星期六";
		}
		return "";
	}
	
	/**
	 * 获取中文年月日
	 * @return 二零一零年六月二十日
	 */
	public static String getCNDate(){
		return getCNDate(new Date());
	}
	/**
	 * 获取中文年月日
	 * @param d 时间
	 * @return
	 */
	public static String getCNDate(Date d){
		String res=getCNNum(d.getYear()+1900)+"年"+getCNNum(d.getMonth()+1)+"月"+getCNNum(d.getDate())+"日";
		return res;
	}
	
	private static String getCNNum(int x){
		String res="";
		if(x<10){
			res+=getx(x);
		}else if(x==10){
			res+="十";
		}else if(x<20){
			res+="十";
			res+=getx(x%10);
		}else if(x<40){
			res+=getx(x/10);
			res+="十";
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
			case 1:return "一";
			case 2:return "二";
			case 3:return "三";
			case 4:return "四";
			case 5:return "五";
			case 6:return "六";
			case 7:return "七";
			case 8:return "八";
			case 9:return "九";
		}
		return "";
	}
	
	/**
	 * 格式化日期
	 * @param dataString
	 * @return
	 */
	public static String formatString(String dataString) {
		if(dataString!=null&&!dataString.equals("")){
			dataString=dataString.replaceAll("[-—/.．]", ",");
			dataString=dataString.replaceAll("年", ",");
			dataString=dataString.replaceAll("月", ",");
			dataString=dataString.replaceAll("日", ",");
			dataString=dataString.replaceAll("[^0-9,-]", "");
		}
		dataString=dataString.trim();
		if(dataString.endsWith(",")){
			dataString=dataString.substring(0,dataString.length()-1);
		}
		if(dataString!=null&&!dataString.equals("")){
			String arr[]=dataString.split(",");
			if(arr.length==3){
				if(arr[2].length()>2){//英标准【日月年】，这种一般是程序导出的，年月日三项认为是全的。
					dataString=arr[2]+"-"+arr[1]+"-"+arr[0];
				}else{//年月日
					dataString=qnc(arr[0])+"-"+arr[1]+"-"+arr[2];
				}
			}else if(arr.length==2){//年月
				dataString=qnc(arr[0])+"-"+arr[1]+"-01";
			}else if(arr[0].length()==8){//年月日串
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-"+dataString.substring(4,6)+"-"+dataString.substring(6);
			}else if(arr[0].length()==6){//年月串
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-"+dataString.substring(4,6)+"-"+"01";
			}else if(arr[0].length()==4){//年串
				dataString=arr[0];
				dataString=dataString.substring(0,4)+"-01-01";
			}
		}
		return dataString;
	}

	/**
	 * 双位年转四位年。
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
