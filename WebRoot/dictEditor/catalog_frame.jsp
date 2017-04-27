
<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />

	<title>main</title>
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
</head>
<FRAMESET border="0" frameSpacing="0" cols="220,*" frameBorder="NO" cols="*">
	<FRAME name="groupFrame" src="/${projectName}/dictEditor/dict.do?method=schCatalogGroup" scrolling="no" style="border-right:#4d67a2 3px solid;cursor:e-resize;">
	<FRAME name="dictFrame" src="" scrolling="yes">
</FRAMESET>
</html:html>
