package com.qtrmoon.toolkit;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
/**
 * �ļ��Ƚ��������԰��ļ������ơ�������С����������޸�ʱ�䡱������չ�������򡣹���ͬϵͳ�ļ��е�����
 */
public class FileSort implements Comparator<File> {
	public static final int LASTMODIFIED=-1;
	public static final int SIZE=0;
	public static final int NAME=1;
	public static final int TYPE=2;
	
	private FileSort instance;
	/**
	 * �Ƚ�����---�ļ������޸�ʱ�䡢�ļ���С
	 */
	private int comparator_flag;
	/**
	 * �Ƿ���
	 */
	private boolean isDesc;
	/**
	 * �������������������ļ��С�
	 * @param comprator_flag
	 * 	 LASTMODIFIEDʱ��������޸�ʱ������
	 *   SIZEʱ�����ļ���С����
	 *   NAMEʱ�����ļ���������
	 * @param isDesc
	 * 		Ĭ��Ϊtrue   ��������
	 *           false  ��������
	 */
	public FileSort(int comparator_flag,boolean isDesc){
		this.comparator_flag=comparator_flag;
		this.isDesc=isDesc;
		this.instance=this;
	}
	/**
	 * ���������������ļ���
	 * @param comprator_flag
	 * 	 LASTMODIFIEDʱ��������޸�ʱ������
	 *   SIZEʱ�����ļ���С����
	 *   NAMEʱ�����ļ���������
	 */
	public FileSort(int comparator_flag){
		this.comparator_flag=comparator_flag;
		this.isDesc=false;
		this.instance=this;
	}
	/**
	 * ���ļ������������ļ���
	 */
	public FileSort(){
		this.comparator_flag=NAME;
		this.isDesc=false;
		this.instance=this;
	}
	
	public int compare(File f1, File f2) {
		if(f1.isDirectory()^f2.isDirectory()){//f1��f2���죬��һ���ļ���һ��Ŀ¼ʱ��
			if(isDesc){//�����ļ�������
				return f1.isFile()==true?-1:1;
			}else{//�����ļ�����
				return f1.isFile()==true?1:-1;
			}
		}
		if(comparator_flag==LASTMODIFIED){//����޸�ʱ������
			if(isDesc){
				return -sign(f1.lastModified() - f2.lastModified());
			}else{
				return sign(f1.lastModified() - f2.lastModified());
			}
		}else if(comparator_flag==SIZE){//�ļ���С����
			if(isDesc){
				return -sign(f1.length() - f2.length());
			}else{
				return sign(f1.length() - f2.length());
			}
		}else if(comparator_flag==NAME){//�ļ���������
			return compCN(f1.getName(),f2.getName());
		}else if(comparator_flag==TYPE){//�ļ���������
			String type1=getExtendName(f1.getName());
			String type2=getExtendName(f2.getName());
			if(type1.equals(type2)){//������ͬ���ļ��а�������
				return compCN(f1.getName(),f2.getName());
			}
			if(isDesc){//����
				return -type1.compareTo(type2);
			}else{//����
				return type1.compareTo(type2); 
			}
		}
		return 0;
	}

	/**
	 * ��ȡ��չ��
	 * @param name
	 * @return
	 */
	private String getExtendName(String name) {
		if(name.indexOf(".")>0){
			name=name.substring(name.lastIndexOf(".")+1);
		}else{
			name="";
		}
		return name;
	}
	
	/**
	 * �����ַ����Ƚ�
	 * @param name1
	 * @param name2
	 * @return
	 */
	private int compCN(String name1, String name2) {
		if(isDesc){//����
			return -Collator.getInstance(Locale.CHINESE).compare(name1, name2);
		}else{//����
			return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
		}
	}
	
	/**
	 * ���ź�����+����1��-����-1��0����0; 
	 * @param n
	 * @return
	 */
	private int sign(long n){
		int r=0;
		try{
			r=(int)(n/Math.abs(n));
		}catch(Exception e){
			
		}
		return r;
	}
	
	public void sort(File[] files){
		Arrays.sort(files, instance);
	}
	
	public static void main(String[] args) {
		
		File file=new File("C:\\Documents and Settings\\JaMao\\My Documents");
		File[] files =file.listFiles();
		new FileSort(FileSort.TYPE).sort(files);
		for(File f : files){
			System.out.println(new Date(f.lastModified())+"	"+f.getName() +"		"+f.length());
		}
	}
}
