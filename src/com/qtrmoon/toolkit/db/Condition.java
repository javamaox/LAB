package com.qtrmoon.toolkit.db;

public class Condition {
	private String key;

	private Class type;

	private Object value;

	public Condition() {
	}

	public Condition(String key, Class type, Object value) {
		this.key = key;
		this.type = type;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}