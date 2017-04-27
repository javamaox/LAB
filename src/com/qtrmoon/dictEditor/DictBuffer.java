package com.qtrmoon.dictEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;
import com.qtrmoon.dictEditor.beanSerDao.DictMapping;
import com.qtrmoon.dictEditor.beanSerDao.DictService;
import com.qtrmoon.dictEditor.beanSerDao.DictTreeUtil;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.toolkit.SortUtil;
import com.qtrmoon.toolkit.XmlUtil;
import com.qtrmoon.toolkit.tree.TreeNode;

public class DictBuffer {

	private static final Map<String, DictCatalog> dictCatalogMap = new HashMap<String, DictCatalog>();

	private static final Map<String, List<DictionaryForm>> dictMap = new HashMap<String, List<DictionaryForm>>();// �����ѯ�����ֵ�����
	
	private static final List<DictCatalog.Listener> dictListeners=new ArrayList<DictCatalog.Listener>();
	
	private static DictService dictService = null;
	
	private static String cfgPath=null;
	public static final String dummyRootId="_JAVAMAO";//�����id��
	public static final String EXP_SEP = "@@@";//��չ�ֶθ�ֵ�ķָ��
	/**
	 * ��������pid=null��pid="0"��ʶ��������ȡ���ݺ�������ֵ����ݹ淶��������pid�淶Ϊ""������������{id:dummyRootId,pid:""}��
	 * ���ݽṹ	#�洢����	#������ʽ
	 * T����		#D����	#A��AI	(С������ʹ���нṹ��ѯ��|����������ʹ���޽ṹ��ѯ��)
	 * L��List	#X��xml	#P��pop
	 * T����		#D����	#S��Seprate
	 * T����		#D����	#X��Ajax
	 * T����		#D����	#N��Normal
	 * ����һ�����ݿ�洢�Ŀ�ʹ�õ���ѡ��������Ϊ��T#D#P
	 */
	public static List<DictionaryForm> getDict(String dictId) {
		List<DictionaryForm> dictList = null;
		if (dictId != null && !dictId.equals("")) {
			dictId=dictId.toUpperCase();
			dictList = dictMap.get(dictId);
			if (dictList == null||dictList.size()==0) {
				DictCatalog catalog = findDictCatalogById(dictId);
				if (catalog == null) {
					System.err.println("�����ֵ��Id���Ӱ���Ƿ���ȷ"+dictId);
					return null;
				}
				if (catalog.isXmlStore()) {// xml�����ֵ��
					dictList=getService().findDictInXml(catalog);
					if(dictList.size()>0&&!catalog.isListType()){
						formatTreeLayer(dictList,catalog);//�������㼶�ṹ
					}
					dictMap.put(dictId, dictList);
				} else {
					// ���ݿ��ֵ��
					// �����ֵ��Ŀ¼����û���뵱ǰ���õ��ֵ�����һ�µ��ֵ䡣�ֵ�Ķ�Ӧ������root��style������ͬ��������ͬ��
					String root=catalog.getRoot();
					if(root==null){root="";}
					//û��������ͬ���ֵ����á�
					if(catalog.isBuffer()){
						dictList = getService().findDict(catalog);//��û������root�������²�һ�飬����ʹ����ǰ��Ľ������Ϊ���������Ĳ㼶��ϵ��
						if(dictList==null){
							System.out.println("service.findDict() is null["+catalog.getTablename()+"]��");
							return null;
						}
						if(dictList.size()==0){
							System.out.println("�����ͼ["+catalog.getTablename()+"]û�����ݣ�");
							return null;
						}
						if(dictList.size()>0&&!catalog.isListType()){
							formatTreeLayer(dictList,catalog);//�������㼶�ṹ
						}
						
						dictMap.put(dictId, dictList);
					}
				}
			}
		}
		return dictList;
	}

