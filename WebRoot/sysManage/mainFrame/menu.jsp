
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
		//ע�����˵�����
		$(".firstMenuA").click(function(){
			if(selectedLi!=undefined&&selectedLi.attr("class")==$(this).parent().attr("class")){//���α��ֵ�ѡ�ڵ��toggle��
				$(this).next().slideToggle("fast");
			}else{
				var menuid=$(this).attr("menuid");
				top.titleFrame.document.location="/${projectName}/sysManage/login.do?method=title&showIco=true&id="+menuid;
			}
			clearSelect();
			selectedLi = $(this).parent();
			$(this).parent().attr("class","firstMenuSelected");
			
		});
		//ע��
		$(".secondMenuA").click(function(){
			if($(this).parent().attr("class")=="secondMenuSelected"){
				;//�ظ�ѡ��ʲô������
			}else{
				clearSelect();
				var mutiChild=$(this).attr("mutiChild");
				if(mutiChild=="false"){//<=һ���Ĵ�˵������¸�����
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
		//ע����ѡ����
		document.onselectstart=function(){
			return false;
		}
		//ͬ������
		$.get("/${projectName}/sysManage/transact.do?method=synchOrgan&id=<%=Math.random()*1000%>");
	}
	//showSubͼ��ҳ���ñ�����
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
				<div style="font-size:12pt;color:#ff0000;">��Ȩ��</div>
			</logic:notPresent>
		</ul>
	</div>
	<logic:equal value="icon" name="operMode">
		<div style="position:absolute;bottom:0px;right:0px;">
			<a href="/${projectName}/sysManage/mainFrame/icon_mainframe.jsp" target="_top" style="color:darkblue;">���ص���</a>
		</div>
	</logic:equal>
</body>
</html:html>
