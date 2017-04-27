package com.qtrmoon.sysManage.action;

import com.qtrmoon.sysManage.bean.FunctionForm;
import com.qtrmoon.common.Constant;
import com.qtrmoon.toolkit.tree.TreeNode;
import com.qtrmoon.toolkit.tree.TreeUtil;

public class FunctionTreeUtil extends TreeUtil {

	public String render(TreeNode node) {
		String res = "";
		FunctionForm functionForm = (FunctionForm) node;
		String style="";
		if(functionForm.getIsShow().equals(SysConstant.FUNCTION_HIDE)){
			style="style='color:#999'";
		}else if(functionForm.getIsShow().equals(SysConstant.FUNCTION_PAGE_HIDE)){
			style="style='color:#88f'";
		}
		res = "<a href=\"/" + Constant.getConstant("projectName")
				+ "/sysManage/function.do?method=updFunction&funcId="
				+ functionForm.getId() + "\" target=\"functionOper\" "+style+">"
				+ functionForm.getName() + "</a>";
		return res;
	}

}
