package com.qtrmoon.sysManage.bean;

/**
 * SysOrgan generated by MyEclipse - Hibernate Tools
 */

public class OrganUnit implements java.io.Serializable {

	// Fields

	private String id;//

	private String organId;// 

	private String unitId;// 

	// Constructors

	/** default constructor */
	public OrganUnit() {
	}

	/** minimal constructor */
	public OrganUnit(String id) {
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

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

}