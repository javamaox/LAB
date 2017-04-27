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
	<title>listDictCatalog.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
</head>
<body class="bodyCenter">
	<pageFmt:head title="×Öµä±íÌí¼Ó" expand="true" />
	<html:form action="/transfer.do?method=import"	method="post">
		<table>
			<tr>
				<td>
					Ãû³Æ
				</td>
				<td>
					<html:text property="tablename" />
				</td>
			</tr>
		</table>
	</html:form>
	<pageFmt:tail
		prev="/dictEditor/dictEditor.do?method=listAllDictCatalog" />
</body>
</html:html>
