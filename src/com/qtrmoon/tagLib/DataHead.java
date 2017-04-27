package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;

public class DataHead extends TagSupport {
	private String title;

	private boolean expand = true;//使用Js实现

	private String button;

	private String tableClass = "tableFrameForm";//页面使用[form,list]

	private String width="100%";
	private String type;//simple:简单底，只有一个div，忽略button。
	

	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			out.println(getCode(title,expand,button,tableClass,width,type));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageContext.setAttribute("expand", expand);
		return (SKIP_BODY);
	}
	public static String getCode(String title){
		return getCode(title,true,"","list","100%","");
	}
	public static String getCode(String title,boolean expand,String button,String tableClass,String width,String type){
		StringBuffer res=new StringBuffer();
		if(tableClass.equals("form")){
			tableClass = "tableFrameForm";
		}else if(tableClass.equals("list")){
			tableClass = "tableFrameData";
		}
		res.append("<div style='width:"+width+"'>");
		if("simple".equals(type)){
			res.append("<div class='"+tableClass+"' style='border-top:#849eb5 1px solid'>");
		}else{
			String className;
			String display;
			if(expand){
				display="display:;";
				className = "tableFrameTitleMinus";
			}else{
				display="display:none;";
				className = "tableFrameTitlePlus";
			}
			String showExpIco="";
			if (title == null||title.equals("")) {
				title = "&nbsp;";
				showExpIco="style='display:none;'";
			}
			res.append("<div class='tableFrameT'><div class=\"tableFrameTC\"><div class=\"tableFrameTL\"><div class=\"tableFrameTR\">");
			res.append("<span class=\"" + className + "\" expand='"+expand+"'"+showExpIco+">&nbsp;</span>" + title);
			if (button != null && button.length() != 0) {
				String[] temp;
				String value;
				String js;
				res.append("<div class='tableFrameTBtn'>");
				if (button.indexOf(",") >= 0) {
					temp = button.split(",");
					for (int i = 0; i + 1 < temp.length; i += 2) {
						if(temp[i+1].indexOf("class:")>=0){
							res.append("<input type=\"button\" value=\"" + temp[i]+ "\" class=\"" + temp[i + 1].substring(temp[i+1].indexOf("class:")+6) + "\"/>");
						}else{
							res.append("<input type=\"button\" value=\"" + temp[i]+ "\" onclick=\"" + temp[i + 1] + "\"/>");
						}
					}
				} else {
					value = "";
					js = button;
					res.append("<input type=\"button\" value=\"" + value+ "\" onclick=\"" + js + "\"/>");
				}
				res.append("</div>");
			}
			res.append("</div></div></div></div>");
			res.append("<div class=\"" + tableClass+ "\" style='clear:both;"+display+"'>");
		}
		return res.toString();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public String getTableClass() {
		return tableClass;
	}

	public void setTableClass(String tableClass) {
		this.tableClass = tableClass;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
