
<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />

	<title>${pageFmt:fn_getConstant('projectInfo')}</title>
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
</head>
<FRAMESET border="0" frameSpacing="0" rows="123,*,28" frameBorder="NO"
	cols="*">
	<FRAME name="bannerFrame" src="banner.jsp" noResize scrolling="no">
	<FRAMESET border="0" frameSpacing="0" rows="*" frameBorder="NO"
		cols="179,9,*">
		<FRAME name="leftFrame" src="/${projectName}/sysManage/login.do?method=menu&moduleId=${param.moduleId}" noResize>
		<FRAME name="VButtFrame" src="vButtPage.jsp" noResize>
		<FRAMESET border="0" frameSpacing="0" rows="27,*" frameBorder="NO" cols="*">
			<FRAME name="titleFrame" src="title.jsp" noResize>
			<FRAME name="workFrame" src="welcome.jsp" scrolling="auto">
		</FRAMESET>
	</FRAMESET>
	<FRAME name="bottomFrame" src="/${projectName}/sysManage/login.do?method=bottom" noResize scrolling="no">
</FRAMESET>
</html:html>
