<%@ page language="java" pageEncoding="GB2312"
	import="com.qtrmoon.sysManage.*,com.qtrmoon.sysManage.bean.*,java.util.*"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%
	String funcs = request.getParameter("funcs");
	String[] funcArr = funcs.split(",");
	UserForm user = SysUtil.getCurrentUser(request);
	List resList = new ArrayList();
	List flist = user.getFunctionlist();
	for (int i = 0; i < flist.size(); i++) {
		FunctionForm fun = (FunctionForm) flist.get(i);
		for (int m = 0, mm = funcArr.length; m < mm; m++) {
			if (fun.getId().equals(funcArr[m])) {
				resList.add(fun);
				break;
			}
		}
	}
	if (resList.size() > 0) {
		FunctionForm node,obj;
		int index=0;
		for (int i = 0; i < resList.size() - 1; i++) {
			node = (FunctionForm) resList.get(i);
			int order = node.getOrder();
			for (int m = i + 1; m < resList.size(); m++) {
				FunctionForm node1 = (FunctionForm) resList.get(m);
				if (order > node1.getOrder()) {
					order = node1.getOrder();
					index = m;
				}
			}
			if (index > 0) {
				obj = (FunctionForm)resList.get(i);
				resList.set(i, resList.get(index));
				resList.set(index, obj);
			}
			index = -1;
		}
		request.setAttribute("link", ((FunctionForm) resList.get(0))
		.getLink());
	}
	request.setAttribute("functionList", resList);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />

	<title>menu.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<script src='/${projectName}/script/pngImg.js'></script>
	<style>
	.imgs{margin:1px;cursor:pointer;}
	</style>
	<SCRIPT>
	function loadPage(){
		//注册免选方法
		document.onselectstart=function(){
			return false;
		}
	}
	var curr="";
	var pngIcon=new PNGIcon();
	pngIcon.defaultClick=true;
	pngIcon.init(80,80,function mth(action, el){
		parent.titleFrame.document.location="./title.jsp?funcid="+action;
		if(curr!=undefined){
			if(prev.attr("id")!=el.parent().attr("id")){
				prev=curr;
				window.setTimeout("prev.css('background','none')","100");//避免IE的滤镜Bug。背景无法完全填充。
			}
		}else{
			prev=el.parent();
		}
		el.parent().css("background","#bfe3ff");
		curr=el.parent();
	});
	</SCRIPT>
</head>

<body style="background:url(../imgs/menuBk.gif) top repeat-x;text-align:center;">
	<logic:notEmpty name="functionList">
		<logic:iterate id="row" name="functionList">
			<div style="padding:5px;" id="${row.id}">
			<script>pngIcon.printPNG("../itemico/${row.picImg}","${row.id}")</script>
			<div style="clear:both;"></div>
			<div style="font-size:15px;font-weight:bolder;">${row.name}</div>
			</div>
		</logic:iterate>
	</logic:notEmpty>
</body>
</html:html>
