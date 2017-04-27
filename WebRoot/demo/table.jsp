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
		<link rel="stylesheet" type="text/css" href="flexigrid.pack.css" />
		<script src='/${projectName}/script/jquery2_6.js'></script>
		<script type="text/javascript" src="/${projectName}/script/flexigrid.pack.js"></script>
		<script>
		$(document).ready(function(){
			$(".del").click(function(){
				alert("delete");
			});
			$('.flexme2').flexigrid({
				width:'auto',
				height:'200',
				striped: true,
				resizable: false,
				title:'学生花名册',
				buttons : [  
                {name: '添加', bclass: 'add', onpress : button},  
                {name: '删除', bclass: 'delete', onpress : button},  
                {name: '修改', bclass: 'modify', onpress : button},                 
                {separator: true}  
                ],
	            usepager: true,
	            useRp: true,
	            pagestat:'Displaying {from} to {to} of {total} items',
				searchitems : [  
	                {display: '信息编号', name : 'RINO', isdefault: true},  
	                {display: '信息标题', name : 'RITITLE'},  
		            {display: '信息类别', name : 'IC.ICNAME'},  
	                {display: '发布作者', name : 'RIAUTHOR'}]
				
			});
		});
		function button(com,grid)  
	       {  
	           if (com=='删除')  
	               {  
	                   ${"hidden"}.value="delete";  
	                   if($('.trSelected',grid).length==0){  
	                       alert("请选择要删除的数据");  
	                   }else{  
	                       if(confirm('是否删除这 ' + $('.trSelected',grid).length + ' 条记录吗?'))  
	                       {  
	                         var  id ="";  
	                        for(var i=0;i<$('.trSelected',grid).length;i++){  
	                           if(i==$('.trSelected',grid).length-1){  
	                               id += "id="+$('.trSelected',grid).find("td:first").eq(i).text();  
	                           } else {  
	                               id += "id="+$('.trSelected',grid).find("td:first").eq(i).text()+"&";  
	                           }  
	                         }  
	                         window.location.href="../ReleaseInfoServlet?hidden="+${"hidden"}.value+"&"+id;  
	                     }  
	                  }  
	               }  
	           else if (com=='添加')  
	               {  
	                   ${"hidden"}.value="add";  
	                   window.location.href="infoAdd.jsp?hidden="+${"hidden"}.value;  
	               }  
	           else if (com=='修改')  
	           {  
	               ${"hidden"}.value="modify";  
	               if($(".trSelected").length==1){  
	                   window.location.href="infoAdd.jsp?hidden="+${"hidden"}.value+"&id="+$('.trSelected',grid).find("td").eq(0).text();  
	               }else if($(".trSelected").length>1){  
	                   alert("请选择一个修改,不能同时修改多个");  
	               }else if($(".trSelected").length==0){  
	                   alert("请选择一个您要修改的新闻信息")  
	               }  
	           }  
	       } 
		function add(){
			alert("Add");
		}
		</script>
	</head>
	<body style="padding:0px;margin:0px;">
		<table class="flexme2">
			<thead>
			<tr>
				<th width="100">姓名</th>
				<th width="100">性别</th>
				<th width="100">年龄</th>
				<th width="100">学好</th>
				<th width="100">专业</th>
				<th width="100">年级</th>
			</tr>
			</thead>
			<tbody>
			<tr><td>姓名姓名姓名姓名姓名姓名</td><td>2</td><td>3</td><td>4</td><td>5</td><td><a href="#">修改</a></td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			<tr><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
			</tbody>
		</table>
	</body> 
</html>
