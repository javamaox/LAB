package com.qtrmoon.toolkit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public String init(String zipFile) {

		return null;
	}

	public static final String EXT = ".zip";

	private static final String BASE_DIR = "";

	private static final String PATH = "/";

	private static final int BUFFER = 1024;

	/**
	 * ѹ��
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void compress(String srcPath) {
		File srcFile = new File(srcPath);
		String name = srcFile.getName();
		String basePath = srcFile.getParent();
		String destPath = basePath + name + EXT;
		compress(new String[]{srcPath}, destPath);
	}
	public static void compress(String srcPath,String destPath) {
		compress(new String[]{srcPath}, destPath);
	}

	/**
	 * ѹ��
	 * 
	 * @param srcFile
	 *            Դ·��
	 * @param destPath
	 *            Ŀ��·��
	 * @throws Exception
	 */
	public static void compress(String[] srcFile, String destFile) {
		// ������ļ���CRC32У��
		try {
			CheckedOutputStream cos;
			cos = new CheckedOutputStream(new FileOutputStream(
					destFile), new CRC32());
			ZipOutputStream zos = new ZipOutputStream(cos);
			for(String src:srcFile){
				compress(new File(src), zos, BASE_DIR);
			}
			zos.closeEntry();
			zos.flush();
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ѹ��
	 * 
	 * @param srcFile
	 *            Դ·��
	 * @param zos
	 *            ZipOutputStream
	 * @param basePath
	 *            ѹ���������·��
	 * @throws Exception
	 */
	private static void compress(File srcFile, ZipOutputStream zos,
			String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, zos, basePath);
		} else {
			compressFile(srcFile, zos, basePath);
		}
	}

	/**
	 * ѹ��Ŀ¼
	 * 
	 * @param dir
	 * @param zos
	 * @param basePath
	 * @throws Exception
	 */
	private static void compressDir(File dir, ZipOutputStream zos,
			String basePath) throws Exception {
		for (File file : dir.listFiles()) {
			// �ݹ�ѹ��
			compress(file, zos, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * �ļ�ѹ��
	 * 
	 * @param file
	 *            ��ѹ���ļ�
	 * @param zos
	 *            ZipOutputStream
	 * @param dir
	 *            ѹ���ļ��еĵ�ǰ·��
	 * @throws Exception
	 */
	private static void compressFile(File file, ZipOutputStream zos, String dir)
			throws Exception {
		/**
		 * ѹ�������ļ�������
		 * 
		 * <pre>
		 * ����ж༶Ŀ¼����ô�������Ҫ��������Ŀ¼���ļ���
		 * �����WinRAR��ѹ����������������ʾΪ����
		 * </pre>
		 */
		ZipEntry entry = new ZipEntry(dir + file.getName());
		zos.putNextEntry(entry);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			zos.write(data, 0, count);
		}
		bis.close();
	}
	
	public static void unZip(String zipFilePath,String unzipDirectory){
		try {
			File file=new File(zipFilePath);
			ZipFile zipFile = new ZipFile(file);
			
			String name=file.getName();
			name=name.substring(0,name.lastIndexOf("."));
			File unzipFile=new File(unzipDirectory+"/"+name);
			if(unzipFile.exists()){
				unzipFile.delete();
			}
			unzipFile.mkdirs();
			
			Enumeration zipEnum=zipFile.entries();
			InputStream input=null;
			OutputStream output=null;
			ZipEntry entry=null;
			while(zipEnum.hasMoreElements()){
				entry=(ZipEntry)zipEnum.nextElement();
				String entryName=new String(entry.getName().getBytes("ISO8859-1"),"GB2312");
				String[] names=entryName.split("\\/");
				String path=unzipFile.getAbsolutePath();
				for(int v=0;v<names.length;v++){
					if(v<names.length-1){
						path+="/"+names[v]+"/";
						new File(path).mkdirs();
					}else{
						if(entryName.endsWith("/")){
							new File(unzipFile.getAbsolutePath()+"/"+entryName).mkdirs();
						}else{
							input = zipFile.getInputStream(entry);
							output=new FileOutputStream(new File(unzipFile.getAbsolutePath()+"/"+entryName));
							byte[] buffer=new byte[1024*8];
							int readLen=0;
							while((readLen=input.read(buffer,0,1024*8))!=-1){
								output.write(buffer, 0, readLen);
							}
							input.close();
							output.flush();
							output.close();
						}
					}
				}
			}
			zipFile.close();
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		unZip("G:\\20120215125433.zip","G:\\");
	}
}
