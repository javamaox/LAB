package com.qtrmoon.toolkit;

public class NumberUtil {
	public static String getCNNum(int x){
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
	
	public static String getx(int x){
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
}
