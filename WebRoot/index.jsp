<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script>
		window.onload = function(){
	        //var w=window.screen.width,h=window.screen.height;
	        var w=1024,h=708;
	        var winx = window.open("/${projectName}/sysManage/login.do?method=login&from=out", "LoginWindow", 'toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=yes,width='+w+',height='+h);
	        winx.focus();
		 	if(document.body.clientHeight<500||document.body.clientWidth<800){
		 		window.resizeTo(window.screen.width,window.screen.height);
		 	}
		 	if(this.opener==undefined){
				this.opener = null;
				this.close();
			}
		}
	</script>
  </head>
  <body>
  </body>
</html>
