/****************************************
字典功能js包，实现职能匹配、弹出树方法。
包内变量及函数名：
变量：
dictModule_aiDictObj
函数名：
dictModule_initAIDictSelecter
css类：
dictInput:AiTree or AiList
dictMutiInput:checkboxInList
*****************************************/

/**dictionary tree*/
var dictModule_CurrNormalPopText;
function dictModule_initNormalPopTree() {
	$(".dict_normalPopTree").click(function(){
		dictModule_CurrNormalPopText=$(this);
		var dictId=$(this).attr("dictId");
		var muti=$(this).attr("muti");
		var ajax=$(this).attr("ajax");
		var dynroot=$(this).attr("dynroot");
		var expand=$(this).attr("expand");
		var context=$(this).attr("context");
		
		var selectedWin = window.open("/"+projectName+"/dictEditor/dictTree.do?method=showDictTree&muti="+muti+"&ajax="+ajax+"&dynroot="+dynroot+"&expand="+expand+"&context="+context+"&dictId=" + dictId,"selectedWin", "width=400,height=500,scrollbars=0,status=1");
		selectedWin.focus();
	});
	//Tree icon
	$(".dict_normalPopTree_ico").click(function(){
		var dict_Obj=$(this).prev("input");
		dictModule_CurrNormalPopText=dict_Obj;
		var dictId=dict_Obj.attr("dictId");
		var muti=dict_Obj.attr("muti");
		var ajax=dict_Obj.attr("ajax");
		var dynroot=dict_Obj.attr("dynroot");
		var expand=dict_Obj.attr("expand");
		var context=dict_Obj.attr("context");
		
		var selectedWin = window.open("/"+projectName+"/dictEditor/dictTree.do?method=showDictTree&muti="+muti+"&ajax="+ajax+"&dynroot="+dynroot+"&expand="+expand+"&context="+context+"&dictId=" + dictId,"selectedWin", "width=400,height=500,scrollbars=0,status=1");
		selectedWin.focus();
	});
}
/********************************************
			  智能匹配类字典.
*********************************************/
var dictModule_aiDictObj;
var dictModule_CurrAIText = 0;
function dictModule_initAIDictSelecter() {
	this.oldValue="";
	$(".dict_AIText").focus(function () {
		var dictInput = $(this);
		var dictInputOffset = dictInput.offset();
		var dictSelecter = dictInput.nextAll(".dict_AIIframe");
		dictSelecter.show();
		dictSelecter.css("left", dictInputOffset.left + "px");
		dictSelecter.css("top", dictInputOffset.top + dictInput.height() + 5 + "px");
		dictModule_CurrAIText = $(this);
	}).keyup(function () {
		var value=$(this).val();
		if(value!=""&&value!=dictModule_aiDictObj.oldValue){
			$(this).nextAll(".dict_AIIframe").get(0).contentWindow.autoSelect($(this).val());
			dictModule_aiDictObj.oldValue=value;
		}
	})

	//验证合法性在treeTag_ai.jsp中
	
	//延时加载iframe
	this.loadIFrame=function(){
		var iframes=$(".dict_AIText");
		for(var i=0;i<iframes.length;i++){
			var iframe=iframes.eq(i).nextAll(".dict_AIIframe");
			iframe.get(0).src=iframe.attr("srcx");
		}
	}
	window.setTimeout("dictModule_aiDictObj.loadIFrame()",200);
}
/********************************************
			  Large智能匹配类字典.
*********************************************/
var dictModule_largeAiDictObj;
var dictModule_CurrLargeAIText = 0;
var dictModule_needclose=false;
function dictModule_initLargeAIDictSelecter() {
	$(".dict_largeAIText").focus(function () {
		dictModule_CurrLargeAIText = $(this);
		var dictInput = $(this);
		var dictInputOffset = dictInput.offset();
		var dictSelecter = dictInput.nextAll(".dict_largeAIIframe");
		dictSelecter.css("left", dictInputOffset.left + "px");
		dictSelecter.css("top", dictInputOffset.top + dictInput.height() + 5 + "px");
		dictSelecter.fadeIn("fast");
		window.setTimeout("dictModule_needclose=true",100);
	});
	$(".dict_largeAIImg").click(function(){
		dictModule_CurrLargeAIText=$(this).prev();
		dictModule_CurrNormalPopText = $(this).prev();//借用
		var dictId=$(this).attr("dictId");
		var selectedWin = window.open("/"+projectName+"/dictEditor/dictTree.do?method=showDictTree&muti=false&ajax=true&dictId=" + dictId,"selectedWin", "width=400,height=500,scrollbars=0,status=1");
		selectedWin.focus();
	});
	//延时加载iframe
	this.loadIFrame=function(){
		var iframes=$(".dict_largeAIText");
		for(var i=0;i<iframes.length;i++){
			var iframe=iframes.eq(i).nextAll(".dict_largeAIIframe");
			iframe.get(0).src=iframe.attr("srcx");
		}
	}
	$(document).click(function(){
		var dictSelecter=dictModule_CurrLargeAIText.nextAll(".dict_largeAIIframe");
		if(dictModule_needclose){
			dictSelecter.hide();
			dictModule_needclose=false;
		}
	});
	window.setTimeout("dictModule_largeAiDictObj.loadIFrame()",200);
}
/********************************************
			  多选下拉字典，使用IFrame来下拉.
*********************************************/
var dictModule_mutiDictObj;
var dictModule_CurrMutiText;
function dictModule_initMutiDictSelecter() {
	$(".dict_mutiText").focus(function () {//DOM<Input.Text>
		dictModule_CurrMutiText = $(this);
		var dictInput = $(this);
		var dictInputOffset = dictInput.offset();
		var dictSelecter = dictInput.nextAll(".dict_mutiIframe");
		dictSelecter.show();
		dictSelecter.css("left", dictInputOffset.left-1 + "px");
		dictSelecter.css("top", dictInputOffset.top + dictInput.height() + 4 + "px");
		if(dictSelecter.attr("src")==""){
			dictId=dictInput.attr("dictId");
			dictSelecter.attr("src","/"+projectName+"/dictEditor/dictTree.do?method=checkboxInList&dictid="+dictId+"");
		}
	});
	//延时加载iframe
	this.loadIFrame=function(){
		var iframes=$(".dict_mutiIframe");
		for(var i=0;i<iframes.length;i++){
			var iframe=iframes.eq(i);
			iframe.get(0).src=iframe.attr("srcx");
		}
	}
	window.setTimeout("dictModule_mutiDictObj.loadIFrame()",200);
}

