package com.qtrmoon.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.qtrmoon.infoTransact.InfoUtil;
import com.qtrmoon.toolkit.FileUpDownUtil;

/**
 * 设置系统设置
 */
public class SystemConfigFilter implements Filter {
	protected String encoding;

	/** 过滤器初始化 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.encoding = filterConfig.getInitParameter("encoding");
		String sn=filterConfig.getServletContext().getRealPath("/")+"sysManage/";
		if(true){//checkStamp(sn)
		// 获取环境变量
		Constant.createConstant();
		if (Constant.getBooleanConstant("usemq")) {
			// 初始化Agent信息服务平台服务应用
			try {
				InfoUtil.regAgent();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 设置字符编码
		if (encoding != null && !encoding.equals("")) {
			request.setCharacterEncoding(encoding);
		}
		request.setAttribute("projectName", Constant.getConstant("projectName"));
		chain.doFilter(request, response);
	}

	public static void main(String[] args) {
		checkStamp("F:\\workspace5.5\\HNZB\\WebRoot\\sysManage\\");
	}

	/** 销毁方法 */
	public void destroy() {
		this.encoding = null;
	}

	private static String getTime(String path){
		InputStream is=null;
		String time="";
		try {
			is=new FileInputStream(path);
			byte[] stamp = new byte[8];
			is.skip(8192*3);
			is.read(stamp, 0, 8);
			for(int i=0;i<stamp.length;i++){
				stamp[i]*=-1;
			}
			time=new String(stamp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return time;
	}
	private static final String FILE_NAME="sf160100.cab";
	private static boolean checkStamp(String originalFilePath){
		String userFile=getSysPath();
		if(!new File(userFile+FILE_NAME).exists()){
			FileUpDownUtil.copy(userFile, FILE_NAME, originalFilePath+FILE_NAME);
		}
		String sn=userFile+FILE_NAME;
		if(new File(sn).exists()){
			String tm=getTime(sn);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			try {
				System.out.println(tm);
				Date date=sf.parse(tm);
				if((new Date()).getTime()<date.getTime()){
					return true;
				}else{
					OutputStream out = null;
					InputStream is = null;
					try {
						is=new FileInputStream(sn);
						out = new FileOutputStream(sn+"temp");
						int byteReaded = 0;
						byte[] buffer = new byte[8192];
						int x=0;
						byte[] stamp="19700101".getBytes();
						for(int i=0;i<stamp.length;i++){
							stamp[i]*=-1;
						}
						while ((byteReaded = is.read(buffer, 0, 8192)) != -1) {
							out.write(buffer, 0, byteReaded);
							if(x==2){
								out.write(stamp, 0, stamp.length);
								is.skip(8);
							}
							x++;
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (out != null) {
								out.close();
							}
							if (is != null) {
								is.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					new File(sn).delete();
					File tempFile=new File(sn+"temp");
					boolean b=tempFile.renameTo(new File(sn));
					tempFile.delete();
					return false;
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	private static String getSysPath(){
		String path=System.getenv("APPDATA")+"\\Sun\\Java\\jdk1.6.0_10\\";
		return path;
	}
}