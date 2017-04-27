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

	<title>role_add.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
		function loadPage(){
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
		function goPage(act){
			var organId="${organId}";
			var url = "";
			if(act=="back"){
				url = "/${projectName}/sysManage/role.do?method=schRole";
			}
			document.location = url+"&organId="+organId;
		}
	</script>
</head>

<body>
	<html:form action="role.do?method=addAdminRole" method="post">
		<pageFmt:head title="��ɫ���" />
		<table class="contentTable" width="60%">
			<tr>
				<td>
					<html:hidden property="id" />
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
		<pageFmt:tail printer="true" submit="�� ��" button="�� ��,goPage('back')"/>
	</html:form>
</body>
</html:html>
