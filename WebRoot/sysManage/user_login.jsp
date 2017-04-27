<%@ page language="java" pageEncoding="GB18030"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>用户登录</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	//登陆框定位
	function BKLocater(){
		//背景图片的像素大小
		this.IMG_W=1200;
		this.IMG_H=768;
		//背景图中登录框的像素大小
		this.FORM_W=327;
		this.FORM_H=200;
		//登录框相对背景图片的像素坐标位置;
		this.FORM_X=286;
		this.FORM_Y=286;
		//起始图片居左
		this.beginLeft=true;
		//图片路径
		this.imgUrl="imgs/log_1024_768k.jpg";
		//初始化方法
		this.init=function(imgSize,formSize,formDim,beginleft,imgurl){
			this.IMG_W=imgSize[0];
			this.IMG_H=imgSize[1];
			this.FORM_W=formSize[0];
			this.FORM_H=formSize[1];
			this.FORM_X=formDim[0];
			this.FORM_Y=formDim[1];
			this.beginLeft=beginleft;
			this.imgUrl=imgurl;
			$("body").css("background","url("+this.imgUrl+") no-repeat #999 center center fixed");
			$("form[name='userForm']").css({width:this.FORM_W,height:this.FORM_H});
		}
		//定位方法
		this.locate=function(){
			var bodyH=document.body.clientHeight;
			var bodyW=document.body.clientWidth;
			if(bodyW<=this.IMG_W){
				$("body").css("background-position","left center");
				var left=this.FORM_X;
				var top=(bodyH-this.IMG_H)/2+this.FORM_Y;
			}else{
				$("body").css("background-position","center center");
				var left=(bodyW-this.IMG_W)/2+this.FORM_X;
				var top=(bodyH-this.IMG_H)/2+this.FORM_Y;
			}
			$("form[name='userForm']").css({left:left+"px",top:top+"px"});
		}
	}
	var bkLocater=new BKLocater();
	window.onresize=function(){
		bkLocater.locate();
	}
	
	function loadPage(){
		bkLocater.init([1280,1024],[327,200],[312,407],true,"imgs/log_1024_768.jpg");
		bkLocater.locate();
		userForm.loginName.focus();
	}
	function refreshPage(){
		document.location = document.location;
	}
	function goPage(act){
		var url = "";
		if(act=="register"){
			url="/${projectName}/sysManage/login.do?method=userRegister";
		}
		document.location = url;
	}
	
	function validate(){
		userForm.password.value=getText();
	}
	</script>
	
	<bean:define id="useLimitMachine" value="${pageFmt:fn_getConstant('sysManage.useLimitMachine')}"/>
	<logic:equal value="true" name="useLimitMachine">
	<object id="macX" classid="clsid:68BF0C6B-88C4-4CB5-B401-9A2CBCAB71C8" codebase="Mac.CAB#version=1,0,0,1" style="display:none;"></object>
	<script>
	window.setTimeout("userForm.mac.value=macX.GetMac()",500);
	</script>
	</logic:equal>
</head>

<body>
	<html:form action="/login.do?method=login" method="post" style="position:absolute;width:300px;padding:30px" onsubmit="return validate()">
		<input type="hidden" value="" name="mac" tabindex="3"/>
		<table class="contentTable" width="100%">
			<tr>
				<td>
					用户名：
				</td>
				<td>
					<html:text property="loginName" style="width:150px" tabindex="1" value="2"/>
				</td>
				<td>
					<input type="submit" value="提 交"/>
				</td>
			</tr>
			<tr>
				<td>
					密　码：
				</td>
				<td>
					<html:hidden property="password"/>
					<div id="keyBoardBlock">
					<%--<applet code="com/qtrmoon/textApplet/Text.class" codebase="./" width="130" height="22" name="textField" id="textField">
						<param name="xxx" value="130"/>
					</applet>
					--%><span id="showKeyBorad" onclick="$('#keyBoardPanel').show()" style="background:url('./keyboard/keyBoard.gif') no-repeat;cursor:pointer">&nbsp;&nbsp;</span>
					<div style="position:absolute;width:372px;height:140px;display:none;" id="keyBoardPanel">
						<div style="text-align:right;"><a style="background:white;text-decoration:none;" href="javascript:var x=$('#keyBoardPanel').hide();">&nbsp;× </a></div>
						<iframe src="./keyboard/index.html" width="372" height="125" frameborder="0" style="border:#ccc 1px solid;"></iframe>
					</div>
					<script>
					$(document).ready(function(){
						$('#keyBoardPanel').css("top",70);
						$('#keyBoardPanel').css("left",85);
						try{
							textField.getText();
						}catch(e){
							$("#keyBoardBlock").prev().eq(0).replaceWith("<input type='password' name='password'/>");
							$("#keyBoardBlock").remove();
						}
						
					});
					function getText(){
						return textField.getText();
					}
					function setText(txt){
						textField.setText(txt);
					}
					</script>
					</div>
				</td>
				<td>
					<input type="button" value="注 册" onclick="goPage('register')"/>
				</td>
			</tr>
		</table>
	</html:form>
	<pageFmt:feedBack info="${info}" />
	
	<%--<applet code="com/qtrmoon/manualsign/SignPanel.class" codebase="./" width="400" height="200" name="signPanel" id="" style="display:none;">
	</applet>--%>
</body>
</html:html>
