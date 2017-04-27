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
	<link href="/${projectName}/dictEditor/contextMenu/contextMenu.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript" src="/${projectName}/script/jquery2_6.js"></script>
	<script type="text/javascript" src="/${projectName}/dictEditor/zTree/jquery.ztree-2.6.js"></script>
	<script type="text/javascript">
	var zTreeObj;
	var selectedNodeIds = "${param.value}";
	var hasMutiValue=false;//valueֵ�Ƿ�����
	if(selectedNodeIds!=""&&selectedNodeIds.split(",").length>1){
		hasMutiValue=true;
	}
	if(selectedNodeIds!=""){selectedNodeIds=","+selectedNodeIds+",";}
	var dummyRootId="${dummyRootId}";
	var muti=${param.muti};
	var ajax=${param.ajax};
	var expand=${param.expand};//��չ��������������10����
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
		if(muti){//��ѡ
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
	//�ֶ�չ���ŵ��ã��������zTreeObj.expandNode������
	function zTreeOnExpand(event, treeId, treeNode){
		preCheck(treeNode);
	}
	//�ڵ���Ϣ����
	function addDiyDom(treeId, treeNode) {
	    var aObj = $("#" + treeNode.tId + "_a");
	    var editStr = parent.appendNodeInfo(treeNode);
	    aObj.append(editStr);
	};
	//�Ҽ��¼�
	function zTreeOnRightClick(event, treeId, treeNode) {
	    zTreeObj.selectNode(treeNode);
	    $("#rMenu").css({"top":event.clientY+"px", "left":event.clientX+"px", "visibility":"visible"});
	    $("#rMenu").show();
	}
	function hideRMenu(){
		$("#rMenu").hide();
	}
	//���ڵ���Ⱦ���������ø�ҳ������
	function setFontCss(treeId, treeNode){
		try{
			return parent.setFontCss(treeId, treeNode);
		}catch(e){
			return {};
		}
	}
	//���ڵ�ͼ����Ⱦ��
	function showIconForTree(treeId, treeNode) {
	    return treeNode.level != 2;
	};
	//Ĭ��չ�����ڵ�
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
				if(!muti){//��ѡ�ҵ�һ���Ͳ��ٵݹ顣
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
			if(!hasMutiValue&&parent.doNodeEvent!=undefined){//���뵥ֵ��ʵ����doNodeEvent������
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
	//ע��С�����¼�
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
	//�����Ҽ��˵��ķ���
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
		<img src="imgs/chain_1.gif" id="lock" lock="1" style="display:none;" title="��������"/><br/>
		<img src="imgs/btn_checkall.gif" id="checkall" onclick="selectAll()" style="display:none;margin-top:2px;" title="ȫѡ"/><br/>
		<img src="imgs/btn_checknone.gif" id="checknone" onclick="unSelectAll()" style="display:none;margin-top:2px;" title="���"/>
	</div>
	<ul id="treeDemo" class="tree"></ul>
	
	<ul id="rMenu" class="contextMenu" >
	</ul>
</body>
</html:html>