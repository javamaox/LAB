package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;

public class DateSubHead extends TagSupport {
	private String title;

	private String addUrl;

	private boolean expand = true;

	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		String className = "";
		String style = "";
		try {
			if (expand) {
				className = "minusSub";
				style = "";
			} else {
				className = "plusSub";
				style = "style=\"display:none;\"";
			}
			out
					.println("<div class=\"subTitleDiv\"><span class="
							+ className
							+ " onclick=\"expandSub(this)\" id=\"oneSubBlock\"></span><span onclick=\"expandSub(this.previousSibling)\" class=\"subTitle\">"
							+ title + "</span></div>");
			if (addUrl != null && addUrl.length() != 0) {
				out
						.print("<span class=\"addNewButton\"><input type=\"button\" onclick=\"javascript:document.location='"
								+ Constant.getConstant("projectName")
								+ addUrl
								+ "'\" value=\"Ìí ¼Ó\"/></span>");
			}
			out.println("<hr class=\"subHr\"/>");
			out.println("<div " + style + ">");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
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

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}
}
