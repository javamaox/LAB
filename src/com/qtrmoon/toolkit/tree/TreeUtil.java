package com.qtrmoon.toolkit.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javamao 生成树代理类
 * 
 * @x 使用本代理必须遵循的规则：
 * @1 节点类继承TreeNode类，提供获取主键和父键的方法getId() getPId()
 * @2 继承本代理类，实现render方法，来打印节点。
 * 
 * @notice 节点必须有主父键才能构建树，这一点是显然的嘛。
 * @x webRoot内的tree/style.css;imgs文件夹都是必不可少的。
 */
public abstract class TreeUtil implements ITreeUtil {

	/**************************************************
	 * 				获取树结构方法组
	 **************************************************/
	/** 构造树的层级结构，返回根节点 */
	public TreeNode getTree(List treeList) {
		return getTree(treeList,getTreeRoot(treeList));
	}
	public TreeNode getTree(List treeList, TreeNode root) {
		root = getLayerTree(root, treeList,0);
		return root;
	}
	/** 构造有序树的层级结构，返回根节点 */
	public TreeNode getOrderTree(List treeList) {
		return getOrderTree(treeList,getTreeRoot(treeList));
	}
	public TreeNode getOrderTree(List treeList, TreeNode root) {
		root = getLayerTree(root, treeList,0);
		orderTree(root);
		return root;
	}
	/**************************************************
	 * 				获取树无序树HTML码方法组
	 **************************************************/
	/** 构造无序树的HTML码，并返回。 */
	public String getTreeCode(List treeList) {
		return getTreeCode(treeList,getTreeRoot(treeList));
	}
	public String getTreeCode(List treeList, TreeNode root) {
		String treeStr = "";
		treeStr += "<div class=\"tree\">\n";
		root = getLayerTree(root, treeList,0);
		treeStr += getTreeCode(root, 1);
		treeStr += "</div>\n";
		return treeStr;
	}
	
	/**************************************************
	 * 		获取有序树结构方法组，默认以ord排列，或
	 * 		覆盖TreeNode的ord的setter&getter方法。
	 **************************************************/
	/** 构造有序树的HTML码，并返回。 */
	public String getOrderTreeCode(List treeList) {
		return getOrderTreeCode(treeList,getTreeRoot(treeList));
	}
	/** 构造有序树的HTML码，并返回。 */
	public String getOrderTreeCode(List treeList, TreeNode root) {
		String treeStr = "";
		treeStr += "<div class=\"tree\">\n";
		root = getLayerTree(root, treeList,0);
		orderTree(root);
		treeStr += getTreeCode(root, 1);
		treeStr += "</div>\n";
		return treeStr;
	}
	