	/**
	 * ǿ�Ʋ��������ݣ������������Ƿ���󡣲�Ӱ������������ʽ���ֵ���á�
	 * @param dictId
	 * @return
	 */
	public static List<DictionaryForm> getDictForce(String dictId) {
		DictCatalog catalog = findDictCatalogById(dictId);
		List<DictionaryForm> dictList;
		dictList = dictMap.get(dictId+"_FORCE");
		if (dictList == null||dictList.size()==0) {
			dictList = getService().findDict(catalog);
			if(dictList==null||dictList.size()==0){
				System.out.println("�����ͼ["+catalog.getTablename()+"]û�����ݣ�");
				return null;
			}
			if(dictList.size()>0&&!catalog.isListType()){
				formatTreeLayer(dictList,catalog);//�������㼶�ṹ
			}
			dictMap.put(dictId+"_FORCE", dictList);
		}
		return dictList;
	}
	public static DictionaryForm getTreeRoot(String dictId){
		return findDictById(dictId, dummyRootId);
	}
	
	
	/** ��ȡpid�µ�һ���ֵ����� */
	public static List<DictionaryForm> getDictOneLayer(String dictid, String pid) {
		List<DictionaryForm> list = getDict(dictid);
		List<DictionaryForm> res = new ArrayList<DictionaryForm>();
		for(DictionaryForm dict:list){
			if(pid.equals(dict.getPid())){
				res.add(dict);
			}
		}
		return res;
	}

	/**
	 * �������ֵ��ʽ��Ϊ��ṹ��
	 * @param dictList
	 */
	private static void formatTreeLayer(List<DictionaryForm> dictList,DictCatalog catalog) {
		//�ҵ������ĸ�����������pid=null��pid="0"��ʶ�����
		boolean isRoot=true;
		List<DictionaryForm> rootList=new ArrayList<DictionaryForm>();
		for (DictionaryForm dict:dictList) {
			isRoot=true;
			for (DictionaryForm dictEver:dictList) {
				if(dict.getPid()==null){
					break;
				}else if(dict.getPid().equals(dictEver.getId())){
					isRoot=false;
					break;
				}
			}
			if(isRoot){
				rootList.add(dict);
			}
		}
		dictList.add(createRoot(catalog));//�������
		for (DictionaryForm dict:rootList) {
			dict.setPid(DictBuffer.dummyRootId);
		}
		if(catalog.getOrderBy()!=null&&!catalog.getOrderBy().equals("")){
			(new DictTreeUtil()).getOrderTree(dictList);
		}else{
			(new DictTreeUtil()).getTree(dictList);
		}
	}

	/**
	 * �������
	 * @param catalog
	 * @return
	 */
	private static DictionaryForm createRoot(DictCatalog catalog) {
		DictionaryForm root = new DictionaryForm();
		root.setId(DictBuffer.dummyRootId);
		root.setLabel(catalog.getTabledesc());
		root.setPid("");
		return root;
	}

	public static DictionaryForm getDictById(String dictId, String value) {
		return findDictById(dictId, value);
	}
	public static DictionaryForm findDictById(String dictId, String value) {
		if(dictId==null||dictId.equals("")){
			System.out.println("dictIdΪ�գ����鴫��Ĳ�����");
			return null;
		}
		DictCatalog ca=getDictCatalogById(dictId);
		if(ca==null){
			System.out.println("�ֵ䣺"+dictId+"�����ڡ�");
			return null;
		}
		if(!ca.isBuffer()){
			if(dummyRootId.equals(value)){
				return createRoot(ca);
			}
			return getService().findDictById(ca, value);
		}else{
			List<DictionaryForm> dictList = getDict(dictId);
			if(dictList==null){
				System.out.println("�ֵ䣺"+dictId+"�����ڡ�");
				return null;
			}
			for (int i = 0; i < dictList.size(); i++) {
				if (dictList.get(i).getId().equals(value)) {
					return dictList.get(i);
				}
			}
			if(ca.isListType()&&dummyRootId.equals(value)){//���������û�У��������б��͵��ֵ䡣
				return createRoot(ca);
			}
		}
		System.out.println("�ֵ䣺"+dictId+"�в�����ֵΪ"+value+"�ļ�¼��");
		return null;
	}
	
