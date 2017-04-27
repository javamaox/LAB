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

	<title>组织机构树</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<script>
	var from="${param.from}";//机构、用户维护公用此页。
	function doNodeEvent(id,info,exp,leaf){
		var url="";
		if(from=="organ"){
			url="/${projectName}/sysManage/organ.do?method=updOrgan&organId="+id;
		}else if(from=="user"){
			url="/${projectName}/sysManage/user.do?method=schUserByOrganId&organId="+id;
		}else if(from=="role"){
			url="/${projectName}/sysManage/role.do?method=schRole&organId="+id;
		}
        parent.operFrame.document.location=url;
    }
	function refreshPage(){
		document.location = document.location;
	}
	</script>
</head>

<body>
	<pageFmt:dictIncludeAjaxTree dictId="sysOrgan" expand="1" value="1"/>
	<pageFmt:feedBack info="${res}" />
	<pageFmt:feedBack info="${info}" />
</body>
</html:html>
