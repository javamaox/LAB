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

	private static final Map<String, List<DictionaryForm>> dictMap = new HashMap<String, List<DictionaryForm>>();// 缓存查询过的字典数据
	
	private static final List<DictCatalog.Listener> dictListeners=new ArrayList<DictCatalog.Listener>();
	
	private static DictService dictService = null;
	
	private static String cfgPath=null;
	public static final String dummyRootId="_JAVAMAO";//虚根的id。
	public static final String EXP_SEP = "@@@";//扩展字段各值的分割符
	/**
	 * 不再依赖pid=null或pid="0"来识别多根。获取数据后对树型字典数据规范化，将根pid规范为""，多根的虚根：{id:dummyRootId,pid:""}。
	 * 数据结构	#存储类型	#表现形式
	 * T：树		#D：库	#A：AI	(小数据量使用有结构查询。|大数据量的使用无结构查询。)
	 * L：List	#X：xml	#P：pop
	 * T：树		#D：库	#S：Seprate
	 * T：树		#D：库	#X：Ajax
	 * T：树		#D：库	#N：Normal
	 * 例如一个数据库存储的库使用弹出选择则配置为：T#D#P
	 */
	public static List<DictionaryForm> getDict(String dictId) {
		List<DictionaryForm> dictList = null;
		if (dictId != null && !dictId.equals("")) {
			dictId=dictId.toUpperCase();
			dictList = dictMap.get(dictId);
			if (dictList == null||dictList.size()==0) {
				DictCatalog catalog = findDictCatalogById(dictId);
				if (catalog == null) {
					System.err.println("请检查字典的Id与表影射是否正确"+dictId);
					return null;
				}
				if (catalog.isXmlStore()) {// xml类型字典表
					dictList=getService().findDictInXml(catalog);
					if(dictList.size()>0&&!catalog.isListType()){
						formatTreeLayer(dictList,catalog);//建立树层级结构
					}
					dictMap.put(dictId, dictList);
				} else {
					// 数据库字典表
					// 检索字典的目录中有没有与当前调用的字典数据一致的字典。字典的对应表名、root、style配置相同，数据相同。
					String root=catalog.getRoot();
					if(root==null){root="";}
					//没有数据相同的字典配置。
					if(catalog.isBuffer()){
						dictList = getService().findDict(catalog);//有没有配置root，都重新查一遍，不能使用以前查的结果，因为其中有树的层级关系。
						if(dictList==null){
							System.out.println("service.findDict() is null["+catalog.getTablename()+"]！");
							return null;
						}
						if(dictList.size()==0){
							System.out.println("表或视图["+catalog.getTablename()+"]没有数据！");
							return null;
						}
						if(dictList.size()>0&&!catalog.isListType()){
							formatTreeLayer(dictList,catalog);//建立树层级结构
						}
						
						dictMap.put(dictId, dictList);
					}
				}
			}
		}
		return dictList;
	}

	/**
	 * 强制查所有数据，不管数据量是否过大。不影响其他正常方式的字典调用。
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
				System.out.println("表或视图["+catalog.getTablename()+"]没有数据！");
				return null;
			}
			if(dictList.size()>0&&!catalog.isListType()){
				formatTreeLayer(dictList,catalog);//建立树层级结构
			}
			dictMap.put(dictId+"_FORCE", dictList);
		}
		return dictList;
	}
	public static DictionaryForm getTreeRoot(String dictId){
		return findDictById(dictId, dummyRootId);
	}
	
	
	/** 获取pid下的一层字典数据 */
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
	 * 将树型字典格式化为层结构。
	 * @param dictList
	 */
	private static void formatTreeLayer(List<DictionaryForm> dictList,DictCatalog catalog) {
		//找到真正的根，不再依赖pid=null或pid="0"来识别根。
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
		dictList.add(createRoot(catalog));//建立虚根
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
	 * 建立虚根
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
			System.out.println("dictId为空，请检查传入的参数。");
			return null;
		}
		DictCatalog ca=getDictCatalogById(dictId);
		if(ca==null){
			System.out.println("字典："+dictId+"不存在。");
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
				System.out.println("字典："+dictId+"不存在。");
				return null;
			}
			for (int i = 0; i < dictList.size(); i++) {
				if (dictList.get(i).getId().equals(value)) {
					return dictList.get(i);
				}
			}
			if(ca.isListType()&&dummyRootId.equals(value)){//如果集合中没有，而且是列表型的字典。
				return createRoot(ca);
			}
		}
		System.out.println("字典："+dictId+"中不存在值为"+value+"的记录。");
		return null;
	}
	
	public static DictionaryForm getDictByLabel(String dictId, String label) {
		return findDictByLabel(dictId, label);
	}
	public static DictionaryForm findDictByLabel(String dictId, String label) {
		if(label==null){
			System.out.println("*****查不到"+dictId+"字典中Label为"+label+"的值!*****");
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
		if(ca.isListType()){//对列表根取子，则返回全部。
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
		}else{//无缓存的树需要每次构建排序
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
	
	/** 获取字典目录对象。 */
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
	 * 监听字典更新，传入的是用户的访问地址。
	 * @param url;//用户的访问地址
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
		if (dictCatalogMap.size() == 0) {// 如果没有初始化则初始化
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
				System.out.println("请检查字典模块的dictCfg路径配置！当前路径为：["+path+"]");
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
	 * 计算某节点下，下一个子的ID。
	 * @param cataForm
	 * @param pid
	 * @return
	 */
	public static String buildId(DictCatalog cataForm,String pid) {
		String tableName = cataForm.getTablename();
		String buildId = "";// 最终编码
		
		if (pid.equals(DictBuffer.dummyRootId)) {// 字典的一级节点。
			pid = "";
		}
		/** 添加根记录，无数据则形成初始高位码；有数据则查出id最大的，高位码加一。 */
		int childLen = getChildLen(pid, cataForm.getPattern());
		if (childLen == 0) {// 顺序编码的树
			buildId = getService().getMaxId(tableName, null, null);
			if (buildId == null) {
				buildId = "0";
			}
		} else if (childLen == -1) {
			System.out.println("超出规定层级！");
			return null;
		} else {// 层级编码的树
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
	 * 根据父id和模板，计算子id的长度。
	 * 
	 * @param pid
	 * @param pattern
	 * @return 0:pattern配置为空或不是整数。-1树层级越界。
	 */
	private static int getChildLen(String pid, String pattern) {
		int len = 0;
		if (pattern.indexOf(",") > 0) {// 用不等位数字表示树的层级。
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
				System.out.println("超出额定层级！");
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
	 * 字典维护.修改字典记录方法
	 * @param dictId 字典标识
	 * @param dict 字典对象<DictionaryForm>。
	 * @return
	 */
	public static synchronized int updDict(String dictId,DictionaryForm dict) {
		DictCatalog cat = findDictCatalogById(dictId);
		getService().updateDict(cat, dict);
		return 1;
	}
	
	/**
	 * 字典维护.删除字典记录方法
	 * @param dictId 字典标识
	 * @param dict 字典对象<DictionaryForm>。
	 * @return
	 */
	public static synchronized int delDict(String dictId,String id) {
		DictCatalog cat = findDictCatalogById(dictId);
		getService().removeDict(cat, id);
		//更新缓存
		if(cat.isBuffer()){
			getDict(dictId).remove(findDictById(dictId, id));
		}
		return 1;
	}
	
	// 排序代码
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
	 * like方式查询字典中的数据
	 * @param dictid 字典标识
	 * @param label 查询的Label
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
	 * 更新字典数据
	 * @param dictid 字典标识
	 */
	public static void updCache(String dictid) {
		dictid=dictid.toUpperCase();
		DictCatalog ca=dictCatalogMap.get(dictid);
		if(ca!=null){
			dictMap.remove(dictid);
		}
		getDict(dictid);//重新获取字典数据
	}
	
	/**
	 * 刷新字典目录配置
	 * @param dictid 字典标识
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
	 * 字典配置路径
	 * 例如可配置：classpath:/dictionary  or  ${workspace}\\${projectName}\\dynafile/dictionary/
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
	 * 复制一个字典值 推荐使用DictionaryForm.clone()
	 * @deprecated 
	 * @param dictFrom 源
	 * @param dictTo 复制到，用于接收一个DictionaryForm的子类。
	 * @return 返回复制结果
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
	 * 推荐使用DictionaryForm.clone()
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
	 * 检测存储类型是否是Xml存储。
	 * @param style 字典类型
	 * @deprecated 请使用catalog.isXmlStore()
	 * @return
	 */
	private static boolean isXmlStore(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[1].equals("X")){
				return true;
			}
		}else{//老版配置的兼容0:数库弹，1:List库select，2:ListXmlSelect
			if(style.equals("2")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @deprecated 请使用catalog.isListType()
	 * 检测数据结构是否是List类型
	 * @param style 字典类型
	 * @return
	 */
	public static boolean isListType(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[0].equals("L")){
				return true;
			}
		}else{//老版配置的兼容0:数库弹，1:List库select，2:ListXmlSelect
			if(style.equals("1")||style.equals("2")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否用弹出树
	 * @param style 字典类型
	 * @deprecated 请使用catalog.isPopTreeView()
	 * @return
	 */
	public static boolean isPopTreeView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("P")){
				return true;
			}
		}else{//老版配置的兼容0:数库弹，1:List库select，2:ListXmlSelect
			if(style.equals("0")){
				return true;
			}
		}
		return false;
	}

	/**
	 * 只能选择
	 * @param style 字典类型
	 * @deprecated 请使用catalog.isAIView()
	 * @return
	 */
	public static boolean isAIView(String style) {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("A")){
				return true;
			}
		}else{//老版配置的兼容0:数库弹，1:List库select，2:ListXmlSelect
			if(false){
				return true;
			}
		}
		return false;
	}
	/**
	 * 只能选择
	 * @param style 字典类型
	 * @deprecated 请使用catalog.isAjaxView()
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
	 * 只能选择
	 * @param style 字典类型
	 * @deprecated 请使用catalog.isAjaxSepView()
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
