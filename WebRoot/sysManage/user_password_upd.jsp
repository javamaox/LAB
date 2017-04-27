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

	<title>user_password_upd.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script src="/${projectName}/script/validate.js"></script>
	<script>
		function validate(frm)
		{
			 if(frm.oldpassword.value==''){
				 alert("ԭ���벻��Ϊ��!");
				 frm.oldpassword.focus();
				 return false;
	   		 }
	   		 if(frm.password.value==''){
				 alert("�����벻��Ϊ��!");
				 frm.password.focus();
				 return false;
	   		 }
	   		 if(frm.password1.value==''){
				 alert("ȷ�������벻��Ϊ��!");
				 frm.password1.focus();
				 return false;
	   		 }
	   		  if(frm.password1.value!=frm.password.value){
				 alert("��������ȷ�������벻һ��!");
				 frm.password1.focus();
				 return false;
	   		 }
		}
	</script>
</head>

<body>
	<html:form action="/user.do?method=updUserInfo" method="post" onsubmit="return validate(this)">
		<pageFmt:head title="�޸�����" />
		<html:hidden property="id" />
		<input type="hidden" name="from" value="updPassword" />
		<table class="viewData">
			<tr>
				<td>
					ԭ����:
				</td>
				<td>
					<input type="password" name="oldpassword" />
				</td>
			</tr>
			<tr>
				<td>
					������:
				</td>
				<td>
					<input type="password" name="password" />
				</td>
			</tr>
			<tr>
				<td>
					ȷ��������:
				</td>
				<td>
					<input type="password" name="password1" />
				</td>
			</tr>
		</table>
		<pageFmt:tail submit="�� ��" button="�� ��,window.close()"/>
	</html:form>
	<pageFmt:feedBack info="${info}"/>
</body>
</html:html>
