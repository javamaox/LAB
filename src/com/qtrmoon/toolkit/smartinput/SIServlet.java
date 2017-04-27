package com.qtrmoon.toolkit.smartinput;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qtrmoon.common.CommunalService;
import com.qtrmoon.common.Constant;

public class SIServlet extends HttpServlet{
	private static CommunalService communalService;
	
	/**
	 * Constructor of the object.
	 */
	public SIServlet() {
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String to=request.getParameter("to");
		if("jsp".equals(to)){
			response.getWriter().print(getSelectPage(request));
			return;
		}
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String table=request.getParameter("table");//查询表
		String colsch=request.getParameter("colsch");//查询条件
		String schKey=request.getParameter("schKey");//查询条件值
		String colback=request.getParameter("colback");//返回的列
		String[] cols=null;
		if(colback!=null&&!colback.equals("")){
			cols=colback.split(",");
		}else{
			cols=new String[]{colsch};
		}
		//处理key输入格式
		schKey = new String(schKey.getBytes("ISO8859-1"),"gb2312");
		schKey=schKey.trim();
		schKey=schKey.replaceAll("　", " ");
		while(schKey.indexOf("  ")>0){
			schKey=schKey.replaceAll("  ", " ");
		}
		String cdtStr="";
		String[] schKeyArr=schKey.split(" ");
		for(String cdt:schKeyArr){
			cdtStr+=" "+colsch+" like '%"+cdt+"%' and";
		}
		cdtStr=cdtStr.substring(0,cdtStr.length()-3);
		
		String colstr="''";
		for(String col:cols){
			colstr+="||"+col+"||','";
		}
		colstr=colstr.substring(0,colstr.length()-5)+" as colback";
		String sql = "select "+colsch+","+colstr+" from "+table+" where "+cdtStr+" and rownum<=20 order by '"+colsch+"'";
		List<Object[]> list = communalService.schData(sql,new String[]{colsch,"colback"},new String[]{"String","String"});
		String res="";
		for(Object[] obj:list){
			res+="['"+obj[0].toString().trim()+"','"+obj[1].toString().trim()+"'],";
		}
		if(res != ""){
			res = res.substring(0,res.length()-1);
		}
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(res);
	}
	
	private Object getSelectPage(HttpServletRequest request) {
		String table=request.getParameter("table"),
			colsch=request.getParameter("colsch"),
			colback=request.getParameter("colback");
		String projectName=Constant.getConstant("projectName");
		String res="";
		res+="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">\r\n";
		res+="<html>\r\n";
		res+="<head>\r\n";
		res+="	<title>智能输入框</title>\r\n";
		res+="	<script src='/"+projectName+"/script/jquery2_6.js'></script>\r\n";
		res+="	<style>\r\n";
		res+="	body{margin:0px;}\r\n";
		res+="	#colschDiv{padding:10px;}\r\n";
		res+="	#colschDiv a{display:block;color:black;background:white;line-height:23px;font-size:13px;}\r\n";
		res+="	#colschDiv a:hover{background:lightblue;}\r\n";
		res+="	\r\n";
		res+="	</style>\r\n";
		res+="	<script>\r\n";
		res+="	var table = '"+table+"';//查询表\r\n";
		res+="	var colsch = '"+colsch+"';//查询条件\r\n";
		res+="	var colback = '"+colback+"';//返回的列\r\n";
		res+="	function schKey(schKey){\r\n";
		res+="		$.post(\"/"+projectName+"/smartInput?colsch=\"+colsch+\"&schKey=\"+schKey+\"&colback=\"+colback+\"&table=\"+table,function(data){\r\n";
		res+="			$(\"#colschDiv\").html(\"\");\r\n";
		res+="			if(data != \"\"){\r\n";
		res+="				var list = eval(\"[\"+data+\"]\");\r\n";
		res+="				var res=\"\";\r\n";
		res+="				for(var key in list){\r\n";
		res+="					var line=list[key];\r\n";
		res+="					res += \"<a href=\\\"javascript:optClick('\"+line[0]+\"','\"+line[1]+\"')\\\" val='\"+line[1]+\"'>\"+line[0]+\"</a>\";\r\n";
		res+="				}\r\n";
		res+="				$(\"#colschDiv\").html(res);\r\n";
		res+="			}\r\n";
		res+="		});\r\n";
		res+="	}\r\n";
		res+="	function optClick(backVal,backStr){\r\n";
		res+="		parent.smartInput_setText(backVal,backStr);\r\n";
		res+="	}\r\n";
		res+="	</script>\r\n";
		res+="</head>\r\n";
		res+="<body>\r\n";
		res+="	<div id=\"colschDiv\">\r\n";
		res+="	</div>\r\n";
		res+="</body>\r\n";
		res+="</html>\r\n";
		return res;
	}

	public void init() throws ServletException {
		// Put your code here
	}

	public CommunalService getCommunalService() {
		return communalService;
	}

	public void setCommunalService(CommunalService communalService) {
		SIServlet.communalService = communalService;
	}
}
