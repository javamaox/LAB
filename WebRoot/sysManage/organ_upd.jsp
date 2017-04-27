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
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<pageFmt:dictInit/>
	<script>
	$("document").ready(function(){
		//注册主菜单方法
		$(".check").click(function(){
			if($(this).parent().parent().attr("id")=="selectTd"){
				$(this).get(0).innerHTML="＋";
				$("#unSelectTd").prepend($(this).parent().clone(true));
				$(this).parent().remove();
			}else{
				$(this).get(0).innerHTML="×";
				$("#selectTd").prepend($(this).parent().clone(true));
				$(this).parent().remove();
			}
		});
	});
	//获取功能单元Id
	function getFuncUnitIds(){
		var divs = $("#selectTd div");
		var unitIds="";
		for(var i=0;i<divs.length;i++){
			unitIds+=divs[i].id+",";
		}
		unitIds = unitIds.substring(0,unitIds.length-1);
		document.location="/${projectName}/sysManage/organ.do?method=updOrgan&organId=${organForm.id}&from=setFuncUnit&unitIds="+unitIds;
	}
	function goPage(act){
		var id = "${organForm.id}";
		var url = "";
		if(act=="upd"){
			organForm.submit();
			return;
		}else if(act=="add"){
			url = "/${projectName}/sysManage/organ.do?method=addOrgan";
		}else if(act=="del"){
			
			if(confirm('请您确认是否删除！')){
			
			}else {
				return;
			}
			url = "/${projectName}/sysManage/organ.do?method=delOrgan&code=${organForm.code}";
		}
		document.location = url+"&id="+id;
	}
	function refreshPage(){
		parent.organTree.document.location = parent.organTree.document.location;
	}
	${scriptCode}
	//机器配置处理
	window.onresize=setMachineIFSize;
	function setMachineIFSize(){
		var iframe=document.getElementById("machineIF");
		iframe.height=document.body.offsetHeight;
	}
	function tab(n){
		if(n==1){
			$("#cfgOrgan").show();
			$("#machineIF").hide();
		}else{
			$("#cfgOrgan").hide();
			$("#machineIF").height($("body").height()-28);
			$("#machineIF").show();
		}
	}
	</script>
	<style>
	.check{cursor:hand;color:#f60;font-weight:bolder;}
	</style>
</head>

<body>
	<bean:define id="useLimitMachine" value="${pageFmt:fn_getConstant('useLimitMachine')}"/>
	<logic:equal value="true" name="useLimitMachine">
	<pageFmt:littleTab>
		<a href="javascript:tab(1)">机构配置</a>
		<a href="javascript:tab(2)">机器绑定</a>
	</pageFmt:littleTab>
	</logic:equal>
	
	<div id="cfgOrgan">
	<html:form action="organ.do?method=updOrgan" method="post">
		<pageFmt:head title="${organForm.name}信息" />
		<table class="viewData">
			<tr>
				<td width="80">
					<html:hidden property="id" />
					<html:hidden property="pid" />
					<html:hidden property="ord" />
					<html:hidden property="treeTrack" />
					<html:hidden property="code" />
					<input type="hidden" name="from" value="submit" />
					机构名称：
				</td>
				<td>
					<html:text property="name" />
				</td>
				<td width="80">
					是否机构：
				</td>
				<td>
					<html:radio property="isMaster" value="1" />
					机构
					<html:radio property="isMaster" value="0" />
					部门
				</td>
			</tr>
			<tr>
				<td>
					机构类别：
				</td>
				<td>
					<pageFmt:dict dictId="sys_organType" value="${organForm.type}" name="type"/>
				</td>
				<td>
					上级机构：
				</td>
				<td>
					<pageFmt:dict dictId="sysOrgan" value="${organForm.pid}" name="pid"/>
				</td>
			</tr>
			<tr>
				<td>
					说明：
				</td>
				<td colspan="3">
					<html:textarea property="info" rows="5" cols="50" />
				</td>
			</tr>
			<logic:present name="colList">
				<logic:iterate name="colList" id="col">
					<tr>
						<td>
							${col.info}：
						</td>
						<td colspan="3">
							<input type="hidden" name="expcolname" value="${col.name}" />
							<logic:empty name="col" property="dict">
								<input type="text" name="expcolvalue" value="${col.value}"
									onblur="val(this,'${col.name}')" />
							</logic:empty>
							<logic:notEmpty name="col" property="dict">
								<pageFmt:dict dictId="${col.dict}" name="expcolvalue"  value="${col.value}" />
							</logic:notEmpty>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
		<pageFmt:tail
			button="修 改,goPage('upd'),删 除,goPage('del'),新增机构,goPage('add')" />
	</html:form>
	<pageFmt:head title="配置功能单元" expand="false"/>
	<table>
		<tr>
			<td id="selectTd" style="border-bottom:#999 1px solid;">
				<logic:notEmpty name="organFunctionUnitList">
					<logic:iterate id="row" name="organFunctionUnitList">
						<div id="${row.id}">
							<logic:empty name="row" property="picIco">
								<img
									src="/${projectName}/sysManage/sysIco/functionUnitDefaultIco.gif" />
							</logic:empty>
							<logic:notEmpty name="row" property="picIco">
								<img src="${row.picIco}" />
							</logic:notEmpty>
							${row.name}
							<span class="check">×</span>
						</div>
					</logic:iterate>
				</logic:notEmpty>
			</td>
		</tr>
		<tr>
			<td id="unSelectTd">
				<logic:notEmpty name="allFUnitList">
					<logic:iterate id="row" name="allFUnitList">
						<div id="${row.id}">
							<logic:empty name="row" property="picIco">
								<img
									src="/${projectName}/sysManage/sysIco/functionUnitDefaultIco.gif" />
							</logic:empty>
							<logic:notEmpty name="row" property="picIco">
								<img src="${row.picIco}" />
							</logic:notEmpty>
							${row.name}
							<span class="check">＋</span>
						</div>
					</logic:iterate>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
	<pageFmt:tail button="确 认,getFuncUnitIds()" />
	<pageFmt:feedBack info="${res}"/>
	<logic:present name="res">
		<logic:notEmpty name="res">
			<script>refreshPage();</script>
		</logic:notEmpty>
	</logic:present>
	</div>
	
	<iframe src="machine.do?method=schMachine" id="machineIF" width="100%" frameborder="0"></iframe>
</body>
</html:html>
