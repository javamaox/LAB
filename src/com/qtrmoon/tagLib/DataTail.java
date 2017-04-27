package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;

public class DataTail extends TagSupport {
	private boolean printer = false;

	private String submit = "";

	private String button;
	private String type;//simple:简单底，只有一个div，忽略button。
	
	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			Object o =pageContext.getAttribute("expand");
			boolean expand=true;
			if(o!=null){
				expand=(Boolean)o;
			}
			out.println(getCode(printer,submit,button,type,expand));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
	public static String getCode(){
		return getCode(false,"","","",true);
	}
	public static String getCode(boolean printer,String submit,String button,String type,boolean expand){
		StringBuffer res=new StringBuffer();
		res.append("</div>");
		String display;
		if(expand){
			display="display:;";
		}else{
			display="display:none;";
		}
		if("simple".equals(type)||(empty(button)&&empty(submit))){
			res.append("");
		}else{
			res.append("<div class='tableFrameD' style='"+display+"'><div class=\"tableFrameDC\"><div class=\"tableFrameDL\"><div class=\"tableFrameDR\">");
			res.append("<div style=\"width:1px;float:left;\">&nbsp;</div>");
			if (printer) {
				res.append("<span class=\"printer\" onclick=\"print()\"><a href=\"javascript:void(0);\"/>打印</a></span>");
			}
			res.append("<div class=\"submitButtonDiv\">\n");
			if (submit.equals("false")) {
				;
			} else if (submit.equals("true")) {
				res.append("<input type=\"submit\" value=\"提 交\"/> ");
			} else if (submit.length() > 0) {
				res.append("<input type=\"submit\" value=\"" + submit+ "\"/> ");
			}
			if (button != null && button.length() != 0) {
				String[] temp;
				String value;
				String js;
				if (button.indexOf(",") >= 0) {
					temp = button.split(",");
					for (int i = 0; i + 1 < temp.length; i += 2) {
						if(temp[i+1].indexOf("class:")>=0){
							res.append("<input type=\"button\" value=\"" + temp[i]+ "\" class=\"" + temp[i + 1].substring(temp[i+1].indexOf("class:")+6) + "\"/> ");
						}else{
							res.append("<input type=\"button\" value=\"" + temp[i]+ "\" onclick=\"" + temp[i + 1] + "\"/> ");
						}
					}
				} else {
					value = "";
					js = button;
					res.append("<input type=\"button\" value=\"" + value+ "\" onclick=\"" + js + "\"/> ");
				}
			}
			res.append("</div>");
			res.append("</div></div></div></div>");
		}
		res.append("</div>");
		return res.toString();
	}
	
	private static boolean empty(String str){
		if(str==null||str.equals("")){
			return true;
		}
		return false;
	}

	public boolean isPrinter() {
		return printer;
	}

	public void setPrinter(boolean printer) {
		this.printer = printer;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
