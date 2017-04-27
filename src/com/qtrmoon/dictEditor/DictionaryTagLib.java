package com.qtrmoon.dictEditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.beanSerDao.DictCatalog;
import com.qtrmoon.dictEditor.beanSerDao.DictService;
import com.qtrmoon.dictEditor.beanSerDao.DictTreeUtil;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

/**
 * @author Javamao The TagLib can read Dictionary,and print selectTag or
 *         TreePage.
 */
public class DictionaryTagLib extends TagSupport {
	private String dictId;// Dictionary catalog Id.

	private String name="";// The name of Form's field
	private String style="";// radio、checkbox、checkboxInList、select(default)、selectTree.
	private String value="";// The Field's value
	private String reign="";// The Reign target.
	private String cssWidth="";// 生成的框长度
	private String feedBack="";// js中返回函数名[function feedBack(id,label){}]
	private boolean readonly = false;// weather can change the value.
	private String dynroot="";//动态字典根，不同于字典目录配置的根。可根据传入的根获取子集，实现对不同用户展示不同子集。但范围需在“字典目录配置的根”的子集内。
	private int expand=1;//默认展开根，0展开全部节点，1展开到一级节点（跟算作0级节点）。
	private String context="AUTO";//checkbox的选择是否影响父子。(yes/no/auto_yes/auto_no)
	
	private static final DictService[] dictService = new DictService[1];

