package com.qtrmoon.dictEditor;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.qtrmoon.common.Constant;

public class ShowDictTreeJsTagLib extends TagSupport {
	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			String res = "";
			res += "<SCRIPT src=\"/"+Constant.getConstant("projectName")+"/dictEditor/dictionary.js\" type=text/javascript></SCRIPT>";
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
}
