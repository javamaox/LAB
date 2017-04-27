package com.qtrmoon.toolkit.keyBuilder;

//36�������������࣬0~9~z
public class StrKey36 {

	public static String getNextSeq(String key) {
		String res = "";
		key = key.toLowerCase();
		String[] arr = key.split("");//Ԫ��Ϊ��ʮ��������String
		char[] ch = new char[arr.length];// Ԫ��Ϊ��ʮ��������char����λΪ0��
		ch[0] = '0';// ����λ0
		// �ַ������鵹���תΪ�ַ�����
		for (int i = 1; i < arr.length; i++) {
			if (!arr[i].equals("")) {
				ch[i] = arr[i].charAt(0);
			}
		}
		add(ch, arr.length-1);
		// ��λ�Ƿ��λ
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
			return false;// ����λ
		} else if (ch[degree] == '9') {
			ch[degree] = 'a';
			return false;
		} else if (ch[degree] < 'z') {
			ch[degree] += 1;
			return false;// ����λ
		} else {// ='z'
			ch[degree] = '0';
			return true;
		}
	}

	public static void main(String args[]) {
		System.out.print(getNextSeq("z"));
	}
}
