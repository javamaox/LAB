package com.qtrmoon.sysManage.bean;

/**
 * �м��
 * SysOrgan generated by MyEclipse - Hibernate Tools
 */

public class UnitFunction implements java.io.Serializable {

	// Fields

	private String id;//

	private String functionId;//

	private String unitId;// 

	// Constructors

	/** default constructor */
	public UnitFunction() {
	}

	/** minimal constructor */
	public UnitFunction(String id) {
		this.id = id;
	}

	// Property accessors

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

}