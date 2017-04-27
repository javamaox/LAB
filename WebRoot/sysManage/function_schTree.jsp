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
	function doNodeEvent(id,info,exp,leaf){
    	parent.functionOper.document.location="/${projectName}/sysManage/function.do?method=updFunction&funcId="+id;
    }
    function RMenuData(){
    	return [{img:"/${projectName}/polity/jquery_contextMenu/images/add1.png",
    			 info:"添加",
    			 fn_click:function(node){
    			 	selectAll();
    			 }},
    			{img:"/${projectName}/polity/jquery_contextMenu/images/del.png",
    			 info:"修改",
    			 fn_click:function(node){
    			 	alert(collect());
    			 }},
    			 {img:"/${projectName}/polity/jquery_contextMenu/images/del.png",
    			 info:"删除",
    			 fn_click:function(node){
    			 	alert(collect());
    			 }}
    			];
    }
    function setFontCss(treeId, treeNode) {
		if(treeNode.label.indexOf("全能")>0){
			return {"color":"#aaa"};
		}else{
			return {};
		}
	}
	function refreshPage(){
		document.location = document.location;
	}
	</script>
</head>
<body>
	<div style="height:100%">
	<pageFmt:dictIncludeAjaxTree dictId="sys_function" expand="2" value="5"/>
	</div>
	<a style="position:absolute;top:10px;right:20px;display:block;" href="/${projectName}/sysManage/function.do?method=freshFunctionTree">刷新</a>
</body>
</html:html>
