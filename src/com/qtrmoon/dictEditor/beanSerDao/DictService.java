package com.qtrmoon.dictEditor.beanSerDao;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.DictBuffer;
import com.qtrmoon.dictEditor.DictCfg;
import com.qtrmoon.toolkit.XmlUtil;

public class DictService {

	private static DictDao dictDAO;

	public long countDict(DictCatalog catalog){
		return getDictDAO().countDict(catalog);
	}
	/** 
	 * ��ѯ�ֵ����м�¼ 
	 * @param catalog
	 * @return
	 */
	public List<DictionaryForm> findDict(DictCatalog catalog) {
		if(catalog.isBuffer()){
			return getDictDAO().findDict(catalog);
		}else{
			System.out.print("�ֵ��"+catalog.getId()+"��ʹ�û������["+catalog.getSize()+">"+DictCfg.MAX_IN_RAM+"]��");
			if(catalog.isListType()){
				System.out.println("�б����ֵ�����������");
				return null;
			}
			if(catalog.getRoot()==null||catalog.getRoot().equals("")){
				System.out.println("������Ҫ����<root>��");
				return null;
			}else{
				return getDictDAO().findDict(catalog);
			}
		}
	}
	
	public List<DictionaryForm> findDictForce(DictCatalog catalog) {
		return getDictDAO().findDict(catalog);
	}
	
	/** 
	 * �ҳ�ǰtop����¼
	 * @param catalog
	 * @param top ǰ������¼��Ŀ��
	 * @return
	 */
	public List<DictionaryForm> findTopDict(DictCatalog catalog,int top) {
		return getDictDAO().findTopDict(catalog,top);
	}
	/** 
	 * ��ѯָ��ID���ֵ��¼
	 * @param catalog
	 * @param id
	 * @return
	 */
	public DictionaryForm findDictById(DictCatalog catalog, String id) {
		if(catalog.isXmlStore()){
			String path = DictBuffer.getCfgPath()+ catalog.getTablename();
			XmlUtil xmlUtil=new XmlUtil();
			xmlUtil.setCharset("GB2312");
			Element root = xmlUtil.loadXml(path);
			List<Element> cols = root.getChildren("col");
			for (Element col : cols) {
				if (col.getChildTextTrim("id").equals(id)) {
					return new DictionaryForm(col.getChildTextTrim("id"),col.getChildTextTrim("label"),col.getChildTextTrim("pid"));
				}
			}
			return null;
		}else{
			return getDictDAO().findDictById(catalog, id);
		}
	}
	
	/**
	 * ��ѯ�ƶ�lable���ֵ䣬���������ֵ��ʹ�á�
	 * @param catalog
	 * @param label
	 * @return
	 */
	public DictionaryForm findDictByLabel(DictCatalog catalog, String label) {
		return getDictDAO().findDictByLabel(catalog, label);
	}
	
	/**
	 * ��ѯָ��Pid�����в㼶�ӽڵ�ļ�¼������������Ľڵ��¼��
	 * @param catalog 
	 * @param pid 
	 * @return ����ĳ�ڵ�����м����ӽڵ�ļ��ϣ�����������Ľڵ㡣
	 */
	public List<DictionaryForm> findDictByPid(DictCatalog catalog, String pid) {
		return getDictDAO().findDictByPid(catalog, pid);
	}
	
	/**
	 * �����洢�ֵ��¼
	 * @param ca
	 * @param dictList
	 */
	public void saveDict(DictCatalog ca, List<DictionaryForm> dictList) {
		if(ca.isXmlStore()){
			String path = DictBuffer.getCfgPath()+ ca.getTablename();
			XmlUtil xmlUtil = new XmlUtil();
			xmlUtil.setCharset("GB2312");
			Element root = xmlUtil.loadXml(path);
			
			for(DictionaryForm dict:dictList){
				Element col=new Element("col");
				
				Element id=new Element("id");
				id.setText(dict.getId());
				col.addContent(id);
				
				Element pid=new Element("pid");
				pid.setText(dict.getPid());
				col.addContent(pid);
				
				Element label=new Element("label");
				label.setText(dict.getLabel());
				col.addContent(label);
				
				root.addContent(col);
			}
			xmlUtil.saveXml();
		}else{
			getDictDAO().saveDict(ca, dictList);
		}
	}

	
	/** 
	 * �޸��ֵ��¼ 
	 * @param catalog
	 * @param df
	 */
	public void updateDict(DictCatalog catalog, DictionaryForm df) {
		if(catalog.isXmlStore()){
			String path = Constant.getConstant("dictCfg")+ catalog.getTablename();
			path = path.replaceAll("%20", " ");
			XmlUtil xmlUtil=new XmlUtil();
			xmlUtil.setCharset("GB2312");
			Element root = xmlUtil.loadXml(path);
			List<Element> cols = root.getChildren("col");
			for (Element col : cols) {
				if (col.getChildTextTrim("id").equals(df.getId())) {
					col.getChild("id").setText(df.getId());
					col.getChild("pid").setText(df.getPid());
					col.getChild("label").setText(df.getLabel());
					break;
				}
			}
			xmlUtil.saveXml();
		}else{
			getDictDAO().updateDict(catalog, df);
		}
	}
	/**
	 * ��������
	 * @param catalog
	 * @param strings
	 */
	public void updateSort(DictCatalog catalog, String[] ids) {
		getDictDAO().updateSort(catalog, ids);
	}

