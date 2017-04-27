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

	<title>user_sch.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	function goPage(act){
		var id="${organId}";
		var url = "";
		if(act==""){
			userForm.submit();
			return;
		}else if(act=="add"){
			url = "/${projectName}/sysManage/user.do?method=addUser";
		}
		document.location = url+"&id="+id;
	}
	</script>
</head>

<body class="contentPage">
	<html:form action="/user.do?method=schUserByOrganId" method="post">
		<pageFmt:head title="${organName}用户" tableClass="list"/>
		<table>
			<thead>
				<tr>
					<th>
						姓名
					</th>
					<th>
						操作
					</th>
				</tr>
			</thead>
			<logic:notEmpty name="userFormList">
				<logic:iterate name="userFormList" id="row">
					<tr>
						<td>
							${row.userName}
						</td>
						<td>
							<a href="/${projectName}/sysManage/user.do?method=viewUser&userId=${row.id}">查看</a>
							<logic:equal value="x" name="row" property="state">
							<logic:equal name="CURRENT_USER" property="state" value="x">
							<a href="/${projectName}/sysManage/user.do?method=updUser&userId=${row.id}">修改</a>
							</logic:equal>
							</logic:equal>
							<logic:notEqual value="x" name="row" property="state">
								<a href="/${projectName}/sysManage/user.do?method=updUser&userId=${row.id}">修改</a>
								<a
									href="/${projectName}/sysManage/user.do?method=delUser&userId=${row.id}
								&organId=${row.organId}"
									onclick="return confirm('请您确认是否删除该用户！');">删除</a>
								<a
									href="/${projectName}/sysManage/user.do?method=schRoleByOrganId&userId=${row.id}&organId=${row.organId}">设定角色</a>
								<a
									href="/${projectName}/sysManage/user.do?method=resetPassword&userId=${row.id}"
									onclick="return confirm('请您确认是否把该用户密码重设为:000！');">重置密码</a>
							</logic:notEqual>
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<pageFmt:tail button="新 增,goPage('add')" />
	</html:form>
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>
