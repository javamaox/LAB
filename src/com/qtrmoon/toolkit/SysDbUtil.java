package com.qtrmoon.toolkit;

import java.util.List;

import org.jdom.Element;

public class SysDbUtil {
	public static String driver_url;
	public static String db_user;
	public static String db_pwd;
	public static String db_url;
	public static String db_sid;
	
	public SysDbUtil(){
		initCfg();
	}
	private void initCfg(){
		if(driver_url==null||driver_url.equals("")){
			XmlUtil xmlUtil=new XmlUtil();
			xmlUtil.setCharset("UTF-8");
			Element root=xmlUtil.loadXml(SysDbUtil.class.getResourceAsStream("/proxoolconf.xml"));
			driver_url=root.getChildTextTrim("driver-url");
			String url=driver_url.substring(driver_url.indexOf("@")+1,driver_url.lastIndexOf(":"));
			url=url.substring(0,url.lastIndexOf(":"));
			String sid=driver_url.substring(driver_url.lastIndexOf(":")+1);
			String user="";
			String password="";
			List<Element> dirverPro=root.getChild("driver-properties").getChildren();
			for (int i = 0; i < dirverPro.size(); i++) {
				if (dirverPro.get(i).getAttributeValue("name").equals("user")) {
					user=dirverPro.get(i).getAttributeValue("value");
				}
				if (dirverPro.get(i).getAttributeValue("name").equals("password")) {
					password=dirverPro.get(i).getAttributeValue("value");
				}
			}
			db_url=url;
			db_sid=sid;
			db_user=user;
			db_pwd=password;
		}
	}
	/**
	 * 获取数据库类型
	 * @return [mysql,oracle,SQLServer,access,Derby...]
	 */
	public static String getDbType() {
		if(driver_url==null||driver_url.equals("")){
			new SysDbUtil();
		}
		String type=null;
		if(driver_url!=null&&!driver_url.equals("")){
			type=driver_url;
			type=type.substring(type.indexOf(":")+1);
			type=type.substring(0,type.indexOf(":"));
		}
		return type;
	}
}
