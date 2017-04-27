package com.qtrmoon.sysManage.action;

import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.common.Constant;
import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

public class MultiFunctionTreeUtil extends TreeUtil {

	public String render(TreeNode node) {
		String res = "";
		FunctionForm functionForm = (FunctionForm) node;
		res = "<input type='checkbox' value='" + functionForm.getId()
				+ "' name='multi'/>" + functionForm.getName();

		return res;
	}

}
