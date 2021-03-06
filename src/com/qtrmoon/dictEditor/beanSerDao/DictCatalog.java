package com.qtrmoon.dictEditor.beanSerDao;

import java.util.ArrayList;
import java.util.List;

import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.DictCfg;

/**
 * DictCatalog generated by MyEclipse - Hibernate Tools
 */

public class DictCatalog implements java.io.Serializable {

	// Fields
	public static final String MODE1="0",MODE2="1";

	private String id;
	private String tabledesc;
	private String tablename;
	private String root;
	private String style;
	private String orderBy;
	private String mode=MODE1;//字典的数据格式类型。MODE1:id,pid,label。MODE2:code,sequence,label。sequence序列描述了树型的层次结构。default is 0。
	private DictMapping mapping;//字典的字段影射。
	private String pattern;
	private boolean edit=false;
	private String sql;//对字典数据的查询条件。
	private String group;//标识该字典属于哪个字典目录文件，其值为“目录文件的文件名”。
	private long size=0;//字典的数据量。
	private List<Listener> listeners;//对字典操作的监听，当数据发生变化时刷新字典数据。
	//是否缓存入内存。如果数据量过大则不缓存内存。量值在dictDao.findDict设置。
	//大数据量缓存，pid==''视为根结点。
	private Boolean buffer=null;
	private String exp;//字典扩展字段,以都号分割的字段串。
	private boolean encrypt=false;
	
	public class Listener{
		private String dictId;
		private String url;
		public Listener(String dictId, String url) {
			this.dictId=dictId;
			this.url=url;
		}
		public String getDictId() {
			return dictId;
		}
		public void setDictId(String dictId) {
			this.dictId = dictId;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	// Constructors

	/** default constructor */
	public DictCatalog() {
	}

	/** minimal constructor */
	public DictCatalog(String id) {
		this.id = id;
	}

	/** full constructor */
	public DictCatalog(String id, String tabledesc, String tablename,
			String style, String pattern,String group) {
		this.id = id;
		this.tabledesc = tabledesc;
		this.tablename = tablename;
		this.style = style;
		this.pattern = pattern;
		this.group=group;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTabledesc() {
		return this.tabledesc;
	}

	public void setTabledesc(String tabledesc) {
		this.tabledesc = tabledesc;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getRoot() {
		return root==null?"":root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public DictMapping getMapping() {
		return mapping;
	}

	public void setMapping(DictMapping mapping) {
		this.mapping = mapping;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}

	public void setListeners(List<Listener> listeners) {
		this.listeners = listeners;
	}
	public void addListener(Listener listener) {
		if(this.listeners == null){
			this.listeners=new ArrayList<Listener>();
		}
		this.listeners.add(listener);
	}
	

	/**
	 * 检测存储类型是否是Xml存储。
	 * @param style;类型描述
	 * @return
	 */
	public boolean isXmlStore() {
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
	 * 检测数据结构是否是List类型
	 * @param style
	 * @return
	 */
	public boolean isListType() {
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
	 * @param style
	 * @return
	 */
	public boolean isPopTreeView() {
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
	 * @param style
	 * @return
	 */
	public boolean isAIView() {
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

	public boolean isAjaxView() {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("X")){
				return true;
			}
		}
		return false;
	}

	public boolean isAjaxSepView() {
		if(style.indexOf("#")>0){
			if(style.split("#")[2].equals("S")){
				return true;
			}
		}
		return false;
	}

	/**
	 * 字典数据标识，字典的数据内容不同则该标识不同。（相同的数据内容可能配置为不同的表现形式）
	 * @return tablename+"_"+root+"_"+mapping+"_"+sql;
	 */
	public String getDataId() {
		return tablename+"_"+root+"_"+getMapping().toString()+"_"+sql;
	}

	/**
	 * 通过getDict检测是否有缓存。
	 * @return
	 */
	public boolean isBuffer() {
		if(buffer==null){
			if(isXmlStore()){
				buffer=true;
			}else{
				long sum=DictBuffer.getService().countDict(this);
				setSize(sum);
				if(sum>DictCfg.MAX_IN_RAM){
					buffer=false;
				}else{
					buffer=true;
				}
			}
		}
		return buffer;
	}
	
	/**
	 * 获取字典数据量
	 * @param ca
	 * @return
	 */
	public long getDictSize() {
		if(size>0){
			return size;
		}
		DictBuffer.getDict(id);
		return size;
	}

	/**
	 * 用于dao设值。
	 * @param size
	 */
	public void setSize(long size) {
		this.size = size;
	}
	public long getSize() {
		return size;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
	
}