	/**************************************************
	 * 		获取文本树方法，用制表符和空格组装
	 **************************************************/
	public String getTextTreeCode(List treeList) {
		return getTextTreeCode(treeList,getTreeRoot(treeList));
	}
	public String getTextTreeCode(List treeList,TreeNode root) {
		root=getLayerTree(root, treeList,0);
		return getTextTreeCode(root,1);
	}
	/** 获取文本树代码 */
	private String getTextTreeCode(TreeNode node, int degree) {
		String res = "";
		res += getTextPrifex(node);
		TreeNode treeNode;
		if (node.getCnodeList().size() > 0) {
			if (isLastChild(node)) {
				res += "└";
			} else {
				res += "├";
			}
		} else {
			if (isLastChild(node)) {
				res += "└";
			} else {
				res += "├";
			}
		}
		treeNode = (TreeNode) node;
		/** ******* 节点字串 ******** */
		res += render(treeNode);
		/***/
		res += ","+treeNode.getTreeId();
		res += "\n";
		for (int i = 0; i < node.getCnodeList().size(); i++) {
			res += getTextTreeCode((TreeNode) node.getCnodeList().get(i),
					degree + 1);
		}
		return res;
	}
	/** 获取前导字符 * */
	private String getTextPrifex(TreeNode node) {
		String res = "";
		while ((node = node.getPNode()) != null) {
			if (isLastChild(node)) {
				res = "　" + res;
			} else {
				res = "｜" + res;
			}
		}
		return res;
	}
	/**************************************************
	 * 		获取表格树方法
	 **************************************************/
	String tableHead="";
	/** 获取表格树代码*/
	public String getTableTreeCode(List treeList) {
		return getTableTreeCode(treeList,getTreeRoot(treeList));
	}
	public String getTableTreeCode(List treeList, TreeNode root) {
		String treeStr = "";
		treeStr += "<table class='tableTree'>\n";
		treeStr += "<thead><tr>"+tableHead+"</tr></thead>";
		root = getLayerTree(root, treeList,0);
		treeStr += getTableTreeCode(root, 1);
		treeStr += "</table>\n";
		return treeStr;
	}
	protected void setTableHead(String[] titles){
		String res="";
		for(int i=0;i<titles.length;i++){
			res+="<td>"+titles[i]+"</td>";
		}
		tableHead = res;
	}
	/** 获取表格树代码*/
	private String getTableTreeCode(TreeNode node, int degree) {
		String res = "";
		res = "<tr class=\"treePrefix\" onclick=\"getId(this,'"
				+ node.getTreeId() + "','" + node.getTreePId() + "')\" style=\"clear:both;\"><td style=\"clear:both;\">";
		res += getPrifex(node);
		TreeNode treeNode;
		if (node.getCnodeList().size() > 0) {
			if (isLastChild(node)) {
				res += "<span class=\"minusL\"  onclick=\"expNode('L')\"/>&nbsp;</span>";
			} else {
				res += "<span class=\"minusT\"  onclick=\"expNode('T')\"/>&nbsp;</span>";
			}
		} else {
			if (isLastChild(node)) {
				res += "<span class=\"squareL\"/>&nbsp;</span>";
			} else {
				res += "<span class=\"squareT\"/>&nbsp;</span>";
			}
		}
		treeNode = (TreeNode) node;

		/** ******* 节点字串 ******** */
		res += render(treeNode);
		/***/
		res += "</tr>";

		if (node.getCnodeList().size() > 0) {
			res += "<tbody class='childNode'>\n";
		}
		for (int i = 0; i < node.getCnodeList().size(); i++) {
			res += getTableTreeCode((TreeNode) node.getCnodeList().get(i),
					degree + 1);
		}

		if (node.getCnodeList().size() > 0) {
			res += "</tbody>\n";
		}
		return res;
	}
	
	/** 查找树根，如果您不知道树的根调用这个方法。 * */
	public TreeNode getTreeRoot(List treeList) {
		TreeNode node = (TreeNode) treeList.get(0);
		return getRoot(treeList, node);
	}

	/** 找node的父 */
	private TreeNode getRoot(List treeList, TreeNode node) {
		TreeNode root = null;
		for (int i = 0; i < treeList.size(); i++) {
			root = (TreeNode) treeList.get(i);
			if (node.getTreePId() != null
					&& node.getTreePId().equals(root.getTreeId())) {
				return getRoot(treeList, root);
			}
		}
		return node;
	}
	/**************************************************
	 * 		树类私有方法组
	 **************************************************/
	/** 建立双链层型树(先根)
	 * @param pNode
	 * @param treeList
	 * @param degree 节点级别，根0级。
	 * @return
	 */
	private TreeNode getLayerTree(TreeNode pNode, List treeList,int degree) {
		if(pNode.getCnodeList()!=null&&pNode.getCnodeList().size()>0){
			//子非空说明已经创建过
			return pNode;
		}
		TreeNode node = null;
		List<TreeNode> cList = new ArrayList<TreeNode>();
		for (int i = 0; i < treeList.size(); i++) {
			node = (TreeNode) treeList.get(i);
			// find child
			if (node.getTreePId() != null&& node.getTreePId().equals(pNode.getTreeId())) {
				node.setPNode(pNode);
				if(!cList.contains(node)){
					cList.add(node);
				}
				getLayerTree(node, treeList,degree+1);
			}
		}
		//兄弟链
		for(int i=0;i<cList.size();i++){
			if(i-1>=0){
				cList.get(i).prev=cList.get(i-1);
			}
			if(i+1<cList.size()){
				cList.get(i).next=cList.get(i+1);
			}
		}
		pNode.setDegree(degree);
		pNode.setCnodeList(cList);
		return pNode;
	}

