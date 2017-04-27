package com.qtrmoon.toolkit;

public class NumberUtil {
	public static String getCNNum(int x){
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
	
	public static String getx(int x){
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
}
