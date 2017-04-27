<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base target="_self" />

	<title>��ѡ��</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="/${projectName}/dictEditor/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
	<link rel="stylesheet" href="/${projectName}/dictEditor/dictionary.css" type="text/css">
	
	<script type="text/javascript" src="/${projectName}/script/jquery2_6.js"></script>
	<script type="text/javascript" src="/${projectName}/dictEditor/zTree/jquery.ztree-2.6.js"></script>
	<script type="text/javascript">
	var zTreeObj;
	var selectedNodeIds = opener.dictModule_CurrNormalPopText.prev().val();
	if(selectedNodeIds!=""){selectedNodeIds=","+selectedNodeIds+",";}
	var dummyRootId="${dummyRootId}";
	var muti=${param.muti};
	var ajax=${param.ajax};
	var expand="${param.expand}";//��չ��������������10����
	if(expand==0||expand>10 || expand==""){
		expand=10;
	}else if(expand==""){
		expand=1;
	}
	var aiOldValue="";
	$(document).ready(function(){
		var simpleNodes =${simpleNodes};
		var setting1 = {
			fontCss: {},
			showLine: true,
			expandSpeed: "",
			editable: false,
			rootPID : "",
			treeNodeKey : "id",
			treeNodeParentKey : "pid",
			nameCol : "label",
			isSimpleData : true,
			callback: {
				click: zTreeOnClick,
				expand: zTreeOnExpand
			}
		};
		if(muti){//��ѡ
			setting1.checkable = true;
			if("${param.context}".toUpperCase().indexOf("NO")>=0){
				window.setTimeout("$('#lock').click();",600);
			}
			if("${param.context}".toUpperCase().indexOf("AUTO")==0){
				$("#lock").show();
			}
		}
		if(ajax){//ajax
			setting1.async = true;
			setting1.asyncUrl = "/${projectName}/dictEditor/dictTree.do?method=schTreeSub&dictId=${param.dictId}";
			setting1.asyncParam = ["id"];
		}
		if(expand>=0){
			window.setTimeout(expandLevel,1000);
		}
		zTreeObj = $("#treeDemo").zTree(setting1, simpleNodes);
		window.setTimeout("zTreeInit()",500);
		bindOper();
		$("#aiText").keyup(function () {
			var value=$(this).val();
			if(value!=""&&value!=aiOldValue){
				if(ajax){
					$.get("/${projectName}/dictEditor/dictTree.do?method=schNode&label="+value+"&dictId=${param.dictId}",function(data){
						if(data!=""){
							schNode_list=data.split("!");
							selectIdx=0;
							schNode_start();
						}else{
							schNode_list=[];
							selectIdx=0;
							alert("û��������");
						}
					});
				}else{
					autoSelect($(this).val());
				}
				aiOldValue=value;
			}
		});
	});
	function zTreeInit(){
		var root=zTreeObj.getNodes()[0];
		zTreeObj.expandNode(root, true, false);
		preCheck(root);
	}
	function zTreeOnClick(event, treeId, treeNode){
		if(treeNode.id!=dummyRootId){
			opener.dictModule_CurrNormalPopText.val(treeNode.label);
			opener.dictModule_CurrNormalPopText.prev().val(treeNode.id);
			var feedBack=$.trim(opener.dictModule_CurrNormalPopText.attr("feedBack"));
			if(feedBack!=""){
				eval("opener."+feedBack+"('"+treeNode.id+"','"+treeNode.label+"',opener.dictModule_CurrNormalPopText)");
			}
			window.close();
		}
	}
	function zTreeOnExpand(event, treeId, treeNode){
		preCheck(treeNode);
	}
	//Ĭ��չ�����ڵ�
	function expandLevel(){
		expandLevel_dg(zTreeObj.getNodes()[0],1);
		window.setTimeout(
			function(){
				var root=zTreeObj.getNodes()[0];
				preCheck(root);
			},
			500*expand
		);
	}
	function expandLevel_dg(root,level){
		if(level<=expand){
			zTreeObj.expandNode(root, true, false);
			var children=root.nodes;
			if(children!=undefined){
				for(var i=0;i<children.length;i++){
					if(ajax){//ajax��Ҫ��ʱչ��
						window.setTimeout("expandLevel_dg(zTreeObj.getNodeByTId('"+children[i].tId+"'),"+(level+1)+")",500*level);
					}else{
						expandLevel_dg(children[i],level+1);
					}
				}
			}
		}
	}
	//��ѡ���Ͷ�ѡ����Ԥѡ��
	function preCheck(treeNode){
		if(selectedNodeIds!=""){
			var nodes=treeNode.nodes;
			if(muti){
				for(var i=0;i<nodes.length;i++){
					if(selectedNodeIds.indexOf(","+nodes[i].id+",")>=0){
						nodes[i].checked=true;
						zTreeObj.updateNode(nodes[i], false);
					}
				}
			}else{
				for(var i=0;i<nodes.length;i++){
					if(selectedNodeIds.indexOf(","+nodes[i].id+",")>=0){
						zTreeObj.selectNode(nodes[i]);
						break;
					}
				}
			}
		}
	}
	function bindOper(){
		$("#oper_submit").click(function(){
			var checkedNodes=zTreeObj.getCheckedNodes();
			var ids="",labels="";
			for(var i=0;i<checkedNodes.length;i++){
				ids+=checkedNodes[i].id+",";
				labels+=checkedNodes[i].label+",";
			}
			if(ids!=""){
				ids=ids.substring(0,ids.length-1);
				labels=labels.substring(0,labels.length-1);
			}
			opener.dictModule_CurrNormalPopText.val(labels);
			opener.dictModule_CurrNormalPopText.prev().val(ids);
			window.close();
		});
		$("#lock").click(function(){
			var setting=zTreeObj.getSetting();
			if($(this).attr("lock")==1){
				$("#lock").attr("lock",2);
				setting.checkType = { "Y": "", "N": "" };
				zTreeObj.updateSetting(setting);
				$("#lock").attr("src","imgs/chain_2.png");
			}else{
				$("#lock").attr("lock",1);
				setting.checkType = { "Y": "ps", "N": "ps" };
				zTreeObj.updateSetting(setting);
				$("#lock").attr("src","imgs/chain_1.png");
			}
		});
		if(muti){
			;
		}else{
			$("#oper_submit").hide();
			$("#lock").hide();
		}
	}
	
	//��������Զ�ѡ��
	var selectIdx=0;
	var selectValue=0;
	function autoSelect(val){
		selectIdx=0;
		selectValue=val;
		select(0);
	}
	function select(n){
		if(ajax){
			selectIdx+=n;
			if(selectIdx>=schNode_list.length){
				selectIdx=0;
			}
			if(selectIdx<0){
				selectIdx=0;
			}
			schNode_start();
		}else{
			selectIdx+=n;
			var res=zTreeObj.getNodesByParamFuzzy("label", selectValue);
			if(selectIdx>=res.length){
				selectIdx=0;
			}
			if(selectIdx<0){
				selectIdx=0;
			}
			zTreeObj.selectNode(res[selectIdx]);
		}
	}
	function cancelx(){
		var cat=opener.dictModule_CurrNormalPopText;
		cat.val("");
		cat.prev().val("");
		window.close();
	}
	function closex(){
		window.close();
	}
	//��ѯ��չ�����ڵ�ķ���
	var schNode_list;//����ƥ��Ľڵ��չ�������ϡ�[{pid,id,subid},{},{}]
	var schNode_index=0;
	var schNode_ids;
	function schNode_start(){
		schNode_index=0;
		schNode_ids=schNode_list[selectIdx].split(",");
		schNode_expand();
	}
	function schNode_expand(){
		var node=zTreeObj.getNodeByParam("id", schNode_ids[schNode_index]);
		zTreeObj.expandNode(node, true, false);
		schNode_index++;
		zTreeObj.selectNode(node);
		if(schNode_index<schNode_ids.length){
			window.setTimeout(schNode_expand,1000);
		}
	}
	</script>
</head>
<body style="padding:0px;margin:0px;">
	<div style="text-align:right;background:#e1dfdf">
	<a href="javascript:select(-1)" title="��һ��" style="background:url(imgs/buttons.gif) no-repeat 0px ;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	<a href="javascript:select(1)" title="��һ��" style="background:url(imgs/buttons.gif) no-repeat -18px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	<a href="javascript:cancelx()" title="���" style="background:url(imgs/buttons.gif) no-repeat -36px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	<a href="javascript:closex()" title="�ر�" style="background:url(imgs/buttons.gif) no-repeat -54px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	</div>
	
	<div style="background:#ccc;">
	��ѯ��<input type="text" name="aiText" id="aiText"/>
	<img src="imgs/chain_1.png" id="lock" lock="1"/>
	</div>
	
	<div style="width:400px;height:480px;;overflow:scroll;">
	<ul id="treeDemo" class="tree"></ul>
	</div>
	
	<div id="operDiv" style="position:absolute;right:0px;bottom:20px;width:70px;">
	<input type="button" value="ȷ ��" id="oper_submit"/>
	</div>
</body>
</html:html>