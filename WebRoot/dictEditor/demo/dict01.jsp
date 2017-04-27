<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<pageFmt:import />
		<style>
		span{color:#ddd}
		#catalog{display:none;}
		</style>
		<script>
		</script>
	</head>
	<body>
		<table>
			<tr>
				<td>选择地区：</td>
				<td><pageFmt:dict dictId="TEST_DQBM_LIST"/></td>
			</tr>
		</table>
		<br/>
		<br>
		
		
		<div onclick="catalog.style.display='block'" style="color:#f60;cursor:pointer;">_catalog.xml</div>
		<div id="catalog">
		<br>
		&nbsp; &lt;table&gt;
		<br>
		&nbsp;&nbsp;&nbsp; &lt;id&gt;DQBM_LIST&lt;/id&gt;<span>项目中使用的别名，一个库表可以反演出多个不同的字典，这里就将一个树型字典的部分做成了列表字典。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;tablename&gt;DM_XZQH&lt;/tablename&gt;<span>数据库的表名</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;tabledesc&gt;地区编码&lt;/tabledesc&gt;
		<br>
		&nbsp;&nbsp;&nbsp; &lt;style&gt;1&lt;/style&gt;<span>这里是1代表列表型，也可以写作L#D#N，L：list列表型；D：Database数据库存储；N：Normal默认配置。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;root&gt;130100&lt;/root&gt;<span>这里只取字典表130100下的数据，实际上库里是一个树结构的字典，这里取一部分做成List型。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;pattern&gt;2&lt;/pattern&gt;<span>维护字典时使用的主键生成模板，例如主键为1012003模板为2,2,3则其下一个兄弟主键生成1012004。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;mode&gt;0&lt;/mode&gt;<span>表示字典是id,pid,lable表述还是,id,序号,lable表述。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &lt;mapping&gt;<span>id,pid,label的数据库映射。</span>
		<br>
		&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;id&gt;XZQHDM&lt;/id&gt;
		<br>
		&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;pid&gt;SJDWDM&lt;/pid&gt;
		<br>
		&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &lt;label&gt;XZQMC&lt;/label&gt;
		<br>
		&nbsp;&nbsp;&nbsp; &lt;/mapping&gt;
		<br>
		&nbsp; &lt;/table&gt;
		<br>
		</div>
		
		<div onclick="jsp.style.display='block'" style="color:#f60;cursor:pointer;">JSP页面</div>
		<div id="jsp" style="display:none;">
		&lt;pageFmt:dict dictId=&quot;DQBM_LIST&quot; /&gt;
		</div>
		
		<pageFmt:feedBack info="Hello World"/>
		<pageFmt:feedBack info="Hello World" style="confirm" confirm="alert('confirm')" cancel="alert('cancel')"/>
	</body>
</html>
