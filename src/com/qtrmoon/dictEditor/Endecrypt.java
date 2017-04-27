package com.qtrmoon.dictEditor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 3-DES ���ܽ��ܷ�װ��
 * @author Joey
 */
public class Endecrypt {
	private static final String SPKEY = "gfdy";
	private static Endecrypt cr;
	private static BASE64Decoder base64Decode;
	private Cipher cipher;
	
	private Endecrypt(){
		super();
		cr=this;
		base64Decode = new BASE64Decoder();
		try {
			cipher = Cipher.getInstance("DESede");
			byte[] key = getEnKey(SPKEY);
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey sKey = keyFactory.generateSecret(dks);
			cipher.init(Cipher.DECRYPT_MODE, sKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String oldString = "��Խ";
		System.out.println("��.����ǰ���ݣ�"+oldString);
		String reValue = Endecrypt.encrypt(oldString);
		reValue = reValue.trim().intern();//�����ַ�������Ĺ淶����ʾ��ʽ
		System.out.println("3-DES���ܺ�"+reValue);
		String reValue2 = Endecrypt.decrypt(reValue);
		System.out.println("3-DES���ܺ�"+reValue2);
		
		/* intern()�������� */
//		String s1 = new String("aaa");
//		String s2 = new String("aaa");
//		System.out.println(s1 == s2);//false
//		System.out.println(s1.intern() == s2.intern());//true
	}
	
	/**
	 * ����MD5����
	 * @param strSrc ԭʼ��SPKEY
	 * @return ָ�����ܷ�ʽΪmd5���byte[]
	 */
	private byte[] md5(String strSrc){
		byte[] returnByte = null;
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(strSrc.getBytes("GBK"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnByte;
	}
	
	/**
	 * �õ�3DES����Կ��
	 * ���ݽӿڹ淶����Կ��Ϊ24�ֽڣ�md5���ܳ�������16���ֽڣ���˺��油8���ֽڵ�0
	 * @param spKey ԭʼ��SPKEY
	 * @return ָ�����ܷ�ʽΪmd5���byte[]
	 */
	private byte[] getEnKey(String spKey){
		byte[] desKey = null;
		byte[] desKey1 = md5(spKey);
		desKey = new byte[24];
		int i = 0;
		while(i<desKey1.length&&i<24){
			desKey[i] = desKey1[i];
			i++;
		}
		if(i<24){
			desKey[i] = 0;
			i++;
		}
		return desKey;
	}
	
	/**
	 * 3-DES����
	 * @param src Ҫ����3-DES���ܵ�byte[] 
	 * @param enKey 3-DES������Կ
	 * @return 3-DES���ܺ��byte[]
	 */
	private byte[] Encrypt(byte[] src,byte[] enKey) {
		byte[] encryptedData = null;
		DESedeKeySpec dks;
		try {
			dks = new DESedeKeySpec(enKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}
	
	/**
	 * ���ַ�������Base64����
	 * @param src Ҫ���б�����ַ�
	 * @return ���б������ַ���
	 */
	private String getBase64Encode(byte[] src){
		String requestValue = "";
		BASE64Encoder base64en = new BASE64Encoder();
		requestValue = base64en.encode(src);
//		System.out.println(requestValue);
		return requestValue;
	}
	
	/**
	 * ȥ���ַ����Ļ��з���
	 * @param str
	 * @return
	 */
	private String filter(String str){
		String output = null;
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<str.length();i++){
			int asc = str.charAt(i);
			if(asc!=10&&asc!=13){
				sb.append(str.subSequence(i, i+1));
			}
		}
		output = new String(sb);
		return output;
	}
	
	/**
	 * ���ַ�������URLDecoder.encode(strEncoding)����
	 * @param src Ҫ���б�����ַ���
	 * @return ���б������ַ���
	 */
	private String getURLEncode(String src){
		String requestValue = "";
		requestValue = URLEncoder.encode(src);
		return requestValue;
	}
	
	/**
	 * 3-DES����
	 * @param src Ҫ����3-DES���ܵ�String
	 * @param spkey �����SPKEY
	 * @return 3-DES���ܺ��String
	 */
	public static String encrypt(String src){
		String requestValue = "";
		try {
			byte[] enKey = getIns().getEnKey(SPKEY);//�õ�3-DES����Կ��
			byte[] src2 = src.getBytes("UTF-16LE");//Ҫ����3-DES���ܵ������ڽ���UTF-16LEȡ�ֽ�
			byte[] encryptedData = getIns().Encrypt(src2,enKey);//����3-DES���ܺ�����ݵ��ֽ�
			String base64String = getIns().getBase64Encode(encryptedData);//����3-DES���ܺ�����ݽ���BASE64����
			String base64Encrypt = getIns().filter(base64String);//BASE64����ȥ�����з�
			requestValue = getIns().getURLEncode(base64Encrypt);//��BASE64�����е�HTML���������ת��Ĺ���
//			System.out.println(requestValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * ���ַ�������URLDecoder.decode��strEncoding)����
	 * @param src Ҫ���н�����ַ���
	 * @return �������ַ���
	 */
	private String getURLDecoderdecode(String src){
		String  requestValue = "";
		requestValue = URLDecoder.decode(src);
		return requestValue;
	}
	
	/**
	 * ����3-DES���ܣ���Կ�׵�ͬ�ڼ��ܵ���Կ�ף�
	 * @param debase64 Ҫ����3-DES���ܵ�byte[]
	 * @param spKey �����SPKEY 
	 * @return 3-DES���ܺ��String 
	 */
	private String deCrypt(byte[] debase64){
		String strDe = null;
		try {
			byte ciphertext[] = cipher.doFinal(debase64);
			strDe = new String(ciphertext,"UTF-16LE");
		} catch (Exception e) {
			strDe = "";
			e.printStackTrace();
		}
		return strDe;
	}
	
	/**
	 * 3-DES����
	 * @param src Ҫ����3-DES����String
	 * @param spkey �����SPKEY
	 * @return 3-DES���ܺ��String
	 */
	public static String decrypt(String src){
		String requestValue = "";
		try {
			String URLValue = getIns().getURLDecoderdecode(src);//URLDecoder.decodeTML���������ת��Ĺ��� 
			byte[] base64DValue = base64Decode.decodeBuffer(URLValue);//����3-DES���ܺ�����ݽ���BASE64����
			requestValue = getIns().deCrypt(base64DValue);//Ҫ����3-DES���ܵ������ڽ���UTF-16LEȡ�ֽ�
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  requestValue;
		
	}
	
	
	
	private static Endecrypt getIns() {
		if(cr==null)new Endecrypt();
		return cr;
	}
}