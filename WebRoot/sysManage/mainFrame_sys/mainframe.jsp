
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
<FRAMESET border="0" frameSpacing="0" rows="100,*,28" frameBorder="NO" cols="*">
	<FRAME name="bannerFrame" src="/${projectName}/sysManage/mainFrame/banner.jsp" noResize scrolling="no">
	<FRAMESET border="0" frameSpacing="0" cols="127,*" frameBorder="NO" cols="*">
		<FRAME name="menuFrame" src="./menu.jsp?funcs=${param.funcs}" noResize scrolling="no">
		<FRAMESET border="0" frameSpacing="0" rows="27,*" frameBorder="NO" cols="*">
			<FRAME name="titleFrame" src="" noResize>
			<FRAME name="workFrame" src="" scrolling="auto">
		</FRAMESET>
	</FRAMESET>
	<FRAME name="bottomFrame" src="./bottom.jsp" noResize scrolling="no">
</FRAMESET>
</html:html>
