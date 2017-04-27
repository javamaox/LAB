package com.qtrmoon.dictEditor.beanSerDao;

import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

public class DictTreeUtil extends TreeUtil {
	private boolean ifmuti=false;
	
	public DictTreeUtil(boolean ifmuti){
		this.ifmuti = ifmuti;
	}
	
	public DictTreeUtil(){
		this.ifmuti = false;
	}
	
	public String render(TreeNode node) {
		DictionaryForm dnode = (DictionaryForm) node;
		if(ifmuti){
			return "<input type='checkbox' id='" + dnode.getId() + "'/><span>" + dnode.getLabel()+"</span>";
		}else{
			return "<a href=\"javascript:void(0)\" id='" + dnode.getId() + "'>" + dnode.getLabel() + "</a>";
		}
	}
}
