package com.qtrmoon.dictEditor;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;

/**
 * @author Javamao The TagLib can read Dictionary,and print selectTag or
 *         TreePage.
 */
public class DictDisplayTagLib extends TagSupport {
	private String dictId;// Dictionary catalog Id.

	private String value;// The Field's value

	public int doStartTag() {
		String res = getElement(dictId);
		JspWriter out = pageContext.getOut();
		try {
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getElement(String dictId) {
		return DictBuffer.getLabel(dictId,value);
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
