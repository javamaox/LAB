package com.qtrmoon.toolkit.multilargefile;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.UserForm;

public class LFileUpTaglib extends TagSupport {
	private boolean showFileInfo=false;
	private boolean single=false;
	private String formName;
	private String formClass;
	private String basePath;
	private String basePathId;
	
	public int doStartTag() {
		try {
			pageContext.include("/multiFile/multiLargeFile.jsp?showFileInfo="+showFileInfo+"&single="+single
								+"&form_Name="+formName+"&form_Class="+formClass+"&base_Path="+basePath+"&base_PathId="+basePathId);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	public boolean isShowFileInfo() {
		return showFileInfo;
	}

	public void setShowFileInfo(boolean showFileInfo) {
		this.showFileInfo = showFileInfo;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public String getFormClass() {
		return formClass;
	}

	public void setFormClass(String formClass) {
		this.formClass = formClass;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getBasePathId() {
		return basePathId;
	}

	public void setBasePathId(String basePathId) {
		this.basePathId = basePathId;
	}
	
}
