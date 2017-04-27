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

	<title>userInfo_upd.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script src="/${projectName}/script/validate.js"></script>
	<script>
		function goPage(act){
			var url="";
			if(act=="upd"){
				url = "/${projectName}/sysManage/user.do?method=updUserInfo&from=perUpdPassword";
			}
			document.location = url;
		}
		${scriptCode}
	</script>
</head>

<body>
	<html:form action="/user.do?method=updUserInfo" method="post">
		<pageFmt:head title="修改个人信息" />
		<table class="viewData">
			<tr>
				<td width="100">
					<html:hidden property="id" />
					<html:hidden property="organId" />
					<html:hidden property="state" />
					<html:hidden property="password" />
					<input type="hidden" name="from" value="submit" />
					姓名：
				</td>
				<td>
					<html:text property="userName" />
				</td>
			</tr>
			<tr>
				<td>
					登录名：
				</td>
				<td>
					<html:text property="loginName" readonly="true" />
				</td>
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
								<input type="text" name="expcolvalue" value="${col.value}"
									onblur="val(this,'${col.name}')" />
							</logic:empty>
							<logic:notEmpty name="col" property="dict">
								<pageFmt:dict dictId="${col.dict}" name="expcolvalue"
									value="${col.value}" />
							</logic:notEmpty>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
		<pageFmt:tail submit="修 改" button="修改密码,goPage('upd')" />
	</html:form>
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>
