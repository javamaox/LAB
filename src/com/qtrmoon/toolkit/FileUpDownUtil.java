package com.qtrmoon.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.upload.FormFile;

public class FileUpDownUtil {
	private String charset="GB2312";
	/**
	 * 
	 * @param formFile
	 *            传输的文件
	 * @param rootPath
	 *            要存储文件的目录
	 * @return 存储后的文件名
	 */
	public static String uploadFile(FormFile formFile, String rootPath) {
		return fileUp(formFile,rootPath);
	}
	
	public static String fileUp(FormFile formFile, String rootPath) {
		SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHMMssSS");
		String ext=formFile.getFileName();
		ext=ext.substring(ext.lastIndexOf("."));
		String fileName = format.format(new Date())+ext;
		try {
			fileUp(rootPath,fileName,formFile.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static void fileUp(String basePath, String fileName, InputStream is) {
		OutputStream os = null;
		int bytesRead = 0;
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			os = new FileOutputStream(basePath + fileName);
			byte[] buffer = new byte[10000];
			while ((bytesRead = is.read(buffer, 0, 10000)) != -1) {
				os.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			os.close();
			is.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void fileUp(String basePath, String fileName, byte[] is) {
		OutputStream os = null;
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			os = new FileOutputStream(basePath + fileName);
			os.write(is);// 将文件写入服务器
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void fileUp(String basePath, String fileName, String is) {
		FileWriter fw = null;
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			fw = new FileWriter(basePath + fileName);
			fw.write(is);// 将文件写入服务器
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param fileUrl
	 *            文件的绝对路径
	 * @param request
	 * @param response
	 * 
	 * 文件下载
	 */
	public static void fileDown(String fileUrl, HttpServletResponse response) {
		try {
			// 文件的绝对路径
			fileUrl = fileUrl.replaceAll("/", "\\\\");
			File file = new File(fileUrl);
			int location = fileUrl.lastIndexOf("\\");
			String name = fileUrl.substring(location + 1, fileUrl.length());// 文件名
			name = URLEncoder.encode(name, "utf-8");
			fileDown(new FileInputStream(file),name,response);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}

	/**
	 * @param is 文件流
	 * @param displayName 下载显示名，如要显示中文请传入编码后文字new String("阿萨德".getBytes(),"ISO8859-1")。
	 * @param response
	 * 
	 * 对加密文件的下载
	 */
	public static void fileDown(InputStream is, String displayName,
			HttpServletResponse response) {
		OutputStream os;
		try {
			os = response.getOutputStream();
			int bytesRead = 0;
			response.setHeader("Content-Disposition", "attachment;filename="+ displayName);
			// response.setContentLength(i);
			byte[] buffer = new byte[10000];
			while ((bytesRead = is.read(buffer, 0, 10000)) != -1) {
				os.write(buffer, 0, bytesRead);// 下载文件
			}
			os.close();
			is.close();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} finally{
		}
	}


	/**
	 * 复制文件方法
	 * @param basePath;//新文件位置
	 * @param fileName;//新文件名
	 * @param file;//被复制的文件
	 */
	public static void copy(String basePath, String fileName, String filePath) {
		OutputStream os = null;
		int bytesRead = 0;
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			File rf=new File(filePath);
			if(!rf.exists()){
				return;
			}
			InputStream is=new FileInputStream(rf);
			os = new FileOutputStream(basePath + fileName);
			byte[] buffer = new byte[10000];
			while ((bytesRead = is.read(buffer, 0, 10000)) != -1) {
				os.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			os.close();
			is.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delete(String path){
		delete(new String[]{path});
	}
	public static void delete(String[] path){
		for(String src:path){
			File f=new File(src);
			if(f.exists()){
				delete(f);
			}
		}
	}
	private static void delete(File srcFile){
		if (srcFile.isDirectory()) {
			for (File file : srcFile.listFiles()) {
				delete(file);
			}
		}
		srcFile.delete();
	}
	
	public static String readString(String path) {
		return readString(path,"GB2312");
	}
	public static String readString(String path,String charset) {
		try {
			return readString(new FileInputStream(path),charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String readString(InputStream is){
		return readString(is,"GB2312");
	}
	public static String readString(InputStream is,String charset){
		StringBuffer sb = new StringBuffer();
		BufferedReader br;
		try {
			InputStreamReader read = new InputStreamReader(is,charset);
			br = new BufferedReader(read);
			String line;
			while((line=br.readLine())!=null){
				sb.append(line+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 文件转为bytes16进制串
	 * @param cerFile;//文件地址。
	 * @return
	 */
	public static String fileToBytesHex(String filePath) {
		String res="";
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			int num;
			byte[] byter=new byte[1024];
			while((num=fis.read(byter))>0){
				for(int i=0;i<num;i++){
					String hex=Integer.toHexString(byter[i]);
					if(hex.length()==1){
						hex="0"+hex;
					}
					res+=hex;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 文件转为base64串
	 * @param cerFile;//文件地址。
	 * @return
	 */
	public static String fileToBase64(String filePath) {
		String res="";
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			int num;
			byte[] byter=new byte[1023];
			while((num=fis.read(byter))>0){
				if(num<1023){
					res+=Base64.encode(Arrays.copyOf(byter, num));
//					System.out.println(Arrays.toString(Arrays.copyOf(byter, num)));
				}else{
					res+=Base64.encode(byter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public static void base64ToFile(String filePath) {
		byte[] ss=Base64.decode(filePath.getBytes());
//		System.out.println();
//		System.out.println(Arrays.toString(ss));
		bytesToFile(ss, "c:\\new.jpg");
	}
	/**
	 * 文件转为bytes串
	 * @param cerFile;//文件地址。
	 * @return
	 */
	public static String fileToBytes(String filePath) {
		String res="";
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			int num;
			byte[] byter=new byte[1024];
			while((num=fis.read(byter))>0){
				for(int i=0;i<num;i++){
					res+=byter[i]+",";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!res.equals("")){
			res=res.substring(0,res.length()-1);
		}
		return res;
	}
	
	/**
	 * bytes写入文件。
	 * @param contents 16进制byte串。
	 * @param file
	 * @return
	 */
	public static void bytesHexToFile(String contents,String file) {
		mdPath(file);
		int len=contents.length()/2;
		byte[]conbytes=new byte[len];
		for(int i=0;i<len;i++){
			conbytes[i]=(byte)Integer.parseInt(contents.substring(i*2, i*2+2), 16);
		}
		bytesToFile(conbytes,file);
	}
	
	/**
	 * bytes写入文件。
	 * @param contents 逗号分割的byte串。
	 * @param file
	 * @return
	 */
	public static void bytesToFile(String contents,String file) {
		mdPath(file);
		String[] cons=contents.split(",");
		byte[]conbytes=new byte[cons.length];
		for(int i=0,ii=conbytes.length;i<ii;i++){
			conbytes[i]=Byte.parseByte(cons[i]);
		}
		bytesToFile(conbytes,file);
	}

	/**
	 * bytes写入文件。
	 * @param contents 字节流
	 * @param file
	 * @return
	 */
	public static void bytesToFile(byte[] contents,String file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(contents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void mdPath(String path){
		if(path.indexOf(".")>0){
			path=path.substring(0,path.lastIndexOf((".")));
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		
	}

	public static void main(String args[]){
		String a=fileToBase64("c:\\318744-130G122093214.jpg");
		fileUp("c:\\", "tu.txt", a);
//		base64ToFile(readString("c:\\aa.txt"));
	}
}
