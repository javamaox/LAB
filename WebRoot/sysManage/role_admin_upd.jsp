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
		<pageFmt:head title="�޸Ľ�ɫ" />
		<table class="contentTable" width="60%">
			<tr>
				<td>
					<html:hidden property="id" />
					<html:hidden property="ord" />
					<html:hidden property="organId" />
					<input type="hidden" name="from" value="submit" />
					��ɫ����:
				</td>
				<td>
					<html:text property="name" />
				</td>
			</tr>
			<tr>
				<td>
					��ɫ˵��:
				</td>
				<td>
					<html:text property="info" />
				</td>
			</tr>
			<tr>
				<td>
					���:
				</td>
				<td>
					<html:radio property="classify" value="0" />
					�������
					<html:radio property="classify" value="3" />
					ע��Ĭ��
					<html:radio property="classify" value="2" />
					�ֹ�/�ƶ�
					<html:radio property="classify" value="1" />
					��ϵͳ����Ա
				</td>
			</tr>
			<tr>
				<td>
					ͨ��:
				</td>
				<td>
					<html:radio property="isLocal" value="1" />
					����
					<html:radio property="isLocal" value="0" />
					ͨ��
				</td>
			</tr>
			<tr>
				<td>
					������:
				</td>
				<td>
					${code}
				</td>
			</tr>
		</table>
		<pageFmt:tail submit="�� ��" />
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
				helper.innerHTML="��ѡ�ý�ɫ���Է��ʵ��������";
			}else if(obj.value==1){
				helper.innerHTML="����ý�ɫ���û����Է����û��Լ����������ݣ������ĸ��������û���������������";
			}else{
				helper.innerHTML="����'������'�����һ�����û��������ӻ������ݡ�";
			}
		}
		function help(){
			
		}
		</script>
		<form name="dataAuthForm" action="/${projectName}/sysManage/role.do?method=updAdminRole&type=dataAuth" method="post">
			<pageFmt:head title="��ɫ����Ȩ������"/>
			<table>
				<tr>
					<td width="100">
						����Ȩ�����:
					</td>
					<td>
						<input type="hidden" name="id" value="${roleForm.id}"/>
						<input type="hidden" name="ord" value="${roleForm.ord}"/>
						<input type="hidden" name="organId" value="${roleForm.organId}"/>
						<input type="hidden" name="from" value="submit" />
						<select onchange="dataAuthChange(this)">
							<option value="0">
								����ö��
							</option>
							<logic:equal value="true" name="useOrgan">
								<option value="1">
									������
								</option>
								<option value="2">
									����������
								</option>
							</logic:equal>
						</select>
						<span id="helper" style="color:#666">��ѡ�ý�ɫ���Է��ʵ��������</span>
					</td>
				</tr>
				<tr id="dataResource">
					<td>
						ѡ������Ȩ��:
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
