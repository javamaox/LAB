<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<pageFmt:import />
		<script>
		$(document).ready(function(){
		});
		function add(){
			alert("Add");
		}
		</script>
	</head>
	<body>
	<pageFmt:smartInput_JDBC table="TEST_DICT" colsch="XZQMC" colback="XZQHDM" feedback="" name="DQBM" beginSch="1"/>
	<pageFmt:smartInput_BEAN table="TEST_DICT" colsch="XZQMC" colback="XZQHDM" feedback="" name="DQBM" beginSch="1"/>
	<pageFmt:smartInput_DICT table="TEST_DICT"                                 feedback="" name="DQBM" beginSch="1"/>
	<pageFmt:smartInput model="JDBC/BEAN/DICT" table="TEST_DICT" colsch="XZQMC" colback="XZQHDM" feedback="" name="DQBM" beginSch="1"/>	
	</body>
</html>
