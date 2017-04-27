package com.qtrmoon.dictEditor;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;

/**
 * @author Javamao The TagLib can read Dictionary,and print selectTag or
 *         TreePage.
 */
public class DictIncludeTreeTagLib extends TagSupport {
	private String dictId;// Dictionary catalog Id.
	private String style;// radio checkbox or select.
	private String context="AUTO";//checkbox��ѡ���Ƿ�Ӱ�츸�ӡ�(yes/no/auto_yes/auto_no)
	private String value;// 
	private int expand=1;//Ĭ��չ������0չ��ȫ���ڵ㣬1չ����һ���ڵ㣨������0���ڵ㣩��
	private String root;//
	private String background;//
	public int doStartTag() {
		DictCatalog catalog=DictBuffer.findDictCatalogById(dictId);
		if(catalog==null){
			System.out.println("δ�ҵ��ֵ䣺"+dictId);
			return SKIP_BODY;
		}
		String muti="false";
		if("checkbox".equals(style)){muti="true";}
		JspWriter out = pageContext.getOut();
		try {
			String res="";
			res+="<iframe src='/"+Constant.getConstant("projectName")+"/dictEditor/dictTree.do?method=showDictTree&include=true"+
				"&muti="+muti+
				"&ajax="+catalog.isAjaxView()+
				"&dictId="+dictId+
				"&context="+getContext()+
				"&value="+getValue()+
				"&expand="+getExpand()+
				"&dynroot="+getRoot()+
				"&background="+getBackground()+"' "+
				"width='100%' height='100' frameBorder='no' id='"+dictId+"Frame'></iframe>\r\n";
			res+="<script>\r\n";
			res+="var DictInclude_iframe=$('#"+dictId+"Frame').eq(0);\r\n";
			res+="var DictInclude_height=0;\r\n";
			res+="if(DictInclude_iframe.parent().attr('nodeName').toUpperCase()=='BODY'){\r\n";
			res+="	DictInclude_height=$(window).height()";
			res+="}else{\r\n";
			res+="	DictInclude_height=DictInclude_iframe.parent().height();\r\n";
			res+="}\r\n";
			res+="if(DictInclude_height==0){DictInclude_height=DictInclude_iframe.parent().css('height');}\r\n";
			res+="else{DictInclude_height+='px';}\r\n";
			res+="DictInclude_iframe.attr('height',DictInclude_height);\r\n";
			res+="</script>\r\n";
			out.println(res);
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getValue() {
		return value==null?"":value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getContext() {
		return context==null?"":context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getExpand() {
		return expand;
	}

	public void setExpand(int expand) {
		this.expand = expand;
	}

	public String getRoot() {
		return root==null?"":root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getBackground() {
		return background==null?"":background;
	}

	public void setBackground(String background) {
		this.background = background;
	}
	
}
