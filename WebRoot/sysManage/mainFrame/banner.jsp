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
	<title>banner.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<script>
	var timeout;
	$("document").ready(function(){
		var bannerIsOpen = true;//banner是否展现
		var rows = top.document.getElementsByTagName("frameset")[0].rows;
		var rowsFlex = "9"+rows.substring(rows.indexOf(","));
		
		$(".flexButton").hide();
		$("#bannerDiv").mouseover(function(){
			if(bannerIsOpen){
				timeout=window.setTimeout("$('.flexButton').fadeIn('fast')","1000");
			}
		}).mouseout(function(){
			window.clearTimeout(timeout);
			if(bannerIsOpen){
				timeout=window.setTimeout("$('.flexButton').fadeOut('fast')","1000");
			}
		});
		$(".flexButton").mouseover(function(){
			if(bannerIsOpen){
				window.clearTimeout(timeout);
			}
		}).mouseout(function(){
			if(bannerIsOpen){
				$(".flexButton").fadeOut("fast");
			}
		});
		
		$(".flexButton a").toggle(
			function(){
				//top.document.html.frameset.rows
				top.document.getElementsByTagName("frameset")[0].rows=rowsFlex;
				$(".flexButton").addClass("flexButtonDown");
				bannerIsOpen = false;//banner关闭
			},
			function(){
				top.document.getElementsByTagName("frameset")[0].rows=rows;
				$(".flexButton").removeClass("flexButtonDown");
				bannerIsOpen = true;
			}
		);
	});
	document.onselectstart=function(){
		return false;
	}
	</script>
</HEAD>
<BODY class="bannerPage">
	<div id="bannerDiv">
		<div>
			<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
				codebase="/${projectName}/swflash.cab#version=10,0,0,0" width="100" height="100">
				<param name="movie" value="/${projectName}/sysManage/mainFrame/top2.swf"></param>
				<param name="allowFullScreen" value="true"></param>
				<param name="allowscriptaccess" value="always"></param>
				<param name="wmode" value="transparent"></param>
				<embed src="/${projectName}/sysManage/mainFrame/top2.swf" quality="high" width="123"
					height="123" name="movie" align="middle" wmode="transparent"
					allowScriptAccess="sameDomain" type="application/x-shockwave-flash"
					pluginspage="http://www.macromedia.com/go/getflashplayer" />
			</object>
		</div>
	</div>
	<div class="flexButtonMask"></div>
	<div class="flexButton">
		<a href="javascript:void(0)"></a>
	</div>
	<div class="" style="margin-top: -40px;">
	<pageFmt:remind align="right"/>
	</div>
</BODY>
</html:html>
