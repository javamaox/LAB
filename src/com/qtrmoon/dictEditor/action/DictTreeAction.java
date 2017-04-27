package com.qtrmoon.dictEditor.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.DictCfg;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.toolkit.CodeConvert;
import com.qtrmoon.toolkit.tree.TreeNode;

public class DictTreeAction extends DispatchAction { 

	private static final String FORWARD_treeTag_pop = "treeTag_pop";
	private static final String FORWARD_treeTag_include = "treeTag_include";
	private static final String FORWARD_treeTag_ai = "treeTag_ai";
	private static final String FORWARD_treeTag_checkboxInList = "checkboxInList";

	/** 查询字典表目录列表 */
	public ActionForward showDictTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String dictId = request.getParameter("dictId");
		String ajax = request.getParameter("ajax");
		DictCatalog ca = DictBuffer.findDictCatalogById(dictId);
		String simpleNodes="";
		if(ca.isListType()){//智能列表使用了该方法
			List<DictionaryForm> dictList=DictBuffer.getDict(dictId);
			for(DictionaryForm dict:dictList){
				simpleNodes+="{ id:'"+dict.getId()+"', pid:'"+DictBuffer.dummyRootId+"', label:'"+dict.getLabel()+"'},";
			}
			simpleNodes=simpleNodes.substring(0,simpleNodes.length()-1);
			simpleNodes="["+simpleNodes+"]";
		}else{
			String muti=request.getParameter("muti");
			String checked="";
			if(!"true".equals(muti)){
				checked="nocheck:true";
			}else{
				checked="nocheck:false";
			}
			String roots = request.getParameter("dynroot");//优先动态根
			if(roots==null||roots.equals("")){
				roots=ca.getRoot();//配置根
			}
			if(!"true".equals(ajax)){//非Ajax树
				DictionaryForm rootDict=null;
				if(roots!=null&&!roots.equals("")){
					rootDict=DictBuffer.findDictById(dictId, roots);
				}else{
					rootDict=DictBuffer.findDictById(dictId, DictBuffer.dummyRootId);
				}
				simpleNodes+="{id:'"+rootDict.getId()+"',pid:'"+rootDict.getPid()+"',label:'"+rootDict.getLabel()+"',isParent:true,"+checked+"},";
				simpleNodes+=getJasonData(rootDict,dictId);
				simpleNodes=simpleNodes.substring(0,simpleNodes.length()-1);
				simpleNodes="["+simpleNodes+"]";
			}else{
				if(roots!=null&&!roots.equals("")){
					String[] rootArr=roots.split(",");
					if(rootArr.length==1){
						DictionaryForm rootDict=DictBuffer.findDictById(dictId, rootArr[0]);
						simpleNodes+="[{id:'"+rootDict.getId()+"',pid:'"+rootDict.getPid()+"',label:'"+rootDict.getLabel()+"',isParent:true,"+checked+"}]";
					}else{
						for(String root:rootArr){
							DictionaryForm rootDict=DictBuffer.findDictById(dictId, root);
							simpleNodes+="{id:'"+rootDict.getId()+"',pid:'"+DictBuffer.dummyRootId+"',label:'"+rootDict.getLabel()+"',isParent:true,"+checked+"},";
						}
						simpleNodes=simpleNodes.substring(0,simpleNodes.length()-1);
						simpleNodes="[{id:'"+DictBuffer.dummyRootId+"', pid:'', label:'"+ca.getTabledesc()+"',isParent:true,"+checked+"},"+"["+simpleNodes+"]]";
					}
				}else{
					simpleNodes="[]";
				}
				
			}
		}
		request.setAttribute("simpleNodes", simpleNodes);
		request.setAttribute("catalog", ca);
		request.setAttribute("dummyRootId", DictBuffer.dummyRootId);
		if("true".equals(request.getParameter("include"))){
			return mapping.findForward(FORWARD_treeTag_include);
		}else if("ai".equals(request.getParameter("include"))){
			return mapping.findForward(FORWARD_treeTag_ai);
		}else if("checkboxInList".equals(request.getParameter("include"))){
			return mapping.findForward(FORWARD_treeTag_checkboxInList);
		}else{
			return mapping.findForward(FORWARD_treeTag_pop);
		}
	}
	/**
	 * 逐层递归，以保证排序
	 * @param root
	 * @return
	 */
	private String getJasonData(DictionaryForm root,String dictId){
		String res="";
		String parent;
		for(TreeNode node:root.cnodeList){
			if(node.cnodeList.size()>0){
				parent=",isParent:true";
			}else{
				parent="";
			}
			DictionaryForm dict=(DictionaryForm)node;
			res+="{id:'"+dict.getId()+"',pid:'"+dict.getPid()+"',label:'"+dict.getLabel()+"'"+parent+"},";
		}
		for(DictionaryForm node:root.getChildren(dictId)){
			res+=getJasonData(node,dictId);
		}
		return res;
	}
	
	public ActionForward schTreeSub(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId = request.getParameter("dictId");
		String id = request.getParameter("id");
		String res="",root="";
		String parent;
		if(id==null){//获取根节点
			String dynroot=request.getParameter("dynroot");
			if(dynroot==null||dynroot.equals("")){
				root=DictBuffer.dummyRootId;
				//如果有一个实际数据根，则过滤掉虚节点
				DictionaryForm rdummy=DictBuffer.findDictById(dictId, DictBuffer.dummyRootId);
				if(rdummy!=null){
					List<DictionaryForm> l=rdummy.getChildren(dictId);
					if(l.size()==1){
						root=l.get(0).getId();
					}
				}
			}else{
				root=dynroot;
			}
			DictionaryForm rootObj=DictBuffer.findDictById(dictId, root);
			if(rootObj==null){//字典无数据
				return null;
			}else{
				id=rootObj.getId();
				root="{ id:'"+rootObj.getId()+"', pid:'"+rootObj.getPid()+"', label:'"+rootObj.getLabel()+"',isParent:true,sort:'1'},";
			}
		}
		List<DictionaryForm> dictList = DictBuffer.findDictById(dictId, id).getChildren(dictId);
		for(DictionaryForm dict:dictList){
			if(dict.getChildren(dictId).size()>0){
				parent=",isParent:true";
			}else{
				parent="";
			}
			res+="{ id:'"+dict.getId()+"', pid:'"+id+"', label:'"+dict.getLabel()+"'"+parent+",sort:'"+dict.getSort()+"'},";
		}
		res=root+res;
		if(!res.equals("")){
			res=res.substring(0,res.length()-1);
		}
		res="["+res+"]";
		try {
			response.setCharacterEncoding("GB2312");
			response.getWriter().print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionForward getChild(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId=request.getParameter("dictId"),
			   nodeid=request.getParameter("nodeid");
		String res="";
		
		List<DictionaryForm> dictList = DictBuffer.getDict(dictId);
		String className="";
		List<DictionaryForm> sibList=new ArrayList<DictionaryForm>();
		//找子
		for(DictionaryForm dict:dictList){
			if(dict.getTreePId().equals(nodeid)){
				sibList.add(dict);
			}
		}
		int i=1,size=sibList.size();
		DictBuffer.orderDict(sibList);
		for(DictionaryForm dict:sibList){
			if(dict.getChildren(dictId).size()>0){
				className="plus";
			}else{
				className="square";
			}
			if(i==size){
				className+="L";
			}else{
				className+="T";
			}
			//{id:"",info:"",className:""}
			res+="{id:'"+dict.getId()+"',info:'"+CodeConvert.covertCode(dict.getLabel())+"',className:'"+className+"'}@@@";
			i++;
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//大数据量对关键词进行like检索的方法
	public ActionForward schLikeLabel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId=request.getParameter("dictId");
		DictCatalog catalog=DictBuffer.getDictCatalogById(dictId);
		String label=request.getParameter("label");
		if(label==null||label.equals("")){
			return null;
		}
		try {
			label=new String(label.getBytes("ISO8859-1"),"GB2312").trim();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String res="";
		String condition="";
		if(label.indexOf(" ")>0){
			String[] arr=label.split(" ");
			for(String a:arr){
				if(a.startsWith("!")){
					a=a.replaceAll("!", "");
					condition+=" and t."+catalog.getMapping().label+" not like '%"+a+"%'";
				}else{
					condition+=" and t."+catalog.getMapping().label+" like '%"+a+"%'";
				}
			}
		}else{
			if(label.startsWith("!")){
				label=label.replaceAll("!", "");
				condition+=" and t."+catalog.getMapping().label+" not like '%"+label+"%'";
			}else{
				condition+=" and t."+catalog.getMapping().label+" like '%"+label+"%'";
			}
		}
		catalog.setSql(condition);
		List<DictionaryForm> dictList=DictBuffer.getService().findTopDict(catalog,DictCfg.LARGE_AI_PAGESIZE);
		for(DictionaryForm dict:dictList){
			res+="{id:'"+dict.getId()+"',label:'"+dict.getLabel()+"'},";
		}
		if(res.length()>0){
			res="["+res.substring(0,res.length()-1)+"]";
		}
		try {
			response.setCharacterEncoding("GB2312");
			response.getWriter().print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//Js版数据导入工具使用的接口
	public ActionForward getDict(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId=request.getParameter("dictId");
		List<DictionaryForm> l = DictBuffer.getDict(dictId);
		String res="";
		for(DictionaryForm dict:l){
			res+="{id:'"+dict.getId()+"',label:'"+CodeConvert.covertCode(dict.getLabel())+"'}@@@";
		}
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//在iframe里的checkbox列表
	public ActionForward checkboxInList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String dictid = request.getParameter("dictid");
		PrintWriter out;
		try {
			out = response.getWriter();
			if (dictid == null || dictid.equals("")) {
				out.print(CodeConvert.covertCode("请填写字典标识！"));
			}
			List<DictionaryForm> dlist=DictBuffer.getDict(dictid);
			String res="<body onclick='parent.dictModule_iframeDictObj.mousedown()' onblur='parent.dictModule_iframeDictObj.blur()' style='background:white;border:black 1px solid;margin:5px;padding:0px;font-size:12px;'>";
			for(DictionaryForm dict:dlist){
				res+="<div><input type='checkbox' name='' value='"+dict.getId()+"' onclick='parent.dictModule_iframeDictObj.check(this)'/>"+CodeConvert.covertCode(dict.getLabel())+"</div>";
			}
			res+="</body>";
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//查询并展开节点的功能，使用的查询待展开Id串的方法，先根。
	public ActionForward schNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictid = request.getParameter("dictId");
		String label = request.getParameter("label");
		try {
			label=new String(label.getBytes("ISO8859-1"),"GB2312");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String res="";
		
		List<DictionaryForm> list=DictBuffer.likeLabel(dictid,label);
		if(list!=null&&list.size()>0){
			for(DictionaryForm dict:list){
				String one=dict.getId();
				while((dict=DictBuffer.getDictById(dictid, dict.getPid()))!=null){
					one=dict.getId()+","+one;
				}
				res+=one+"!";
			}
			res=res.substring(0,res.length()-1);
		}
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/******************************************************************/
	
}
