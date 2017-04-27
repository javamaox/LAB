<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />
	<title>添加机器MacIp绑定交验</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<pageFmt:import />
</head>
<body>
	<html:form action="/machine.do?method=addMachine">
			<pageFmt:head title="机器绑定" />
			<input type='hidden' name='from' value='submit'/>
			<html:hidden property="id" />
			<table>
				<tr>
					<td>
						添加时间：
					</td>
					<td>
						<html:text property="addtime" />
					</td>
					<td>
						主键：
					</td>
					<td>
						<html:text property="id" />
					</td>
				</tr>
				<tr>
					<td>
						IP地址：
					</td>
					<td>
						<html:text property="ip" />
					</td>
					<td>
						MAC地址：
					</td>
					<td>
						<html:text property="mac" />
					</td>
				</tr>
			</table>
			<pageFmt:tail submit="true" button="返 回,javascript:document.location='/${projectName}/sysManage/machine.do?method=schMachine&from=page'"/>
	</html:form>
	<pageFmt:feedBack info="${info}"/>
</body>
</html:html>
