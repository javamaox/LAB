package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class FeedBack extends TagSupport {
	private String info;
	private String onclick="";//The event when press theInfoWindow。
	private String confirm="";//The event when press "确定"。
	private String cancel="";//The event when press "取消"。
	private String style="alert";//alert/confirm;default:alert.
	private String hide="no";//if auto dispeared.[no,auto,5,load]. 
	private String id="";
	
	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			String res="";
			if (info != null && !info.equals("")) {
				String hstr="";
				if("load".equals(hide)){
					hstr="display:none;";
				}
				if(!onclick.equals("")){
					res+="<div class='info' id='"+id+"' style='"+hstr+"' onclick=\"this.parentNode.parentNode.style.display='none';"+onclick+"\">";
				}else{
					res+="<div class='info' id='"+id+"' style='"+hstr+"' hide='"+hide+"'>";
				}
				res+="<div class='infoinner'>"+info+"</div>";
				if("confirm".equals(style)){
					res+="<div style='text-align:right;padding-right:20px;'>"+
							"<input class='confirm' type='button' value='' onclick=\"this.parentNode.parentNode.style.display='none';"+confirm+"\"/> "+
							"<input class='cancel'  type='button' value='' onclick=\"this.parentNode.parentNode.style.display='none';"+cancel +"\"/></div>";
				}else{
					res+="<div style='text-align:right;padding-right:20px;'>"+
							"<input class='confirm' type='button' value='' onclick=\"this.parentNode.parentNode.style.display='none';"+confirm+"\"/></div>";
				}
				res+="</div>";
			}
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getHide() {
		return hide;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
