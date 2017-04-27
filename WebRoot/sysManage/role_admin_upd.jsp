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
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<pageFmt:dictInit />
	<script>
		function loadPage(){
			var funcIds = "${funcIds}";
			if(funcIds!=""){
				var code = funcIds.split(",");
				var boxes = document.roleForm.multi;
				if(boxes.length == undefined){
					if(boxes.value==code[0]){
						boxes.checked = "checked";
					}
				}else{
					for(var i=0;i<boxes.length;i++){
						for(var m=0;m<code.length;m++){
							if(boxes[i].value == code[m]){
								boxes[i].checked = "checked";
							}
						}
					}
				}
			}
			<logic:notEmpty name="useDataAuthority">
			var funcIds = "${dataAuthorites}";
			if(funcIds!=""){
				var code = funcIds.split(",");
				var boxes = document.dataAuthForm.dataAuth;
			
				if(boxes.length == undefined){
					if(boxes.value==code[0]){
						boxes.checked = "checked";
					}
				}else{
					for(var i=0;i<boxes.length;i++){
						for(var m=0;m<code.length;m++){
							if(boxes[i].id == code[m]){
								boxes[i].checked = "checked";
							}
						}
					}
				}
			}
			</logic:notEmpty>
			$(".tree :checkbox").click(function(){
					if($(this).attr("checked")==true){
						var pcheck=$(this).parent().prev().prev();
						while(pcheck.attr("name")=="multi"){
							pcheck.attr("checked",true);
							pcheck=pcheck.parent().prev().prev();
						}
					}else{
						var pcheck=$(this).next().next().find(":checkbox");
						pcheck.attr("checked",false);
					}
			});
		}
	</script>
</head>

<body>
	<html:form action="role.do?method=updAdminRole" method="post">
		<pageFmt:head title="修改角色" />
		<table class="contentTable" width="60%">
			<tr>
				<td>
					<html:hidden property="id" />
					<html:hidden property="ord" />
					<html:hidden property="organId" />
					<input type="hidden" name="from" value="submit" />
					角色名称:
				</td>
				<td>
					<html:text property="name" />
				</td>
			</tr>
			<tr>
				<td>
					角色说明:
				</td>
				<td>
					<html:text property="info" />
				</td>
			</tr>
			<tr>
				<td>
					类别:
				</td>
				<td>
					<html:radio property="classify" value="0" />
					浏览访问
					<html:radio property="classify" value="3" />
					注册默认
					<html:radio property="classify" value="2" />
					分管/制定
					<html:radio property="classify" value="1" />
					超系统管理员
				</td>
			</tr>
			<tr>
				<td>
					通用:
				</td>
				<td>
					<html:radio property="isLocal" value="1" />
					本地
					<html:radio property="isLocal" value="0" />
					通用
				</td>
			</tr>
			<tr>
				<td>
					功能树:
				</td>
				<td>
					${code}
				</td>
			</tr>
		</table>
		<pageFmt:tail submit="提 交" />
	</html:form>
	
	<logic:equal name="useDataAuthority" value="true">
		<script>
		function dataAuthChange(obj){
			if(obj.value==0){
				dataResource.style.display="";
			}else{
				dataResource.style.display="none";
			}
			if(obj.value==0){
				helper.innerHTML="钩选该角色可以访问的数据类别。";
			}else if(obj.value==1){
				helper.innerHTML="赋予该角色的用户可以访问用户自己机构的数据，具体哪个机构由用户所属机构而定。";
			}else{
				helper.innerHTML="类似'本机构'，而且会包含用户机构的子机构数据。";
			}
		}
		function help(){
			
		}
		</script>
		<form name="dataAuthForm" action="/${projectName}/sysManage/role.do?method=updAdminRole&type=dataAuth" method="post">
			<pageFmt:head title="角色数据权限配置"/>
			<table>
				<tr>
					<td width="100">
						数据权限类别:
					</td>
					<td>
						<input type="hidden" name="id" value="${roleForm.id}"/>
						<input type="hidden" name="ord" value="${roleForm.ord}"/>
						<input type="hidden" name="organId" value="${roleForm.organId}"/>
						<input type="hidden" name="from" value="submit" />
						<select onchange="dataAuthChange(this)">
							<option value="0">
								数据枚举
							</option>
							<logic:equal value="true" name="useOrgan">
								<option value="1">
									本机构
								</option>
								<option value="2">
									本机构旗下
								</option>
							</logic:equal>
						</select>
						<span id="helper" style="color:#666">钩选该角色可以访问的数据类别。</span>
					</td>
				</tr>
				<tr id="dataResource">
					<td>
						选择数据权限:
					</td>
					<td>
						${dataCode}
					</td>
				</tr>
			</table>
			<pageFmt:tail submit="true"/>
		</form>
	</logic:equal>
</body>
</html:html>