	/** 
	 * ɾ���ֵ��¼ 
	 * @param catalog
	 * @param id
	 */
	public void removeDict(DictCatalog catalog, String id) {
		if(catalog.isXmlStore()){
			String path = Constant.getConstant("dictCfg")+ catalog.getTablename();
			XmlUtil xmlUtil = new XmlUtil();
			xmlUtil.setCharset("GB2312");
			Element root = xmlUtil.loadXml(path);
			List<Element> cols = root.getChildren("col");
			for (Element col : cols) {
				if (col.getChildTextTrim("id").equals(id)) {
					root.removeContent(col);
					break;
				}
			}
			xmlUtil.saveXml();
		}else{
			getDictDAO().removeDict(catalog, id);
		}
	}
	/**
	 * ����ɾ���ֵ��¼
	 * @param catalog
	 * @param ids
	 */
	public void removeDict(DictCatalog catalog, String[] ids) {
		getDictDAO().removeDict(catalog, ids);
	}
	
	
	/** ��ȡ�ֵ��Id������¼���޼�¼����null */
	public String getMaxId(String tableName, String condition,String len) {
		return getDictDAO().getMaxId(tableName, condition, len);
	}
	
	public List<DictionaryForm> findDictInXml(DictCatalog catalog) {
		List<DictionaryForm> dictList=new ArrayList<DictionaryForm>();
		String path = Constant.getConstant("dictCfg")+ catalog.getTablename();
		XmlUtil xmlUtil = new XmlUtil();
		xmlUtil.setCharset("GB2312");
		Element groupERoot = xmlUtil.loadXml(path);
		List<Element> tableList = groupERoot.getChildren("col");
		Element elem;
		DictionaryForm dict;
		dictList = new ArrayList<DictionaryForm>();
		for (int i = 0; i < tableList.size(); i++) {
			elem = tableList.get(i);
			dict = new DictionaryForm();
			dict.setId(elem.getChildText("id"));
			dict.setPid(elem.getChildText("pid"));
			dict.setLabel(elem.getChildText("label"));
			dictList.add(dict);
		}
		return dictList;
	}
	
	public String getMaxIdInXml(DictCatalog cataForm, String pid, int length) {
		String maxId="";
		if(cataForm.isXmlStore()){//Xml�洢����
			String path = Constant.getConstant("dictCfg")+ cataForm.getTablename();
			path = path.replaceAll("%20", " ");
			XmlUtil xmlUtil=new XmlUtil();
			xmlUtil.setCharset("GB2312");
			Element root = xmlUtil.loadXml(path);
			List<Element> cols = root.getChildren("col");
			String id = "";
			if(length==0){
				for (Element col : cols) {
					id = col.getChildTextTrim("id");
					if (id.length() == length&&id.compareTo(maxId) > 0) {
						maxId = id;
					}
				}
			}else{
				for (Element col : cols) {
					id = col.getChildTextTrim("id");
					if (id.length() > maxId.length()) {
						maxId = id;
					} else if (id.length() == maxId.length()) {
						if (id.compareTo(maxId) > 0) {
							maxId = id;
						}
					}
				}
			}
		}
		return maxId;
	}

	
	public DictDao getDictDAO() {
		if(dictDAO==null){
			dictDAO=new DictDao();
			System.out.println("DictService.dictDAO is null!");
		}
		return dictDAO;
	}
	public void setDictDAO(DictDao dictDAO) {
		DictService.dictDAO = dictDAO;
	}
}