	/** 树排序 * */
	private TreeNode orderTree(TreeNode pNode) {
		TreeNode node = null;
		List<TreeNode> treeList = pNode.getCnodeList();
		int index = -1;
		TreeNode obj = null;
		// 排序代码
		for (int i = 0; i < treeList.size() - 1; i++) {
			node = (TreeNode) treeList.get(i);
			Integer order = node.getOrder();
			for (int m = i + 1; m < treeList.size(); m++) {
				TreeNode node1 = (TreeNode) treeList.get(m);
				if (order > node1.getOrder()) {
					order = node1.getOrder();
					index = m;
				}
			}
			if (index > 0) {
				obj = treeList.get(i);
				treeList.set(i, treeList.get(index));
				treeList.set(index, obj);
			}
			index = -1;
		}

		//兄弟链
		for(int i=0;i<treeList.size();i++){
			if(i-1>=0){
				treeList.get(i).prev=treeList.get(i-1);
			}
			if(i+1<treeList.size()){
				treeList.get(i).next=treeList.get(i+1);
			}
		}
		// 子节点排序
		for (int i = 0; i < treeList.size(); i++) {
			node = (TreeNode) treeList.get(i);
			if (node.cnodeList.size() > 0) {
				orderTree(node);
			}
		}
		return pNode;
	}

	/** 获取树代码 */
	private StringBuffer getTreeCode(TreeNode node, int degree) {
		StringBuffer res = new StringBuffer();
		res.append("<div class=\"treePrefix\">");
		res.append(getPrifex(node));//获取前驱
		TreeNode treeNode;
		if (node.getCnodeList().size() > 0) {
			if (isLastChild(node)) {
				res.append("<span class=\"minusL\" nodeid='"+node.getTreeId()+"'></span>");
			} else {
				res.append("<span class=\"minusT\" nodeid='"+node.getTreeId()+"'></span>");
			}
		} else {
			if (isLastChild(node)) {
				res.append("<span class=\"squareL\"></span>");
			} else {
				res.append("<span class=\"squareT\"></span>");
			}
		}
		treeNode = (TreeNode) node;
		res.append("</div>");

		/** ******* 节点字串 ******** */
		res.append(render(treeNode));
		/***/

		res.append("");
		res.append("<div style=\"clear:both;\"></div>");

		if (node.getCnodeList().size() > 0) {
			res.append("<div class=\"childNode\">\n");
		}
		for (int i = 0; i < node.getCnodeList().size(); i++) {
			res.append(getTreeCode((TreeNode) node.getCnodeList().get(i),degree + 1));
		}

		if (node.getCnodeList().size() > 0) {
			res.append("</div>\n");
		}
		return res;
	}
	
	/** 获取前导图片 * */
	private String getPrifex(TreeNode node) {
		String res = "";
		while ((node = node.getPNode()) != null) {
			if (isLastChild(node)) {
				res = "<span class=\"blank\" />&nbsp;</span>" + res;
			} else {
				res = "<span class=\"I\" />&nbsp;</span>" + res;
			}
		}
		return res;
	}
	
	/** 当前节点是否位于其父的最末 * */
	private boolean isLastChild(TreeNode node) {
		int index = getNodeIndex(node);
		TreeNode pnode = node.getPNode();
		if (pnode != null && pnode.getCnodeList() != null) {
			if (index == pnode.getCnodeList().size() - 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/** 获取节点在其父的子序 * */
	private int getNodeIndex(TreeNode node) {
		TreeNode pnode = node.getPNode();
		if (pnode != null && pnode.getCnodeList() != null) {
			for (int i = 0; i < pnode.getCnodeList().size(); i++) {
				if (((TreeNode) pnode.getCnodeList().get(i)).getTreeId()
						.equals(((TreeNode) node).getTreeId())) {
					return i;
				}
			}
		} else {
			return 0;
		}
		return 0;
	}

}
