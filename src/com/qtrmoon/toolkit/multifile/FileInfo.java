package com.qtrmoon.toolkit.multifile;

public class FileInfo{
	String filePath;//�ļ�·����ʵ��ֻ�д��ڷ������ϵ��ļ�����
	String fileName;//�û��ύʱд���ļ�������
	
	public FileInfo(String filePath, String fileName) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
	}
	/**
	 * ��ȡ�û��ύʱд���ļ�����
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * ��ȡ���ڷ������ϵ��ļ���
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
