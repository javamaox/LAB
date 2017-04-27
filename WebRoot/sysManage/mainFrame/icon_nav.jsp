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
	<title>ban.jsp</title>
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<style>
		body{background:url(../imgs/wel_bottom.jpg) fixed;margin:0px;padding-bottom:60px;}
	</style>
	<script src='/${projectName}/script/pngImg.js'></script>
	<script>
	var pngIcon=new PNGIcon();
	pngIcon.defaultClick=false;
	pngIcon.highlight=false;
	pngIcon.init(165,186,function mth(action, el){
		var obj=eval("("+action+")");
		if(obj.link==""){
			top.document.location="/${projectName}/sysManage/mainFrame/mainframe.jsp?moduleId="+obj.id;//触发菜单
		}else{
			if(obj.link.indexOf("?")>0){
				top.document.location="/${projectName}/"+obj.link+"&moduleId="+obj.id+"&funcs="+obj.funcs;
			}else{
				top.document.location="/${projectName}/"+obj.link+"?moduleId="+obj.id+"&funcs="+obj.funcs;
			}
		}
	});
	</script>
</HEAD>
<BODY>
	<div style="padding:20px 0px;">
		<logic:iterate id="row" name="moduleList">
			<div class="showSub_block" style="margin:20px 5px;">
				<script>pngIcon.printPNG("/${projectName}/${row.pic}","{id:'${row.id}',link:'${row.link}',funcs:'${row.funcs}'}")</script>
			</div>
		</logic:iterate>
	</div>
	<a href="/${projectName}/demo/table.jsp">样式Demo</a>
	<a href="/${projectName}/dictEditor/catalog_frame.jsp">字典维护</a>
	<a href="/${projectName}/dictEditor/demo/frame.jsp">字典DEMO</a>
	<a href="/${projectName}/dictEditor/transfer.do?method=exportDmp">字典导出</a>
	<pageFmt:littleTab>
		<a href="javascript:void(0)">样式样式</a>
		<a href="javascript:void(0)">样式样式样式</a>
		<a href="javascript:void(0)">样式样式样式样式</a>
		<a href="javascript:void(0)">样式样式样式样式样式</a>
		<a href="javascript:void(0)">样式样式样式样式样式样式</a>
	</pageFmt:littleTab>
</BODY>
</html:html>
