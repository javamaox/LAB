<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

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
	<script>
	var selectedLi;
	function changeTab(atag){
		if(selectedLi!=undefined){
			selectedLi.removeClass("tableM");
			selectedLi.prev().removeClass("tableM");
			selectedLi.next().removeClass("tableM");
			selectedLi.removeClass("tableR");
			selectedLi.prev().removeClass("tableR");
			selectedLi.next().removeClass("tableR");
		}
		selectedLi = atag.parent();
		atag.parent().addClass("tableM");
		atag.parent().prev().addClass("tableL");
		atag.parent().next().addClass("tableR");
	}
	function loadPage(){
		$("a").click(function(){
			if($(this).parent().attr("class")=="tableM"){
				;//重复选择什么都不做
			}else{
				changeTab($(this));
				parent.workFrame.document.location=$(this).attr("link");
			}
		}).focus(function(){//取消选择后的虚线方框
			$(this).blur();
		});
		
		//注册双击最大化方法
		var rowFrameSet,rowsInit,rowsFlex,colFrameSet,colsInit,colsFlex;
		rowFrameSet=top.document.getElementsByTagName("frameset")[0];
		colFrameSet = top.document.getElementsByTagName("frameset")[1];
		rowsInit = rowFrameSet.rows;
		rowsFlex = "0,*,0";
		colsInit = colFrameSet.cols;
		colsFlex = "0,0,*";
		$("body").dblclick(function(){
			if(rowFrameSet.rows == rowsInit){
				rowFrameSet.rows=rowsFlex;
				colFrameSet.cols=colsFlex;
			}else{
				rowFrameSet.rows=rowsInit;
				colFrameSet.cols=colsInit;
			}
		});
		//禁止选择页面方法
		document.onselectstart=function(){
			return false;
		}
		//第一次转入title页刷新workFrame
		if($("a").length>0){
			$("a").eq(0).click();
		}
	});
	</script>
</head>
<body class="titlePage">
	<ul>
		<logic:present name="functionList">
			<logic:iterate id="title" name="functionList">
				<li>
					<a href="javascript:void(0)" link="/${projectName}/${title.link}" target="workFrame">${title.name}</a>
				</li>
			</logic:iterate>
			<li>
				&nbsp;&nbsp;&nbsp;
			</li>
		</logic:present>
	</ul>
</body>
</html:html>
