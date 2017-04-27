package com.qtrmoon.toolkit;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
/**
 * 文件比较器，可以按文件“名称”、“大小”、“最后修改时间”、“扩展名”排序。规则同系统文件夹的排序。
 */
public class FileSort implements Comparator<File> {
	public static final int LASTMODIFIED=-1;
	public static final int SIZE=0;
	public static final int NAME=1;
	public static final int TYPE=2;
	
	private FileSort instance;
	/**
	 * 比较类型---文件名、修改时间、文件大小
	 */
	private int comparator_flag;
	/**
	 * 是否降序
	 */
	private boolean isDesc;
	/**
	 * 按参数排序，正序优先文件夹。
	 * @param comprator_flag
	 * 	 LASTMODIFIED时，按最后修改时间排序
	 *   SIZE时，按文件大小排序
	 *   NAME时，按文件名称排序
	 * @param isDesc
	 * 		默认为true   降序排列
	 *           false  升序排列
	 */
	public FileSort(int comparator_flag,boolean isDesc){
		this.comparator_flag=comparator_flag;
		this.isDesc=isDesc;
		this.instance=this;
	}
	/**
	 * 按参数正序，优先文件夹
	 * @param comprator_flag
	 * 	 LASTMODIFIED时，按最后修改时间排序
	 *   SIZE时，按文件大小排序
	 *   NAME时，按文件名称排序
	 */
	public FileSort(int comparator_flag){
		this.comparator_flag=comparator_flag;
		this.isDesc=false;
		this.instance=this;
	}
	/**
	 * 按文件名正序，优先文件夹
	 */
	public FileSort(){
		this.comparator_flag=NAME;
		this.isDesc=false;
		this.instance=this;
	}
	
	public int compare(File f1, File f2) {
		if(f1.isDirectory()^f2.isDirectory()){//f1和f2相异，即一个文件，一个目录时。
			if(isDesc){//倒序文件夹优先
				return f1.isFile()==true?-1:1;
			}else{//正序文件优先
				return f1.isFile()==true?1:-1;
			}
		}
		if(comparator_flag==LASTMODIFIED){//最后修改时间排序
			if(isDesc){
				return -sign(f1.lastModified() - f2.lastModified());
			}else{
				return sign(f1.lastModified() - f2.lastModified());
			}
		}else if(comparator_flag==SIZE){//文件大小排序
			if(isDesc){
				return -sign(f1.length() - f2.length());
			}else{
				return sign(f1.length() - f2.length());
			}
		}else if(comparator_flag==NAME){//文件名称排序
			return compCN(f1.getName(),f2.getName());
		}else if(comparator_flag==TYPE){//文件类型排序
			String type1=getExtendName(f1.getName());
			String type2=getExtendName(f2.getName());
			if(type1.equals(type2)){//类型相同或文件夹按名称排
				return compCN(f1.getName(),f2.getName());
			}
			if(isDesc){//降序
				return -type1.compareTo(type2);
			}else{//升序
				return type1.compareTo(type2); 
			}
		}
		return 0;
	}

	/**
	 * 获取扩展名
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
	 * 中文字符串比较
	 * @param name1
	 * @param name2
	 * @return
	 */
	private int compCN(String name1, String name2) {
		if(isDesc){//降序
			return -Collator.getInstance(Locale.CHINESE).compare(name1, name2);
		}else{//升序
			return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
		}
	}
	
	/**
	 * 符号函数，+返回1，-返回-1，0返回0; 
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
