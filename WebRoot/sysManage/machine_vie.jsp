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
	<title>查看机器MacIp绑定交验</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<pageFmt:import />
</head>
<body>
			<pageFmt:head title="查看机器MacIp绑定交验详情" />
			<table>
				<tr>
					<td width='15%'>
						添加时间：
					</td>
					<td width='35%'>
						<fmt:formatDate value="${machineDto.addtime}" timeStyle="medium"/>
					</td>
					<td width='15%'>
						主键：
					</td>
					<td width='35%'">
						${machineForm.id}
					</td>
				</tr>
				<tr>
					<td>
						IP地址：
					</td>
					<td>
						${machineForm.ip}
					</td>
					<td>
						MAC地址：
					</td>
					<td>
						${machineForm.mac}
					</td>
				</tr>
			</table>
			<pageFmt:tail button="返 回,javascript:document.location='/${projectName}/sysmanage/machine.do?method=schMachine&from=page'"/>
</body>
</html:html>
