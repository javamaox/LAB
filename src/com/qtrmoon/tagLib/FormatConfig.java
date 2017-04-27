package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;

public class FormatConfig extends TagSupport {
	// 为了在需要按地区限制数据的页面进行数据限制，将当前用户地区码传给js。
	private boolean showAreaId = false;

	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + Constant.getConstant("projectName") + "/css/masterCss.css\">");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + Constant.getConstant("projectName") + "/script/datepicker/ui.datepicker.css\">");
			out.println("<script>var projectName='"+Constant.getConstant("projectName")+"'</script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/jquery2_6.js'></script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/sorttable.js'></script>");//排序扩展
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/ui.core.js'></script>");//
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/datepicker/ui.datepicker.js'></script>");//时间选择器扩展
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/jquery.cookie.js'></script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/scrollData.js'></script>");//锁定行列的滚动数据
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/validate.js'></script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/pageFrame.js'></script>");
			pageContext.setAttribute("projectName", Constant.getConstant("projectName"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	public boolean isShowAreaId() {
		return showAreaId;
	}

	public void setShowAreaId(boolean showAreaId) {
		this.showAreaId = showAreaId;
	}

}
