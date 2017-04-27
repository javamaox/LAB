<%@ page language="java" pageEncoding="gb2312" import="com.qtrmoon.sysManage.*,com.qtrmoon.sysManage.bean.*,java.util.*"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<%
UserForm user=SysUtil.getCurrentUser(request);
String organName=SysUtil.getCurrentOrgan(request).getName();
request.setAttribute("organName",organName);
request.setAttribute("userName",user.getUserName());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />
	<title>bottom.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="/${projectName}/sysManage/css/style.css">
	<SCRIPT>
	$("document").ready(function(){
		//隐藏xx
		var frameSet,rowsInit,rowsFlex;
		if($.browser.msie){
			//isIE
			frameSet = top.document.childNodes[1].childNodes[1].childNodes[1].childNodes[2];
		}else{
			frameSet = top.document.childNodes[1].childNodes[1].childNodes[3].childNodes[5];
		}
		if(frameSet!=undefined){
			rowsInit = frameSet.rows;
			rowsFlex = rowsInit.substring(0,rowsInit.lastIndexOf(","))+",0";
		}
		$("div a").click(function(){
			frameSet.rows = rowsInit;
		});
	});
	document.onselectstart=function(){
		return false;
	}
	</SCRIPT>
</HEAD>
<BODY class="bottomPage">
	<bean:define id="operMode" value="${pageFmt:fn_getConstant('operMode')}"></bean:define>
	<div style="line-height:28px;color:white;padding-left:50px;">
		<b>登录机构：${organName}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<b>登录用户：${userName}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<logic:equal value="icon" name="operMode">
				<a href="/${projectName}/sysManage/user.do?method=updUserInfo&from=perUpdPassword" target="_blank" style="color:white;display:none;">修改密码</a>
				<div style="text-align:right;margin-top:-30px;margin-right:30px;">
				<a href="/${projectName}/sysManage/mainFrame/icon_mainframe.jsp" target="_top" style="color:white;">返回主界面</a>
				</div>
		</logic:equal>
		<a href="/${projectName}/download/ncigis.exe" style="color:white;">下载地理信息客户端</a>
	</div>
</BODY>
</html:html>
