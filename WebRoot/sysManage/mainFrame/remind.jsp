
<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />
	<title>top.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<style>
	#soundSel{position:absolute;bottom:0px;line-height:30px;background:white;right:80px;display:none;}
	#soundSel A{text-decoration:none;color:blue}
	#soundSel A:hover{text-decoration:underline;color:#f60}
	
	#noteButton{text-align:right;margin-right:50px;}
	#noteButton img{cursor:pointer;}
	</style>
	<script>
	var userSound;
	$("document").ready(function(){
		//1秒后隐藏叉叉，告诉用户那有个功能。
		window.setTimeout("$(\"#closeButton a\").fadeOut(\"fast\")",1000);
		
		//注册xx显隐方法
		$("#closeButton").mouseover(function(){
			$(this).children("a").fadeIn("fast");
		});
		$("#closeButton a").mouseout(function(){
			$(this).fadeOut("fast");
		});
		
		//隐藏remind页
		var frameSet,rowsInit,rowsFlex;
		if($.browser.msie){
			//isIE
			frameSet = top.document.childNodes[1].childNodes[1].childNodes[1].childNodes[2];
		}else{
			frameSet = top.document.childNodes[1].childNodes[1].childNodes[3].childNodes[5];
		}
		rowsInit = frameSet.rows;
		rowsFlex = rowsInit.substring(0,rowsInit.lastIndexOf(","))+",0";
		$("#closeButton a").click(function(){
			frameSet.rows = rowsFlex;
		});
		
		//打开即时通信
		$("#openChat").click(function(){
			var win = window.open("/${projectName}/ajaxchat/ajaxchat.do?method=openUserList","","width=200px;height=800px;scroll=no;");
			win.focus();
		});
		
		//设置提示音
		userSound=$.cookie('user_sound');
		if(userSound==undefined){
			userSound="s1.wav";
		}
		var hideSoundDiv;
		$("#soundSel").mouseout(function(){
			hideSoundDiv=window.setTimeout("$('#soundSel').hide('fast')",1000);
		}).mouseover(function(){
			window.clearTimeout(hideSoundDiv);
		});
		$("#soundSel a").click(function(){
			userSound=$(this).attr("title")
			$.cookie('user_sound', userSound);
			window.setTimeout("alarm()",100);
		});
		$("#noteButton img").click(function(){
			$("#soundSel").show("fast");
		});
		
		//调用提醒
		window.setTimeout("remind()","5000");
	});
	var remindPath = [];
	<logic:iterate id="row" name="list">
	remindPath[remindPath.length]="/${projectName}/${row.ajaxlink}";
	</logic:iterate>
	var index=0;
	function remind(){
		$.get(remindPath[index++], function(data){
			if(data!=""){
				var arr = data.split(",");
				if(arr[1]!=""&&arr[1]*1>0){
					$("#"+arr[0]).show();
					//window.focus();
					alarm();
				}else{
					$("#"+arr[0]).hide();
				}
			}
			if(index>=remindPath.length){
				//循环同步每个提示
				index=0;
			}
	  		window.setTimeout("remind()","10000");
		}); 
	}
	//播放提示音
	function alarm(){
		sound.src="../remSound/"+userSound;
	}
	</script>
</head>
<body class="remindPage">
<bgsound src="" loop="1" id="sound">
	<ul>
		<li>
			<a href="javascript:void(0);" id="openChat"> <img
					src="../css/imgs/remindIco.gif" border="0" style="float:left" />即时通讯
			</a>
		</li>
		<logic:iterate id="row" name="list">
			<li id="${row.id}" style="display:none">
				<a href="/${projectName}/${row.link}" target="workFrame"><img
						src="/${projectName}/${row.ico}" border="0" style="float:left" />
					${row.text}</a>
			</li>
		</logic:iterate>
	</ul>
	<div id="closeButton">
		<a href="javascript:void(0)">&nbsp;&nbsp;&nbsp;&nbsp;</a>
	</div>
	<div id="noteButton">
		<img src="../imgs/note.gif" title="设定提示音"/>
	</div>
	<div id="soundSel">
		<a href="javascript:void(0)" title="s1.wav">钟声</a>
		<a href="javascript:void(0)" title="s2.wav">女孩</a>
		<a href="javascript:void(0)" title="s3.wav">女声</a>
		<a href="javascript:void(0)" title="s4.wav">铃声</a>
		<a href="javascript:void(0)" title="s5.wav">电话</a>
		<a href="javascript:void(0)" title="s6.wav">钢琴</a>
		<a href="javascript:void(0)" title="s7.wav">鼓声</a>
	</div>
</body>
</html:html>