	public int doStartTag() {
		String res = getElement(dictId,name,style,value,readonly,reign,cssWidth,feedBack,dynroot,expand,context);
		JspWriter out = pageContext.getOut();
		try {
			out.print(res);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	public static String getElement(String dictId,String name,String style,String value) {
		return getElement(dictId,name,style,value,false,"","","","",2,"AUTO");
	}
	public static String getElement(String dictId,String name,String style,String value,boolean readonly,String reign,String cssWidth,String feedBack,String dynroot) {
		return getElement(dictId,name,style,value,readonly,reign,cssWidth,feedBack,dynroot,2,"AUTO");
	}
	public static String getElement(String dictId,String name,String style,String value,boolean readonly,String reign,String cssWidth,String feedBack,String dynroot,int expand,String context) {
		String proName=Constant.getConstant("projectName");
		DictCatalog catalog = DictBuffer.findDictCatalogById(dictId);
		if(dynroot==null||dynroot.equals("")){//未设定临时根，则使用配置文件里根。
			dynroot=catalog.getRoot();
		}
		try{catalog.getId();}catch(Exception e){
			System.err.println("字典目录未配置Id为"+dictId+"的字典。");
			return "<span style='color:red;background:yellow'>［字典目录未配置Id为＂"+dictId+"＂的字典。］</span>";
		}
		if(cssWidth!=null&&!cssWidth.equals("")){
			cssWidth="width:"+cssWidth;
		}
		boolean isList = catalog.isListType();
		List<DictionaryForm> dictList = DictBuffer.getDict(dictId);
		String res = "";
		String selectedId,selectedLabel="";
		if (value != null && !value.equals("")) {
			value=value.trim();
			if(value.indexOf(",")>0){
				String[] values = value.split(",");
				for(int i=0;i<values.length;i++){
					DictionaryForm dictForm = DictBuffer.findDictById(dictId, values[i]);
					if (dictForm != null) {
						selectedLabel += dictForm.getLabel().trim()+",";
					}
				}
				selectedLabel = selectedLabel.substring(0,selectedLabel.length()-1);
			}else{
				DictionaryForm dictForm = DictBuffer.findDictById(dictId, value);
				if (dictForm != null) {
					selectedLabel = dictForm.getLabel().trim();
				}
			}
		} else {
			value = "";
		}
		selectedId=value;
		if(reign!=null&&!reign.equals("")){//上下文约束类字典，目前都是select类。
			String pid="";
			String head="";
			DictionaryForm root = (DictionaryForm)(new DictTreeUtil()).getTree(dictList);
			if(reign.startsWith("#HEAD_")){
				reign=reign.replace("#HEAD_", "");
				pid=root.getId();
				head="true";
			}else{
				head="false";
				if(value!=null&&!value.equals("")){
					pid=DictBuffer.findDictById(dictId, value).getPid();
				}
				if(reign.startsWith("#TAIL")){
					reign="";
				}
			}
			List<DictionaryForm> clist=new ArrayList<DictionaryForm>();
			if(!pid.equals("")){
				if(!pid.equals(DictBuffer.dummyRootId)&&reign.startsWith("#HEAD_")){//非虚根则根有实际意义。
					clist.add(root);
				}else{
					for(DictionaryForm dict:dictList){
						if(pid.equals(dict.getPid())){
							clist.add(dict);
						}
					}
				}
			}
			res="<select name='"+name+"' dictId='"+dictId+"' reign='"+reign+"' val='"+value+"' head='"+head+"' style='"+cssWidth+"'>";
			res+="<option value=''>请选择</option>";
			for(DictionaryForm dict:clist){
				if(dict.getId().equals(value)){
					res+="<option value='"+dict.getId()+"' exp='"+dict.getExp()+"' selected>"+dict.getLabel()+"</option>";
				}else{
					res+="<option value='"+dict.getId()+"' exp='"+dict.getExp()+"'>"+dict.getLabel()+"</option>";
				}
			}
			res+="</select>";
			return res;
		}
		if(catalog.isAIView()){//智能匹配类，不区分树还是列表
			String ico="";
			if (!isList) {//树型
				ico="tree";
			}else{
				ico="list";
			}
			if(catalog.isBuffer()&&dictList.size()<=DictCfg.MINI_AI){//小数据量智能字典使用，树结构查询。|大数据量的使用无树结构库查询。
				res += "<span>";
				res += "<input name='"+name+"' type='hidden' value='"+selectedId+"'/><input name='"+name+"DictShower' type='text' class='dict_AIText' value='"+selectedLabel+"' style='"+cssWidth+"'/>";
				res += "<img src='/"+proName+"/dictEditor/imgs/"+ico+".gif'/>";
				res += "<iframe class='dict_AIIframe' src='' srcx='/"+proName+"/dictEditor/dictTree.do?method=showDictTree&feedBack="+feedBack+"&include=ai&muti=false&ajax="+catalog.isAjaxView()+"&dynroot="+dynroot+"&dictId="+dictId+"&value="+value+"' width='300' height='100' scrolling='no' frameBorder='no' style='position:absolute;display:none;width:300px;height:230px;border:#7f9db9 1px solid'></iframe>\r\n";
				res += "</span>";
			}else{
				res += "<span>";
				res += "<input name='"+name+"' type='hidden' value='"+selectedId+"'/><input name='"+name+"DictShower' type='text' class='dict_largeAIText' feedBack='"+feedBack+"' readonly='readonly' value='"+selectedLabel+"' style='"+cssWidth+"'/>";
				res += "<img src='/"+proName+"/dictEditor/imgs/"+ico+"_a.gif' class='dict_largeAIImg' dictId='"+dictId+"' style='cursor:pointer;'/>";
				res += "<iframe class='dict_largeAIIframe' src='' srcx='/"+proName+"/dictEditor/tree_largeAi.jsp?dictId="+dictId+"&dynroot="+dynroot+"&feedBack="+feedBack+"' frameBorder='no' style='position:absolute;display:none;width:200px;height:150px;border:#7f9db9 1px solid'></iframe>\r\n";
				res += "</span>";
			}
			return res;
		}
		if (!isList) {//树型
//			if(dictList.size()==0){
//				return "树形字典["+dictId+"]无数据。";
//			}
			if("selectTree".equals(style)){//select单选树
				String rootid=dynroot;
				if(rootid==null||rootid.equals("")){
					rootid=DictBuffer.dummyRootId;
				}
				DictionaryForm root=DictBuffer.getDictById(dictId, rootid);
				StringBuffer str=new StringBuffer();
				getSelectTreeCode(dictId,root,value,0,str);
				res = ""+//<input type='text' value='' onclick='dictModule_initSelectTree_expand(this)' style='cursor:pointer;'/>
						"<select class='selectTree' name='" + name + "' style='"+cssWidth+"'>"+
						str.toString()+
						"</select>";
				return res;
			}if("fakeSelectTree".equals(style)){//伪select单选树，使用Div实现。
				String label=selectedLabel.replaceAll("　", selectedLabel);
				res = "<input type='hidden' name='"+name+"' value='"+value+"'/>"+
						"<input type='text' name='"+name+"DictShower' value='"+label+"' feedBack='"+feedBack+"' style='cursor:default;height:21px;"+cssWidth+";float:left;' class='fakeSelectTree'/>"+
						"<img src='/"+proName+"/dictEditor/imgs/fakeselect.gif' style='float:left;margin-left:-4px;'/>"+
						"<iframe src='/"+proName+"/dictEditor/treeTag_fakeselect.jsp?dictId="+dictId+"&dynroot="+dynroot+"&value="+value+"' style='position:absolute;top:0px;left:0px;border:black 1px solid;display:none;' frameborder=0></iframe>";
				return res;
			}else if(catalog.isAjaxSepView()){//Ajax分离树，将树分离为多个Select。
				res += "<div>";
				res += "<input type='hidden' value='"+value+"' name='"+name+"' class='dict_ajaxSepTree' dictId='"+dictId+"' root='"+DictBuffer.dummyRootId+"'/>";
				res += "</div>";
			}else{//(ajax,普通)*(单选,多选)弹出树。
				boolean muti=false,//多选弹出树
					    ajax=false;//ajax弹出树
				if(catalog.isAjaxView()){//Ajax树,代码与弹出树相同,只有弹出的页面不同。
					ajax=true;
				}
				if ("checkbox".equals(style)) {
					muti=true;
				}
				res += "<input type='hidden' name='" + name + "' value='" + selectedId.trim() + "'>";
				res += "<input type='text' name='" + name + "DictShower' ";
				res += "class='dict_normalPopTree' dictId='"+dictId+"' muti='"+muti+"' ajax='"+ajax+"' feedBack='"+feedBack+"'";
				res += " value=\"" + selectedLabel + "\" readonly  style='"+cssWidth+"' dynroot='"+dynroot+"' expand='"+expand+"' context='"+context+"'>";// 显示
				res += "<img class='dict_normalPopTree_ico' style='cursor:pointer' src='/"+proName+"/dictEditor/imgs/tree.gif'/>";// 记录
			}
		} else {//列表型
			if(dictList==null||dictList.size()==0){
				return "<span style='color:red;background:yellow'>［字典＂"+dictId+"＂无数据。］</span>";
			}
			if("checkboxInList".equals(style)){//多选下拉列表
				res += "<div>";
				res += "<input name='"+name+"' type='hidden' value='"+selectedId+"'/><input name='nameShower' type='text' class='dict_mutiText' value='"+selectedLabel+"' style='"+cssWidth+"'/>";
				res += "<img src='/"+proName+"/dictEditor/imgs/list.gif'/>";
				res += "<iframe class='dict_mutiIframe' src='' srcx='/"+proName+"/dictEditor/dictTree.do?method=showDictTree&include=checkboxInList&muti=true&ajax=false&dictId="+dictId+"&value="+value+"' width='300' height='100' frameBorder='no' scrolling='no' style='position:absolute;display:none;width:200px;height:150px;border:#7f9db9 1px solid'></iframe>\r\n";
				res += "</div>";
			}else{//非智能匹配列表
				if("checkbox".equals(style)){
					//多选checkbox列表
					if(value!=null&&!value.equals("")){
						String[] vals = value.split(",");
						boolean ifchecked=false;
						int i=0;
						for (DictionaryForm dict:dictList) {
							ifchecked = false;
							for(String val:vals){
								if(val.equals(dict.getId())){
									ifchecked=true;
									break;
								}
							}
							if (ifchecked) {
								res += "<input type='checkbox' value='" + dict.getId() + "' checked name='"+name+"' id='"+name+i+"'/>";
							} else {
								res += "<input type='checkbox' value='" + dict.getId() + "' name='"+name+"'id='"+name+i+"'/>";
							}
							res += "<label for='"+name+i+"'>"+dict.getLabel()+"</label>";
							i++;
						}
					}else{
						int i=0;
						for (DictionaryForm dict:dictList) {
							res += "<input type='checkbox' value='" + dict.getId() + "' name='"+name+"' id='"+name+i+"'><label for='"+name+(i++)+"'>"+dict.getLabel()+"</label>";
						}
					}
				}else if("radio".equals(style)) {//单选radio列表
					int i=0;
					if(value==null||"".equals(value)){//value为空，radio默认选第一个
						value=dictList.get(0).getId();
					}
					for (DictionaryForm dict:dictList) {
						if (value.equals(dict.getId())) {
							res += "<input type=\"radio\" name=\""+name+"\" value=\"" + dict.getId() + "\" checked id='"+name+i+"'/>";
						} else {
							res += "<input type=\"radio\" name=\""+name+"\" value=\"" + dict.getId() + "\" id='"+name+i+"'/>";
						}
						res += "<label for='"+name+i+"'>"+dict.getLabel()+"</label>";
						i++;
					}
				}else{//单选select列表
					res += "<select name=\"" + name + "\" style='"+cssWidth+"'>";
					res += "<option value=\"\"></option>";
					for (DictionaryForm dict:dictList) {
						if (value != null && value.equals(dict.getId())) {
							res += "<option value=\"" + dict.getId() + "\" selected>";
						} else {
							res += "<option value=\"" + dict.getId() + "\">";
						}
						res += dict.getLabel();
						res += "</option>";
					}
					res += "</select>";
				}
			}
		}
		return res;
	}

	/**
	 * 递归构造树形select代码
	 * @param root
	 * @return
	 */
	private static void getSelectTreeCode(String dictId,DictionaryForm root,String value,int level,StringBuffer str) {
		String selected="";
		if(value!=null&&!value.equals("")&&value.equals(root.getId())){
			selected="selected='selected'";
		}
		str.append("<option value='"+root.getId()+"' level='"+level+"' exp='"+root.getExp()+"' "+selected+">"+
				"　　　　　　　　　　　　　　　　　　　　　".substring(0,level)+root.getLabel()+"</option>\r\n");
		level+=1;
		for(DictionaryForm dict:root.getChildren(dictId)){
			getSelectTreeCode(dictId,dict,value,level,str);
		}
	}
	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getReign() {
		return reign;
	}

	public void setReign(String reign) {
		this.reign = reign;
	}

	public String getCssWidth() {
		return cssWidth;
	}

	public void setCssWidth(String cssWidth) {
		this.cssWidth = cssWidth;
	}

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public String isDynroot() {
		return dynroot;
	}

	public void setDynroot(String dynroot) {
		this.dynroot = dynroot;
	}

	public int getExpand() {
		return expand;
	}

	public void setExpand(int expand) {
		this.expand = expand;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
}