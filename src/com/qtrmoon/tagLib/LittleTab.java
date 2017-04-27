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

public class LittleTab extends BodyTagSupport {
	public int doAfterBody() throws JspException {
		try {
			BodyContent body=getBodyContent();
			String content=body.getString().trim();
			String split="/a>";
			String res="";
			if(content.indexOf(split)>0){
				String[] cons=content.split(split);
				res+="<ul class='littleTab'>\r\n";
				for(String a:cons){
					int strlength=a.substring(a.lastIndexOf(">")+1, a.lastIndexOf("<")).getBytes().length;
					res+="<li class='littleTab_li' style='width:"+(strlength*6+20)+"px'>"+a.trim()+split+"</li>\r\n";
				}
				res+="</ul>";
			}
			JspWriter out=body.getEnclosingWriter();
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

}
