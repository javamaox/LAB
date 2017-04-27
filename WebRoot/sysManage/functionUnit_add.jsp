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
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	var dummyRootId="<%=com.qtrmoon.dictEditor.DictBuffer.dummyRootId%>";
	function add(){
		var nodes=sys_menuTree.getCheckedNodes();
		var res="";
		for(var i=0;i<nodes.length;i++){
			res+=nodes[i].id+",";
		}
		if(res!=""){res=res.substring(0,res.length-1).replace(dummyRootId+",","");}
		functionUnitForm.multi.value=res;
		functionUnitForm.submit();
	}
	</script>
</head>

<body>
	<html:form action="functionUnit.do?method=addFunctionUnit" method="post">
		<pageFmt:head title="新增功能单元" />
		<table class="viewData">
			<tr>
				<td width="100">
					<html:hidden property="id" />
					<html:hidden property="ord" />
					<input type="hidden" name="multi" value="">
					<input type="hidden" name="from" value="submit" />
					功能单元名称：
				</td>
				<td>
					<html:text property="name" />
				</td>
				<td width="100">
					功能单元图标：
				</td>
				<td>
					<html:text property="picIco" />
				</td>
			</tr>
			<tr>
				<td valign="top">
					功能单元说明：
				</td>
				<td colspan="3">
					<html:textarea property="info" rows="5" style="width:80%"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					功能配置：
				</td>
				<td colspan="3" style="height:300px;">
					<pageFmt:dictIncludeAjaxTree dictId="sys_menu" expand="4" style="checkbox" value="${funcIds}"/>
				</td>
			</tr>
		</table>
		<pageFmt:tail button="提 交,add()"/>
	</html:form>
</body>
</html:html>