	public static DictionaryForm getDictByLabel(String dictId, String label) {
		return findDictByLabel(dictId, label);
	}
	public static DictionaryForm findDictByLabel(String dictId, String label) {
		if(label==null){
			System.out.println("*****�鲻��"+dictId+"�ֵ���LabelΪ"+label+"��ֵ!*****");
			return null;
		}
		DictCatalog catalog=getDictCatalogById(dictId);
		if(catalog.isBuffer()){
			List<DictionaryForm> dictList = getDict(dictId);
			label=label.trim();
			for (int i = 0; i < dictList.size(); i++) {
				if (dictList.get(i).getLabel()!=null&&dictList.get(i).getLabel().equalsIgnoreCase(label)) {
					return dictList.get(i);
				}
			}
		}else{
			return getService().findDictByLabel(catalog, label);
		}
		return null;
	}
	
	public static String getLabel(String dictId, String value) {
		if(value==null||value.equals("")){
			return "";
		}
		String label ="";
		if(value.indexOf(",")>0){
			String[] values = value.split(",");
			for(int j= 0 ;j<values.length;j++){
				label += getDictById(dictId, values[j]).getLabel()+",";
			}
			if(!label.equals("")){
				label = label.substring(0,label.length()-1);
			}
		}else{
			DictionaryForm dict=getDictById(dictId, value);
			if(dict!=null){
				label=dict.getLabel();
			}
		}
		return label;
	}

	public static List<DictionaryForm> getChildDict(String dictId, String id) {
		DictCatalog ca=DictBuffer.getDictCatalogById(dictId);
		if(ca.isListType()){//���б��ȡ�ӣ��򷵻�ȫ����
			if(dummyRootId.equals(id)){
				return getDict(dictId);
			}
		}
		if(ca.isBuffer()){
			List<DictionaryForm> children=new ArrayList<DictionaryForm>();
			DictionaryForm dict=findDictById(dictId, id);
			for(TreeNode node:dict.getCnodeList()){
				children.add((DictionaryForm)node);
			}
			return children;
		}else{//�޻��������Ҫÿ�ι�������
			List<DictionaryForm> children=getService().findDictByPid(ca, id);
			if(ca.getOrderBy()!=null&&!ca.getOrderBy().equals("")){
				SortUtil.sortList(children, "sort", Integer.class);
			}
			return children;
		}
	}
	public static DictionaryForm getParentDict(String dictId,String id) {
		DictionaryForm dict=getDictById(dictId, id);
		return getDictById(dictId, dict.getPid());
	}
	
	/** ��ȡ�ֵ�Ŀ¼���� */
	public static DictCatalog getDictCatalogById(String dictId) {
		return findDictCatalogById(dictId);
	}
	public static DictCatalog findDictCatalogById(String dictId) {
		dictId=dictId.toUpperCase();
		DictCatalog ca=getDictCatalogMap().get(dictId);
		if(ca==null){
			String path = getCfgPath();
			File pf=new File(path);
			if(pf.isDirectory()){
				String[] fns=pf.list();
				String fpath="";
				XmlUtil xmlUtil=new XmlUtil();
				for(String fn:fns){
					if(fn.indexOf("_catalog")==0&&fn.endsWith(".xml")){
						fpath=path+"/"+fn;
						Element groupERoot;
						groupERoot = xmlUtil.loadXml(fpath);
						List<Element> tableList = groupERoot.getChildren("table");
						Element elem;
						for (int i = 0; i < tableList.size(); i++) {
							elem = tableList.get(i);
							if(elem.getChildText("id").equalsIgnoreCase(dictId)){
								ca=createCatalog(elem,fn);
								dictCatalogMap.put(dictId, ca);
								return ca;
							}
						}
					}
				}
			}
		}
		return ca;
	}

