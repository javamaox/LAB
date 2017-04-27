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

	<title>user_add.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script src='/${projectName}/script/validate.js'></script>
	<script>
	${scriptCode}
	function goPage(act){
		var organId="${userForm.organId}";
		var url = "";
		if(act=="back"){
			url = "/${projectName}/sysManage/user.do?method=schUserByOrganId";
		}
		document.location = url+"&organId="+organId;
	}
	</script>
</head>

<body>
	<html:form action="/user.do?method=addUser" method="post">
		<pageFmt:head title="用户添加" />
		<table class="contentTable">
			<tr>
				<td width="120">
					<html:hidden property="id" />
					<html:hidden property="organId" />
					<input type="hidden" name="from" value="submit" />
					姓名：
				</td>
				<td>
					<html:text property="userName" style="width:100px;" />
				</td>
			</tr>
			<tr>
				<td>
					登录名：
				</td>
				<td>
					<html:text property="loginName" style="width:100px;" />
				</td>
			</tr>
			<tr>
				<td>
					密码：
				</td>
				<td>
					<html:password property="password" style="width:100px;" />
				</td>
			</tr>
			<tr>
				<td colspan="2" style="height:2px;background:#555;"></td>
			</tr>
			<logic:present name="colList">
				<logic:iterate name="colList" id="col">
					<tr>
						<td>
							${col.info}：
						</td>
						<td>
							<input type="hidden" name="expcolname" value="${col.name}" />
							<logic:empty name="col" property="dict">
								<input type="text" name="expcolvalue"
									onblur="val(this,'${col.name}')" />
							</logic:empty>
							<logic:notEmpty name="col" property="dict">
								<pageFmt:dict dictId="${col.dict}" name="expcolvalue" />
							</logic:notEmpty>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
		<pageFmt:tail printer="true" submit="添 加" button="返 回,goPage('back')"/>
	</html:form>
</body>
</html:html>
