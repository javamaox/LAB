package com.qtrmoon.toolkit.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

import com.qtrmoon.common.PageForm;

/**
 * @author Javamao Root.id='0';Root.pid='';
 */
public abstract class TreeNode extends PageForm implements INode {
	protected TreeNode pNode[] = new TreeNode[1];

	public List<TreeNode> cnodeList = new ArrayList<TreeNode>();
	
	protected TreeNode prev;
	protected TreeNode next;
	
	protected int order;
	protected int degree;

	public TreeNode getPNode() {
		return this.pNode[0];
	}

	public void setPNode(TreeNode node) {
		this.pNode[0] = node;
	}


	public void addCNode(TreeNode node) {
		this.cnodeList.add(node);
	}


	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<TreeNode> getCnodeList() {
		return cnodeList;
	}

	public void setCnodeList(List<TreeNode> cnodeList) {
		this.cnodeList = cnodeList;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public TreeNode getPrev() {
		return prev;
	}

	public void setPrev(TreeNode prev) {
		this.prev = prev;
	}

	public TreeNode getNext() {
		return next;
	}

	public void setNext(TreeNode next) {
		this.next = next;
	}

}