/********************************************
			  Ajax select分离树字典.
*********************************************/
function dictModule_initAjaxSepTree(){
	this.changeMe=function(){
		var mainSel=$(this);
		var value = mainSel.val();
		var valueElem = mainSel.prevAll("input").eq(0);//设定值
		valueElem.val(value);
		mainSel.nextAll("select").remove();//移除后续select
		if(value!=""){
			var dictId = mainSel.attr("dictId");
			createSelect(mainSel,value,dictId);
		}
	}
	this.createSelect=function(posObj,pid,dictId){
		$.get("/"+projectName+"/dictEditor/dictTree.do?method=schTreeSub&dictId="+dictId+"&id="+pid, function(data){
			if(data.length>0){
				posObj.after("<select dictId='"+dictId+"'></select>");
				var newObj=posObj.next();
				newObj.bind("change",changeMe);
				var dicts=eval("("+data+")");
				newObj.append("<option value=''>请选择</option>");
				for(var i=0;i<dicts.length;i++){
					newObj.append("<option value='"+dicts[i].id+"'>"+dicts[i].label+"</option>");
				}
			}
		});
	}
	var inputObjs=$(".dict_ajaxSepTree");
	for (var i=0;i<inputObjs.length;i++){
		createSelect(inputObjs.eq(i),inputObjs.attr("root"),inputObjs.eq(i).attr("dictId"));
	}
}
/********************************************
			  约束类型字典.
*********************************************/
var dictModule_initReignDict_reigns;
function dictModule_initReignDict(){
	var reigns=$("select[reign]");
	reigns.change(function(){
		var value=$(this).val();
		var reign=$(this).attr("reign");
		var dictId=$(this).attr("dictId");
		if(value!=""&&reign!=""){
			var reignObj=$("select[name='"+reign+"']");
			$.get("/"+projectName+"/dictEditor/dictTree.do?method=schTreeSub&dictId="+dictId+"&id="+value,function(data){
				if(data.length >0){
					var nodeArr = eval("("+data+")");
					var opts=reignObj[0].options;
					opts.length=0;
					opts[opts.length]=new Option("请选择","");
					for(var i=0;i<nodeArr.length-1;i++){
						opts[opts.length]=new Option(nodeArr[i].label,nodeArr[i].id);
					}
 				}
			});
		}
	});
	function setValue(pReign){
		var value=pReign.val();
		if(value!=""){
			var reign=pReign.attr("reign");
			if(reign=="")return;
			var reignObj=$("select[name='"+reign+"']");
			if(reignObj.length==0)return;
			var dictId=reignObj.attr("dictId");
			$.get("/"+projectName+"/dictEditor/dictTree.do?method=schTreeSub&dictId="+dictId+"&id="+value,function(data){
				if(data.length >1){
					var nodeArr = eval("("+data+")");
					var opts=reignObj[0].options;
					opts.length=0;
					opts[opts.length]=new Option("请选择","");
					var info,infoArr,res;
					for(var i=0;i<nodeArr.length-1;i++){
						opts[opts.length]=new Option(nodeArr[i].label,nodeArr[i].id);
					}
 				}
 				reignObj.val(reignObj.attr("val"));
 				setValue(reignObj);
			});
		}
	}
}

