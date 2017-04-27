<%@ page language="java" pageEncoding="GB2312"%>
<jsp:directive.page import="com.qtrmoon.dictEditor.DictBuffer"/>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>多选嵌入树的右键操作</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<pageFmt:import />
		<pageFmt:dictInit/>
		<style>
		span{color:#ddd}
		#catalog{display:none;}
		</style>
		<script>
		function feedBack(id,label){
            alert(id+" "+label);
        }
		</script>
	</head>
	<body>
		动态根单选嵌入树
		<div style="height:300px">
		<pageFmt:dictIncludeAjaxTree dictId="TEST_DQBM" expand="1" root="131000"/>
		</div>
		动态根Ajax单选嵌入树
		<div style="height:300px">
		<pageFmt:dictIncludeAjaxTree dictId="TEST_DQBM_AJAX"  expand="1" root="130100"/>
		</div>
	</body>
</html>
