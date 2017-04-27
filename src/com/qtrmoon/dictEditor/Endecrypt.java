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
 * 3-DES 加密解密封装类
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
		String oldString = "赵越";
		System.out.println("②.加密前内容："+oldString);
		String reValue = Endecrypt.encrypt(oldString);
		reValue = reValue.trim().intern();//返回字符串对象的规范化表示形式
		System.out.println("3-DES加密后："+reValue);
		String reValue2 = Endecrypt.decrypt(reValue);
		System.out.println("3-DES解密后："+reValue2);
		
		/* intern()方法测试 */
//		String s1 = new String("aaa");
//		String s2 = new String("aaa");
//		System.out.println(s1 == s2);//false
//		System.out.println(s1.intern() == s2.intern());//true
	}
	
	/**
	 * 进行MD5加密
	 * @param strSrc 原始的SPKEY
	 * @return 指定加密方式为md5后的byte[]
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
	 * 得到3DES的密钥匙
	 * 根据接口规范，密钥匙为24字节，md5加密出来的是16个字节，因此后面补8个字节的0
	 * @param spKey 原始的SPKEY
	 * @return 指定加密方式为md5后的byte[]
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
	 * 3-DES加密
	 * @param src 要进行3-DES加密的byte[] 
	 * @param enKey 3-DES加密密钥
	 * @return 3-DES加密后的byte[]
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
	 * 对字符串进行Base64编码
	 * @param src 要进行编码的字符
	 * @return 进行编码后的字符串
	 */
	private String getBase64Encode(byte[] src){
		String requestValue = "";
		BASE64Encoder base64en = new BASE64Encoder();
		requestValue = base64en.encode(src);
//		System.out.println(requestValue);
		return requestValue;
	}
	
	/**
	 * 去掉字符串的换行符号
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
	 * 对字符串进行URLDecoder.encode(strEncoding)编码
	 * @param src 要进行编码的字符串
	 * @return 进行编码后的字符串
	 */
	private String getURLEncode(String src){
		String requestValue = "";
		requestValue = URLEncoder.encode(src);
		return requestValue;
	}
	
	/**
	 * 3-DES加密
	 * @param src 要进行3-DES加密的String
	 * @param spkey 分配的SPKEY
	 * @return 3-DES加密后的String
	 */
	public static String encrypt(String src){
		String requestValue = "";
		try {
			byte[] enKey = getIns().getEnKey(SPKEY);//得到3-DES的密钥匙
			byte[] src2 = src.getBytes("UTF-16LE");//要进行3-DES加密的内容在进行UTF-16LE取字节
			byte[] encryptedData = getIns().Encrypt(src2,enKey);//进行3-DES加密后的内容的字节
			String base64String = getIns().getBase64Encode(encryptedData);//进行3-DES加密后的内容进行BASE64编码
			String base64Encrypt = getIns().filter(base64String);//BASE64编码去除换行符
			requestValue = getIns().getURLEncode(base64Encrypt);//对BASE64编码中的HTML控制码进行转义的过程
//			System.out.println(requestValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 对字符串进行URLDecoder.decode（strEncoding)解码
	 * @param src 要进行解码的字符串
	 * @return 解码后的字符串
	 */
	private String getURLDecoderdecode(String src){
		String  requestValue = "";
		requestValue = URLDecoder.decode(src);
		return requestValue;
	}
	
	/**
	 * 进行3-DES解密（密钥匙等同于加密的密钥匙）
	 * @param debase64 要进行3-DES解密的byte[]
	 * @param spKey 分配的SPKEY 
	 * @return 3-DES解密后的String 
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
	 * 3-DES解密
	 * @param src 要进行3-DES解密String
	 * @param spkey 分配的SPKEY
	 * @return 3-DES加密后的String
	 */
	public static String decrypt(String src){
		String requestValue = "";
		try {
			String URLValue = getIns().getURLDecoderdecode(src);//URLDecoder.decodeTML控制码进行转义的过程 
			byte[] base64DValue = base64Decode.decodeBuffer(URLValue);//进行3-DES加密后的内容进行BASE64编码
			requestValue = getIns().deCrypt(base64DValue);//要进行3-DES加密的内容在进行UTF-16LE取字节
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