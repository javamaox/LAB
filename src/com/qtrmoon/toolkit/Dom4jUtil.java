package com.qtrmoon.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

public class Dom4jUtil {
	private Document doc;

	private String charset = "GB2312";

	private String path;

	public Element loadXml(String path) {
		this.path = path;
		try {
			return loadXml(new InputStreamReader(new FileInputStream(new File(path)),charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Element loadXmlStr(String str) {
		return loadXml(new StringReader(str));
	}

	public Element loadXml(InputStream is) {
		Element root = null;
		try {
			SAXReader sb = new SAXReader();
			doc = sb.read(is);
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	
	public Element loadXml(InputStreamReader isr) {
		return loadXml((Reader)isr);
	}
	
	public Element loadXml(Reader isr) {
		Element root = null;
		try {
			SAXReader sb = new SAXReader();
			char[]xx=new char[1000];
			isr.read(xx);
			doc = sb.read(isr);
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	/** 保存xml文档，存储到load时的path。 */
	public void saveXml() {
		saveAs(path);
	}
	
	/** 保存xml文档，另存到newPath。 */
	public void saveAs(String newPath) {
		try {
			FileWriter write = new FileWriter(newPath);
			doc.write(write);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造简单的标签
	 * @param tag;//标签名
	 * @param content;//标签的body
	 * @return
	 */
	public static Element simpleElem(String tag, String content) {
		Element elem = new DefaultElement(tag);
		elem.setText(content);
		return elem;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public static void main(String args[]) throws Exception {
		SAXReader sb = new SAXReader();
		sb.setDefaultHandler(new Handler(){
			@Override
			public void doNode(Element element) {
				System.out.println(element.getName());
			}}
		);
		sb.read(new FileInputStream("c:/EXP_QLZY_DQJBQK.xml"));
		
//		Dom4jUtil dj=new Dom4jUtil();
//		Element root = dj.loadXml(new FileInputStream("c:/EXP_QLZY_DQJBQK.xml"));
//		
//		List<Element> tables=root.selectNodes("table");
//		for(Element table:tables){
//			Element tableNameEle=(Element)table.selectNodes("groups/group/value").get(0);
//			System.out.println(tableNameEle.getTextTrim());
//			table.detach();
//		}
	}
}
abstract class Handler implements ElementHandler{
	public abstract void doNode(Element element);
	
	public void onEnd(ElementPath ep) {
		Element element=ep.getCurrent();
		doNode(element);
		element.detach();
	}

	public void onStart(ElementPath arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}