
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
<FRAMESET border="0" frameSpacing="0" rows="100,*,28" frameBorder="NO" cols="*">
	<FRAME name="bannerFrame" src="banner.jsp" noResize scrolling="no">
	<FRAME name="navFrame" src="/${projectName}/sysManage/login.do?method=iconNav" noResize scrolling="no">
	<FRAME name="bottomFrame" src="/${projectName}/sysManage/login.do?method=bottom" noResize scrolling="no">
</FRAMESET>
</html:html>
