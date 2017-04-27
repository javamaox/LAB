package com.qtrmoon.toolkit.multilargefile;

import com.qtrmoon.common.PageForm;

public class LFileUpForm extends PageForm {
	private String[] fileNames;

	private String[] filePaths;

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileName) {
		this.fileNames = fileName;
	}

	public String[] getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(String[] filePaths) {
		this.filePaths = filePaths;
	}

}
