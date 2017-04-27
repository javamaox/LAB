package com.qtrmoon.tagLib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;

public class FormatConfig extends TagSupport {
	// Ϊ������Ҫ�������������ݵ�ҳ������������ƣ�����ǰ�û������봫��js��
	private boolean showAreaId = false;

	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + Constant.getConstant("projectName") + "/css/masterCss.css\">");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + Constant.getConstant("projectName") + "/script/datepicker/ui.datepicker.css\">");
			out.println("<script>var projectName='"+Constant.getConstant("projectName")+"'</script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/jquery2_6.js'></script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/sorttable.js'></script>");//������չ
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/ui.core.js'></script>");//
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/datepicker/ui.datepicker.js'></script>");//ʱ��ѡ������չ
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/jquery.cookie.js'></script>");
			out.println("<script src='/" + Constant.getConstant("projectName") + "/script/scrollData.js'></script>");//�������еĹ�������
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
