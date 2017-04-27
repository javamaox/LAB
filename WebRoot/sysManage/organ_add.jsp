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
	function refreshPage(){
		document.location = document.location;
	}
	function goPage(act){
		var organId="${organForm.pid}";
		var url = "";
		if(act=="back"){
			url = "/${projectName}/sysManage/organ.do?method=updOrgan";
		}
		document.location = url+"&organId="+organId;
	}
	${scriptCode}
	</script>
</head>

<body>
	<html:form action="organ.do?method=addOrgan" method="post">
		<pageFmt:head title="添加${pOrganName}下辖机构" />
		<table class="viewData">
			<tr>
				<td width="80">
					<html:hidden property="id" />
					<html:hidden property="pid" />
					<html:hidden property="ord" />
					<html:hidden property="treeTrack" />
					<input type="hidden" name="from" value="submit" />
					机构名称：
				</td>
				<td>
					<html:text property="name" />
				</td>
				<td width="80">
					是否机构：
				</td>
				<td>
					<html:radio property="isMaster" value="1" />
					机构
					<html:radio property="isMaster" value="0" />
					部门
				</td>
			</tr>
			<tr>
				<td>
					机构类别：
				</td>
				<td>
					<pageFmt:dict dictId="sys_organType" name="type"/>
				</td>
				<td>
					上级机构：
				</td>
				<td>
					<pageFmt:dictDisplay dictId="sysOrgan" value="${organForm.pid}"/>
				</td>
			</tr>
			<tr>
				<td>
					说明：
				</td>
				<td>
					<html:text property="info" />
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<logic:present name="colList">
				<logic:iterate name="colList" id="col">
					<tr>
						<td>
							${col.info}：
						</td>
						<td colspan="3">
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
		<pageFmt:tail submit="提 交" button="返 回,goPage('back')"/>
	</html:form>
</body>
</html:html>
