package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class DateSubTail extends TagSupport {
	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			out.print("</div>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
}
