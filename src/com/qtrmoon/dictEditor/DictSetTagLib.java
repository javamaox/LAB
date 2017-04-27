package com.qtrmoon.dictEditor;

import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;

/**
 * @author Javamao The TagLib can read Dictionary,and print selectTag or
 *         TreePage.
 */
public class DictSetTagLib extends TagSupport {
	private String dictId;// Dictionary catalog Id.

	private String var;// The name of setArribute("xxx").

	public int doStartTag() {
		List<DictionaryForm> list=DictBuffer.getDict(dictId);
		pageContext.setAttribute(var, list);
		return SKIP_BODY;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
