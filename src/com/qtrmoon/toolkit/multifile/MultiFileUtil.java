package com.qtrmoon.toolkit.multifile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.qtrmoon.toolkit.FileUpDownUtil;

/**
 * @author JavaMao
 * 批量上传图片的代理方法。
 * 具体使用方法如下：
 * 1、需要上传多附件的表单Form必须继承FileUpForm。
 * 2、在处理该表单的Action中调用MultiFileUtil.upMultiFile()方法，传入表单form和存储文件的路径。
 * 3、页面中使用<pageFmt:multiFile showFileInfo="true"/>来引入上传文件部分HTML和JS，例如：
 * <tr>
 * 	<td valign="top">
 * 		附件：
 * 	</td>
 * 	<td>
 * 		<pageFmt:multiFile showFileInfo="true"/>
 * 	</td>
 * </tr>
 *
 */
public class MultiFileUtil {
	/**
	 * @param form 继承了FileUpForm的表单Form。
	 * @param rootPath 存储文件的路径 
	 * @return 返回[文件描述，带扩展名的物理文件名]
	 */
	public static List<FileInfo> upMultiFile(FileUpForm form,String rootPath){
		List<String> fileNameList=Arrays.asList(form.getFileNames());
		List<FormFile> fileList=form.getReferenceFileList();
		List<FileInfo> infoList=new ArrayList<FileInfo>();
		for(int i=0;i<fileList.size();i++){
			FormFile f=fileList.get(i);
			String fileName=fileNameList.get(i);
			if(fileName==null||fileName.equals("")){
				fileName=f.getFileName();
			}
			if(f.getFileName()!=null&&!f.getFileName().equals("")){
				String filePath=FileUpDownUtil.uploadFile(f, rootPath);
				infoList.add(new FileInfo(filePath,fileName));
			}
		}
		System.out.println(fileList.size());
		return infoList;
	}
}
