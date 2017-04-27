package com.qtrmoon.toolkit;

public class CodeConvert {
	/**
	 * ת���ַ���&#23233;���롣����ajax���ַ�,ҳ����������롣
	 * ���з�ascII���ַ�����ת����&#xxxx;����ʽ��
	 * Js��ʹ��String.fromCharCode(23233)�ɻָ�����
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
