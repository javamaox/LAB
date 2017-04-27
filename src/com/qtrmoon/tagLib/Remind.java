package com.qtrmoon.tagLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.qtrmoon.common.Constant;
import com.qtrmoon.sysManage.action.LoginAction;
import com.qtrmoon.sysManage.bean.RemindBean;

public class Remind extends TagSupport {
	private int imgWidth=0,imgHeight=0;//

	private String align="left";//[left,right,center]
	
	private static List<RemindBean> remindList = null;
	
	public int doStartTag() {
		try {
			HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
			request.setAttribute("list", getRemindList());
			String width="",height="";
			if(imgWidth!=0){
				width="width='"+imgWidth+"'";
			}
			if(imgHeight!=0){
				width="height='"+imgHeight+"'";
			}
			request.setAttribute("width", width);
			request.setAttribute("height", height);
			request.setAttribute("align", align);
			pageContext.include("/remind/remind_include.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
	
	private List<RemindBean> getRemindList(){
		if (remindList == null) {
			try {
				remindList = new ArrayList<RemindBean>();
				String path = LoginAction.class.getResource("/").getPath()
						+ "remind-config.xml";
				path = path.replaceAll("%20", " ");
				Element groupERoot;
				groupERoot = (new SAXBuilder(false)).build(path)
						.getRootElement();
				List<Element> elemList = groupERoot.getChildren("remind");
				for (Element elem : elemList) {
					remindList
							.add(new RemindBean(elem.getChildText("id"), elem
									.getChildText("link"), elem
									.getChildText("ajaxlink"), elem
									.getChildText("ico"), elem
									.getChildText("text")));
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return remindList;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int height) {
		this.imgHeight = height;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int width) {
		this.imgWidth = width;
	}

	
}
