package com.qtrmoon.sysManage.transact;

import java.util.List;

import com.ddr.agent.Client;
import com.qtrmoon.toolkit.tree.TreeNode;

public class OrganNode extends TreeNode {
	// 通讯服务器ID
	private String id;

	// 通讯服务器注册名
	private String name;

	// 通讯服务器的父服务器ID
	private String parentid;

	// 通讯服务器的子服务器列表
	private List<String> childids;

	// 客户端列表
	private List<Client> clients;

	// 结点类型
	private String type;

	// 结点的key
	private String key;

	public List<String> getChildids() {
		return childids;
	}

	public void setChildids(List<String> childids) {
		this.childids = childids;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTreeId() {
		return id;
	}

	public String getTreePId() {
		return parentid;
	}

	
}
