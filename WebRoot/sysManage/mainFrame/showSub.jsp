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
	<title>ban.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<style>
		body{background:url(../imgs/wel_bottom.gif) left bottom no-repeat fixed;margin:0px;padding-bottom:60px;}
		.infor{background:transparent;width:100px;height:100px;color:#000;text-align:left;float:left;font-size:13px;}
		.block{width:250px;float:left;margin-bottom:20px;margin-left:20px;margin-right:20px;}
		.block img{border:0px;}
		.titleBack{background:url(../imgs/titleBack.gif);width:210px;height:31px;font-size:25px;font-family:ºÚÌå;color:#0034a2;text-align:center;padding-top:3px;font-weight:bolder;}
		.titleBack A{color:black;text-decoration:none;}
		.titleBack A:hover{color:red}
	</style>
	<script>
	function goPage(id){
		top.leftFrame.window.vil(id);//´¥·¢²Ëµ¥
	}
	</script>
</HEAD>
<BODY>
	<div style="padding:20px 0px;">
		<logic:iterate id="row" name="funcList">
			<div class="block">
				<a href="javascript:goPage('${row.id}')">
					<logic:notEmpty name="row" property="picImg">
						<img src="../itemico/${row.picImg}" style="float:left;" /> 
					</logic:notEmpty>
					<logic:empty name="row" property="picImg">
						 <img src="../itemico/grxx.gif" style="float:left;" /> 
					</logic:empty>
				</a>
				<div class="infor">
					${row.info}
				</div>
				<div style="clear:both;"></div>
				<div class="titleBack">
					<a href="javascript:goPage('${row.id}')">${row.name}</a>
				</div>
			</div>
		</logic:iterate>
	</div>
</BODY>
</html:html>
