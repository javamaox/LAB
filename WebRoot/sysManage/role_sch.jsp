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

	<title>role_sch.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	function goPage(act){
		var id="";
		var url = "";
		if(act=="add"){
			roleForm.submit();
			return;
		}else if(act=="addAdmin"){
			url = "/${projectName}/sysManage/role.do?method=addAdminRole&organId=${organId}";
		}
		document.location = url+"&id="+id;
	}
	</script>
</head>
<body>
	<html:form action="role.do?method=addRole&organId=${organId}"
		method="post">
		<pageFmt:head title="机构内角色" tableClass="list" button="新 增,goPage('add')"/>
		<table>
			<thead>
				<tr>
					<th>
						角色名称
					</th>
					<th width="80">
						操作
					</th>
				</tr>
			</thead>
			<logic:notEmpty name="roleList">
				<logic:iterate name="roleList" id="row">
					<tr>
						<td>
							<a href="/${projectName}/sysManage/role.do?method=updRole&id=${row.id}&organId=${organId}">${row.name}</a>
						</td>
						<td>
							<a href="/${projectName}/sysManage/role.do?method=updRole&id=${row.id}&organId=${organId}">修改</a>
							<a href="/${projectName}/sysManage/role.do?method=delRole&id=${row.id}&organId=${organId}" onclick="return confirm('请您确认是否删除！');">删除</a>
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<pageFmt:tail/>
		<logic:present name="isAdmin">
			<logic:equal name="isAdmin" value="1">
				<pageFmt:head title="系统级角色" tableClass="list" button="新 增,goPage('addAdmin')"/>
				<table>
					<thead>
						<tr>
							<th>
								角色名称
							</th>
							<th width="80">
								操作
							</th>
						</tr>
					</thead>
					<logic:notEmpty name="roleAdminList">
						<logic:iterate name="roleAdminList" id="row">
							<tr>
								<td>
									<a href="/${projectName}/sysManage/role.do?method=updAdminRole&id=${row.id}&organId=${organId}">${row.name}</a>
								</td>
								<td>
									<a href="/${projectName}/sysManage/role.do?method=updAdminRole&id=${row.id}&organId=${organId}">修改</a>
									<a href="/${projectName}/sysManage/role.do?method=delRole&id=${row.id}&organId=${organId}" onclick="return confirm('请您确认是否删除！');">删除</a>
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
				<pageFmt:tail/>
			</logic:equal>
		</logic:present>
	</html:form>
	<pageFmt:feedBack info="${res}" />
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>


