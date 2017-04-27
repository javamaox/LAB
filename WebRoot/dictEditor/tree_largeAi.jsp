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

	<title>listDictCatalog.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="/${projectName}/dictEditor/dictionary.css" type="text/css">
	
	<script src='/${projectName}/script/jquery2_6.js'></script>
	<script src='/${projectName}/script/jquery.cookie.js'></script>
	
	<style>
	#result a{display:block;color:blue;text-decoration:none;line-height:20px;}
	#result a:hover{background:yellow;}
	</style>
	<script type="text/javascript">
	var oldMc="";
	$(document).ready(function loadPage(){
		$("#label").keydown(function(){
			window.setTimeout(checkEq,100);
		});
		//readCookies();
	});
	function checkEq(){
		var label=$.trim($("#label").val());
		if(label==""){
			readCookies();
			oldMc="";
			return;
		}
		if(label!=""&&oldMc!=label){
			$("#loading").css("background","url(imgs/loading.gif) no-repeat");
			$.get("/${projectName}/dictEditor/dictTree.do?method=schLikeLabel&dictId=${param.dictId}&label="+label,function(data){
				if(data!=""){
					var lis=eval("{"+data+"}");
					var res="";
					for(var i=0;i<lis.length;i++){
						res+="<a href=\"javascript:doSelect('"+lis[i].id+"','"+lis[i].label+"')\" id='"+lis[i].id+"'>"+lis[i].label+"</a>";
					}
					$("#result a").remove();
					$("#result").append(res);
				}else{
					$("#result a").remove();
				}
				$("#loading").css("background","none");
			});
			oldMc=label;
		}
	}
	function doSelect(id,label){
		parent.dictModule_CurrLargeAIText.val(label);
		parent.dictModule_CurrLargeAIText.prev().val(id);
		//父窗口的返回函数
		var feedBack="${param.feedBack}";
		if(feedBack!=""){
			eval("parent.${param.feedBack}(id,label)");
		}
		parent.dictModule_CurrLargeAIText.nextAll(".dict_largeAIIframe").fadeOut("fast");
		parent.dictModule_needclose=false;
		writeCookies(id,label);
	}
	function cancelx(){
		parent.dictModule_CurrLargeAIText.val("");
		parent.dictModule_CurrLargeAIText.prev().val("");
		parent.dictModule_CurrLargeAIText.nextAll(".dict_largeAIIframe").fadeOut("fast");
	}
	function close(){
		parent.dictModule_CurrLargeAIText.nextAll(".dict_largeAIIframe").fadeOut("fast");
	}
	//最近选择列表，缓存在Cookies中。
	var usual=[];
	function readCookies(){
		var ck=$.cookie("usual");
		if(ck!=""&&ck!=null){
			usual=eval("["+ck+"]");
			var res="";
			for(var i=0;i<usual.length;i++){
				res+="<a href=\"javascript:doSelect('"+usual[i].id+"','"+usual[i].label+"')\" id='"+usual[i].id+"'>"+usual[i].label+"</a>";
			}
			$("#result a").remove();
			$("#result").append(res);
		}
	}
	function writeCookies(id,label){
		//已加入最近列表的交验。
		for(var i=0;i<usual.length;i++){
			if(usual[i].id==id){
				return;
			}
		}
		
		var res="{id:'"+id+"',label:'"+label+"'},";
		for(var i=0;i<usual.length&&i<10;i++){
			res+="{id:'"+usual[i].id+"',label:'"+usual[i].label+"'},";
		}
		res=res.substring(0,res.length-1);
		$.cookie("usual",res);
	}
	</script>
</head>
<body>
	<div style="">
	<input type="text" id="label" style="width:150px;float:left;"/>
	<span id="loading" style="width:16px;height:16px;display:block;position:absolute;left:130px;top:3px;">&nbsp;&nbsp;</span>
	<a href="javascript:close()" title="关闭" style="background:url(imgs/buttons.gif) no-repeat -54px;text-decoration:none;width:15px;height:15px;float:right;display:block;">&nbsp;&nbsp;</a>
	<a href="javascript:cancelx()" title="清空" style="background:url(imgs/buttons.gif) no-repeat -36px;text-decoration:none;width:15px;height:15px;float:right;display:block;">&nbsp;&nbsp;</a>
	</div>
	<div style="clear:both;"></div>
	<div id="result">
	
	</div>
</body>
</html:html>
