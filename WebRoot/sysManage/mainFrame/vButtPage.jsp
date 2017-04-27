<%@ page language="java" pageEncoding="GB2312"%>

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
	<title>vButtPage.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<script>
	$("document").ready(function(){
		var colFrameSet,colsInit,colsFlex;
		colFrameSet = top.document.getElementsByTagName("frameset")[1];
		colsInit = colFrameSet.cols;
		colsFlex = "0"+colsInit.substring(colsInit.indexOf(","));
		$("a").toggle(
			function(){
				$(this).parent().addClass("vButtPageR");
				//top.document.html.frameset.rows
				colFrameSet.cols=colsFlex;
			},
			function(){
				$(this).parent().removeClass("vButtPageR");
				colFrameSet.cols=colsInit;
			}
		);
		//◊¢≤·√‚—°‘Ò∑Ω∑®
		document.onselectstart=function(){
			return false;
		}
	});
	
	</script>
</HEAD>
<BODY class="vButtPageL">
	<a href="#"></a>
</BODY>
</html:html>
