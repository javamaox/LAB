package com.qtrmoon.dictEditor.action;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.qtrmoon.common.Constant;
import com.qtrmoon.dictEditor.beanSerDao.DictionaryForm;
import com.qtrmoon.toolkit.DateTransfer;
import com.qtrmoon.toolkit.FileUpDownUtil;
import com.qtrmoon.toolkit.SysDbUtil;

public class DictTransferAction extends DispatchAction {
	private static final String FORWARD_importDmp = "import";
	
	public DictTransferAction(){
		super();
		new SysDbUtil();
	}
	
	public ActionForward exportDmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String db_filepath=Constant.getConstant("dictCfg")+"data/";
		while(db_filepath.indexOf("//")>0){
			db_filepath=db_filepath.replaceAll("\\/\\/", "\\/");
		}
		db_filepath=db_filepath.replaceAll("\\/", "\\\\");
		String fileName=db_filepath+DateTransfer.timeToId();
		//Ö´ÐÐ
		String bat="echo off\r\n";
		bat+="e:\r\n";
		bat+="cmd /c start E:\\oracle\\product\\10.2.0\\db_1\\BIN\\exp "+SysDbUtil.db_user+"/"+SysDbUtil.db_pwd+"@"+SysDbUtil.db_sid+"  file=\""+fileName+".DMP\" log=\""+fileName+"export.LOG\" compress=n\r\n";
		bat+=" \r\n";
		try {
			FileUpDownUtil.fileUp(db_filepath, "export.bat", bat);
			
			BufferedInputStream bis=null;
			InputStream is=null;
			System.out.println("cmd /c "+db_filepath+"export.bat");
			Process process = Runtime.getRuntime().exec("cmd /c \""+db_filepath+"export.bat\"");
			is = process.getInputStream();
			bis = new BufferedInputStream(is);
			byte[] b=new byte[1024];
			int length=1024;
			String feedback;
			while((length=bis.read(b))>0){
				feedback=new String(Arrays.copyOf(b, length));
				System.out.println(feedback);
			}
			bis.close();
			is.close();
			FileUpDownUtil.fileDown(fileName+".dmp", response);
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		return null;
	}

	public ActionForward importDmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DictionaryForm df=(DictionaryForm)form;
		String db_filepath=Constant.getConstant("dictCfg")+"data/";
		String dmpFileName=FileUpDownUtil.uploadFile(df.getFile(), db_filepath);
		
		//Ö´ÐÐ
		
		return null;
	}
	
}
