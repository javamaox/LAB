package com.qtrmoon.toolkit.multifile;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.UserForm;

public class FileUpTaglib extends TagSupport {
	private boolean showFileInfo=false;
	private boolean single=false;

	public int doStartTag() {
		try {
			pageContext.include("/multiFile/multiFile.jsp?showFileInfo="+showFileInfo+"&single="+single);
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
	
}
