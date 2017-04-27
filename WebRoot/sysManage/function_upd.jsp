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
	<title>schFunctionTree.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<pageFmt:dictInit/>
	<script>
	function goPage(act){
		var id = "${functionForm.id}";
		var url = "";
		if(act=="upd"){
			functionForm.submit();
			return;
		}else if(act=="add"){
			url = "/${projectName}/sysManage/function.do?method=addFunction";
		}else if(act=="up"){
			url = "/${projectName}/sysManage/function.do?method=upFunction";
		}else if(act=="down"){
			url = "/${projectName}/sysManage/function.do?method=downFunction";
		}else if(act=="del"){
			if(confirm("ȷ��ɾ���ù��ܣ�")){
				url = "/${projectName}/sysManage/function.do?method=delFunction";
			}
		}
		document.location = url+"&id="+id;
	}
	function refreshPage(){
		parent.functionTree.document.location = parent.functionTree.document.location;
	}
	function showInfo(){
		$("#helpDiv").toggle();
	}
	</script>
</head>

<body>
	<html:form action="/function.do?method=updFunction" method="post">
		<pageFmt:head title="�����޸�" button="�� ��,javascript:showInfo()"/>
		<table class="viewData">
			<tr>
				<td width="80">
					<html:hidden property="id" />
					<html:hidden property="pid" />
					<html:hidden property="ord" />
					<html:hidden property="treeTrack" />
					<input type="hidden" name="from" value="submit" />
					�������ƣ�
				</td>
				<td>
					<html:text property="name" />
				</td>
				<td width="80">
					�����ܣ�
				</td>
				<td>
					<pageFmt:dict dictId="sys_function" name="pid" value="${functionForm.pid}"/>
				</td>
			</tr>
			<tr>
				<td>
					���ӵ�ַ��
				</td>
				<td colspan="3">
					<html:text property="link" style="width:80%;" />
				</td>
			</tr>
			<tr>
				<td>
					ģ�����ƣ�
				</td>
				<td>
					<html:text property="moduleName" style="width:80%;" />
				</td>
				<td>
					�������
				</td>
				<td>
					<html:radio property="isShow" value="1" />
					�˵�
					<html:radio property="isShow" value="2" />
					Ȩ��
					<html:radio property="isShow" value="0" />
					����Ȩ��
				</td>
			</tr>
			<tr>
				<td>
					����ͼƬ��
				</td>
				<td>
					<html:text property="picImg" />
				</td>
				<td>
					ͼ�꣺
				</td>
				<td>
					<html:text property="picIco" />
				</td>
			</tr>
			<tr>
				<td>
					˵����
				</td>
				<td colspan="3">
					<html:textarea property="info" style="width:80%;" rows="5" />
				</td>
			</tr>
		</table>
		<pageFmt:tail
			button="�� ��,goPage('upd'),ɾ ��,goPage('del'),����ӹ���,goPage('add'),�� ��,goPage('up'),�� ��,goPage('down')" />
	</html:form>
	<pageFmt:feedBack info="${res}" />
	<pageFmt:feedBack info="${info}" />
	<div id="helpDiv" style="display:none;padding:20px;font-size:13px;">
	<p><b>�˵���</b>�ṹ����ʾ�Ĳ˵���ͬʱҲ��Ϊһ��Ȩ�ޱ������û����ڽ�ɫ����ʱ�����ֶ���ѡ�ýڵ㡣</p>
	<p><b>Ȩ�ޣ�</b>��������ʾ�˵������ڽ�ɫ����ʱ���Թ�ѡ��</p>
	<p><b>����Ȩ�ޣ�</b>�����ɲ˵����ڽ�ɫ����ʱҲ���ܹ�ѡ���丸Ϊ�˵��ڵ㣬����游�˵�һͬ��������Ƴ����൱��Ϊĳ���˵����صĸ�����һ��Ȩ���顣</p>
	<div>
</body>
</html:html>
