package com.qtrmoon.toolkit.keyBuilder;

//36进制序列生成类，0~9~z
public class StrKey36 {

	public static String getNextSeq(String key) {
		String res = "";
		key = key.toLowerCase();
		String[] arr = key.split("");//元素为百十个的正序String
		char[] ch = new char[arr.length];// 元素为百十个的正序char，高位为0。
		ch[0] = '0';// 补高位0
		// 字符串数组倒叙后转为字符数组
		for (int i = 1; i < arr.length; i++) {
			if (!arr[i].equals("")) {
				ch[i] = arr[i].charAt(0);
			}
		}
		add(ch, arr.length-1);
		// 百位是否进位
		if(ch[0]!='0'){
			res += ch[0];
		}
		for (int i = 1; i < arr.length; i++) {
			res += ch[i] + "";
		}
		return res.trim();
	}

	private static void add(char[] ch, int degree) {
		if (plusOneDegree(ch, degree)) {
			add(ch, degree - 1);
		}
	}

	private static boolean plusOneDegree(char[] ch, int degree) {
		if (ch[degree] < '9') {
			ch[degree] += 1;
			return false;// 不进位
		} else if (ch[degree] == '9') {
			ch[degree] = 'a';
			return false;
		} else if (ch[degree] < 'z') {
			ch[degree] += 1;
			return false;// 不进位
		} else {// ='z'
			ch[degree] = '0';
			return true;
		}
	}

	public static void main(String args[]) {
		System.out.print(getNextSeq("z"));
	}
}
