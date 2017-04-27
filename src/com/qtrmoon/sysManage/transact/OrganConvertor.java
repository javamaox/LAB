package com.qtrmoon.sysManage.transact;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.toolkit.DateTransfer;

public class OrganConvertor {

	/**
	 * �ѹ��ĵ�formת����Ϊxml�ļ���Ȼ��ת����Ϊbyte�������ڴ��͡�
	 * 
	 * @param archiveForm
	 * @return
	 */
	public static byte[] form2XML_Organ(OrganForm organForm) {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version='1.0'  encoding='GBK' ?>\r\n");
		sb.append("<item>\r\n");
		sb.append("<code>" + organForm.getId() + "</code>\r\n");
		sb.append("<pcode>" + organForm.getPid() + "</pcode>\r\n");
		sb.append("<name>" + organForm.getName() + "</name>\r\n");
		sb.append("</item>");
		return sb.toString().getBytes();
	}

	/**
	 * �ѽ��յ���byte����ת����Ϊxml�ļ���Ȼ����xml�ļ����������ĵ�FORM
	 * 
	 * @param bytes
	 * @return
	 */
	public static OrganForm xml2Form_Organ(byte[] bytes) {
		OrganForm result = new OrganForm();
		try {
			File file = File.createTempFile("arch", "xml");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			SAXBuilder saxB = new SAXBuilder();
			Document document = saxB.build(file);
			Element root = document.getRootElement();
			List<Element> children = root.getChildren();
			for (int i = 0; i < children.size(); i++) {
				Element child = children.get(i);
				if (child.getName().equals("code")
						&& !child.getName().equals("null")) {
					result.setId(child.getText());
				} else if (child.getName().equals("pcode")
						&& !child.getName().equals("null")) {
					result.setPid(child.getText());
				} else if (child.getName().equals("name")
						&& !child.getName().equals("null")) {
					result.setName(child.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
