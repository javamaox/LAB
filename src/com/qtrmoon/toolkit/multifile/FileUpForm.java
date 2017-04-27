package com.qtrmoon.toolkit.multifile;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.qtrmoon.common.PageForm;

public class FileUpForm extends PageForm {
	private String[] fileNames;

	private ArrayList<FormFile> referenceFileList = new ArrayList<FormFile>();// ----------¸½¼þÊý×é

	public ArrayList getReferenceFileList() {
		return referenceFileList;
	}

	public void setReferenceFileList(ArrayList<FormFile> referenceFileList) {
		this.referenceFileList = referenceFileList;
	}

	public FormFile getReferenceFormFile(String index) {
		return (FormFile) this.referenceFileList.get(Integer.parseInt(index));
	}

	public void setReferenceFormFile(String index, FormFile formFile) {
		this.referenceFileList.add(formFile);
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileName) {
		this.fileNames = fileName;
	}

}
