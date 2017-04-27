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
public class WakeJsDictBufferTagLib extends TagSupport {
	private String dictId;// Dictionary catalog Id.

	public int doStartTag() {
		String res="";
		JspWriter out = pageContext.getOut();
		try {
			List l=DictBuffer.getDict(dictId);
			res+="<script>\t\r";
			res+="var "+dictId+"Dict=[];\t\r";
			for(int i=0;i<l.size();i++){
				DictionaryForm dict=(DictionaryForm)l.get(i);
				res+=dictId+"Dict["+dictId+"Dict.length]=JsDictBuffer.createDict('"+dict.getId()+"','"+dict.getPid()+"','"+dict.getLabel()+"','"+dict.getExp()+"');\t\r";
			}
			res+="</script>";
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}


	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
}
