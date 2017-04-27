<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />
	<title>查询机器MacIp绑定交验</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<pageFmt:import />
	<script>
	function loadPage(){
		$("#schcfg").click(function(){
			var box=$(this);
			if(box.attr("checked")==true){
				$("#sch1").hide();
				$("#sch2").show();
			}else{
				$("#sch1").show();
				$("#sch2").hide();
			}
		});
	}
	</script>
</head>
<body>
	
	<pageFmt:head title="机器绑定" tableClass="list" button="添加机器,javascript:document.location='/${projectName}/sysManage/machine.do?method=addMachine'"/>
		<html:form action="/machine.do?method=schMachine">
			<input type="hidden" name="from" value="submit"/>
			<div class="tableFrameForm" style="padding:5px;">
				IP/Mac地址：
				<span id="sch1">
					<input type="text" name="schTxt"/>
				</span>
				<span id="sch2" style="display:none;">
					<input type="text" name="schTxt1"/>
					<span style="font-size:18px;font-weight:bolder">→</span>
					<input type="text" name="schTxt2"/>
				</span>
				<input type="checkbox" id="schcfg" name="schcfg" value="1"/>区段查询
				<input type="submit" value="查 询"/>
			</div>
		</html:form>
		<table class="sortable"><thead>
			<tr>
				<th>IP地址</th>
				<th>MAC地址</th>
				<th>添加时间</th>
				<th class="sorttable_nosort">操作</th>
			</tr></thead>
			<logic:present name="machineList">
				<logic:notEmpty name="machineList">
					<logic:iterate name="machineList" id="row">
						<tr>
							<td>${row.ip}</td>
							<td>${row.mac}</td>
							<td>${row.addtime}</td>
							<td>
								<html:link action="/machine.do?method=vieMachine&id=${row.id}">查看</html:link>
								<html:link action="/machine.do?method=updMachine&id=${row.id}">修改</html:link>
								<html:link action="/machine.do?method=delMachine&id=${row.id}">删除</html:link>
							</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
			</logic:present>
		</table>
		<pageFmt:page pageForm="${machineForm}" url="/${projectName}/sysmanage/machine.do?method=schMachine&from=page"/>
		<pageFmt:tail/>
	<pageFmt:feedBack info="${info}"/>
</body>
</html:html>
