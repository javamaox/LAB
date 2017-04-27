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
		<pageFmt:head title="${organName}�û�" tableClass="list"/>
		<table>
			<thead>
				<tr>
					<th>
						����
					</th>
					<th>
						����
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
							<a href="/${projectName}/sysManage/user.do?method=viewUser&userId=${row.id}">�鿴</a>
							<logic:equal value="x" name="row" property="state">
							<logic:equal name="CURRENT_USER" property="state" value="x">
							<a href="/${projectName}/sysManage/user.do?method=updUser&userId=${row.id}">�޸�</a>
							</logic:equal>
							</logic:equal>
							<logic:notEqual value="x" name="row" property="state">
								<a href="/${projectName}/sysManage/user.do?method=updUser&userId=${row.id}">�޸�</a>
								<a
									href="/${projectName}/sysManage/user.do?method=delUser&userId=${row.id}
								&organId=${row.organId}"
									onclick="return confirm('����ȷ���Ƿ�ɾ�����û���');">ɾ��</a>
								<a
									href="/${projectName}/sysManage/user.do?method=schRoleByOrganId&userId=${row.id}&organId=${row.organId}">�趨��ɫ</a>
								<a
									href="/${projectName}/sysManage/user.do?method=resetPassword&userId=${row.id}"
									onclick="return confirm('����ȷ���Ƿ�Ѹ��û���������Ϊ:000��');">��������</a>
							</logic:notEqual>
						</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<pageFmt:tail button="�� ��,goPage('add')" />
	</html:form>
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>
