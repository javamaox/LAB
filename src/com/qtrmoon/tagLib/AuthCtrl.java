package com.qtrmoon.tagLib;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.UserForm;

public class AuthCtrl extends BodyTagSupport {
	private String url;

	public int doStartTag() {
		BodyContent body=getBodyContent();
		UserForm user = SysUtil.getCurrentUser((HttpServletRequest) pageContext
				.getRequest());
		boolean runBody=false;
		if (user != null) {
			List<FunctionForm> funcList = user.getFunctionlist();
			for (FunctionForm func : funcList) {
				if (func.getLink()!=null&&func.getLink().indexOf(url) >= 0) {
					runBody=true;
					break;
				}
			}
		}
		if(runBody){
			return (EVAL_BODY_INCLUDE);
		}else{
			return (SKIP_BODY);
		}
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
