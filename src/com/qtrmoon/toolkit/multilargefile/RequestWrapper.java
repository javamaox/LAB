package com.qtrmoon.toolkit.multilargefile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
	private HttpServletRequest request;
	private String[] filePaths;
	Map<String,String> valMap=new HashMap<String,String>();
	
	public RequestWrapper(HttpServletRequest req,String[] fpArr, Map<String, String> valMap) {
		super(req);
		this.request=req;
		this.filePaths=fpArr;
		this.valMap=valMap;
	}

	public String[] getParameterValues(String arg){
		if("filePaths".equals(arg)){
			return filePaths;
		}else{
			return request.getParameterValues(arg);
		}
	}
	
	public String getParameter(String arg){
		String val=valMap.get(arg);
		if(val==null){
			return request.getParameter(arg);
		}
		return val;
		
	}
}
