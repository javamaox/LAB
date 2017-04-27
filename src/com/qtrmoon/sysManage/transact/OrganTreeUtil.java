package com.qtrmoon.sysManage.transact;

import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

public class OrganTreeUtil extends TreeUtil {

	public String render(TreeNode node) {
		String res = "";
		OrganNode organNode = (OrganNode) node;
		res += "<div>";
		res += "<div>";
		if (organNode.getParentid() != null) {
			res += "<span onclick=\"javascript:getId('" + organNode.getId()
					+ "','" + organNode.getType() + "',this,'"
					+ organNode.getKey() + "')\">" + organNode.getName()
					+ "</span>";
		} else {
			res += organNode.getName();
		}
		res += "</div>";
		res += "</div>";
		return res;
	}

}
