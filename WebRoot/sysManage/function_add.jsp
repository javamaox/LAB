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
</head>

<body>
	<html:form action="/function.do?method=addFunction" method="post">
		<pageFmt:head title="�������" />
		<table class="viewData">
			<tr>
				<td width="100">
					<html:hidden property="id" />
					<html:hidden property="pid" />
					<html:hidden property="ord" />
					<html:hidden property="treeTrack" />
					<input type="hidden" name="from" value="submit" />
					�������ƣ�
				</td>
				<td>
					<html:text property="name" />
				</td>
			</tr>
			<tr>
				<td>
					���ӵ�ַ��
				</td>
				<td>
					<html:text property="link" style="width:80%;" />
				</td>
			</tr>
			<tr>
				<td>
					ģ�����ƣ�
				</td>
				<td>
					<html:text property="moduleName" style="width:80%;" />
				</td>
			</tr>
			<tr>
				<td>
					�������
				</td>
				<td>
					<html:radio property="isShow" value="1" />
					�˵�
					<html:radio property="isShow" value="2" />
					Ȩ��
					<html:radio property="isShow" value="0" />
					����Ȩ��
				</td>
			</tr>
			<tr>
				<td>
					ͼ�꣺
				</td>
				<td>
					<html:text property="picIco" />
				</td>
			</tr>
			<tr>
				<td>
					����ͼƬ��
				</td>
				<td>
					<html:text property="picImg" />
				</td>
			</tr>
			<tr>
				<td>
					˵����
				</td>
				<td>
					<html:textarea property="info" style="width:80%;" rows="5" />
				</td>
			</tr>
		</table>
		<pageFmt:tail submit="�� ��" />
	</html:form>
</body>
</html:html>
