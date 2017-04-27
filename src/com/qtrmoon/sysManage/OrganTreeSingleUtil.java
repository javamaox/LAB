package com.qtrmoon.sysManage;

import com.qtrmoon.sysManage.bean.OrganForm;
import com.qtrmoon.common.Constant;
import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

public class OrganTreeSingleUtil extends TreeUtil {

	public String render(TreeNode node) {
		String res = "";
		OrganForm organForm = (OrganForm) node;
		res = "<a href=\"javascript:nodeEvent('" + organForm.getId() + "','"
				+ organForm.getPid() + "','" + organForm.getName() + "')\">"
				+ organForm.getName() + "</a>";
		return res;
	}

}
