<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title>FlashSign</title>
		<pageFmt:import />
		<script>
		function sendPixcel(){
			var colors=sign.helloJs('javamao');
			$.ajax({
			   type: "POST",
			   url: "/${projectName}/flashSign",
			   data: "colors="+colors+"&act=create",
			   success: function(msg){
					parent.showSign("/${projectName}/flashSign?act=view&id="+new Date().getTime());
			   }
			}); 

		}
		</script>
	</head>
	<body>
		<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
			codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"
			width="400" height="200" id="sign" align="middle">
			<param name="allowScriptAccess" value="sameDomain" />
			<param name="movie" value="sign.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<embed src="sign.swf" quality="high" bgcolor="#ffffff"
				width="400" height="200" name="sign" align="middle"
				allowScriptAccess="sameDomain" type="application/x-shockwave-flash"
				pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
		<div style="text-align:center"><input type="button" value="确 定" onclick="sendPixcel()" /></div>
	</body>
</html>
