var pngIconObj;
function PNGIcon(){
	this.timeout=0;
	this.isIE=navigator.userAgent.indexOf("MSIE")>-1;
	this.w=100;//icon's width.
	this.h=100;//icon's height.
	this.act={};//click action.
	this.defaultClick=true;//click first icon after load.
	this.highlight=true;//highlight background when selected.
	this.currentIcon=null;
	pngIconObj=this;
	
	this.init=function(w,h,act){
		this.w=w;
		this.h=h;
		this.act=act;
	}
	
	this.printPNG=function(src,id){
		if(this.isIE){
			document.write("<div class='PNGIcon_imgs' action=\""+id+"\" src2='"+src+"' style=\"width:"+this.w+"px;height:"+this.h+"px;cursor:pointer;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+src+"', sizingMethod='scale');\"></div>");
		}else{
			document.write("<img class='PNGIcon_imgs' action=\""+id+"\" src='"+src+"' src2='"+src+"' style='float:left;width:"+this.w+"px;height:"+this.h+"px;cursor:pointer;' />");
		}
		if(this.timeout!=0){
			window.clearTimeout(this.timeout);
		}
		this.timeout=window.setTimeout("pngIconObj.bindEvent()",200);
	}
	
	this.bindEvent=function(){
		$(".PNGIcon_imgs").click(function(){
			if(pngIconObj.highlight){pngIconObj.highlightMenu($(this));}
			pngIconObj.act($(this).attr("action"),$(this));
		}).mouseover(function(){
			var src2=$(this).attr("src2");
			var obj=$(this)[0];
			if(pngIconObj.isIE){
				src2=src2.substring(0,src2.length-4)+"_o"+src2.substring(src2.length-4);
				obj.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+src2+"', sizingMethod='scale')";
			}else{
				src2=src2.substring(0,src2.length-4)+"_o"+src2.substring(src2.length-4);
				obj.src=src2;
			}
		}).mouseout(function(){
			var src2=$(this).attr("src2");
			var obj=$(this)[0];
			if(pngIconObj.isIE){
				obj.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+src2+"', sizingMethod='scale')";
			}else{
				obj.src=src2;
			}
		});
		if(pngIconObj.defaultClick){
			$(".PNGIcon_imgs").eq(0).click();
		}
	}
	
	this.highlightMenu=function(activeMenu){
		if(pngIconObj.currentIcon!=null){
			pngIconObj.currentIcon.parent().css("background","none");
			pngIconObj.currentIcon.parent().css("border","0px");
		}
		activeMenu.parent().css("background","#b5eeff");
		activeMenu.parent().css("border","#7ea8b5 1px solid");
		pngIconObj.currentIcon=activeMenu;
	}
	
	this.click=function(idx){
		$(".PNGIcon_imgs").eq(idx).click();
	}
}