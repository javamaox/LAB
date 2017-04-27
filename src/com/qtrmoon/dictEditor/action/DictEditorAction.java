package com.qtrmoon.dictEditor.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jdom.Element;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;
import com.qtrmoon.dictEditor.beanSerDao.DictService;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.toolkit.XmlUtil;
import com.qtrmoon.toolkit.tree.TreeNode;

public class DictEditorAction extends DispatchAction {

	private DictService dictService;

	private static final String FORWARD_schCatalog = "schCatalog";
	private static final String FORWARD_schCatalogGroup = "schCatalogGroup";
	private static final String FORWARD_addCatalog = "addCatalog";

	private static final String FORWARD_schDictTree = "schDictTree";
	private static List<DictionaryForm> caList=null;
	
	/** ��ѯ�ֵ��Ŀ¼����б� */
	public ActionForward schCatalogGroup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if(caList==null){
			caList=new ArrayList<DictionaryForm>();
			String dictPath=Constant.getConstant("dictCfg");
			File[] files=(new File(dictPath)).listFiles();
			XmlUtil xmlUtil=new XmlUtil();
			for(File file:files){
				if(file.getName().startsWith("_catalog")){
					Element root=xmlUtil.loadXml(file.getAbsolutePath());
					String description=root.getChildTextTrim("description");
					boolean edit=false;
					List<Element> tables=root.getChildren("table");
					for(Element table:tables){
						if("true".equals(table.getChildText("edit"))){
							edit=true;
							break;
						}
					}
					if(edit){
						caList.add(new DictionaryForm(file.getName(),description,""));
					}
				}
			}
		}
		request.setAttribute("list", caList);
		return mapping.findForward(FORWARD_schCatalogGroup);
	}
	
	/** ��ѯ�ֵ��Ŀ¼�б� */
	public ActionForward schCatalog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String group=request.getParameter("group");
		List<DictCatalog> list;
		list = DictBuffer.schUserDictCatalog();
		List<DictCatalog> resList=new ArrayList<DictCatalog>();
		for(DictCatalog ca:list){
			if(ca.getGroup().equals(group)){
				resList.add(ca);
			}
		}
		request.setAttribute("list", resList);
		return mapping.findForward(FORWARD_schCatalog);
	}

	/** Ԥ�洢�ֵ��Ŀ¼�б� */
	public ActionForward addCatalog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward(FORWARD_addCatalog);
	}

	
	/** -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- */
	/** ���ֵ��༭�����棬�������б�ı༭��ͳһ��Ϊ��Ajax���ı༭�� */
	public ActionForward schTreeDict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String dictId = request.getParameter("dictId");
		DictCatalog ca = DictBuffer.findDictCatalogById(dictId);
		
		request.setAttribute("isList", ca.isListType());
		request.setAttribute("catalog", ca);
		request.setAttribute("dummyRootId", DictBuffer.dummyRootId);
		return mapping.findForward(FORWARD_schDictTree);
	}
	public ActionForward schTreeSub(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId = request.getParameter("dictId");
		DictCatalog ca = DictBuffer.findDictCatalogById(dictId);
		String id = request.getParameter("id");
		String res="";
		String parent;
		if(ca.isListType()){
			if(id==null){//������ڵ�
				res="{ id:'"+DictBuffer.dummyRootId+"', pid:'', label:'"+ca.getTabledesc()+"',isParent:true,sort:'1'},";
			}else{
				List<DictionaryForm> dictList=DictBuffer.getDict(dictId);
				for(DictionaryForm dict:dictList){
					parent="";
					res+="{ id:'"+dict.getId()+"', pid:'"+DictBuffer.dummyRootId+"', label:'"+dict.getLabel()+"'"+parent+",sort:'"+dict.getSort()+"'},";
				}
			}
		}else{
			if(id==null){//��ȡ���ڵ�
				DictionaryForm rootObj=DictBuffer.findDictById(dictId, DictBuffer.dummyRootId);
				id=rootObj.getId();
				res="{ id:'"+rootObj.getId()+"', pid:'"+rootObj.getPid()+"', label:'"+rootObj.getLabel()+"',isParent:true,sort:'1'},";
			}else{
				List<TreeNode> dictList = DictBuffer.findDictById(dictId, id).getCnodeList();
				for(TreeNode node:dictList){
					DictionaryForm dict=(DictionaryForm)node;
					if(dict.getChildren(dictId).size()>0){
						parent=",isParent:true";
					}else{
						parent="";
					}
					res+="{ id:'"+dict.getId()+"', pid:'"+dict.getPid()+"', label:'"+dict.getLabel()+"'"+parent+",sort:'"+dict.getSort()+"'},";
				}
			}
		}
		if(!res.equals("")){
			res=res.substring(0,res.length()-1);
			res="["+res+"]";
		}
		try {
			response.setCharacterEncoding("GB2312");
			response.getWriter().print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** ����ֵ���¼ */
	// �����ʽΪ"2,3,3"��Ϊ"01"��ʼ
	// 01
	// --01 001
	// -----01 001 001
	public ActionForward addDictTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId = request.getParameter("dictId");
		DictCatalog ca = DictBuffer.findDictCatalogById(dictId);
		String from = request.getParameter("from");
		if ("submit".equals(from)) {
			String data=request.getParameter("data");
			try {
				data=new String(data.getBytes("ISO8859-1"),"GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String[] dictArr=data.split("@@@");
			List<DictionaryForm> dictList=new ArrayList<DictionaryForm>();
			for(String dictStr:dictArr){
				String[] dict=dictStr.split("@");
				String sort=dict[0];
				if(sort.length()>3){
					sort=sort.substring(sort.length()-3);
				}
				DictionaryForm d = new DictionaryForm(dict[0],dict[2],dict[1],Integer.parseInt(sort));
				if(d.getPid().equals(DictBuffer.dummyRootId)){
					d.setPid("");
				}
				dictList.add(d);
			}
			getDictService().saveDict(ca, dictList);
			return null;
		} else {
			try {
				String pid = request.getParameter("id");
				response.getWriter().print(DictBuffer.buildId(ca,pid));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/** �޸��ֵ���¼ */
	public ActionForward updDictTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId = request.getParameter("dictId");
		DictCatalog catalog=DictBuffer.findDictCatalogById(dictId);
		DictionaryForm df = getDictService().findDictById(catalog, request.getParameter("id"));//�����formû�ж����������Ϣ�����Ȳ顣
		String pid=request.getParameter("pid");
		
		if(pid!=null){
			df.setPid(pid);
		}else{
			df.setPid("");
		}
		
		String label=request.getParameter("label");
		if(label!=null){
			try {
				label=new String(label.getBytes("ISO8859-1"),"GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			df.setLabel(label);
		}
		getDictService().updateDict(catalog, df);
		return null;
	}

	/** ɾ�������ֵ���¼ */
	public ActionForward delDictTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		String dictId = request.getParameter("dictId");
		DictCatalog catalog=DictBuffer.findDictCatalogById(dictId);
		String id = request.getParameter("id");
		if(catalog.isListType()){
			getDictService().removeDict(catalog, id);
		}else{
			List<DictionaryForm> allChildren = DictBuffer.getChildDict(dictId, id);
			String[] ids=new String[allChildren.size()+1];
			for(int i=0;i<allChildren.size();i++){
				ids[i]=allChildren.get(i).getId();
			}
			ids[allChildren.size()]=id;
			getDictService().removeDict(catalog, ids);
		}
		return null;
	}
	
	/** �ֵ����� */
	public ActionForward updSort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		HttpSession session=request.getSession();
		String dictId = request.getParameter("dictId");
		String hasSort = request.getParameter("hasSort");
		DictBuffer.updCache(dictId);
		if("true".equals(hasSort)){
			String ids=request.getParameter("ids");
			DictCatalog catalog=DictBuffer.findDictCatalogById(dictId);
			if(catalog.getOrderBy()!=null&&!catalog.getOrderBy().equals("")){
				if(ids!=null&&!ids.equals("")){
					getDictService().updateSort(catalog, ids.split(","));
				}
			}
		}
		lock(dictId,session.getId(),false);
		return null;
	}
	public ActionForward lock(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,must-revalidate");
		HttpSession session=request.getSession();
		String dictId = request.getParameter("dictId");
		try {
			boolean canEdit=lock(dictId,session.getId(),true);
			response.getWriter().print(canEdit);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static Map<String,String> locks=new HashMap<String,String>();
	/**
	 * @param dictid �ֵ��ʶ
	 * @param sessionid session��ʶ
	 * @param lock ������true������false������
	 * @return lock==trueʱ������true��ʾ���Ա༭��
	 */
	private static synchronized boolean lock(String dictid,String sessionid,boolean lock){
		if(lock){//����ĳ�ֵ�ı༭��
			if(locks.get(dictid)!=null){
				return false;//�ֵ��ѱ���������
			}else{
				locks.put(dictid, sessionid);//�����ֵ�
				return true;
			}
		}else{//����ĳ�ֵ�ı༭
			if(locks.get(dictid)!=null&&locks.get(dictid).equals(sessionid)){
				locks.remove(dictid);
			}
			return true;
		}
	}
	public DictService getDictService() {
		if(dictService==null){
			dictService=new DictService();
			System.out.println("DictEditorAction.dictService is null!");
		}
		return dictService;
	}

	public void setDictService(DictService dictService) {
		this.dictService = dictService;
	}
}