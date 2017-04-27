package com.qtrmoon.tagLib;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.qtrmoon.sysManage.SysUtil;
import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.sysManage.bean.UserForm;

public class SwitchBack extends BodyTagSupport {
	private String var;
	
	public int doAfterBody() throws JspException {
		BodyContent body=getBodyContent();
		String content=body.getString().trim();
		String uri="";
		if(content!=null){
			content=content.trim();
			if(!content.equals("")){
				uri=getBackUri(pageContext.getSession(),content.split("\n"));
			}
		}
		pageContext.setAttribute(var, uri);
		return (SKIP_BODY);
	}
	
	/**
	 * @param req
	 * @param backUris 转向本页面的来源页地址（不必带工程名）。地址不需要精确，只需要能跟其他页区分出来即可。
	 * @return
	 */
	private String getBackUri(HttpSession sess,String[] backUris) {
		Object o=sess.getAttribute("REQUEST_HISTORY");
		LinkedList<String> historyList=(LinkedList)o;
		int[] backUriIndexes=new int[backUris.length];
		for(int i=0;i<backUriIndexes.length;i++){
			backUriIndexes[i]=999;
		}
		for(int i=0;i<backUris.length;i++){
			String bu=backUris[i].trim();
			for(int m=0;m<historyList.size();m++){
				if(!bu.equals("")&&historyList.get(m).indexOf(bu)>0){
					backUriIndexes[i]=m;
					break;
				}
			}
		}
		//找最小索引，即多个返回连接中，最近访问过的。
		int min=backUriIndexes[0];
		int minIdx=0;
		for(int i=0;i<backUriIndexes.length;i++){
			if(min>backUriIndexes[i]){
				min=backUriIndexes[i];
				minIdx=i;
			}
		}
		String res="";
		if(backUriIndexes[minIdx]<historyList.size()){
			res=historyList.get(backUriIndexes[minIdx]).trim();
		}
		return res;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	
}
