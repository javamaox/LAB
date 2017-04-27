package com.qtrmoon.toolkit.multifile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.qtrmoon.toolkit.FileUpDownUtil;

/**
 * @author JavaMao
 * �����ϴ�ͼƬ�Ĵ�������
 * ����ʹ�÷������£�
 * 1����Ҫ�ϴ��฽���ı�Form����̳�FileUpForm��
 * 2���ڴ���ñ���Action�е���MultiFileUtil.upMultiFile()�����������form�ʹ洢�ļ���·����
 * 3��ҳ����ʹ��<pageFmt:multiFile showFileInfo="true"/>�������ϴ��ļ�����HTML��JS�����磺
 * <tr>
 * 	<td valign="top">
 * 		������
 * 	</td>
 * 	<td>
 * 		<pageFmt:multiFile showFileInfo="true"/>
 * 	</td>
 * </tr>
 *
 */
public class MultiFileUtil {
	/**
	 * @param form �̳���FileUpForm�ı�Form��
	 * @param rootPath �洢�ļ���·�� 
	 * @return ����[�ļ�����������չ���������ļ���]
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
