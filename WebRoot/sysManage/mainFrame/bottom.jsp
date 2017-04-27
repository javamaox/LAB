<%@ page language="java" pageEncoding="gb2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />
	<title>bottom.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<SCRIPT>
	document.onselectstart=function(){
		return false;
	}
	</SCRIPT>
</HEAD>
<BODY class="bottomPage">
	<bean:define id="operMode" value="${pageFmt:fn_getConstant('operMode')}"></bean:define>
	<div style="line-height:28px;color:white;padding-left:20px;">
		<b>登录机构：${organName}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<b>登录用户：${userName}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<logic:equal value="icon" name="operMode">
			<a href="/${projectName}/sysManage/user.do?method=updUserInfo&from=perUpdPassword" target="_blank" style="color:white;display:none;">修改密码</a>
			<div style="text-align:right;margin-top:-30px;margin-right:30px;">
			<a href="/${projectName}/sysManage/login.do?method=logout" target="_top" style="color:white;">退出系统</a>
			</div>
		</logic:equal>
	</div>
</BODY>
</html:html>
