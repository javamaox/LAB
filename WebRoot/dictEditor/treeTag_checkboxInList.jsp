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

	<title>请选择</title>

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
	var selectedNodeIds = "${param.value}";
	if(selectedNodeIds!=""){selectedNodeIds=","+selectedNodeIds+",";}
	var dummyRootId="${dummyRootId}";
	var muti=${param.muti};
	var ajax=${param.ajax};
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
		if(muti){//多选
			setting1.checkable = true;
		}
		if(ajax){//ajax
			setting1.async = true;
			setting1.asyncUrl = "/${projectName}/dictEditor/dictTree.do?method=schTreeSub&dictId=${param.dictId}";
			setting1.asyncParam = ["id"];
		}
		zTreeObj = $("#treeDemo").zTree(setting1, simpleNodes);
		window.setTimeout("zTreeInit()",500);
		bindOper();
	});
	function zTreeInit(){
		var root=zTreeObj.getNodes()[0];
		zTreeObj.expandNode(root, true, false);
		preCheck(root);
		parent.${param.dictId}Tree=zTreeObj;
	}
	function zTreeOnClick(event, treeId, treeNode){
		if(treeNode.id!=dummyRootId){
			var cat=parent.dictModule_CurrMutiText;
			cat.val(treeNode.label);
			cat.prev().val(treeNode.id);
			cat.nextAll(".dict_mutiIframe").hide();
		}
	}
	function zTreeOnExpand(event, treeId, treeNode){
		preCheck(treeNode);
	}
	//单选树和多选树的预选。
	function preCheck(treeNode){
		if(selectedNodeIds!=""){
			var nodes=zTreeObj.getNodes();
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
	function collect(){
		var datas=zTreeObj.getCheckedNodes();
		var res="",labels="";
		for(var i=0,ii=datas.length;i<ii;i++){
			res+=datas[i].id+",";
			labels+=datas[i].label+",";
		}
		if(res!=""){
			res=res.substring(0,res.length-1);
			labels=labels.substring(0,labels.length-1);
		}
		var cat=parent.dictModule_CurrMutiText;
		cat.val(labels);
		cat.prev().val(res);
		cat.nextAll(".dict_mutiIframe").hide();
	}
	function cancelx(){
    	var cat=parent.dictModule_CurrMutiText;
		cat.val("");
		cat.prev().val("");
		cat.nextAll(".dict_mutiIframe").hide();
    }
	function close(){
		var cat=parent.dictModule_CurrMutiText;
    	cat.nextAll(".dict_mutiIframe").hide();
	}
	function bindOper(){
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
	}
	</script>
</head>
<body>
	<div style="margin:0px;text-align:right;">
	<a href="javascript:collect()" title="确定" style="background:url(imgs/buttons.gif) no-repeat -72px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	<a href="javascript:cancelx()" title="清空" style="background:url(imgs/buttons.gif) no-repeat -36px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	<a href="javascript:close()" title="关闭" style="background:url(imgs/buttons.gif) no-repeat -54px;font-size:15px;text-decoration:none;">&nbsp;&nbsp;</a>
	</div>
	<div style="overflow-y:scroll;overflow-x:auto;width:200px;height:130px;">
	<ul id="treeDemo" class="tree"></ul>
	</div>
</body>
</html:html>