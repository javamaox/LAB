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
	<script>
		function loadPage(){
			$("a").get(0).click();
		}
		function delFunctionUnit(id){
			parent.functionUnitOper.location="/${projectName}/sysManage/functionUnit.do?method=delFunctionUnit&funcUnitId="+id;
		}
		function addFunctionUnit(){
			parent.functionUnitOper.location='/${projectName}/sysManage/functionUnit.do?method=addFunctionUnit';
		}
	</script>
	<style>
	.funUnitNameDiv{line-height:24px;cursor:hand;}
	</style>
</head>
<body>
	<pageFmt:head title="请选择功能单元" button="新 增,addFunctionUnit()"/>
	<table>
		<logic:notEmpty name="functionUnitList">
			<logic:iterate name="functionUnitList" id="row">
				<tr title="[${row.info}]">
					<td>
						<a href="/${projectName}/sysManage/functionUnit.do?method=updFunctionUnit&funcUnitId=${row.id}"
							target="functionUnitOper">${row.name}</a>
					</td>
					<td width="30">
						<a href="javascript:delFunctionUnit('${row.id}')" onclick="return confirm('请您确认是否删除！');">删除</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>
	</table>
	<pageFmt:tail/>
</body>
</html:html>


