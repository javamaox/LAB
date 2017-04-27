package com.qtrmoon.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.qtrmoon.toolkit.XmlUtil;

public class Constant {
	public static String CONDITION_IN_SESSION = "CONDITION_IN_SESSION";// ��ѯ����session����

	public static Map<String, String> CONFIG_VALUE = new HashMap<String, String>();

	public static void push(String key, String value) {
		CONFIG_VALUE.put(key, value);
	}

	public static String getConstant(String key) {
		return CONFIG_VALUE.get(key.toUpperCase());
	}

	public static boolean getBooleanConstant(String key) {
		return Boolean.parseBoolean(CONFIG_VALUE.get(key.toUpperCase()));
	}

	/**
	 * 
	 */
	public static void createConstant() {
		String path = SystemConfigFilter.class.getResource("/").getPath()+"system-config.xml";
		path = path.replaceAll("%20", " ");
		XmlUtil xmlUtil = new XmlUtil();
		Element groupERoot = xmlUtil.loadXml(path);
		List<Element> constantList = groupERoot.getChildren();
		String workspace = null, projectName = null,nodeName;
		for (Element elem:constantList) {
			nodeName=elem.getName().toUpperCase();
			List<Element> cList = elem.getChildren();
			if(cList.size()>0){
				for (Element ce:cList) {
					push(nodeName+"."+ce.getName().toUpperCase(),ce.getValue());
					push(ce.getName().toUpperCase(),ce.getValue());
				}
			}else{
				push(nodeName, elem.getValue());
			}
		}

		Set key = CONFIG_VALUE.keySet();
		workspace = CONFIG_VALUE.get("WORKSPACE");
		projectName = CONFIG_VALUE.get("PROJECTNAME");
		String name,value;
		for (Iterator<String> iter = key.iterator(); iter.hasNext();) {
			name = iter.next();
			value = CONFIG_VALUE.get(name);
			value = value.replaceAll("\\$\\{workspace\\}", workspace).replaceAll("\\$\\{projectName\\}", projectName);
			CONFIG_VALUE.put(name, fmt(value));
		}
	}

	private static String fmt(String value) {
		value = value.replaceAll("\\\\", "/");
		while(value.indexOf("//")>=0){
			value=value.replaceAll("//", "/");
		}
		return value;
	}

	/**
	 * ������û�������������δ����ʱʹ�á�
	 * createConstant���ڱ��������initʱ���õģ�����ڹ�������ʼ��ǰʹ��getConstant���ȡ����ֵ������ContextServlet�����ļ���ʱ���޷�ʹ�á�
	 * @param string
	 * @return
	 */
	public static String getConstantNoServer(String name) {
		String res=null;
		String path = SystemConfigFilter.class.getResource("/").getPath()+ "system-config.xml";
		path = path.replaceAll("%20", " ");
		XmlUtil xmlUtil = new XmlUtil();
		Element root = xmlUtil.loadXml(path);
		List<Element> constantList = root.getChildren();
		String workspace = null, projectName = null, value;
		for (Element elem:constantList) {
			value = elem.getValue();
			if(elem.getName().equals("workspace")){
				workspace=value;
			}
			if(elem.getName().equals("projectName")){
				projectName=value;
			}
			if(elem.getName().equals(name)){
				value = value.replaceAll("[${}]", "#");// ���${}�����ַ�ת����#��
				res=value;
			}
		}
		if(res!=null){
			res = res.replaceAll("##workspace#", workspace);
			res = res.replaceAll("##projectName#", projectName);
			res = res.replaceAll("\\\\", "/");
			res = res.replaceAll("//", "/");
			res = res.replaceAll("//", "/");
		}
		return res;
	}
}
