document.onselectstart=function(){
	//return false;
}
var LOWIE=false;
if(navigator.appName=="Microsoft Internet Explorer"){
	var ver=navigator.appVersion;
	if(ver.indexOf("6.")>0||ver.indexOf("7.")>0||ver.indexOf("8.")>0){LOWIE=true;}
}
$(document).ready(function(){
	/********************************************
					Tree method
	 tree explod & unexplod method,and cookies set.
	*********************************************/
	if($(".tree")[0]!=undefined){
		var clsNode=$.cookie('tree_clsNode');
		if(clsNode==null||clsNode==""){
			clsNode = "#";
		}
		$(".tree .minusL").click(function(){
			var subDiv = $(this).parent().nextAll(".childNode").eq(0);
			if($(this).attr("class")=="minusL"){
				subDiv.hide();
				$(this).attr("class","plusL");
				tree_add_close_node($(this).attr("nodeid"));
			}else{
				subDiv.show();
				$(this).attr("class","minusL");
				tree_del_close_node($(this).attr("nodeid"));
			}
			$.cookie('tree_clsNode', clsNode);
		});
		$(".tree .minusT").click(function(){
			var subDiv = $(this).parents(".treePrefix").nextAll(".childNode").eq(0);
			if($(this).attr("class")=="minusT"){
				subDiv.hide();
				$(this).attr("class","plusT");
				tree_add_close_node($(this).attr("nodeid"));
			}else{
				subDiv.show();
				$(this).attr("class","minusT");
				tree_del_close_node($(this).attr("nodeid"));
			}
			$.cookie('tree_clsNode', clsNode);
		});
		function tree_add_close_node(node){
			if(clsNode.indexOf("#"+node+"#")<0){
				clsNode += node+"#";
			}
		}
		function tree_del_close_node(node){
			clsNode = clsNode.replace("#"+node+"#","#");
		}
		if(clsNode!="#"){
			var clsNodeArr = clsNode.split("#");
			var node;
			for(var i=0;i<clsNodeArr.length;i++){
				if(clsNodeArr[i]!=""){
					node = $(".tree span[nodeid='"+clsNodeArr[i]+"']");
					if(node.attr("class")=="minusL"){
						node.attr("class","plusL");
					}else if(node.attr("class")=="minusT"){
						node.attr("class","plusT");
					}
					node = node.parent().nextAll(".childNode").eq(0);
					node.hide();
				}
			}
		}
	}
	//页码行下有按钮行时，页码底线设为虚线。
	if($(".updownPageSep").parent("div").next(".tableFrameD").length>0){
		$(".updownPageSep").css("border-bottom-style","dashed");
	}
	//智能输入
	if($(".smartInput").length>0){
		smartInput_init();
	}
	try{loadPage();}catch(e){};
	window.setTimeout(init_page,100);
});
/********************************************
				littleTab标签辅助方法
*********************************************/
var littleTabSelectedLi;
//Switch the Style of LittleTab,no method will be called.
function littleTabRender(n){
	_littleTabRender($(".littleTab a").eq(n));
}
function littleTabRenderObj(a_jQ){
	_littleTabRender(a_jQ);
}
function _littleTabRender(atag){
	if(littleTabSelectedLi!=undefined){
		littleTabSelectedLi.removeClass("littleTab_liActive");
	}
	littleTabSelectedLi = atag.parent();
	atag.parent().addClass("littleTab_liActive");
	atag.blur();
}
/********************************************
			table充满页面方法。full="auto"
*********************************************/
function resizeFull(table){
	if(table.parent(".tableFrameDataFlowDiv").length>0){
		table.parent(".tableFrameDataFlowDiv").height($(window).height()-tableFrameDataFlow_getOtherHeight(table));
	}
}
function tableFrameDataFlow_getOtherHeight(JQtableFull){
	var otherHeight=JQtableFull.attr("full");
	otherHeight=$.trim(otherHeight);
	if(otherHeight.indexOf("auto")==0){
		var fix=0;
		if(otherHeight.length>4){//auto-30 自动计算再减30
			fix=$.trim(otherHeight.substring(4));
		}
		otherHeight=0;
		if(JQtableFull.parents(".tableFrameData").prev("div").length){//有标题栏
			otherHeight+=30;
		}
		if(JQtableFull.parents(".tableFrameForm").prev("div").length){//有标题栏
			otherHeight+=30;
		}
		
		if(JQtableFull.parents(".tableFrameData").find(".updownPageSep").length){//有分页栏
			otherHeight+=24;
		}
		
		if(JQtableFull.parents(".tableFrameData").next(".tableFrameD").length){//有底栏
			otherHeight+=25;
		}
		if(JQtableFull.parents(".tableFrameForm").next(".tableFrameD").length){//有底栏
			otherHeight+=25;
		}
		otherHeight+=fix*1;
	}
	return otherHeight;
}
/********************************************
				智能输入框
*********************************************/
var smartInput_currentIframe;
function smartInput_init(){
	var smartInputs=$(".smartInput");
	for(var i=0;i<smartInputs.length;i++){
		var smartInput=smartInputs.eq(i);
		var table    = smartInput.attr("table");//查询表
		var colsch   = smartInput.attr("colsch");//查询条件
		var colback  = smartInput.attr("colback");//返回的列
		smartInput.after("<iframe src='/"+projectName+"/smartInput?to=jsp&table="+table+"&colsch="+colsch+"&colback="+colback+"' scrolling='YES' width='350' height='350' style='position:absolute;top=0px;left=0px;display:none;' frameborder='0'></iframe>");
	}
	$(document).click(function(){
		if(smartInput_currentIframe!=undefined){
			smartInput_currentIframe.hide();
		}
	});
}
function smartInput_nameChange(o){
	var obj = $(o);
	var key = $.trim(obj.val());//查询条件值
	if(key.length >= 2){
		smartInput_currentIframe=obj.next("iframe");
		var offset=obj.offset();
		smartInput_currentIframe.css({'top':offset.top+22,'left':offset.left});
		smartInput_currentIframe.get(0).contentWindow.schKey(key);
		smartInput_currentIframe.show();
	}else{
		if(smartInput_currentIframe!=undefined){
			smartInput_currentIframe.hide();
		}
	}
}
function smartInput_setText(backVal,backStr){
	var smartInput=smartInput_currentIframe.prev("input");
	smartInput.val(backVal);
	var feedback = smartInput.attr("feedback");//返回函数名
	smartInput_currentIframe.hide();
	smartInput_currentIframe=undefined;
	eval(feedback+"('"+backStr+"')");
}




