function com_qtrmoon_Remind(){
	//注册点击方法
	$(".com_qtrmoon_RemindStyle img").click(function(){
		var active=$(this).attr("active");
		if(active=="true"){
			var link=$(this).attr("link");
			top.document.location=link;
		}
	});
	
	this.remindPath = [];
	this.index=0;
	this.sound="";
	this.firstLoop=true;
	
	this.remind=function(){
		var index=this.index;
		var remindPath=this.remindPath;
		$.get(remindPath[index++], function(data){
			if(data!=""){
				var arr = data.split(",");
				var src=$("#"+arr[0]).attr("src");
				if(arr[1]!=""&&arr[1]*1>0){
					if(src.indexOf("_alarm.")<0){
						//$("#"+arr[0]).show();
						com_qtrmoon_remind.alarm();
						src=src.substring(0,src.lastIndexOf("."))+"_alarm"+src.substring(src.lastIndexOf("."));
						$("#"+arr[0]).attr("src",src);
						$("#"+arr[0]).attr("active","true");
						$("#"+arr[0]).css("cursor","pointer");
					}
					if(arr.length>2){//有动态参数
						$("#"+arr[0]).attr("link",$("#"+arr[0]).attr("link2")+"&"+arr[2]);
					}
				}else{
					if(src.indexOf("_alarm.")>0){
						//$("#"+arr[0]).show();
						//com_qtrmoon_remind.alarm();
						$("#"+arr[0]).attr("src",$("#"+arr[0]).attr("src2"));
						$("#"+arr[0]).attr("active","false");
						$("#"+arr[0]).css("cursor","normal");
					}
				}
			}
			if(index>=remindPath.length){//循环同步每个提示
				index=0;
				com_qtrmoon_remind.firstLoop=false;
			}
			if(com_qtrmoon_remind.firstLoop){//第一轮进行快速循环
				window.setTimeout("com_qtrmoon_remind.remind()",200);
			}else{//之后5秒监测一个提醒
	  			window.setTimeout("com_qtrmoon_remind.remind()",5000);
	  		}
			com_qtrmoon_remind.index=index;
		});
	}
	
	this.tmout;
	this.currImg;
	this.bindAni=function(){
		$(".com_qtrmoon_RemindStyle img").mouseover(function(){
			if(this.tmout!=undefined){
				window.clearTimeout(com_qtrmoon_remind.tmout);
			}
			com_qtrmoon_remind.currImg=$(this);
			$(this).animate({width: "84",height: "84",marginTop:"16"},500);
			var p=$(this).prev();
			p.animate({width: "60",height: "60",marginTop:"40"},500);
			p=p.prev();
			p.animate({width: "40",height: "40",marginTop:"60"},500);
			var n=$(this).next();
			n.animate({width: "60",height: "60",marginTop:"40"},500);
			n=n.next();
			n.animate({width: "40",height: "40",marginTop:"60"},500);
		}).mouseout(function(){
			this.tmout=window.setTimeout("com_qtrmoon_remind.reset()",1000);
		});
	}
	
	this.reset=function(){
		this.currImg.animate({width: "40",height: "40",marginTop:"60"},500);
		var p=this.currImg.prev();
		p.animate({width: "40",height: "40",marginTop:"60"},500);
		var n=this.currImg.next();
		n.animate({width: "40",height: "40",marginTop:"60"},500);
	}
	
	//播放提示音
	this.alarm=function (){
		com_qtrmoon_Remindsound.src=this.sound;
	}
}