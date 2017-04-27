<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title>FlashSign</title>
		<pageFmt:import />
		<script>
		function showSign(pic){
			signImg.src=pic;
		}
		</script>
	</head>
	<body>
		<img src="" id="signImg"/>
		<iframe src="main.jsp" frameborder="0" width="400" height="225" style="border:black 1px solid;" scrolling="no"></iframe>
	</body>
</html>
