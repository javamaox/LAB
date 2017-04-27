package com.qtrmoon.toolkit;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlUtil {
	private Document doc;

	private String charset = "";

	private String path;

	public Element loadXml(String path) {
		this.path = path;
		Element root = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			FileInputStream fi;
			fi = new FileInputStream(path);
			InputStreamReader isr;
			if (charset.equals("")) {
				isr = new InputStreamReader(fi);
			} else {
				isr = new InputStreamReader(fi, charset);
			}
			doc = sb.build(isr);
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}
	
	public Element loadXmlStr(String str) {
		Element root = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			StringReader sr=new StringReader(str);
			doc = sb.build(sr);
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	/** 可以忽略识别<!DOCTYPE节点的解析 */
	public Element loadXmlNoFeature(String path) {
		this.path = path;
		Element root = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			sb
					.setFeature(
							"http://apache.org/xml/features/nonvalidating/load-external-dtd",
							false);
			FileInputStream fi;
			fi = new FileInputStream(path);
			InputStreamReader isr;
			if (charset.equals("")) {
				isr = new InputStreamReader(fi);
			} else {
				isr = new InputStreamReader(fi, charset);
			}
			doc = sb.build(isr);
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	public Element loadXml(InputStream cataIs) {
		Element root = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			InputStreamReader isr;
			if (charset.equals("")) {
				isr = new InputStreamReader(cataIs);
			} else {
				isr = new InputStreamReader(cataIs, charset);
			}
			doc = sb.build(isr);
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	/** 保存xml文档，存储到load时的path。 */
	public void saveXml() {
		try {
			Format fo = getFormat();
			if (charset.equals("")) {
				;
			} else {
				fo.setEncoding(charset);
			}
			XMLOutputter outputter = new XMLOutputter(fo);
			FileWriter write;
			write = new FileWriter(path);
			outputter.output(doc, write);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 保存xml文档，另存到newPath。 */
	public void saveAs(String newPath) {
		try {
			Format fo = getFormat();
			if (charset.equals("")) {
				;
			} else {
				fo.setEncoding(charset);
			}
			XMLOutputter outputter = new XMLOutputter(fo);
			FileWriter write;
			write = new FileWriter(newPath);
			outputter.output(doc, write);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取xml格式对象。
	 */
	private static Format getFormat() {
		Format fo = Format.getCompactFormat();// 取得输出格式Format
		fo.setLineSeparator("\n");
		fo.setIndent("  ");
		fo.setEncoding("GB2312");
		return fo;
	}

	/**
	 * 构造简单的标签
	 * @param tag;//标签名
	 * @param content;//标签的body
	 * @return
	 */
	public static Content simpleElem(String tag, String content) {
		Element elem = new Element(tag);
		elem.setText(content);
		return elem;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
