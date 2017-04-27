package com.qtrmoon.toolkit;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public class EncryptUtil {
	/**
	 * ����
	 * 
	 * @param is//����������
	 * @param keyIn//��Կ�ļ���
	 * @return
	 */
	public static CipherInputStream encrypt(InputStream is, InputStream keyIn) {
		Key key = null;
		try {
			// ��Կ���Ķ���
			key = (Key) (((ObjectInputStream) keyIn).readObject());
			keyIn.close();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			return cis;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����
	 * 
	 * @param is//�����ļ���
	 * @param keyIn//��Կ�ļ���
	 * @return
	 */
	public static CipherInputStream decrypt(InputStream is, InputStream keyIn) {
		Key key = null;
		try {
			// ��Կ���Ķ���
			key = (Key) (((ObjectInputStream) keyIn).readObject());
			keyIn.close();
			Cipher cipher;
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			return cis;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ������Կ
	 * 
	 * @param keyOut//���ɵ���Կ
	 */
	public static void genKey(String keyPath) {
		ObjectOutputStream keyOut;
		try {
			keyOut = new ObjectOutputStream(new FileOutputStream(keyPath));
			Key key = null;
			KeyGenerator keyGen;
			keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128);
			key = keyGen.generateKey();
			keyOut.writeObject(key);
			keyOut.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ɹ�Կ��˽Կ
	 * 
	 * @param keyPath
	 */
	public static void genkeyDAS(String keyPath) {
		try {
			KeyPairGenerator pairgen = KeyPairGenerator.getInstance("DSA");
			SecureRandom random = new SecureRandom();
			pairgen.initialize(512, random);
			KeyPair keyPair = pairgen.generateKeyPair();
			PublicKey pubkey = keyPair.getPublic();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(keyPath + "public.key"));
			out.writeObject(pubkey);
			PrivateKey privatekey = keyPair.getPrivate();
			out = new ObjectOutputStream(new FileOutputStream(keyPath
					+ "private.key"));
			out.writeObject(privatekey);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * DAS����
	 * 
	 * @param privateKeyPath
	 *            ˽Կ
	 * @param message
	 *            Ҫ���ܵ��ļ�
	 * @param cryptograph
	 *            ����
	 */
	public static void encryptDAS(String privateKeyPath, byte[] message,
			String cryptograph) {
		try {
			ObjectInputStream keyIn = new ObjectInputStream(
					new FileInputStream(privateKeyPath));
			PrivateKey privateKey = (PrivateKey) keyIn.readObject();
			keyIn.close();

			Signature signalg = Signature.getInstance("DSA");
			signalg.initSign(privateKey);

			// Ҫ���ܵ��ļ�
			// File file = new File(cryptograph);
			// InputStream in = new FileInputStream(file);
			// int length = (int) file.length();
			// byte[] message = new byte[length];
			// in.read(message, 0, length);
			// in.close();

			signalg.update(message);
			byte[] signature = signalg.sign();

			// ��������
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					cryptograph));
			int signlength = signature.length;
			out.writeInt(signlength);
			out.write(signature, 0, signlength);
			out.write(message, 0, message.length);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * DAS����
	 * 
	 * @param publicKeyPath ��Կ
	 * @param cryptograph ����
	 * @return
	 */
	public static byte[] decryptDAS(String publicKeyPath ,String cryptograph) {
		try {
			ObjectInputStream keyIn = new ObjectInputStream(
					new FileInputStream(publicKeyPath));
			PublicKey publicKey = (PublicKey) keyIn.readObject();
			keyIn.close();

			Signature signalg = Signature.getInstance("DSA");
			signalg.initVerify(publicKey);

			File file = new File(cryptograph);
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int signlength = in.readInt();
			byte[] signature = new byte[signlength];
			in.read(signature, 0, signlength);

			int length = (int) file.length() - signlength - 4;
			byte[] message = new byte[length];
			in.read(message, 0, length);
			in.close();

			signalg.update(message);
			if (!signalg.verify(signature)) {
				System.out.println("����");
				return null;
			} else {
				System.out.println("��ȷ");
				return message;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡ��ϢժҪ�������ݿ������ָ��
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] computeDigest(String filePath) {
		byte[] hash = null ;
		try {
			MessageDigest currentAlgorithm = MessageDigest.getInstance("SHA-1");
			currentAlgorithm.reset();
			File file = new File(filePath);
			InputStream in = new FileInputStream(file);
			int length = (int) file.length();
			byte[] message = new byte[length];
			in.read(message, 0, length);
			in.close();
			currentAlgorithm.update(message);
			hash = currentAlgorithm.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	/**
	 * ����ϢժҪתΪ20�ֽ��ַ���
	 * 
	 * @param hash
	 * @return
	 */
	public static String bytetostring(byte[] hash){
		String d = "";
		for (int i = 0; i < hash.length; i++) {
			int v = hash[i] & 0xFF;
			if (v < 16) {
				d += "0";
			}
			d += Integer.toString(v, 16).toUpperCase() + " ";
		}
		System.out.println(d);
		return d;
	}
}
