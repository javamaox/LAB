function BuildScrollTable() {
	this.resizeTimeoutObj;
	this.c_Div;
	this.t_Div;
	this.l_Div;
	this.r_Div;
	this.staticColWidth=0;//静态列宽度
	this.hasStaticCol;
	this.resizeInterval=true;
	this.tableFrameTHeight=$(".tableFrameT").height();
	this.tableFrameDHeight=$(".tableFrameD").height();
	this.tableFramePageHeight=$(".updownPageSep").height();
	this.pageSepHeight=30;
	this.headHeight;
	this.PAGE_PADDING=this.tableFrameTHeight+this.tableFrameDHeight+this.tableFramePageHeight+this.pageSepHeight;
	this.oldWinWidth=$(window).width();
	this.PERF_COLW=80;
	this.resize=function(){
		var w=$(window).width();
		if(w<this.oldWinWidth){//缩小窗口，会出现滚动条
			this.oldWinWidth=w;
			w=w*1+17;
		}
		w=w-this.staticColWidth;
		var h=$(window).height()-this.PAGE_PADDING-this.headHeight;
		if(w<200){
			w=200;
		}
		if(h<100){
			h=100;
		}
		t_Div.css("width",w);
		r_Div.css("width",w);
		r_Div.css("height",h);
		if(this.hasStaticCol){
			l_Div.css("height",h);
		}
	}
	this.build=function(){
		var bodyClientWidth=$(window).width();//窗口宽(不计滚动条)
		var bodyClientHeight=$(window).height();//窗口高(不计滚动条)。document.body.clientWidth数据高
		var staticColWidth=0;
		//构造双滚动数据格式
		var scrollTargetTable=$(".scrollTargetTable");
		var PAGE_PADDING=scrollTargetTable.attr("space");
		if(isNaN(PAGE_PADDING*1)){
			PAGE_PADDING=this.PAGE_PADDING;
		}else{
			PAGE_PADDING=this.PAGE_PADDING*1+PAGE_PADDING*1;
		}
		this.PAGE_PADDING=PAGE_PADDING;
		var colShowNum=scrollTargetTable.children("thead").children("tr").children("th").length;
		var tablePerfectWidth;
		if(document.body.clientWidth>colShowNum*100){
			tablePerfectWidth=bodyClientWidth;
		}else{
			tablePerfectWidth=colShowNum*this.PERF_COLW;
		}
		scrollTargetTable.css("width",tablePerfectWidth);//先给个合适的宽度，比较宽的列会自动pack。后文在扩展宽度过小的列时才能获取宽列的合适宽度。
		//构造盛放各滚动table的Div。
		scrollTargetTable.before("<div></div>");
		var scrollFrameDiv=scrollTargetTable.prev();
		var scrollStaticColTds=$(".scrollTargetTable .scrollStaticColTd");//固列
		var hasStaticCol=false;
		if(scrollStaticColTds.length>0){//有静态列的情况
			hasStaticCol=true;
		}
		this.hasStaticCol=hasStaticCol;
		if(hasStaticCol){
			staticColWidth=scrollStaticColTds.eq(0).width();
			//构造用于滚动的四Div，没有静态列只有两个div。
			scrollFrameDiv.append( "<div style='overflow:hidden;float:left;'></div>"
								  +"<div style='overflow-x:hidden;overflow-y:scroll;float:left;'></div>"
								  +"<div style='overflow-x:scroll;overflow-y:hidden;float:left;'></div>"
								  +"<div style='overflow:scroll;float:left;'></div>"
								  +"<div style='clear:both'></div>");
			var temp=scrollFrameDiv.children();
			c_Div=temp.eq(0);//角Div
			t_Div=temp.eq(1);//标题Div
			l_Div=temp.eq(2);//固列Div
			r_Div=temp.eq(3);//数据Div
		}else{
			staticColWidth=0;
			//构造用于滚动的二Div，没有静态列只有两个div。
			scrollFrameDiv.append( ""
								  +"<div style='overflow-x:hidden;overflow-y:scroll;float:left;height:26px;'></div>"
								  +""
								  +"<div style='overflow:scroll;float:left;'></div>"
								  +"<div style='clear:both'></div>");
			var temp=scrollFrameDiv.children();
			t_Div=temp.eq(0);//标题Div
			r_Div=temp.eq(1);//数据Div
		}
		this.staticColWidth=staticColWidth;
		if(hasStaticCol){
			//角Div
			c_Div.append("<table><thead><tr></tr></thead></table>");
			var c_Div_tr=c_Div.children("table").children("thead").children("tr").eq(0);
			c_Div_tr.append(scrollStaticColTds.eq(0));
			c_Div.children("table").css("width",staticColWidth);
			c_Div.css("width",staticColWidth);
		}
		//构造t_Div
		t_Div.append("<table></table>");
		t_Div.children("table").append($(".scrollTargetTable thead"));
		t_Div.find("th").css("border","0px").css("text-align","center");
		t_Div.css("width",bodyClientWidth-staticColWidth);
		if(hasStaticCol){
			//构造l_Div
			l_Div.append("<table><tbody><tr></tr></tbody></table>");
			var l_Div_Table=l_Div.children("table").children("tbody");
			for(var i=scrollStaticColTds.length-1;i>0;i--){//添加固列,i=0的是表头的th。
				l_Div_Table.prepend("<tr></tr>");
				l_Div_Table.children("tr").eq(0).append(scrollStaticColTds.eq(i));
			}
			l_Div.children("table").children("tbody").children("tr:even").addClass("alt");//设定固定列的样式
			l_Div.children("table").css("width",staticColWidth+"px");
			l_Div.css("width",staticColWidth);
		}
		//构造r_Div
		r_Div.append("<table></table>");
		r_Div.children("table").append($(".scrollTargetTable tbody"));
		r_Div.css("width",bodyClientWidth-staticColWidth);
		r_Div.scroll(function(){
			if(hasStaticCol){
				l_Div.get(0).scrollTop=$(this).get(0).scrollTop;
			}
			t_Div.get(0).scrollLeft=$(this).get(0).scrollLeft;
		});
		t_Div.children("table").css("width",tablePerfectWidth);//初置宽度。
		r_Div.children("table").css("width",tablePerfectWidth-staticColWidth);//初置宽度。数据宽度要剪掉固定列宽度。没有固列则固列宽为0。
		var t_Div_FirstTds=t_Div.children("table").children("tbody").children("tr").eq(0).children("td");
		var r_Div_FirstTds=r_Div.children("table").children("tbody").children("tr").eq(0).children("td");
		var sum=0,tdArr=[],oneTd;
		for(var i=0,ii=r_Div_FirstTds.length;i<ii;i++){
			var maxwidth=Math.max(r_Div_FirstTds.eq(i).width(),t_Div_FirstTds.eq(i).width());
			if(maxwidth<this.PERF_COLW){
				tdArr[tdArr.length]=this.PERF_COLW;
				sum+=this.PERF_COLW;
			}else{
				tdArr[tdArr.length]=maxwidth;
				sum+=maxwidth;
			}
		}
		if(sum<bodyClientWidth-staticColWidth){
			sum=bodyClientWidth-staticColWidth-17;//剪滚动条宽度。
		}
		r_Div.children("table").css("width",sum);//这里在重置table的宽度
		t_Div.children("table").css("width",sum);
		var t_Div_Table_Ths=t_Div.children("table").children("thead").children("tr").children("th");
		for(var i=0,ii=tdArr.length;i<ii;i++){
			r_Div_FirstTds.eq(i).css("width",tdArr[i]+"px");
			t_Div_Table_Ths.eq(i).css("width",tdArr[i]+"px");
		}
		//宽度设置完成，设置表头高度。设置宽度后表头可能换行。
		var headHeight=0;//表头行高
		if(hasStaticCol){
			headHeight=c_Div.children("table").children("thead").children("tr").eq(0).height();
			var temp=t_Div.children("table").children("thead").children("tr").eq(0).height();//表头行高
			if(headHeight<temp){
				headHeight=temp;
			}
			c_Div.children("table").children("thead").children("tr").eq(0).css("height",headHeight);
			c_Div.css("height",headHeight);
			l_Div_Table.children("tr").css("height",r_Div.children("table").children("tbody").children("tr").height());
			t_Div.children("table").children("thead").children("tr").eq(0).css("height",headHeight);
			t_Div.css("height",headHeight);
			l_Div.css("height",bodyClientHeight-PAGE_PADDING-headHeight);
			r_Div.css("height",bodyClientHeight-PAGE_PADDING-headHeight);
			this.c_Div=c_Div;
			this.l_Div=l_Div;
		}else{
			var headHeight=0;//表头行高
			headHeight=t_Div.children("table").children("thead").children("tr").eq(0).height();
			r_Div.css("height",bodyClientHeight-PAGE_PADDING-headHeight);
		}
		this.headHeight=headHeight;
		this.t_Div=t_Div;
		this.r_Div=r_Div;
		$(window).resize(function(){
			window.clearTimeout(this.resizeTimeoutObj);
			this.resizeTimeoutObj=window.setTimeout("buildScrollTable.resize()",100);
		});
	}
}
//-----------------------------
function BuildScrollTable_x() {
	this.PERF_COLW=80;
	this.build=function(){
		var bodyClientWidth=$(window).width();//窗口宽(不计滚动条)
		var bodyClientHeight=$(window).height();//窗口高(不计滚动条)。document.body.clientWidth数据高
		var scrollTargetTable=$(".scrollTargetTable_x");
		var PAGE_PADDING=scrollTargetTable.attr("space");
		if(isNaN(PAGE_PADDING*1)){
			PAGE_PADDING=0;
		}
		var colShowNum=scrollTargetTable.children("thead").children("tr").children("th").length;
		var tablePerfectWidth;
		if(document.body.clientWidth>colShowNum*100){
			tablePerfectWidth=bodyClientWidth;
		}else{
			tablePerfectWidth=colShowNum*this.PERF_COLW;
		}
		scrollTargetTable.css("width",tablePerfectWidth);//先给个合适的宽度，比较宽的列会自动pack。后文在扩展宽度过小的列时才能获取宽列的合适宽度。
		//构造盛放各滚动table的Div。
		scrollTargetTable.wrap("<div style='overflow-x:scroll;overflow-y:scroll;width:"+bodyClientWidth+";height:"+(bodyClientHeight-PAGE_PADDING)+"px'></div>");
		$(window).resize(function(){
			window.clearTimeout(this.resizeTimeoutObj);
			this.resizeTimeoutObj=window.setTimeout("buildScrollTable.resize()",100);
		});
	}
}