	/**
	 * �����ֵ���£���������û��ķ��ʵ�ַ��
	 * @param url;//�û��ķ��ʵ�ַ
	 */
	public static void listenDictUpd(String url){
		if(dictListeners.size()>0){
			while(url.indexOf("//")>=0){
				url=url.replaceAll("//", "/");
			}
			for(DictCatalog.Listener l:dictListeners){
				if(l.getUrl().indexOf(url)>=0){
					updCache(l.getDictId());
					break;
				}
			}
		}
	}

	private static Map<String, DictCatalog> getDictCatalogMap() {
		if (dictCatalogMap.size() == 0) {// ���û�г�ʼ�����ʼ��
			XmlUtil xmlUtil=new XmlUtil();
			String path = getCfgPath();
			File pf=new File(path);
			if(pf.isDirectory()){
				String[] fns=pf.list();
				String fpath="";
				for(String fn:fns){
					if(fn.indexOf("_catalog")==0&&fn.endsWith(".xml")){
						fpath=path+"/"+fn;
						Element groupERoot = xmlUtil.loadXml(fpath);
						List<Element> tableList = groupERoot.getChildren("table");
						DictCatalog catalog;
						Element elem;
						String dictId;
						for (int i = 0; i < tableList.size(); i++) {
							elem = tableList.get(i);
							dictId = elem.getChildTextTrim("id").toUpperCase();
							catalog=createCatalog(elem,fn);
							dictCatalogMap.put(dictId, catalog);
						}
					}
				}
			}else{
				System.out.println("�����ֵ�ģ���dictCfg·�����ã���ǰ·��Ϊ��["+path+"]");
				return dictCatalogMap;
			}
		}
		return dictCatalogMap;
	}
	
	private static DictCatalog createCatalog(Element elem,String group){
		DictCatalog catalog = new DictCatalog();
		catalog.setGroup(group);
		String dictId = elem.getChildTextTrim("id").toUpperCase();
		catalog.setId(dictId);
		catalog.setTablename(elem.getChildText("tablename"));
		catalog.setTabledesc(elem.getChildText("tabledesc"));
		catalog.setRoot(elem.getChildText("root"));
		catalog.setStyle(elem.getChildText("style"));
		catalog.setPattern(elem.getChildText("pattern"));
		catalog.setOrderBy(elem.getChildText("orderBy"));
		String ed=elem.getChildText("edit");
		if(ed!=null&&(ed.equals("true")||ed.equals("false"))){
			catalog.setEdit(Boolean.parseBoolean(ed));
		}
		String encrypt=elem.getChildText("encrypt");
		if(encrypt!=null&&(encrypt.equals("true")||encrypt.equals("false"))){
			catalog.setEncrypt(Boolean.parseBoolean(encrypt));
		}
		String sql=elem.getChildText("sql");
		if(sql!=null&&!sql.equals("")){
			catalog.setSql(sql);
		}
		String mode=elem.getChildText("mode");
		if(mode==null||mode.equals("")){
			mode=DictCatalog.MODE1;
		}
		catalog.setMode(mode);
		Element mapping=elem.getChild("mapping");
		if(mapping!=null){
			if(mode.equals(DictCatalog.MODE1)){
				String id=mapping.getChildText("id");
				String pid=mapping.getChildText("pid");
				String label=mapping.getChildText("label");
				catalog.setMapping(new DictMapping(id,pid,label));
			}else{
				String code=mapping.getChildText("code");
				String sequence=mapping.getChildText("sequence");
				String label=mapping.getChildText("label");
				String depth=mapping.getChildText("depth");
				catalog.setMapping(new DictMapping(code,sequence,label,depth));
			}
		}else{
			catalog.setMapping(new DictMapping(mode));
		}
		String expand=elem.getChildText("expand");
		if(expand!=null&&!expand.equals("")){
			catalog.setExp(expand);
		}
		List<Element> listeners;
		DictCatalog.Listener listenerObj;
		if(elem.getChild("listeners")!=null){
			listeners=elem.getChild("listeners").getChildren("listener");
			for(Element listener:listeners){
				listenerObj=catalog.new Listener(dictId,listener.getTextTrim());
				catalog.addListener(listenerObj);
				dictListeners.add(listenerObj);
			}
		}
		return catalog;
	}
	
