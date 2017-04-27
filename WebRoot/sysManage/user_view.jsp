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
</head>

<body>
	<html:form action="/user.do?method=schUserByOrganId" method="post">
		<pageFmt:head title="用户查看" />
		<table class="viewData">
			<tr>
				<td width="80">
					<html:hidden property="id" />
					<html:hidden property="organId" />
					<input type="hidden" name="from" value="submit" />
					姓名:
				</td>
				<td>
					<html:text property="userName" readonly="true"/>
				</td>
			</tr>
			<tr>
				<td>
					登录名:
				</td>
				<td>
					<html:text property="loginName" readonly="true"/>
				</td>
			</tr>
			<logic:present name="colList">
				<logic:iterate name="colList" id="col">
					<tr>
						<td>
							${col.info}:
						</td>
						<td>
							<input type="hidden" name="expcolname" value="${col.name}" />
							<logic:empty name="col" property="dict">
								<input type="text" name="expcolvalue" value="${col.value}" readonly="readonly"/>
							</logic:empty>
							<logic:notEmpty name="col" property="dict">
								<pageFmt:dictDisplay dictId="${col.dict}"
									value="${col.value}" />
							</logic:notEmpty>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
		<pageFmt:tail submit="返 回"/>
	</html:form>
</body>
</html:html>
