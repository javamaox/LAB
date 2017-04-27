package com.qtrmoon.toolkit;

public class CodeConvert {
	/**
	 * 转换字符到&#23233;编码。用于ajax传字符,页面输出的乱码。
	 * 所有非ascII码字符都被转换成&#xxxx;的形式。
	 * Js中使用String.fromCharCode(23233)可恢复中文
	 * @param content
	 * @return
	 */
	public static String covertCode(String content) {
		if(content==null){
			return "";
		}
		String res = "";
		char ch;
		for (int i = 0; i < content.length(); i++) {
			ch=content.charAt(i);
			if((int) ch>255){
				res += "&#" + (int) ch + ";";
			}else{
				res+=ch;
			}
		}
		return res;
	}
}
