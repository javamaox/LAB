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
	<script>
		function loadPage(){
			var roleIds = "${roleIds}";
			if(roleIds!=""){
				var code = roleIds.split(",");
				var boxes = document.userForm.roles;
			
				if(boxes.length == undefined){
					if(boxes.value==code[0]){
						boxes.checked = "checked";
					}
				}else{
					for(var i=0;i<boxes.length;i++){
						for(var m=0;m<code.length;m++){
							if(boxes[i].value == code[m]){
								boxes[i].checked = "checked";
							}
						}
					}
				}
			}
		}
		function goPage(act){
		var organId = "${organId}";
		var url = "";
		if(act=="back"){
			url = "/${projectName}/sysManage/user.do?method=schUserByOrganId";
		}
		document.location = url+"&organId="+organId;
	}
	</script>
</head>

<body>
	<html:form action="/user.do?method=addRoleForUser" method="post">
		<pageFmt:head title="角色列表" />
		<input type="hidden" name="userId" value="${userId}" />
		<input type="hidden" name="organId" value="${organId}" />
		<table class="contentTable">
			<logic:notEmpty name="localRole">
				<tr>
					<td>
						本机构角色
					</td>
				</tr>
				<tr>
					<td>
						<logic:iterate name="localRole" id="row">
							<input type="checkbox" name="roles" value="${row.id}">
							${row.name}
						</logic:iterate>
					</td>
				</tr>
			</logic:notEmpty>
			<logic:notEmpty name="roleList">
				<tr>
					<td>
						通用角色
					</td>
				</tr>
				<tr>
					<td>
						<logic:iterate name="roleList" id="row">
							<input type="checkbox" name="roles" value="${row.id}">
							${row.name}
						</logic:iterate>
					</td>
				</tr>
			</logic:notEmpty>
		</table>
		<pageFmt:tail submit="提 交" button="返 回,goPage('back')"/>
	</html:form>
</body>
</html:html>