	/**
	 * ����ĳ�ڵ��£���һ���ӵ�ID��
	 * @param cataForm
	 * @param pid
	 * @return
	 */
	public static String buildId(DictCatalog cataForm,String pid) {
		String tableName = cataForm.getTablename();
		String buildId = "";// ���ձ���
		
		if (pid.equals(DictBuffer.dummyRootId)) {// �ֵ��һ���ڵ㡣
			pid = "";
		}
		/** ��Ӹ���¼�����������γɳ�ʼ��λ�룻����������id���ģ���λ���һ�� */
		int childLen = getChildLen(pid, cataForm.getPattern());
		if (childLen == 0) {// ˳��������
			buildId = getService().getMaxId(tableName, null, null);
			if (buildId == null) {
				buildId = "0";
			}
		} else if (childLen == -1) {
			System.out.println("�����涨�㼶��");
			return null;
		} else {// �㼶�������
			if(cataForm.isXmlStore()){
				buildId = getService().getMaxIdInXml(cataForm,pid,childLen);
			}else{
				buildId = getService().getMaxId(tableName,
						" and t.id like '" + pid + "%'", Integer
								.toString(childLen));
			}
			if (buildId == null) {
				buildId = pid;
				for (int i = 0, ii = childLen - pid.length(); i < ii; i++) {
					buildId += "0";
				}
			}
		}
		if(buildId.startsWith("0")){
			buildId="1"+buildId.substring(1);
			buildId = Long.toString(Long.parseLong(buildId) + 1);
			buildId="0"+buildId.substring(1);
		}else{
			buildId = Long.toString(Long.parseLong(buildId) + 1);
		}
		
		return buildId;
	}

	/**
	 * ���ݸ�id��ģ�壬������id�ĳ��ȡ�
	 * 
	 * @param pid
	 * @param pattern
	 * @return 0:pattern����Ϊ�ջ���������-1���㼶Խ�硣
	 */
	private static int getChildLen(String pid, String pattern) {
		int len = 0;
		if (pattern.indexOf(",") > 0) {// �ò���λ���ֱ�ʾ���Ĳ㼶��
			String[] pats = pattern.split(",");
			int patsIdx = 0, idx = 0;
			for (patsIdx = 0; patsIdx < pats.length; patsIdx++) {
				if (pid.length() == idx) {
					break;
				}
				idx += Integer.parseInt(pats[patsIdx]);
			}
			patsIdx -= 1;
			if (patsIdx < pats.length - 1) {
				len = idx + Integer.parseInt(pats[patsIdx + 1]);
			} else {
				len = -1;
				System.out.println("������㼶��");
			}
		} else {
			try {
				len = pid.length() + Integer.parseInt(pattern);
			} catch (Exception e) {
				len = 0;
			}
		}
		return len;
	}
	
	public static synchronized int addDict(String dictId,DictionaryForm dict) {
		DictCatalog cat = findDictCatalogById(dictId);
		List<DictionaryForm> l=new ArrayList<DictionaryForm>();
		if(dict.getId()==null){
			dict.setId(buildId(cat,dict.getPid()));
		}
		l.add(dict);
		getService().saveDict(cat, l);
		return 1;
	}
	
	
	
