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
				title:'ѧ��������',
				buttons : [  
                {name: '���', bclass: 'add', onpress : button},  
                {name: 'ɾ��', bclass: 'delete', onpress : button},  
                {name: '�޸�', bclass: 'modify', onpress : button},                 
                {separator: true}  
                ],
	            usepager: true,
	            useRp: true,
	            pagestat:'Displaying {from} to {to} of {total} items',
				searchitems : [  
	                {display: '��Ϣ���', name : 'RINO', isdefault: true},  
	                {display: '��Ϣ����', name : 'RITITLE'},  
		            {display: '��Ϣ���', name : 'IC.ICNAME'},  
	                {display: '��������', name : 'RIAUTHOR'}]
				
			});
		});
		function button(com,grid)  
	       {  
	           if (com=='ɾ��')  
	               {  
	                   ${"hidden"}.value="delete";  
	                   if($('.trSelected',grid).length==0){  
	                       alert("��ѡ��Ҫɾ��������");  
	                   }else{  
	                       if(confirm('�Ƿ�ɾ���� ' + $('.trSelected',grid).length + ' ����¼��?'))  
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
	           else if (com=='���')  
	               {  
	                   ${"hidden"}.value="add";  
	                   window.location.href="infoAdd.jsp?hidden="+${"hidden"}.value;  
	               }  
	           else if (com=='�޸�')  
	           {  
	               ${"hidden"}.value="modify";  
	               if($(".trSelected").length==1){  
	                   window.location.href="infoAdd.jsp?hidden="+${"hidden"}.value+"&id="+$('.trSelected',grid).find("td").eq(0).text();  
	               }else if($(".trSelected").length>1){  
	                   alert("��ѡ��һ���޸�,����ͬʱ�޸Ķ��");  
	               }else if($(".trSelected").length==0){  
	                   alert("��ѡ��һ����Ҫ�޸ĵ�������Ϣ")  
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
				<th width="100">����</th>
				<th width="100">�Ա�</th>
				<th width="100">����</th>
				<th width="100">ѧ��</th>
				<th width="100">רҵ</th>
				<th width="100">�꼶</th>
			</tr>
			</thead>
			<tbody>
			<tr><td>������������������������</td><td>2</td><td>3</td><td>4</td><td>5</td><td><a href="#">�޸�</a></td></tr>
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
