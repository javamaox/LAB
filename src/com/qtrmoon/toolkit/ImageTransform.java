package com.qtrmoon.toolkit;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageTransform {
	// ͼƬ��͸ߵ����ߴ�
	public static final int IMAGE_MAX_BIG = 2000;

	// ͼƬ��͸ߵ���С�ߴ�
	public static final int IMAGE_MIN_BIG = 10;

	// ��ָ���Ĵ�С������ͼ
	private static final int LIMIT_ON_WIDTH_HEIGHT = 1;

	// ��ԭͼ��߱���������ͼ-��ָ���Ŀ��
	private static final int LIMIT_WIDTH_IN_SCALE = 2;

	// ��ԭͼ��߱���������ͼ-��ָ���ĸ߶�
	private static final int LIMIT_HEIGHT_IN_SCALE = 3;

	// ��ԭͼ��߱���������ͼ-��ָ���Ŀ�͸��нϴ�ĳߴ硣����������newͼ��ʹnewͼ��oldͼ��
	private static final int IN_MAX_SIZE_IN_SCALE = 4;

	// ��ԭͼ��߱���������ͼ-��ָ���Ŀ�͸��н�С�ĳߴ硣����������newͼ��ʹoldͼ��newͼ��
	private static final int IN_MIN_SIZE_IN_SCALE = 5;
	
	private static Image mark = null;//ͼƬˮӡ
	private static int markWidth,markHeight;
	
	public ImageTransform(){
		
	}
	
	//-------------------
	public static void loadMark(File file){
		try {
			if(mark==null){
				mark=ImageIO.read(file);
				markWidth=mark.getWidth(null);
				markHeight=mark.getHeight(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void updMark(File file){
		mark=null;
		loadMark(file);
	}
	//-------------------
	public static void resize(String resourceFile,String targetFile,int newW,int newH) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_ON_WIDTH_HEIGHT, newW,newH,false);
	}
	public static void resize(InputStream is,String targetFile,int newW,int newH) throws Exception{
		createNewImage(is,targetFile, LIMIT_ON_WIDTH_HEIGHT, newW,newH,false);
	}
	//-------------------
	public static void resizeOnWidth(String resourceFile,String targetFile,int newW) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_WIDTH_IN_SCALE, newW,0,false);
	}
	public static void resizeOnWidth(InputStream is,String targetFile,int newW) throws Exception{
		createNewImage(is,targetFile, LIMIT_WIDTH_IN_SCALE, newW,0,false);
	}
	//-------------------
	public static void resizeOnHeight(String resourceFile,String targetFile,int newH) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_HEIGHT_IN_SCALE, 0,newH,false);
	}
	public static void resizeOnHeight(InputStream is,String targetFile,int newH) throws Exception{
		createNewImage(is,targetFile, LIMIT_HEIGHT_IN_SCALE, 0,newH,false);
	}
	//-------------------
	
	
	
	
	//-------------------
	public static void resizeWithMark(String resourceFile,String targetFile,int newW,int newH) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_ON_WIDTH_HEIGHT, newW,newH,true);
	}
	public static void resizeWithMark(InputStream is,String targetFile,int newW,int newH) throws Exception{
		createNewImage(is,targetFile, LIMIT_ON_WIDTH_HEIGHT, newW,newH,true);
	}
	//-------------------
	public static void resizeOnWidthWithMark(String resourceFile,String targetFile,int newW) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_WIDTH_IN_SCALE, newW,0,true);
	}
	public static void resizeOnWidthWithMark(InputStream is,String targetFile,int newW) throws Exception{
		createNewImage(is,targetFile, LIMIT_WIDTH_IN_SCALE, newW,0,true);
	}
	//-------------------
	public static void resizeOnHeightWithMark(String resourceFile,String targetFile,int newH) throws Exception{
		createNewImage(resourceFile,targetFile, LIMIT_HEIGHT_IN_SCALE, 0,newH,true);
	}
	public static void resizeOnHeightWithMark(InputStream is,String targetFile,int newH) throws Exception{
		createNewImage(is,targetFile, LIMIT_HEIGHT_IN_SCALE, 0,newH,true);
	}
	//-------------------
	public static void mark(String is,String targetFile) throws FileNotFoundException{
		mark(new FileInputStream(is),targetFile);
	}
	public static void mark(InputStream is,String targetFile){
		try {
			Image src;
			src = ImageIO.read(is);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			// ����Ŀ��ͼƬ
			src.getGraphics().drawImage(src, 0, 0, w, h, null);
			BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
	
			// �������󻭳�Ŀ��ͼƬ ��ʽ1
			tag.getGraphics().drawImage(src, 0, 0, w, h, null);
			
			tag.getGraphics().drawImage(mark, w-markWidth, h-markHeight, markWidth, markHeight, null);
			// �����õ�Ŀ��ͼ���������� ��ʽ1
			OutputStream out=new FileOutputStream(targetFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param _file
	 *            ԭͼƬ
	 * @param createType
	 *            ��������
	 * @param newW
	 *            �¿��
	 * @param newH
	 *            �¸߶�
	 * @return
	 * @throws Exception
	 * 
	 */
	private static void createNewImage(String resourceFile,String outFileName, int createType, int newW,int newH,boolean drawMark) throws Exception {
		File _file=new File(resourceFile);
		if (_file == null){
			System.out.println("ͼƬԴ�ļ�δ�ҵ���");
			return;
		}
		while(outFileName.indexOf("/")>0){
			outFileName=outFileName.replace("/", File.separator);
		}
		File out=new File(outFileName.substring(0,outFileName.lastIndexOf(File.separator)));
		if (!out.exists()){
			out.mkdirs();
		}
		String filePath = _file.getPath();
		String extName=filePath.substring(filePath.lastIndexOf(".")+1);
		// �õ�ԭͼ��Ϣ
		if (!_file.exists()||!_file.isAbsolute()||!_file.isFile()||!checkImageFile(extName)){
			return;
		}
		FileInputStream fi=new FileInputStream(_file);
		createNewImage(fi,outFileName,createType,newW,newH,drawMark);
		fi.close();
	}
	private static void createNewImage(InputStream is,String outFileName, int createType, int newW,int newH,boolean drawMark) throws Exception {
		if (newW < IMAGE_MIN_BIG){
			newW = IMAGE_MIN_BIG;
		}else if (newW > IMAGE_MAX_BIG){
			newW = IMAGE_MAX_BIG;
		}
		if (newH < IMAGE_MIN_BIG){
			newH = IMAGE_MIN_BIG;
		}else if (newH > IMAGE_MAX_BIG){
			newH = IMAGE_MAX_BIG;
		}
		Image src = ImageIO.read(is);
		int w = src.getWidth(null);
		int h = src.getHeight(null);

		// ȷ��Ŀ��ͼƬ�Ĵ�С
		int nw = w;
		int nh = h;
		if(createType == LIMIT_ON_WIDTH_HEIGHT) {
			nw = newW;
			nh = newH;
		}else if(createType == LIMIT_WIDTH_IN_SCALE) {
			nw = newW;
			nh = (int) ((double) h / (double) w * nw);
		}else if(createType == LIMIT_HEIGHT_IN_SCALE) {
			nh = newH;
			nw = (int) ((double) w / (double) h * nh);
		}else if(createType == IN_MAX_SIZE_IN_SCALE) {
			if((double)w/(double)h>=(double)newW/(double)newH){
				nh = newH;
				nw = (int)((double)w/(double)h*nh);
			}else{
				nw = newW;
				nh = (int)((double)h/(double)w*nw);
			}
		} else if (createType == IN_MIN_SIZE_IN_SCALE){
			if ((double)w/(double)h<=(double)newW/(double)newH){
				nh = newH;
				nw = (int)((double)w/(double)h*nh);
			}else{
				nw = newW;
				nh = (int)((double)h/(double)w*nw);
			}
		}

		// ����Ŀ��ͼƬ
		BufferedImage tag = new BufferedImage(nw, nh,BufferedImage.TYPE_INT_RGB);

		// �õ�Ŀ��ͼƬ�����

		// �������󻭳�Ŀ��ͼƬ ��ʽ1
		tag.getGraphics().drawImage(src, 0, 0, nw, nh, null);
		if(drawMark){
			if(mark!=null){
				tag.getGraphics().drawImage(mark, w-markWidth, h-markHeight, markWidth, markHeight, null);
			}else{
				System.out.println("The wartermark is null!");
			}
		}
		// �����õ�Ŀ��ͼ���������� ��ʽ1
		createPath(outFileName);
		OutputStream out=new FileOutputStream(outFileName);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(tag);
		out.close();
		is.close();
	}

	private static void createPath(String outFileName) {
		String basePath=outFileName.substring(0,outFileName.lastIndexOf("/")+1);
		File file = new File(basePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private static boolean checkImageFile(String extName) {
		if ("jpg".equalsIgnoreCase(extName))
			return true;
		if ("gif".equalsIgnoreCase(extName))
			return true;
		if ("bmp".equalsIgnoreCase(extName))
			return true;
		if ("jpeg".equalsIgnoreCase(extName))
			return true;
		if ("png".equalsIgnoreCase(extName))
			return true;
		return false;
	}
	
	public void rotate(Image image,double theta){
		AffineTransform transform = new AffineTransform();
		transform.rotate(theta);
	}
	
	public static void main(String args[]){
		String url="C:\\Documents and Settings\\javamao\\My Documents\\k1_xJa814ZzcXw2.jpg";
		try {
//			resizeOnHeight(url,"", 100);
			loadMark(new File("C:\\Documents and Settings\\javamao\\My Documents\\watermark.png"));
			mark("C:\\Documents and Settings\\javamao\\My Documents\\xx.jpg","C:\\Documents and Settings\\javamao\\My Documents\\xx.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
