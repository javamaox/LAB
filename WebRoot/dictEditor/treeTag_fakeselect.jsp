<%@ page language="java" pageEncoding="GB2312"%>
<jsp:directive.page import="com.qtrmoon.dictEditor.beanSerDao.DictionaryForm"/>
<jsp:directive.page import="com.qtrmoon.dictEditor.DictBuffer"/>
<jsp:directive.page import="java.util.List"/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />
	<title>listDictCatalog.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="/${projectName}/dictEditor/dictionary.css" type="text/css">
	
	<script src='/${projectName}/script/jquery2_6.js'></script>
	<script src='/${projectName}/script/jquery.cookie.js'></script>
	
	<style>
	div{cursor:pointer;font-size:13px;line-height:17px;}
	</style>
	<script type="text/javascript">
	var old;
	var value="";
	$(document).ready(function loadPage(){
		if(value==""){
			old=$("div").eq(0);
			value=old.attr("id");
		}
		$("#"+value).css({background:"#316ac5",color:"white"});
		
		
		$("div").click(function(){
			$(this).css({background:"#316ac5",color:"white"});
			if(old!=undefined){
				old.css({background:"white",color:"black"});
			}
			old=$(this);
			parent.dictModule_fakeSelectTree_select({id:old.attr('id'),label:old.text(),exp:old.attr('exp')});
		}).mouseover(function(){
			if($(this).attr("id")!=old.attr("id")){
				$(this).css({background:"#69a3ff",color:"white"});
			}
		}).mouseout(function(){
			if($(this).attr("id")!=old.attr("id")){
				$(this).css({background:"white",color:"black"});
			}
		});
	});
	</script>
</head>
<body>
<%
String dictId=request.getParameter("dictId");
String dynroot=request.getParameter("dynroot");
String value=request.getParameter("value");
if(value!=null&&value.equals("")){
	out.println("<script>value='"+value+"'</script>");
}
String rootid=dynroot;
if(rootid==null||rootid.equals("")){
	rootid=DictBuffer.dummyRootId;
}
DictionaryForm root=DictBuffer.getDictById(dictId, rootid);
StringBuffer sb=new StringBuffer();
getFakeSelectTreeCode(dictId,root,null,1,sb);
out.print(sb.toString());
%>
</body>
</html:html>
<%!
private void getFakeSelectTreeCode(String dictId,DictionaryForm root,String value,int level,StringBuffer str) {
	String selected="";
	if(value!=null&&!value.equals("")&&value.equals(root.getId())){
		selected="selected='selected'";
	}
	str.append("<div id='"+root.getId()+"' level='"+level+"' exp='"+root.getExp()+"' "+selected+">"+
			"　　　　　　　　　　　　　　　　　　　　".substring(0,level-1)+root.getLabel()+"</div>\r\n");
	level+=1;
	List list=root.getChildren(dictId);
	for(int i=0;i<list.size();i++){
		DictionaryForm dict=(DictionaryForm)list.get(i);
		getFakeSelectTreeCode(dictId,dict,value,level,str);
	}
}
%>