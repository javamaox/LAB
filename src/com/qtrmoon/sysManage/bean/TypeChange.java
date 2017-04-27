package com.qtrmoon.sysManage.bean;

import com.qtrmoon.toolkit.DateTransfer;

public class TypeChange {

	public static UserForm userObj2Form(User obj) {
		UserForm form = new UserForm();
		form.setId(obj.getId());
		form.setLoginName(obj.getLoginName());
		form.setPassword(obj.getPassword());
		form.setUserName(obj.getUserName());
		form.setClassify(obj.getClassify());
		form.setRegTime(obj.getRegTime());
		form.setState(obj.getState());
		form.setOrganId(obj.getOrganId());
		return form;
	}

	public static User userForm2Obj(UserForm form) {
		User obj = new User();
		obj.setId(form.getId());
		obj.setLoginName(form.getLoginName());
		obj.setPassword(form.getPassword());
		obj.setUserName(form.getUserName());
		obj.setClassify(form.getClassify());
		obj.setRegTime(form.getRegTime());
		obj.setState(form.getState());
		obj.setOrganId(form.getOrganId());
		return obj;
	}

	public static OrganForm organObj2Form(Organ obj) {
		OrganForm form = new OrganForm();
		form.setId(obj.getId());
		form.setInfo(obj.getInfo());
		form.setIsMaster(obj.getIsMaster());
		form.setName(obj.getName());
		form.setOrd(obj.getOrd());
		form.setPid(obj.getPid());
		form.setTreeTrack(obj.getTreeTrack());
		form.setCode(obj.getCode());
		form.setType(obj.getType());
		return form;
	}

	public static Organ organForm2Obj(OrganForm form) {
		Organ obj = new Organ();
		obj.setId(form.getId());
		obj.setInfo(form.getInfo());
		obj.setIsMaster(form.getIsMaster());
		obj.setName(form.getName());
		obj.setOrd(form.getOrd());
		obj.setPid(form.getPid());
		obj.setTreeTrack(form.getTreeTrack());
		obj.setCode(form.getCode());
		obj.setType(form.getType());
		return obj;
	}

	public static FunctionForm functionObj2Form(Function obj) {
		FunctionForm form = new FunctionForm();
		form.setId(obj.getId());
		form.setName(obj.getName());
		form.setPid(obj.getPid());
		form.setLink(obj.getLink());
		form.setIsShow(obj.getIsShow());
		form.setInfo(obj.getInfo());
		form.setPicIco(obj.getPicIco());
		form.setPicImg(obj.getPicImg());
		form.setOrd(obj.getOrd());
		form.setTreeTrack(obj.getTreeTrack());
		form.setModuleName(obj.getModuleName());
		return form;
	}

	public static Function functionForm2Obj(FunctionForm form) {
		Function obj = new Function();
		obj.setId(form.getId());
		obj.setName(form.getName());
		obj.setPid(form.getPid());
		obj.setLink(form.getLink());
		obj.setIsShow(form.getIsShow());
		obj.setInfo(form.getInfo());
		obj.setPicIco(form.getPicIco());
		obj.setPicImg(form.getPicImg());
		obj.setOrd(form.getOrd());
		obj.setTreeTrack(form.getTreeTrack());
		obj.setModuleName(form.getModuleName());
		return obj;
	}

	public static RoleForm roleObj2Form(Role obj) {
		RoleForm roleForm = new RoleForm();
		roleForm.setId(obj.getId());
		roleForm.setInfo(obj.getInfo());
		roleForm.setIsLocal(obj.getIsLocal());
		roleForm.setName(obj.getName());
		roleForm.setClassify(obj.getClassify());
		roleForm.setOrd(obj.getOrd());
		roleForm.setOrganId(obj.getOrganId());
		return roleForm;
	}

	public static Role roleForm2Obj(RoleForm form) {
		Role obj = new Role();
		obj.setId(form.getId());
		obj.setInfo(form.getInfo());
		obj.setIsLocal(form.getIsLocal());
		obj.setName(form.getName());
		obj.setClassify(form.getClassify());
		obj.setOrd(form.getOrd());
		obj.setOrganId(form.getOrganId());
		return obj;
	}

	public static FunctionUnit functionUnitForm2Obj(FunctionUnitForm form) {
		FunctionUnit obj = new FunctionUnit();
		obj.setId(form.getId());
		obj.setInfo(form.getInfo());
		obj.setName(form.getName());
		obj.setOrd(form.getOrd());
		obj.setPicIco(form.getPicIco());
		return obj;
	}

	public static FunctionUnitForm functionUnitObj2Form(FunctionUnit obj) {
		FunctionUnitForm form = new FunctionUnitForm();
		form.setId(obj.getId());
		form.setInfo(obj.getInfo());
		form.setName(obj.getName());
		form.setOrd(obj.getOrd());
		form.setPicIco(obj.getPicIco());
		return form;
	}
	public static ModuleForm moduleObj2Form(Module obj) {
		ModuleForm form = new ModuleForm();
		form.setModule(obj.getModule());
		form.setFuncs(obj.getFuncs());
		form.setPic(obj.getPic());
		form.setName(obj.getName());
		form.setLink(obj.getLink());
		form.setInfo(obj.getInfo());
		form.setOrd(obj.getOrd());
		form.setId(obj.getId());
		return form;
	}

	public static Module moduleForm2Obj(ModuleForm form) {
		Module obj = new Module();
		obj.setModule(form.getModule());
		obj.setFuncs(form.getFuncs());
		obj.setPic(form.getPic());
		obj.setName(form.getName());
		obj.setLink(form.getLink());
		obj.setInfo(form.getInfo());
		obj.setOrd(form.getOrd());
		obj.setId(form.getId());
		return obj;
	}
	
	public static MachineForm machineObj2Form(Machine obj) {
		MachineForm form = new MachineForm();
		form.setAddtime(DateTransfer.dateToString(obj.getAddtime()));
		form.setId(obj.getId());
		form.setIp(obj.getIp());
		form.setMac(obj.getMac());
		form.setOrganid(obj.getOrganid());
		return form;
	}

	public static Machine machineForm2Obj(MachineForm form) {
		Machine obj = new Machine();
		obj.setAddtime(DateTransfer.stringToDate(form.getAddtime()));
		obj.setId(form.getId());
		obj.setIp(form.getIp());
		obj.setMac(form.getMac());
		obj.setOrganid(form.getOrganid());
		return obj;
	}
}
