
<%@ page language="java" pageEncoding="GB2312"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tlds/PageFormat.tld" prefix="pageFmt"%>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested"%>
<div class="com_qtrmoon_RemindStyle" style="text-align:${align};width:100%;background:url(/${projectName}/remind/imgs/bk.gif) top right no-repeat;">
<logic:iterate id="row" name="list">
<img src="/${projectName}/${row.ico}" src2="/${projectName}/${row.ico}" border="0" title="${row.text}" id="${row.id}" ${width} ${height} link="/${projectName}/${row.link}" link2="/${projectName}/${row.link}"/>
</logic:iterate>
</div>
<bgsound src="" loop="5" id="com_qtrmoon_Remindsound">
<script src='/${projectName}/remind/remind.js'></script>
<script>
	//µ˜”√Ã·–—
	var com_qtrmoon_remind=new com_qtrmoon_Remind();
	com_qtrmoon_remind.sound="/${projectName}/remind/remSound/s2.wav"
	<logic:iterate id="row" name="list">
	com_qtrmoon_remind.remindPath[com_qtrmoon_remind.remindPath.length]="/${projectName}/${row.ajaxlink}";
	</logic:iterate>
	if('${pageFmt:fn_getConstant("debug")}'=="false"){
		com_qtrmoon_remind.remind();
	}
</script>