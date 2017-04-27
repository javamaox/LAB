
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

	<title>menu.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<SCRIPT>
	var selectedLi;
	
	function loadPage(){
		//注册主菜单方法
		$(".firstMenuA").click(function(){
			if(selectedLi!=undefined&&selectedLi.attr("class")==$(this).parent().attr("class")){//两次保持点选节点才toggle。
				$(this).next().slideToggle("fast");
			}else{
				var menuid=$(this).attr("menuid");
				top.titleFrame.document.location="/${projectName}/sysManage/login.do?method=title&showIco=true&id="+menuid;
			}
			clearSelect();
			selectedLi = $(this).parent();
			$(this).parent().attr("class","firstMenuSelected");
			
		});
		//注册
		$(".secondMenuA").click(function(){
			if($(this).parent().attr("class")=="secondMenuSelected"){
				;//重复选择什么都不做
			}else{
				clearSelect();
				var mutiChild=$(this).attr("mutiChild");
				if(mutiChild=="false"){//<=一个的大菜单，重新高亮。
					selectedLi.attr("class","firstMenuSelected");
				}else{
					selectedLi = $(this).parent();
					$(this).parent().addClass("secondMenuSelected");
				}
			}
			var menuid=$(this).attr("menuid");
			top.titleFrame.document.location="/${projectName}/sysManage/login.do?method=title&id="+menuid;
		});
		function clearSelect(){
			if(selectedLi!=undefined){
				if(selectedLi.attr("class")=="secondMenuSelected"){
					selectedLi.removeClass("secondMenuSelected");
				}else if(selectedLi.attr("class")=="firstMenuSelected"){
					selectedLi.attr("class","firstMenuLi");
				}
			}
		}
		//注册免选方法
		document.onselectstart=function(){
			return false;
		}
		//同步机构
		$.get("/${projectName}/sysManage/transact.do?method=synchOrgan&id=<%=Math.random()*1000%>");
	}
	//showSub图标页调用本方法
	function vil(menuid){
		$("a[menuid='"+menuid+"']").click();
	}
	</SCRIPT>
</head>

<body class="menubody">
	<div>
		<ul class="firstMenu">
			<logic:present name="menu">
				<logic:notEmpty name="menu">
					<logic:iterate name="menu" id="firstMenu">
						<li class="firstMenuLi">
							<a class="firstMenuA" href="javascript:void(0)" menuid="${firstMenu.id}">&gt;&gt; ${firstMenu.name}</a>
							<bean:define id="childNum" value="${pageFmt:fn_size(firstMenu.cnodeList)}"/>
							<logic:greaterThan value="1" name="childNum">
								<bean:define id="style" value=""/>
								<bean:define id="mutiChild" value="true"/>
							</logic:greaterThan>
							<logic:lessEqual value="1" name="childNum">
								<bean:define id="style" value="style='display:none;'"/>
								<bean:define id="mutiChild" value="false"/>
							</logic:lessEqual>
							<ul class="secondMenu" ${style}>
								<logic:iterate id="secondMenu" name="firstMenu" property="cnodeList">
									<li>
										<a class="secondMenuA" href="javascript:void(0)" menuid="${secondMenu.id}" mutiChild="${mutiChild}">${secondMenu.name}</a>
									</li>
								</logic:iterate>
							</ul>
						</li>
					</logic:iterate>
				</logic:notEmpty>
			</logic:present>
			<logic:notPresent name="menu">
				<div style="font-size:12pt;color:#ff0000;">无权限</div>
			</logic:notPresent>
		</ul>
	</div>
	<logic:equal value="icon" name="operMode">
		<div style="position:absolute;bottom:0px;right:0px;">
			<a href="/${projectName}/sysManage/mainFrame/icon_mainframe.jsp" target="_top" style="color:darkblue;">返回导航</a>
		</div>
	</logic:equal>
</body>
</html:html>
