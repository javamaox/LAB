package com.qtrmoon.toolkit.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javamao ������������
 * 
 * @x ʹ�ñ����������ѭ�Ĺ���
 * @1 �ڵ���̳�TreeNode�࣬�ṩ��ȡ�����͸����ķ���getId() getPId()
 * @2 �̳б������࣬ʵ��render����������ӡ�ڵ㡣
 * 
 * @notice �ڵ���������������ܹ���������һ������Ȼ���
 * @x webRoot�ڵ�tree/style.css;imgs�ļ��ж��Ǳز����ٵġ�
 */
public abstract class TreeUtil implements ITreeUtil {

	/**************************************************
	 * 				��ȡ���ṹ������
	 **************************************************/
	/** �������Ĳ㼶�ṹ�����ظ��ڵ� */
	public TreeNode getTree(List treeList) {
		return getTree(treeList,getTreeRoot(treeList));
	}
	public TreeNode getTree(List treeList, TreeNode root) {
		root = getLayerTree(root, treeList,0);
		return root;
	}
	/** �����������Ĳ㼶�ṹ�����ظ��ڵ� */
	public TreeNode getOrderTree(List treeList) {
		return getOrderTree(treeList,getTreeRoot(treeList));
	}
	public TreeNode getOrderTree(List treeList, TreeNode root) {
		root = getLayerTree(root, treeList,0);
		orderTree(root);
		return root;
	}
	/**************************************************
	 * 				��ȡ��������HTML�뷽����
	 **************************************************/
	/** ������������HTML�룬�����ء� */
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
	 * 		��ȡ�������ṹ�����飬Ĭ����ord���У���
	 * 		����TreeNode��ord��setter&getter������
	 **************************************************/
	/** ������������HTML�룬�����ء� */
	public String getOrderTreeCode(List treeList) {
		return getOrderTreeCode(treeList,getTreeRoot(treeList));
	}
	/** ������������HTML�룬�����ء� */
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
	 * 		��ȡ�ı������������Ʊ���Ϳո���װ
	 **************************************************/
	public String getTextTreeCode(List treeList) {
		return getTextTreeCode(treeList,getTreeRoot(treeList));
	}
	public String getTextTreeCode(List treeList,TreeNode root) {
		root=getLayerTree(root, treeList,0);
		return getTextTreeCode(root,1);
	}
	/** ��ȡ�ı������� */
	private String getTextTreeCode(TreeNode node, int degree) {
		String res = "";
		res += getTextPrifex(node);
		TreeNode treeNode;
		if (node.getCnodeList().size() > 0) {
			if (isLastChild(node)) {
				res += "��";
			} else {
				res += "��";
			}
		} else {
			if (isLastChild(node)) {
				res += "��";
			} else {
				res += "��";
			}
		}
		treeNode = (TreeNode) node;
		/** ******* �ڵ��ִ� ******** */
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
	/** ��ȡǰ���ַ� * */
	private String getTextPrifex(TreeNode node) {
		String res = "";
		while ((node = node.getPNode()) != null) {
			if (isLastChild(node)) {
				res = "��" + res;
			} else {
				res = "��" + res;
			}
		}
		return res;
	}
	/**************************************************
	 * 		��ȡ���������
	 **************************************************/
	String tableHead="";
	/** ��ȡ���������*/
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
	/** ��ȡ���������*/
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

		/** ******* �ڵ��ִ� ******** */
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
	
	/** �����������������֪�����ĸ�������������� * */
	public TreeNode getTreeRoot(List treeList) {
		TreeNode node = (TreeNode) treeList.get(0);
		return getRoot(treeList, node);
	}

	/** ��node�ĸ� */
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
	 * 		����˽�з�����
	 **************************************************/
	/** ����˫��������(�ȸ�)
	 * @param pNode
	 * @param treeList
	 * @param degree �ڵ㼶�𣬸�0����
	 * @return
	 */
	private TreeNode getLayerTree(TreeNode pNode, List treeList,int degree) {
		if(pNode.getCnodeList()!=null&&pNode.getCnodeList().size()>0){
			//�ӷǿ�˵���Ѿ�������
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
		//�ֵ���
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

	/** ������ * */
	private TreeNode orderTree(TreeNode pNode) {
		TreeNode node = null;
		List<TreeNode> treeList = pNode.getCnodeList();
		int index = -1;
		TreeNode obj = null;
		// �������
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

		//�ֵ���
		for(int i=0;i<treeList.size();i++){
			if(i-1>=0){
				treeList.get(i).prev=treeList.get(i-1);
			}
			if(i+1<treeList.size()){
				treeList.get(i).next=treeList.get(i+1);
			}
		}
		// �ӽڵ�����
		for (int i = 0; i < treeList.size(); i++) {
			node = (TreeNode) treeList.get(i);
			if (node.cnodeList.size() > 0) {
				orderTree(node);
			}
		}
		return pNode;
	}

	/** ��ȡ������ */
	private StringBuffer getTreeCode(TreeNode node, int degree) {
		StringBuffer res = new StringBuffer();
		res.append("<div class=\"treePrefix\">");
		res.append(getPrifex(node));//��ȡǰ��
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

		/** ******* �ڵ��ִ� ******** */
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
	
	/** ��ȡǰ��ͼƬ * */
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
	
	/** ��ǰ�ڵ��Ƿ�λ���丸����ĩ * */
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

	/** ��ȡ�ڵ����丸������ * */
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