/********************************************
			  select树字典.
*********************************************/
function dictModule_initSelectTree(){
	$("select[class='selectTree']").change(function(){
		var val=$(this).val();
		$(this).attr("valOld",val);
		var opt=$(this).children("option[value='"+val+"']");
		$(this).prev(":text").val(opt.text().replace(/[　]*/,''));
		$(this).prev(":text").show();
		$(this).hide();
	});
	$("select[class='selectTree']").blur(function(){
		$(this).hide();
		$(this).prev(":text").show();
	});
	var selectTrees=$("select[class='selectTree']");
	for(var i=0;i<selectTrees.length;i++){
		var opt=selectTrees.eq(i).children("option[value='"+selectTrees.eq(i).val()+"']");
		selectTrees.eq(i).prev(":text").val(opt.text().replace(/[　]*/,''));
		selectTrees.eq(i).hide();
		selectTrees.eq(i).prev(":text").css("width",selectTrees.eq(i).width());
	}
}
function dictModule_initSelectTree_expand(input){
	var inobj=$(input);
	inobj.next("select").show();
	inobj.next("select").focus();
	inobj.hide();
}
/********************************************
			   伪(fake) select树字典.
*********************************************/
var dictModule_fakeSelectTree_text;
function dictModule_initFakeSelectTree(){
	var fakeSTree=$(":text[class='fakeSelectTree']");
	for(var i=0;i<fakeSTree.length;i++){
		var w=fakeSTree.eq(i).width();
		fakeSTree.eq(i).next().next('iframe').css({width:w+20});
	}
	
	fakeSTree.click(function(){
		var offset=$(this).offset();
		var iframe=$(this).next().next('iframe');
		iframe.css({top:offset.top+21,left:offset.left});
		iframe.show();
		iframe.focus();
		dictModule_fakeSelectTree_text=$(this);
	}).mouseover(function(){
		$(this).next("img").attr("src","/"+projectName+"/dictEditor/imgs/fakeselect_o.gif");
	}).mouseout(function(){
		$(this).next("img").attr("src","/"+projectName+"/dictEditor/imgs/fakeselect.gif");
	});
	fakeSTree.next("img").click(function(){
		$(this).prev().click();
	}).mouseover(function(){
		$(this).attr("src","/"+projectName+"/dictEditor/imgs/fakeselect_o.gif");
	}).mouseout(function(){
		$(this).attr("src","/"+projectName+"/dictEditor/imgs/fakeselect.gif");
	});
	
	fakeSTree.next().next('iframe').blur(function(){
		$(this).hide();
	});
}
function dictModule_fakeSelectTree_select(idv){
	dictModule_fakeSelectTree_text.prev("input").val(idv.id);
	var label=idv.label.replace(/[　]*/,'');
	dictModule_fakeSelectTree_text.val(label);
	dictModule_fakeSelectTree_text.next().next('iframe').hide();
	var feedBack=dictModule_fakeSelectTree_text.attr("feedBack");
	if(feedBack!=""){
		var feedBack_fun;
		try{feedBack_fun=eval(feedBack);}
		catch(e){alert("方法'"+feedBack+"'未定义");return;}
		feedBack_fun(idv.id,label,idv.exp,dictModule_fakeSelectTree_text);
	}
}
/********************************************
			  字典DictBuffer的JS扩展.
			  不要用new JSDictBuffer在Jsp中，直接使用JsDictBuffer类。注意s是小写。
*********************************************/
var JsDictBuffer=new JSDictBuffer();
function JSDictBuffer(){
	this.createDict=function(id,pid,label,exp){
		return new this.Dictionary(id,pid,label,exp);
	}
	this.getDict=function (dictid){
		return eval(dictid+"Dict");
	}
	this.findDictById=function (dictid,id){
		var dict=this.getDict(dictid);
		for(var i=0;i<dict.length;i++){
			if(dict[i].id==id){
				return dict[i].label;
			}
		}
	}
	this.getDictById=function (dictid,id){
		var dict=this.getDict(dictid);
		for(var i=0;i<dict.length;i++){
			if(dict[i].id==id){
				return dict[i];
			}
		}
	}
	this.Dictionary=function(id,pid,label,exp){
		this.id=id;
		this.pid=pid;
		this.label=label;
		this.exp=exp;
	}
	this.setSelect=function(dictid,root,selectObj){
		if(selectObj instanceof jQuery){
			;
		}else{
			selectObj=$(selectObj);
		}
		$(selectObj).remove("option");
		
		var dict=this.getDict(dictid);
		var n=0;
		for(var m=0;m<selectObj.length;m++){
			for(var i=0;i<dict.length;i++){
				if(root!=""){
					if(dict[i].pid==root){
						selectObj.eq(m).append("<option value='"+dict[i].id+"'>"+dict[i].label+"</option>");
					}
				}else{
					selectObj.eq(m).append("<option value='"+dict[i].id+"'>"+dict[i].label+"</option>");
				}
			}
	    };
	}
}
/********************************************
			  字典DictBuffer的JS扩展.
*********************************************/
$(document).ready(function () {
	//普通树
	if($(".dict_normalPopTree").length>0){
		dictModule_initNormalPopTree();
	}
	//智能匹配类字典.(列表、树)
	if($(".dict_AIText").length>0){
		dictModule_aiDictObj=new dictModule_initAIDictSelecter();
	}
	//large智能匹配类字典.(列表、树)
	if($(".dict_largeAIText").length>0){
		dictModule_largeAiDictObj=new dictModule_initLargeAIDictSelecter();
	}
	//多选下拉字典，使用IFrame来下拉.
	if($(".dict_mutiText").length>0){
		dictModule_mutiDictObj=new dictModule_initMutiDictSelecter();
	}
	//Ajax select分离树字典.
	if($(".dict_ajaxSepTree").length>0){
		dictModule_initAjaxSepTree();
	}
	//Reign Dictionary
	if($("select[reign]").length>0){
		dictModule_initReignDict();
	}
	//selectTree Dictionary
	if($("select[class='selectTree']").length>0){
		//dictModule_initSelectTree();
	}
	//fakeSelectTree
	if($(":text[class='fakeSelectTree']").length>0){
		dictModule_initFakeSelectTree();
	}
});