	/**
	 * �ֵ�ά��.�޸��ֵ��¼����
	 * @param dictId �ֵ��ʶ
	 * @param dict �ֵ����<DictionaryForm>��
	 * @return
	 */
	public static synchronized int updDict(String dictId,DictionaryForm dict) {
		DictCatalog cat = findDictCatalogById(dictId);
		getService().updateDict(cat, dict);
		return 1;
	}
	
	/**
	 * �ֵ�ά��.ɾ���ֵ��¼����
	 * @param dictId �ֵ��ʶ
	 * @param dict �ֵ����<DictionaryForm>��
	 * @return
	 */
	public static synchronized int delDict(String dictId,String id) {
		DictCatalog cat = findDictCatalogById(dictId);
		getService().removeDict(cat, id);
		//���»���
		if(cat.isBuffer()){
			getDict(dictId).remove(findDictById(dictId, id));
		}
		return 1;
	}
	
	// �������
	public static void orderDict(List<DictionaryForm> list){
		DictionaryForm node,node1,obj;
		int index=0;
		for (int i = 0; i < list.size() - 1; i++) {
			node = list.get(i);
			String order = node.getId();
			for (int m = i + 1; m < list.size(); m++) {
				node1 = list.get(m);
				if (order.compareTo(node1.getId())>0) {
					order = node1.getId();
					index = m;
				}
			}
			if (index > 0) {
				obj = list.get(i);
				list.set(i, list.get(index));
				list.set(index, obj);
			}
			index = -1;
		}
	}
	

	/**
	 * like��ʽ��ѯ�ֵ��е�����
	 * @param dictid �ֵ��ʶ
	 * @param label ��ѯ��Label
	 * @return
	 */
	public static List<DictionaryForm> likeLabel(String dictid, String label) {
		DictCatalog cat = findDictCatalogById(dictid);
		String sql=cat.getSql();
		cat.setSql(" and t."+cat.getMapping().label+" like '%"+label+"%'");
		List<DictionaryForm> list=getService().findDict(cat);
		cat.setSql(sql);
		return list;
	}
	
	/**
	 * �����ֵ�����
	 * @param dictid �ֵ��ʶ
	 */
	public static void updCache(String dictid) {
		dictid=dictid.toUpperCase();
		DictCatalog ca=dictCatalogMap.get(dictid);
		if(ca!=null){
			dictMap.remove(dictid);
		}
		getDict(dictid);//���»�ȡ�ֵ�����
	}
	
	/**
	 * ˢ���ֵ�Ŀ¼����
	 * @param dictid �ֵ��ʶ
	 */
	public static void updCfgCache(String dictid) {
		dictCatalogMap.remove(dictid);
		findDictCatalogById(dictid);
	}
	
	//getter and setter
	public static Map<String, List<DictionaryForm>> getDictMap() {
		return dictMap;
	}

	public static List<DictCatalog> schAllDictCatalog() {
		return new ArrayList(getDictCatalogMap().values());
	}
	
	public static List<DictCatalog> schUserDictCatalog() {
		List<DictCatalog> list=schAllDictCatalog();
		List<DictCatalog> cl= new ArrayList<DictCatalog>();
		for(DictCatalog ca:list){
			if(ca.isEdit()){
				cl.add(ca);
			}
		}
		return cl;
	}
	/**
	 * �ֵ�����·��
	 * ��������ã�classpath:/dictionary  or  ${workspace}\\${projectName}\\dynafile/dictionary/
	 * @return
	 */
	public static String getCfgPath() {
		if(cfgPath==null){
			String str=Constant.getConstant("dictionary.dictCfg");
			if(str.startsWith("classpath")){
				str=str.substring(str.indexOf(":")+1);
				return DictBuffer.class.getResource(str).getFile().replaceAll("%20", " ");
			}else{
				return str;
			}
		}
		return null;
	}
	public DictService getDictService() {
		return getService();
	}