function init_page(){
	var windowHeight=$(window).height();
	/********************************************
					表头表尾样式
	*********************************************/
	var hight_Light_Tr="";
	$(".tableFrameData table tr").mouseover(function(){  
	    $(this).addClass("over");
	}).mouseout(function(){ 
	    $(this).removeClass("over");
	}).click(function(){
		if(hight_Light_Tr!=""){
			hight_Light_Tr.removeClass("click");
		}
		$(this).addClass("click");
		hight_Light_Tr=$(this);
	});
	$(".tableFrameData table tr:odd").addClass("alt");
	
	$(".tableFrameTR span[expand]").click(function(){
		if($(this).attr("class")=="tableFrameTitleMinus"){
			$(this).parents(".tableFrameT").next().fadeOut("fast");
			$(this).parents(".tableFrameT").next().next().fadeOut("fast");
			$(this).removeClass("tableFrameTitleMinus");
			$(this).addClass("tableFrameTitlePlus");
			try{pageFmt_head_minus($(this).parents(".tableFrameT"));}catch(e){}
		}else{
			$(this).parents(".tableFrameT").next().fadeIn("fast");
			$(this).parents(".tableFrameT").next().next().fadeIn("fast");
			$(this).removeClass("tableFrameTitlePlus");
			$(this).addClass("tableFrameTitleMinus");
			try{pageFmt_head_plus($(this).parents(".tableFrameT"));}catch(e){}
		}
	});
	/********************************************
				table充满页面方法 table标签加full="auto" 或 full="auto-n" n为修证整数。
	*********************************************/
	if($("table[full]").length>0){
		var JQtableFull=$("table[full]");
		JQtableFull.wrap("<div class='tableFrameDataFlowDiv' style='height:"+(windowHeight-tableFrameDataFlow_getOtherHeight($("table[full]")))+"px;'></div>");
		JQtableFull.resize(function(){
			$(this).parent(".tableFrameDataFlowDiv").height($(window).height()-tableFrameDataFlow_getOtherHeight($("table[full]")));
		});
		var tableFrameDataFlowDiv_head;
		if(JQtableFull.parents(".tableFrameData").length>0){//数据列表悬停表头
			$(".tableFrameDataFlowDiv").scroll(function(){
				if(tableFrameDataFlowDiv_head==undefined){
					$(this).append("<div class='tableFrameDataFlowHead' style='position:absolute;top:0px;left:0px;'><table></table></div>")
					tableFrameDataFlowDiv_head=$(this).children(".tableFrameDataFlowHead");
					tableFrameDataFlowDiv_head.children("table").append($("table[full] tr").eq(0).clone(true));
					var bkimg=$("table[full] thead tr").eq(0).css("background-image");
					tableFrameDataFlowDiv_head.find("th").css("background",bkimg);
					//set th width.
					var originThs=$("table[full] th");
					var headThs=tableFrameDataFlowDiv_head.find("th");
					for(var i=0;i<headThs.length;i++){
						headThs.eq(i).width(originThs.eq(i).width());
					}
				}
				if($(this).scrollTop()==0){
					tableFrameDataFlowDiv_head.hide();
				}else{
					var sTop=0;
					if(LOWIE){sTop=$(this).scrollTop();}else{sTop=$(this).offset().top;}
					tableFrameDataFlowDiv_head.css({top:sTop});
					tableFrameDataFlowDiv_head.show();
				}
			});
		}
	}
	/********************************************
					必填项的小红星
	*********************************************/
	$(".required").after("<span class='requiredStar'>*</span>");
	
	/********************************************
					构造查看页面样式
	*********************************************/
	if($(".viewData").length>0){
		(function(){
			var trs=$(".viewData tr");
			for(var i=0,ii=trs.length;i<ii;i++){
				trs.eq(i).children("td:even").addClass("tableFrameForm_info");
				trs.eq(i).children("td:odd").addClass("tableFrameForm_value");
			}
		})()
	}
	
	/********************************************
					按钮样式替换
	*********************************************/
	//$(".tableFrameForm :button").addClass("button_n");
	//$(".tableFrameForm :submit").addClass("button_n");
	$(".tableFrameD :button").addClass("button_n");
	$(".tableFrameD :submit").addClass("button_n");
	$(".tableFrameT :button").addClass("button2_n");
	$(".tableFrameT :submit").addClass("button2_n");
	
	/********************************************
					构造littleTab标签
	*********************************************/
	if($(".littleTab a").length>0){
		$(".littleTab a").click(function(){
			var atag=$(this);
			if(littleTabSelectedLi!=undefined){
				littleTabSelectedLi.removeClass("littleTab_liActive");
			}
			littleTabSelectedLi = atag.parent();
			atag.parent().addClass("littleTab_liActive");
			atag.blur();
		});
		//try{$(".littleTab a").get(0).click();}catch(e){}
	}
	
	/********************************************
					scroll table.
	*********************************************/
	if($(".scrollTargetTable").length>0){
		buildScrollTable=new BuildScrollTable();
		buildScrollTable.build();
	}
	if($(".scrollTargetTable_x").length>0){
		buildScrollTable=new BuildScrollTable_x();
		buildScrollTable.build();
	}
	/********************************************
					提示窗.
	*********************************************/
	if($(".info .infoinner").length>0){
		var infoDiv=$(".info");
		var hide=infoDiv.attr("hide");
		if(hide!="load"){
			infoDiv.css({"top":(windowHeight-150)/2+"px","left":($(window).width()-234)/2+"px"});
			infoDiv.fadeIn("fast");
			if(hide!="no"){
				var sec=3;
				if(hide=="auto"){
					sec=3;
				}else{
					sec=hide;
				}
				window.setTimeout("$('.info').fadeOut('fast')",sec*1000);
			}
		}
	}
	/********************************************
					jQuery 扩展
	*********************************************/
	jQuery.extend({
		open: function(url,winW,winH,winName,params) {
			var w=window.screen.width;
			var h=window.screen.height;
			var top=(h-winH)/2;
			var left=(w-winW)/2;
			var win=window.open(url,winName,'width='+winW+',height='+winH+',top='+top+',left='+left+","+params);
			win.focus();
			return win;
		}
	});
	jQuery.fn.extend({
		collectVal: function() {
			var res="";
			if(typeof this == "object"&&this.length>0){
				if(this.eq(0).attr("type")=="radio"){
				
				}
				for(var i=0;i<this.length;i++){
					if(this.eq(i).attr("checked")){
						res+=this.eq(i).val()+",";
					}
				}
			}
			if(res!=""){
				res=res.substring(0,res.length-1);
			}
			return res;
		}
	});

	/********************************************
					time picker
	*********************************************/
	if($(".datepicker").length>0){
		$(".datepicker").datepicker({ 
			duration: "", 
			showOn: "both", 
			buttonImage: "/"+projectName+"/script/datepicker/calendar.gif", 
			buttonImageOnly: true 
		}).attr("readonly", "readonly");
	}
	/********************************************
					check all sub checkboxes
	use:<input type="checkbox" class="checkAll" target="box"/>
	*********************************************/
	if($(".checkAll:checkbox").length>0){
		$(".checkAll:checkbox").click(function(){
			var cka=$(this);
			var target=cka.attr("target");
			if(cka.attr("checked")){//不延时，与js页面排序控件不兼容。
				window.setTimeout("$(\":checkbox[name='"+target+"']:not(:disabled)\").attr('checked',true)",100);
			}else{
				window.setTimeout("$(\":checkbox[name='"+target+"']:not(:disabled)\").attr('checked',false)",100);
			}
		});
	}
	/********************************************
					是否勾选checkAll
	<input type="checkbox" class="checkAll" target="box"/>
	*********************************************/
	if($(".checkAll:checkbox").length>0){
		var target=$(".checkAll:checkbox").attr("target");
		$(":checkbox[name='"+target+"']").click(function(){
			var target = $(this).attr("name");
			var flag = true;
			var subck=$(":checkbox[name='"+target+"']");
			for(var i = 0;i<subck.length;i++){
				if(!subck.eq(i).attr("checked")){
					flag = false;
					break;
				}
			}
			
			var cka = $(".checkAll:checkbox[target='"+target+"']").eq(0);
			if(!flag){
				cka.attr("checked",false);
			}else{
				cka.attr("checked",true);
			}
		});
	}
}


/********************************************
				取消backspace返回
*********************************************/
function forbidBackSpace(){
	$(document).keydown(function(event){
		var ev = event||window.event;//获取event对象
		var obj= ev.target||ev.srcElement;//获取事件源
		var t = obj.type||obj.getAttribute("type");//获取事件源类型
		if(event.keyCode==8&&t!="text"&&t!="textarea") {
			return false;
		}
	});
}

/********************************************
				DOM对象居中，例：$(obj).center();
*********************************************/
jQuery.fn.center = function(){
	this.css("position","absolute");
	this.css("top",($(window).height()-this.height())/2+$(window).scrollTop()+"px");
	this.css("left",($(window).width()-this.width())/2+$(window).scrollLeft()+"px");
	return this;
}
