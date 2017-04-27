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
	<link href="/${projectName}/dictEditor/contextMenu/contextMenu.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript" src="/${projectName}/script/jquery2_6.js"></script>
	<script type="text/javascript" src="/${projectName}/dictEditor/zTree/jquery.ztree-2.6.js"></script>
	<script type="text/javascript">
	var zTreeObj;
	var selectedNodeIds = "${param.value}";
	var hasMutiValue=false;//value值是否传入多个
	if(selectedNodeIds!=""&&selectedNodeIds.split(",").length>1){
		hasMutiValue=true;
	}
	if(selectedNodeIds!=""){selectedNodeIds=","+selectedNodeIds+",";}
	var dummyRootId="${dummyRootId}";
	var muti=${param.muti};
	var ajax=${param.ajax};
	var expand=${param.expand};//欲展开几级树，至多10级。
	if(expand==0||expand>10){
		expand=10;
	}else if(expand==""){
		expand=1;
	}
	$(document).ready(function(){
		var simpleNodes =${simpleNodes};
		var setting1 = {
			fontCss: setFontCss,
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
			if("${param.context}".toUpperCase().indexOf("NO")>=0){
				window.setTimeout("$('#lock').click();",600);
			}
			if("${param.context}".toUpperCase().indexOf("AUTO")==0){
				$("#lock").show();
			}
			$("#checkall").show();
			$("#checknone").show();

			window.setTimeout("$('#btnDiv').show();$('#btnDiv').css({'top':$(window).scrollTop(),'left':$(window).width()-17})",3000);
		    $(window).scroll(function() {
		    	$("#btnDiv").css({'top':$(window).scrollTop(),'left':$(window).width()-17});
		    });
		    $(document).click(function() {
		    	window.setTimeout("$('#btnDiv').css({'top':$(window).scrollTop(),'left':$(window).width()-17})",100);
		    });
		}
		if(ajax){//ajax
			setting1.async = true;
			setting1.asyncUrl = "/${projectName}/dictEditor/dictTree.do?method=schTreeSub&dictId=${param.dictId}";
			setting1.asyncParam = ["id"];
		}
		if(parent.RMenuData!=undefined&&parent.RMenuData().length>0){
			setting1.callback.rightClick=zTreeOnRightClick;
		}
		if(parent.appendNodeInfo!=undefined){
			setting1.addDiyDom=addDiyDom;
		}
		if(expand>=0){
			window.setTimeout(expandLevel,1000);
		}
		zTreeObj = $("#treeDemo").zTree(setting1, simpleNodes);
		window.setTimeout("zTreeInit()",200);
		bindOper();
		createRMenu();
	});
	function zTreeInit(){
		parent.${param.dictId}Tree=zTreeObj;
	}
	function zTreeOnClick(event, treeId, treeNode){
		if(treeNode.id!=dummyRootId){
			parent.doNodeEvent(treeNode.id,treeNode.label,treeNode,!treeNode.isParent);
		}
		hideRMenu();
	}
	//手动展开才调用，不会监听zTreeObj.expandNode方法。
	function zTreeOnExpand(event, treeId, treeNode){
		preCheck(treeNode);
	}
	//节点信息增加
	function addDiyDom(treeId, treeNode) {
	    var aObj = $("#" + treeNode.tId + "_a");
	    var editStr = parent.appendNodeInfo(treeNode);
	    aObj.append(editStr);
	};
	//右键事件
	function zTreeOnRightClick(event, treeId, treeNode) {
	    zTreeObj.selectNode(treeNode);
	    $("#rMenu").css({"top":event.clientY+"px", "left":event.clientX+"px", "visibility":"visible"});
	    $("#rMenu").show();
	}
	function hideRMenu(){
		$("#rMenu").hide();
	}
	//树节点渲染方法，调用父页方法。
	function setFontCss(treeId, treeNode){
		try{
			return parent.setFontCss(treeId, treeNode);
		}catch(e){
			return {};
		}
	}
	//树节点图标渲染。
	function showIconForTree(treeId, treeNode) {
	    return treeNode.level != 2;
	};
	//默认展开树节点
	function expandLevel(){
		if(zTreeObj.getNodes()[0]!=undefined){
			expandLevel_dg(zTreeObj.getNodes()[0],1);
			window.setTimeout(
				function(){
					var root=zTreeObj.getNodes()[0];
					preCheck(root);
				},
				500*expand
			);
		}
	}
	function expandLevel_dg(root,level){
		if(level<=expand){
			if(root==undefined)return;
			zTreeObj.expandNode(root, true, false);
			var children=root.nodes;
			if(children!=undefined){
				for(var i=0;i<children.length;i++){
					if(ajax){//ajax需要延时展开
						window.setTimeout("expandLevel_dg(zTreeObj.getNodeByTId('"+children[i].tId+"'),"+(level+1)+")",500*level);
					}else{
						expandLevel_dg(children[i],level+1);
					}
				}
			}
		}
	}
	//单选树和多选树的预选。
	function preCheck(treeNode){
		if(selectedNodeIds==",_firstReal,"){
			var nodes=zTreeObj.getNodes();
			if(nodes.length==1&&nodes[0].id==dummyRootId){
				nodes=nodes[0].nodes;
			}
			for(var i=0;i<nodes.length;i++){
				preCheck_do(nodes[i]);
				break;
			}
		}else if(selectedNodeIds!=""){
			if(selectedNodeIds.indexOf(","+treeNode.id+",")>=0){
				preCheck_do(treeNode);
				if(!muti){//单选找到一个就不再递归。
					return;
				}
			}
			var nodes=treeNode.nodes;
			if(nodes!=undefined){
				for(var i=0;i<nodes.length;i++){
					preCheck(nodes[i]);
				}
			}
		}
	}
	function preCheck_do(node){
		if(muti){
			if(!hasMutiValue&&parent.doNodeEvent!=undefined){//传入单值且实现了doNodeEvent方法。
				zTreeObj.selectNode(node);
				parent.doNodeEvent(node.id,node.label,node,!node.isParent);
			}else{
				node.checked=true;
				zTreeObj.updateNode(node, false);
			}
		}else{
			zTreeObj.selectNode(node);
			if(parent.doNodeEvent!=undefined){
				parent.doNodeEvent(node.id,node.label,node,!node.isParent);
			}
		}
	}
	//注册小锁的事件
	function bindOper(){
		$("#lock").click(function(){
			var setting=zTreeObj.getSetting();
			if($(this).attr("lock")==1){
				$("#lock").attr("lock",2);
				setting.checkType = { "Y": "", "N": "" };
				zTreeObj.updateSetting(setting);
				$("#lock").attr("src","imgs/chain_2.gif");
			}else{
				$("#lock").attr("lock",1);
				setting.checkType = { "Y": "ps", "N": "ps" };
				zTreeObj.updateSetting(setting);
				$("#lock").attr("src","imgs/chain_1.gif");
			}
		});
		if(!muti){
			$("#lock").hide();
		}
	}
	//构建右键菜单的方法
	function createRMenu(){
		var data="";
		try{data=parent.RMenuData();}catch(e){};
		if(data!=""&&data.length>0){
			var res="";
			for(var i=0;i<data.length;i++){
				res+="<li><a href='javascript:void(0)' style='text-align:left;padding-left:10px;'><img src='"+data[i].img+"' border='0'/> "+data[i].info+"</a></li>";
			}
			$("#rMenu").append(res);
			for(var i=0;i<data.length;i++){
				(function(item,func){
					item.bind("click",function(){
						func(zTreeObj.getSelectedNode());
						$("#rMenu").hide();
					});
				})($("#rMenu a").eq(i),data[i].fn_click);
			}
		}
	}
	
	 function selectAll(){
       	zTreeObj.checkAllNodes(true);
     }
     function unSelectAll(){
       	zTreeObj.checkAllNodes(false);
     }
	</script>
</head>
<%
String background=request.getParameter("background");
if(background!=null&&!background.equals("")){
	background="background:"+background+";";
}else{
	background="background:url(imgs/boxBack.gif);";
}
%>
<body onclick="hideRMenu();" style="margin:0px;padding:0px;<%=background%>" >
	<div id="btnDiv" style="height:20px;text-align:right;position:absolute;top:0px;display:none;">
		<img src="imgs/chain_1.gif" id="lock" lock="1" style="display:none;" title="锁定级联"/><br/>
		<img src="imgs/btn_checkall.gif" id="checkall" onclick="selectAll()" style="display:none;margin-top:2px;" title="全选"/><br/>
		<img src="imgs/btn_checknone.gif" id="checknone" onclick="unSelectAll()" style="display:none;margin-top:2px;" title="清空"/>
	</div>
	<ul id="treeDemo" class="tree"></ul>
	
	<ul id="rMenu" class="contextMenu" >
	</ul>
</body>
</html:html>