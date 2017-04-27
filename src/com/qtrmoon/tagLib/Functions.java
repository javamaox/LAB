package com.qtrmoon.tagLib;

import java.util.Arrays;
import java.util.List;

import com.qtrmoon.common.Constant;

public class Functions {
	public static int fn_size(Object list) {
		int num = 0;
		if (list instanceof List) {
			num = ((List) list).size();
		}
		return num;
	}
	
	public static String fn_getConstant(String name) {
		return Constant.getConstant(name);
	}
	
	public static String fn_substring(String name,Integer f,Integer t) {
		if (name == null || name.length() < t || t < f) {
			return name;
		}
		return name.substring(f,t);
	}
	
	/**
	 * ºº×Ö°´2×Ö·û³¤¶È£¬½ØÈ¡¾ø¶Ô³¤¶È´®¡£
	 * @param name
	 * @param f
	 * @return
	 */
	public static String fn_cncut(String name,Integer f) {
		if (name == null || name.getBytes().length < f) {
			return name;
		}
		name=new String(Arrays.copyOf(name.getBytes(),f));
		//´¦ÀíÄ©Î²°ëºº×ÖÂÒÂë¡£
		if((int)name.charAt(name.length()-1)>65500){
			name=name.substring(0,name.length()-2);
		}
		return name;
	}
	
	public static int fn_length(String name) {
		return name.length();
	}
}
