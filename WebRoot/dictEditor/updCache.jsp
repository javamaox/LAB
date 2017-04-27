<%@ page language="java" pageEncoding="GB2312"%>
<jsp:directive.page import="com.qtrmoon.dictEditor.DictBuffer"/>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base target="_self" />

	<title>请选择</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<pageFmt:dictInit/>
	<script type="text/javascript">
	function loadPage(){
	}
	</script>
</head>
<body>
<%
String dictid = request.getParameter("dictid");
String cfg = request.getParameter("cfg");
if (dictid != null && !dictid.equals("")) {
	if("true".equals(cfg)){DictBuffer.updCfgCache(dictid);}//刷配置
	DictBuffer.updCache(dictid);
	request.setAttribute("info",dictid + "刷新成功！");
}
%>
<form name="dictForm">
字典标识：<input type="text" name="dictid" value="${param.dictid}"/><input type="submit" value="刷 新" id="updCache"/>
<br/>
刷新配置：<input type="checkbox" name="cfg" value="true"/>
</form>
<pageFmt:feedBack info="${info}"/>
</body>
</html:html>
