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
	<title>listDictCatalog.jsp</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<pageFmt:import />
	<style>
	li{line-height:25px;list-style-type:decimal;}
	a{color:blue}
	a:hover{color:#f60}
	</style>
	<script>
	</script>
</head>
<body>
<ul>
	<li><a href="dict01.jsp" target="demoFrame">select型字典</a></li>
	<li><a href="dict02.jsp" target="demoFrame">复选框字典</a></li>
	<li><a href="dict03.jsp" target="demoFrame">单选框字典</a></li>
	<li><a href="dict04.jsp" target="demoFrame">列表型智能匹配字典</a></li>
	<li><a href="dict05.jsp" target="demoFrame">单选弹出树</a></li>
	<li><a href="dict06.jsp" target="demoFrame">Ajax单选弹出树</a></li>
	<li><a href="dict07.jsp" target="demoFrame">多选弹出树</a></li>
	<li><a href="dict08.jsp" target="demoFrame">Ajax多选弹出树</a></li>
	<li><a href="dict09.jsp" target="demoFrame">智能匹配树</a></li>
	<li><a href="dict10.jsp" target="demoFrame">分离树</a></li>
	<li><a href="dict11.jsp" target="demoFrame">约束树</a></li>
	<li><a href="dict12.jsp" target="demoFrame">单选嵌入树</a></li>
	<li><a href="dict13.jsp" target="demoFrame">多选嵌入树</a></li>
	<li><a href="dict14.jsp" target="demoFrame">多选下拉</a></li>
	<li><a href="dict15.jsp" target="demoFrame">单选嵌入树的右键操作</a></li>
	<li><a href="dict16.jsp" target="demoFrame">多选嵌入树的右键操作</a></li>
	<li><a href="dict17.jsp" target="demoFrame">大数据量嵌入树字典</a></li>
	<li><a href="dict18.jsp" target="demoFrame">大数据量智能树字典</a></li>
	<li><a href="dict19.jsp" target="demoFrame">动态根嵌入树</a></li>
	<li><a href="dict20.jsp" target="demoFrame">扩展信息</a></li>
	<li><a href="dict21.jsp" target="demoFrame">select树</a></li>
	<li><a href="dict22.jsp" target="demoFrame">fake select树</a></li>
	<li><a href="test.jsp" target="demoFrame">TEST</a></li>
	<li><a href="../dict.do?method=schTreeDict&dictId=TEST_DICTEDIT&group=_catalog_dictionaryDemo.xml" target="demoFrame">库字典维护</a>
	</li>
	<li><a href="../dict.do?method=schTreeDict&dictId=sex&group=_catalog_dictionaryDemo.xml" target="demoFrame">XML字典维护</a>
	</li>
</ul>
</body>
</html:html>
