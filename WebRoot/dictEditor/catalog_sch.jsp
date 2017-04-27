<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<title>listDictCatalog.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	function addDict(){
		document.loaction='/${projectName}/dictEditor/dictCatalogEditor.do?method=preSaveDictCatalog';
	}
	</script>
</head>
<body>
	<pageFmt:head title="可维护的字典" button="添加字典表,javascript:addDict()" tableClass="list"/>
	<table class="sortable">
		<thead>
			<tr>
				<th>
					序号
				</th>
				<th>
					字典表名称
				</th>
				<th>
					字典表类型
				</th>
			</tr>
		</thead>
		<logic:iterate id="row" name="list">
			<bean:define id="style" value="${row.style}"/>
			<%if(com.qtrmoon.dictEditor.DictBuffer.isListType(style)){pageContext.setAttribute("isList","true");}else{pageContext.setAttribute("isList","false");}%>
			<tr>
				<td>
					${row.id}
				</td>
				<td>
					<a href="/${projectName}/dictEditor/dict.do?method=schTreeDict&dictId=${row.id}&group=${param.group}">${row.tabledesc}</a>
				</td>
				<td>
					<logic:equal value="true" name="isList">
						<img src="imgs/list.gif" /> 列表型
					</logic:equal>
					<logic:notEqual value="true" name="isList">
						<img src="imgs/tree.gif" /> 树型
					</logic:notEqual>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<pageFmt:tail />
</body>
</html:html>
