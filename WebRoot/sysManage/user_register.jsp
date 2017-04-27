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

	<title>�û�ע��</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script src='/${projectName}/script/validate.js'></script>
	<script>
	${scriptCode}
	function validate(frm)
		{
			if(frm.loginName.value==''){
				 alert("��¼������Ϊ��!");
				 frm.loginName.focus();
				 return false;
	   		 }
	   		 if(frm.password.value==''){
				 alert("���벻��Ϊ��!");
				 frm.password.focus();
				 return false;
	   		 }
	   		 if(frm.password1.value==''){
				 alert("ȷ�����벻��Ϊ��!");
				 frm.password1.focus();
				 return false;
	   		 }
	   		  if(frm.password1.value!=frm.password.value){
				 alert("������ȷ�����벻һ��!");
				 frm.password1.focus();
				 return false;
	   		 }
		}
	function goPage(act){
		var url = "";
		if(act=="back"){
			url="/${projectName}/sysManage/login.do?method=logout";
		}
		document.location = url;
	}
	</script>
</head>

<body>
	<html:form action="/login.do?method=userRegister" method="post"
		onsubmit="return validate(this)">
		<pageFmt:head title="�û�ע��" />
		<input type="hidden" name="from" value="submit" />
		<table class="contentTable">
			<tr>
				<td>
					��¼����
				</td>
				<td>
					<html:text property="loginName" style="width:100px;" />*
				</td>
			</tr>
			<tr>
				<td>
					���룺
				</td>
				<td>
					<html:password property="password" style="width:100px;" />*
				</td>
			</tr>
			<tr>
				<td>
					ȷ�����룺
				</td>
				<td>
					<input type="password" name="password1" style="width:100px;" />*
				</td>
			</tr>
			<tr>
				<td>
					����������
				</td>
				<td>
					<select name="organId">
						<logic:present name="organList">
							<logic:notEmpty name="organList">
								<logic:iterate name="organList" id="row">
									<option value="${row.id}">
										${row.name}
									</option>
								</logic:iterate>
							</logic:notEmpty>
						</logic:present>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					������
				</td>
				<td>
					<html:text property="userName" style="width:100px;" />*
				</td>
			</tr>
			<logic:present name="colList">
				<logic:iterate name="colList" id="col">
					<tr>
						<td>
							${col.info}��
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
		<pageFmt:tail submit="�� ��" button="�� ��,goPage('back')" />
	</html:form>
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>
