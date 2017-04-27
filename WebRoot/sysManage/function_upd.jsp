<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />
	<title>schFunctionTree.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<pageFmt:dictInit/>
	<script>
	function goPage(act){
		var id = "${functionForm.id}";
		var url = "";
		if(act=="upd"){
			functionForm.submit();
			return;
		}else if(act=="add"){
			url = "/${projectName}/sysManage/function.do?method=addFunction";
		}else if(act=="up"){
			url = "/${projectName}/sysManage/function.do?method=upFunction";
		}else if(act=="down"){
			url = "/${projectName}/sysManage/function.do?method=downFunction";
		}else if(act=="del"){
			if(confirm("确定删除该功能？")){
				url = "/${projectName}/sysManage/function.do?method=delFunction";
			}
		}
		document.location = url+"&id="+id;
	}
	function refreshPage(){
		parent.functionTree.document.location = parent.functionTree.document.location;
	}
	function showInfo(){
		$("#helpDiv").toggle();
	}
	</script>
</head>

<body>
	<html:form action="/function.do?method=updFunction" method="post">
		<pageFmt:head title="功能修改" button="帮 助,javascript:showInfo()"/>
		<table class="viewData">
			<tr>
				<td width="80">
					<html:hidden property="id" />
					<html:hidden property="pid" />
					<html:hidden property="ord" />
					<html:hidden property="treeTrack" />
					<input type="hidden" name="from" value="submit" />
					功能名称：
				</td>
				<td>
					<html:text property="name" />
				</td>
				<td width="80">
					父功能：
				</td>
				<td>
					<pageFmt:dict dictId="sys_function" name="pid" value="${functionForm.pid}"/>
				</td>
			</tr>
			<tr>
				<td>
					链接地址：
				</td>
				<td colspan="3">
					<html:text property="link" style="width:80%;" />
				</td>
			</tr>
			<tr>
				<td>
					模块名称：
				</td>
				<td>
					<html:text property="moduleName" style="width:80%;" />
				</td>
				<td>
					功能类别：
				</td>
				<td>
					<html:radio property="isShow" value="1" />
					菜单
					<html:radio property="isShow" value="2" />
					权限
					<html:radio property="isShow" value="0" />
					隐藏权限
				</td>
			</tr>
			<tr>
				<td>
					描述图片：
				</td>
				<td>
					<html:text property="picImg" />
				</td>
				<td>
					图标：
				</td>
				<td>
					<html:text property="picIco" />
				</td>
			</tr>
			<tr>
				<td>
					说明：
				</td>
				<td colspan="3">
					<html:textarea property="info" style="width:80%;" rows="5" />
				</td>
			</tr>
		</table>
		<pageFmt:tail
			button="修 改,goPage('upd'),删 除,goPage('del'),添加子功能,goPage('add'),上 移,goPage('up'),下 移,goPage('down')" />
	</html:form>
	<pageFmt:feedBack info="${res}" />
	<pageFmt:feedBack info="${info}" />
	<div id="helpDiv" style="display:none;padding:20px;font-size:13px;">
	<p><b>菜单：</b>会构成显示的菜单，同时也作为一个权限被赋予用户。在角色配置时可以手动勾选该节点。</p>
	<p><b>权限：</b>不构成显示菜单，但在角色配置时可以勾选。</p>
	<p><b>隐藏权限：</b>不构成菜单，在角色配置时也不能勾选，其父为菜单节点，会跟随父菜单一同被赋予或移出。相当于为某个菜单暗藏的赋予了一个权限组。</p>
	<div>
</body>
</html:html>
