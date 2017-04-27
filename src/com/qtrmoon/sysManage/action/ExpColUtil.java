package com.qtrmoon.sysManage.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.qtrmoon.sysManage.bean.XmlCol;

public class ExpColUtil {
	public static List<XmlCol> getExpCol(String infoPath) {
		if (new File(infoPath).exists()) {
			try {
				Element groupERoot;
				groupERoot = (new SAXBuilder(false)).build(infoPath)
						.getRootElement();
				List<Element> colEList = groupERoot.getChildren();
				Element colE;
				List<XmlCol> colList = new ArrayList<XmlCol>();
				XmlCol col;
				for (int i = 0; i < colEList.size(); i++) {
					colE = colEList.get(i);
					col = new XmlCol();
					col.setName(colE.getChildText("name"));
					col.setInfo(colE.getChildText("info"));
					col.setValue(colE.getChildText("value"));
					col.setDict(colE.getChildText("dict"));
					col.setValidate(colE.getChildText("validate"));
					col.setScript(colE.getChildText("script"));
					colList.add(col);
				}
				return colList;
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void setExpCol(String infoModelPath, String infoPath,
			String[] expcolname, String[] expcolvalue) {
		try {
			Element groupERoot;
			Document doc = (new SAXBuilder(false)).build(infoModelPath);
			groupERoot = doc.getRootElement();
			List<Element> colEList = groupERoot.getChildren();
			Element colE;
			String colEName;
			for (int i = 0; i < colEList.size(); i++) {
				colE = colEList.get(i);
				colEName = colE.getChildText("name");
				for (int m = 0; m < expcolname.length; m++) {
					if (colEName.equals(expcolname[m])) {
						colE.getChild("value").setText(expcolvalue[m]);
						break;
					}
				}
			}

			Format fo = Format.getCompactFormat();// 取得输出格式Format
			fo.setEncoding("GB2312");
			XMLOutputter outputter = new XMLOutputter(fo);
			FileWriter write = new FileWriter(infoPath);
			outputter.output(doc, write);
			write.close();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValidate(List<XmlCol> colList) {
		String scriptCode="\nfunction val(obj,name){\n";
		if(colList!=null){
			XmlCol col;
			String[] valType;
			boolean isSpecial;
			for (int i = 0; i < colList.size(); i++) {
				col = colList.get(i);
				if (!col.getValidate().equals("")) {
					valType = col.getValidate().split(",");
					scriptCode += "  if(name=='" + col.getName() + "'){\n";
					isSpecial = false;
					for (int m = 0; m < valType.length; m++) {
						scriptCode += getValByType(valType[m]);
						if (valType[m].equals("special")) {
							isSpecial = true;
						}
					}
					if (isSpecial) {
						scriptCode += "    "+col.getScript()+"\n";
					}
					scriptCode += "  }\n";
				}
			}
		}
		scriptCode += "}\n";
		return scriptCode;
	}

	private static String getValByType(String type) {
		String res = "";
		if (type.indexOf("empty") >= 0) {
			res += "    if(obj.value==''){\n";
		} else if (type.indexOf("number") >= 0) {
			res += "    if(!isNum(obj.value)){\n";
		} else if (type.indexOf("length_") >= 0) {
			String length = type.substring(7);
			res += "    if(obj.value.length<=" + length + "){\n";
		} else if (type.indexOf("char") >= 0) {
			res += "    if(isChar(obj.value)){\n";
		}
		if (!res.equals("")) {
			if (type.indexOf("#") > 0) {
				res += "      alert('" + type.substring(type.indexOf("#") + 1) + "');\n";
			} else {
				res += "      alert()\n";
			}
			res += "      obj.focus();\n";
			res += "      return false;\n";
			res += "    }\n";
		}
		return res;
	}
}