	public void setDictService(DictService dictService) {
		DictBuffer.dictService = dictService;
	}
	
	public static DictService getService() {
		if(dictService==null){
			dictService=new DictService();
			System.out.println("DictBuffer.dictService is null!");
		}
		return dictService;
	}
	public static String getDbType() {
		return Constant.getConstant("dictionary.dbType");
	}
	/***************************************************
	 ************* deprecated Methods ******************
	 ***************************************************/
	/**
	 * ����һ���ֵ�ֵ �Ƽ�ʹ��DictionaryForm.clone()
	 * @deprecated 
	 * @param dictFrom Դ
	 * @param dictTo ���Ƶ������ڽ���һ��DictionaryForm�����ࡣ
	 * @return ���ظ��ƽ��
	 */
	public static DictionaryForm clone(DictionaryForm dictFrom, DictionaryForm dictTo) {
		if(dictTo==null){
			dictTo=new DictionaryForm();
		}
		dictTo.setId(dictFrom.getId());
		dictTo.setPid(dictFrom.getPid());
		dictTo.setLabel(dictFrom.getLabel());
		dictTo.setOrder(dictFrom.getOrder());
		return dictTo;
	}
	/**
	 * �Ƽ�ʹ��DictionaryForm.clone()
	 * @deprecated 
	 * @param dictFrom
	 * @return
	 */
	public static DictionaryForm clone(DictionaryForm dictFrom) {
		DictionaryForm dictTo=new DictionaryForm();
		dictTo.setId(dictFrom.getId());
		dictTo.setPid(dictFrom.getPid());
		dictTo.setLabel(dictFrom.getLabel());
		dictTo.setOrder(dictFrom.getOrder());
		return dictTo;
	}
	/**
	 * ���洢�����Ƿ���Xml�洢��
	 * @param style �ֵ�����
	 * @deprecated ��ʹ��catalog.isXmlStore()
	 * @return
	 */
	private static boolean isXmlStore(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[1].equals("X")){
				return true;
			}
		}else{//�ϰ����õļ���0:���ⵯ��1:List��select��2:ListXmlSelect
			if(style.equals("2")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @deprecated ��ʹ��catalog.isListType()
	 * ������ݽṹ�Ƿ���List����
	 * @param style �ֵ�����
	 * @return
	 */
	public static boolean isListType(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[0].equals("L")){
				return true;
			}
		}else{//�ϰ����õļ���0:���ⵯ��1:List��select��2:ListXmlSelect
			if(style.equals("1")||style.equals("2")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �Ƿ��õ�����
	 * @param style �ֵ�����
	 * @deprecated ��ʹ��catalog.isPopTreeView()
	 * @return
	 */
	public static boolean isPopTreeView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("P")){
				return true;
			}
		}else{//�ϰ����õļ���0:���ⵯ��1:List��select��2:ListXmlSelect
			if(style.equals("0")){
				return true;
			}
		}
		return false;
	}

	/**
	 * ֻ��ѡ��
	 * @param style �ֵ�����
	 * @deprecated ��ʹ��catalog.isAIView()
	 * @return
	 */
	public static boolean isAIView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("A")){
				return true;
			}
		}else{//�ϰ����õļ���0:���ⵯ��1:List��select��2:ListXmlSelect
			if(false){
				return true;
			}
		}
		return false;
	}
	/**
	 * ֻ��ѡ��
	 * @param style �ֵ�����
	 * @deprecated ��ʹ��catalog.isAjaxView()
	 * @return
	 */
	public static boolean isAjaxView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("X")){
				return true;
			}
		}
		return false;
	}
	/**
	 * ֻ��ѡ��
	 * @param style �ֵ�����
	 * @deprecated ��ʹ��catalog.isAjaxSepView()
	 * @return
	 */
	public static boolean isAjaxSepView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("S")){
				return true;
			}
		}
		return false;
	}

}
