package com.qtrmoon.toolkit.multifile;

public class FileInfo{
	String filePath;//文件路径（实际只有存于服务器上的文件名）
	String fileName;//用户提交时写得文件描述。
	
	public FileInfo(String filePath, String fileName) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
	}
	/**
	 * 获取用户提交时写得文件描述
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取存于服务器上的文件名
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
