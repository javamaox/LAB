
function atCalendarControl() {
    var calendar = this;
    this.calendarPad = null;
    this.prevMonth = null;
    this.nextMonth = null;
    this.prevYear = null;
    this.nextYear = null;
    this.goToday = null;
    this.calendarClose = null;
    this.calendarAbout = null;
    this.head = null;
    this.body = null;
    this.today = [];
    this.currentDate = [];
    this.sltDate;
    this.target;
    this.source;
    this.yearTxt = null;
    this.monthTxt = null;
    this.dayTxt = null;
    /************** ??????????????????????????? *********************/
    this.addCalendarPad = function () {
        document.write("<div id='divCalendarpad' style='position:absolute;top:100;left:0;width:255;height:167;display:none;'>");
        document.write("<iframe frameborder=0 height=168 width=255></iframe>");
        document.write("<div style='position:absolute;top:4;left:4;width:248;height:164;background-color:#336699;'></div>");
        document.write("</div>");
        calendar.calendarPad = document.getElementById("divCalendarpad");
    };
    /************** ?????????????????? *********************/
    this.addCalendarBoard = function () {
        var BOARD = this;
        var divBoard = document.createElement("div");
        calendar.calendarPad.insertAdjacentElement("beforeEnd", divBoard);
        divBoard.style.cssText = "position:absolute;top:0;left:0;width:250;height:166;border:1 outset;background-color:buttonface;";
        var tbBoard = document.createElement("table");
        divBoard.insertAdjacentElement("beforeEnd", tbBoard);
        tbBoard.style.cssText = "position:absolute;top:0;left:0;width:100%;height:10;font-size:9pt;";
        tbBoard.cellPadding = 0;
        tbBoard.cellSpacing = 1;
        tbBoard.bgColor = "#333333";
        /************** ?????????????????????????????? *********************/
        /*********** Calendar About Button ***************/
        trRow = tbBoard.insertRow(0);
        calendar.calendarAbout = calendar.insertTbCell(trRow, 0, "-", "center");
        calendar.calendarAbout.title = "\u5E2E\u52A9";
        calendar.calendarAbout.onclick = function () {
            calendar.about();
        };
        /*********** Calendar Head ***************/
        tbCell = trRow.insertCell(1);
        tbCell.colSpan = 5;
        tbCell.bgColor = "#99CCFF";
        tbCell.align = "center";
        tbCell.style.cssText = "cursor:default";
        calendar.head = tbCell;
        /*********** Calendar Close Button ***************/
        tbCell = trRow.insertCell(2);
        calendar.calendarClose = calendar.insertTbCell(trRow, 2, "x", "center");
        calendar.calendarClose.title = "\u5173\u95ED";
        calendar.calendarClose.onclick = function () {
            calendar.hide();
        };
        /*********** Calendar PrevYear Button ***************/
        trRow = tbBoard.insertRow(1);
        calendar.prevYear = calendar.insertTbCell(trRow, 0, "&lt;&lt;", "center");
        calendar.prevYear.title = "\u524D\u4E00\u5E74";
        calendar.prevYear.onmousedown = function () {
            calendar.currentDate[0]--;
            calendar.show(calendar.target, calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + calendar.currentDate[2], calendar.source);
        };
        /*********** Calendar PrevMonth Button ***************/
        calendar.prevMonth = calendar.insertTbCell(trRow, 1, "&lt;", "center");
        calendar.prevMonth.title = "\u524D\u4E00\u6708";/*?????????*/
        calendar.prevMonth.onmousedown = function () {
            calendar.currentDate[1]--;
            if (calendar.currentDate[1] == 0) {
                calendar.currentDate[1] = 12;
                calendar.currentDate[0]--;
            }
            calendar.show(calendar.target, calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + calendar.currentDate[2], calendar.source);
        };
        /*********** Calendar Today Button ***************/
        calendar.goToday = calendar.insertTbCell(trRow, 2, "\u4eca\u5929", "center", 3);
        calendar.goToday.title = "\u9009\u62e9\u4eca\u5929";
        calendar.goToday.onclick = function () {
            calendar.sltDate = calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + calendar.currentDate[2];
            calendar.target.value = calendar.sltDate;
            calendar.hide();
    //calendar.show(calendar.target,calendar.today[0]+"-"+calendar.today[1]+"-"+calendar.today[2],calendar.source);
        };
        /*********** Calendar NextMonth Button ***************/
        calendar.nextMonth = calendar.insertTbCell(trRow, 3, "&gt;", "center");
        calendar.nextMonth.title = "\u4e0b\u4e00\u6708";
        calendar.nextMonth.onmousedown = function () {
            calendar.currentDate[1]++;
            if (calendar.currentDate[1] == 13) {
                calendar.currentDate[1] = 1;
                calendar.currentDate[0]++;
            }
            calendar.show(calendar.target, calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + calendar.currentDate[2], calendar.source);
        };
        /*********** Calendar NextYear Button ***************/
        calendar.nextYear = calendar.insertTbCell(trRow, 4, "&gt;&gt;", "center");
        calendar.nextYear.title = "\u4e0b\u4e00\u5e74";
        calendar.nextYear.onmousedown = function () {
            calendar.currentDate[0]++;
            calendar.show(calendar.target, calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + calendar.currentDate[2], calendar.source);
        };
        trRow = tbBoard.insertRow(2);
        var cnDateName = new Array("\u5468\u65e5", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d");
        for (var i = 0; i < 7; i++) {
            tbCell = trRow.insertCell(i);
            tbCell.innerText = cnDateName[i];
            tbCell.align = "center";
            tbCell.width = 35;
            tbCell.style.cssText = "cursor:default;border:1 solid #99CCCC;background-color:#99CCCC;";
        }
        /*********** Calendar Body ***************/
        trRow = tbBoard.insertRow(3);
        tbCell = trRow.insertCell(0);
        tbCell.colSpan = 7;
        tbCell.height = 97;
        tbCell.vAlign = "top";
        tbCell.bgColor = "#F0F0F0";
        var tbBody = document.createElement("table");
        tbCell.insertAdjacentElement("beforeEnd", tbBody);
        tbBody.style.cssText = "position:relative;top:0;left:0;width:245;height:103;font-size:9pt;";
        tbBody.cellPadding = 0;
        tbBody.cellSpacing = 1;
        calendar.body = tbBody;
        /************* quick select **************/
        trRow = tbBoard.insertRow(4);
        tbCell = trRow.insertCell(0);
        tbCell.colSpan = 7;
        tbCell.bgColor = "#F0F0F0";
        yearTxt = document.createElement("input");
        yearTxt.type = "text";
        yearTxt.size = "4";
        monthTxt = document.createElement("input");
        monthTxt.type = "text";
        monthTxt.size = "2";
        dayTxt = document.createElement("input");
        dayTxt.type = "text";
        dayTxt.size = "2";
        submitTxt = document.createElement("input");
        submitTxt.type = "button";
        submitTxt.value = "\u786e \u5b9a";
        submitTxt.onclick = function () {
            if (!isInt(yearTxt.value) || !isInt(monthTxt.value) || !isInt(dayTxt.value)) {
                alert("\u5e74\u6708\u65e5\u5fc5\u987b\u4e3a\u6570\u5b57");
                return;
            }
            if (yearTxt.value.length > 4 || monthTxt.value.length > 2 || dayTxt.value.length > 2) {
                alert("\u5e74\u6708\u65e5\u683c\u5f0f\u9519\u8bef");//年月日格式错误
                return;
            }
            if (monthTxt.value > 12 || monthTxt.value < 1 || dayTxt.value > 31 || dayTxt.value < 1) {
                alert("\u5e74\u6708\u65e5\u683c\u5f0f\u9519\u8bef");//年月日格式错误
                return;
            }
            calendar.sltDate = yearTxt.value + "-" + monthTxt.value + "-" + dayTxt.value;
            calendar.target.value = calendar.sltDate;
            calendar.hide();
        };
        tbCell.appendChild(yearTxt);
        tbCell.appendChild(monthTxt);
        tbCell.appendChild(dayTxt);
        tbCell.appendChild(submitTxt);
    };
    /************** ?????????????????????????????? *********************/
    this.insertTbCell = function (trRow, cellIndex, TXT, trAlign, tbColSpan) {
        var tbCell = trRow.insertCell(cellIndex);
        if (tbColSpan != undefined) {
            tbCell.colSpan = tbColSpan;
        }
        var btnCell = document.createElement("button");
        tbCell.insertAdjacentElement("beforeEnd", btnCell);
        btnCell.value = TXT;
        btnCell.style.cssText = "width:100%;border:1 outset;background-color:buttonface;";
        btnCell.onmouseover = function () {
            btnCell.style.cssText = "width:100%;border:1 outset;background-color:#F0F0F0;";
        };
        btnCell.onmouseout = function () {
            btnCell.style.cssText = "width:100%;border:1 outset;background-color:buttonface;";
        };
  // btnCell.onmousedown=function(){
  //  btnCell.style.cssText="width:100%;border:1 inset;background-color:#F0F0F0;";
  // }
        btnCell.onmouseup = function () {
            btnCell.style.cssText = "width:100%;border:1 outset;background-color:#F0F0F0;";
        };
        btnCell.onclick = function () {
            btnCell.blur();
        };
        return btnCell;
    };
    this.setDefaultDate = function () {
        var dftDate = new Date();
        calendar.today[0] = dftDate.getYear();
        calendar.today[1] = dftDate.getMonth() + 1;
        calendar.today[2] = dftDate.getDate();
    };
    /****************** Show Calendar *********************/
    this.show = function (targetObject, defaultDate, sourceObject) {
        if (targetObject == undefined) {
            alert("?????????????????????. \n??????: ATCALENDAR.show(obj ????????????,string ????????????,obj ????????????);\n\n????????????:??????????????????????????????.\n????????????:?????????\"yyyy-mm-dd\",?????????????????????.\n????????????:????????????????????????calendar,?????????????????????.\n");
            return false;
        } else {
            calendar.target = targetObject;
        }
        if (sourceObject == undefined) {
            calendar.source = calendar.target;
        } else {
            calendar.source = sourceObject;
        }
        var firstDay;
        var Cells = new Array();
        if (defaultDate == undefined || defaultDate == "") {
            var theDate = new Array();
            calendar.head.innerText = calendar.today[0] + "-" + calendar.today[1] + "-" + calendar.today[2];
            yearTxt.value = calendar.today[0];
            monthTxt.value = calendar.today[1];
            dayTxt.value = calendar.today[2];
            theDate[0] = calendar.today[0];
            theDate[1] = calendar.today[1];
            theDate[2] = calendar.today[2];
        } else {
            var reg = /^\d{4}-\d{1,2}-\d{1,2}$/;
            if (!defaultDate.match(reg)) {
                alert("??????????????????????????????\n\n??????????????????????????????:'yyyy-mm-dd'");
                return;
            }
            var theDate = defaultDate.split("-");
            yearTxt.value = theDate[0];
            monthTxt.value = theDate[1];
            dayTxt.value = theDate[2];
            calendar.head.innerText = defaultDate;
        }
        calendar.currentDate[0] = theDate[0];
        calendar.currentDate[1] = theDate[1];
        calendar.currentDate[2] = theDate[2];
        theFirstDay = calendar.getFirstDay(theDate[0], theDate[1]);
        theMonthLen = theFirstDay + calendar.getMonthLen(theDate[0], theDate[1]);
   //calendar.setEventKey();
        calendar.calendarPad.style.display = "";
        var theRows = Math.ceil((theMonthLen) / 7);
   //??????????????????;
        while (calendar.body.rows.length > 0) {
            calendar.body.deleteRow(0);
        }
   //??????????????????;
        var n = 0;
        day = 0;
        for (i = 0; i < theRows; i++) {
            theRow = calendar.body.insertRow(i);
            for (j = 0; j < 7; j++) {
                n++;
                if (n > theFirstDay && n <= theMonthLen) {
                    day = n - theFirstDay;
                    calendar.insertBodyCell(theRow, j, day);
                } else {
                    var theCell = theRow.insertCell(j);
                    theCell.style.cssText = "background-color:#F0F0F0;cursor:default;";
                }
            }
        }

   //****************??????????????????**************//
        var offsetPos = calendar.getAbsolutePos(calendar.source);//?????????????????????;
        if ((document.body.offsetHeight - (offsetPos.y + calendar.source.offsetHeight - document.body.scrollTop)) < calendar.calendarPad.style.pixelHeight) {
            var calTop = offsetPos.y - calendar.calendarPad.style.pixelHeight;
        } else {
            var calTop = offsetPos.y + calendar.source.offsetHeight;
        }
        if ((document.body.offsetWidth - (offsetPos.x + calendar.source.offsetWidth - document.body.scrollLeft)) > calendar.calendarPad.style.pixelWidth) {
            var calLeft = offsetPos.x;
        } else {
            var calLeft = offsetPos.x;//calendar.source.offsetLeft + calendar.source.offsetWidth;
        }
   //alert(offsetPos.x);
        calendar.calendarPad.style.pixelLeft = calLeft;
        calendar.calendarPad.style.pixelTop = calTop;
    };
    /****************** ????????????????????? *************************/
    this.getAbsolutePos = function (el) {
        var r = {x:el.offsetLeft, y:el.offsetTop};
        if (el.offsetParent) {
            var tmp = calendar.getAbsolutePos(el.offsetParent);
            r.x += tmp.x;
            r.y += tmp.y;
        }
        return r;
    };

  //************* ????????????????????? **************/
    this.insertBodyCell = function (theRow, j, day, targetObject) {
        var theCell = theRow.insertCell(j);
        if (j == 0) {
            var theBgColor = "#FF9999";
        } else {
            var theBgColor = "#FFFFFF";
        }
        if (day == calendar.currentDate[2]) {
            var theBgColor = "#CCCCCC";
        }
        if (day == calendar.today[2]) {
            var theBgColor = "#99FFCC";
        }
        theCell.bgColor = theBgColor;
        theCell.innerText = day;
        theCell.align = "center";
        theCell.width = 35;
        theCell.style.cssText = "border:1 solid #CCCCCC;cursor:hand;";
        theCell.onmouseover = function () {
            theCell.bgColor = "#FFFFCC";
            theCell.style.cssText = "border:1 outset;cursor:hand;";
        };
        theCell.onmouseout = function () {
            theCell.bgColor = theBgColor;
            theCell.style.cssText = "border:1 solid #CCCCCC;cursor:hand;";
        };
        theCell.onmousedown = function () {
            theCell.bgColor = "#FFFFCC";
            theCell.style.cssText = "border:1 inset;cursor:hand;";
        };
        theCell.onclick = function () {
            if (calendar.currentDate[1].length < 2) {
                calendar.currentDate[1] = "0" + calendar.currentDate[1];
            }
            if (day.toString().length < 2) {
                day = "0" + day;
            }
            calendar.sltDate = calendar.currentDate[0] + "-" + calendar.currentDate[1] + "-" + day;
            calendar.target.value = calendar.sltDate;
            calendar.hide();
        };
    };
    /************** ???????????????????????????????????? *********************/
    this.getFirstDay = function (theYear, theMonth) {
        var firstDate = new Date(theYear, theMonth - 1, 1);
        return firstDate.getDay();
    };
    /************** ???????????????????????? *********************/
    this.getMonthLen = function (theYear, theMonth) {
        theMonth--;
        var oneDay = 1000 * 60 * 60 * 24;
        var thisMonth = new Date(theYear, theMonth, 1);
        var nextMonth = new Date(theYear, theMonth + 1, 1);
        var len = Math.ceil((nextMonth.getTime() - thisMonth.getTime()) / oneDay);
        return len;
    };
    /************** ???????????? *********************/
    this.hide = function () {
   //calendar.clearEventKey();
        calendar.calendarPad.style.display = "none";
    };
    /************** ??????????????? *********************/
    this.setup = function (defaultDate) {
        calendar.addCalendarPad();
        calendar.addCalendarBoard();
        calendar.setDefaultDate();
    };
    /************** ??????AgetimeCalendar *********************/
    this.about = function () {
        /*//alert("Agetime Calendar V1.0nnwww.agetime.comn");
   popLeft = calendar.calendarPad.style.pixelLeft+4;
   popTop = calendar.calendarPad.style.pixelTop+25;
   var popup = window.createPopup();
   var popupBody = popup.document.body;
   popupBody.style.cssText="border:solid 2 outset;font-size:9pt;background-color:#F0F0F0;";
   var popHtml = "<span style='color:#336699;font-size:12pt;'><U>?????? AgetimeCalendar</U></span><BR><BR>";
   popHtml+="??????: v1.0<BR>??????: 2004-03-13";
   popupBody.innerHTML=popHtml;
   popup.show(popLeft,popTop,240,136,document.body); */
        var strAbout = "About AgetimeCalendar\n\n";
        strAbout += "-\t: \u5e2e\u52a9\n";
        strAbout += "x\t: \u5173\u95ed\n";
        strAbout += "<<\t: \u524d\u4e00\u5e74\n";
        strAbout += "<\t: \u524d\u4e00\u6708\n";
        strAbout += "\u4eca\u5929\t: \u9009\u4e2d\u663e\u793a\u7684\u65e5\u671f\n";
        strAbout += ">\t: \u540e\u4e00\u6708\n";
        strAbout += "<<\t: \u540e\u4e00\u5e74\n";
        strAbout += "\nAgetimeCalendar\nVersion:v1.0\nDesigned By:\u4f5a\u540d 2008-03-16  afos_koo@hotmail.com \n";
        alert(strAbout);
    };
    calendar.setup();
}
function isInt(n) {
    return /^\d{1,}$/.test(n);
}

