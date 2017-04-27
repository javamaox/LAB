package com.qtrmoon.toolkit.multilargefile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.qtrmoon.common.Constant;
import com.qtrmoon.toolkit.DateTransfer;

/**
 * @author JavaMao
 * �н������������ϴ�ͼƬ�Ĵ�������ʹ����dwr���ϴ����������struts�Ļ��Ʋ�����ߴ��ļ����߶�ȡ�����ʹ��Filter����struts����������ǰ�洢�ļ���
 * 1����Web.xml�����ô󸽼��ϴ���Filter��ע�ⲻ��Servlet��Ϊ��ʵ����ActionForm�ļ̳й�û��ʹ��Filter��
 * ���ʹ��struts��form��Ҫ���������������ã�
 * 2����Ҫ�ϴ��฽���ı�Form����̳�LFileUpForm��
 * 3��ҳ����ʹ��<pageFmt:multiLargeFile/>�������ϴ��ļ�����HTML��JS��������formName��formClass������formName��struts-config�����õ�form-bean��name��
 *    formClass��form-bean��type���ԡ�
 * ��ʹ����ActionForm���Ὣ�������õ�form�У���û������ActionForm��ͨ��request���л�ȡ��
 */
public class LFileUpFilter implements Filter {

	static final String UPLOAD_INFO = "UPLOAD_INFO";
	

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		String multiLargeFile = request.getParameter("multiLargeFile");
		if(multiLargeFile!=null&&!multiLargeFile.equals("")){
			HttpSession session=request.getSession();
			String formName = request.getParameter("form_Name");
			String formClass = request.getParameter("form_Class");
			String basePath = request.getParameter("base_Path");
			String basePathId = request.getParameter("base_PathId");
			if(basePath==null||basePath.equals("")||basePath.equals("null")){
				basePath=Constant.getConstant(basePathId);
			}
			if(basePath==null||basePath.equals("")||basePath.equals("null")){
				basePath="c:\\";
			}
			
			if("submit".equals(multiLargeFile)){//�ϴ��ļ�
				List<String> filePaths=new ArrayList<String>();
				session.removeAttribute(UPLOAD_INFO);
				UploadListener listener = new UploadListener(request, 0);
			    FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
			    ServletFileUpload upload = new ServletFileUpload(factory);
			    Map<String,String> valMap=new HashMap<String,String>();
			    try{
			        List list =upload.parseRequest(request);
			        Iterator it =list.iterator();
					while(it.hasNext()){
						FileItem fi =(FileItem) it.next();
						if(fi.isFormField()){
							valMap.put(fi.getFieldName(), fi.getString("GB2312"));
						}else{
							if(fi.getName()==null || fi.getSize()==0){
								System.out.println("ISNULL  getName:   "+fi.getName());
								continue;
							}
							String fileName=fi.getName();
							if(fileName.indexOf(".")>0){
								fileName=fileName.substring(fileName.lastIndexOf("."));
							}
							fileName=DateTransfer.timeToId()+fileName;
							filePaths.add(fileName);
							File f =new File(basePath+"/"+fileName);
							fi.write(f);
						}
					}
			    }catch(Exception e){
			    	
			    }
			    String[] fpArr=new String[filePaths.size()];
			    filePaths.toArray(fpArr);
			    RequestWrapper reqWarpper=new RequestWrapper(request,fpArr,valMap);
			    if(formName!=null&&formClass!=null&&!formName.equals("")&&!formClass.equals("")){//�趨��ActionForm�򣬹���Form����ֵ��
			    	try {
						Class classModel=Class.forName(formClass);
						Object form=classModel.newInstance();
						Method[] methods=classModel.getMethods();
						String param;
						for(Method method:methods){
							if(method.getName().startsWith("set")&&method.getReturnType().equals(void.class)&&method.getParameterTypes().length==1){//����setter����
								param=method.getName().substring(3);
								param=(param.charAt(0)+"").toLowerCase()+param.substring(1);//��λתСд
								if(method.getParameterTypes()[0].equals(String.class)){
									method.invoke(form, reqWarpper.getParameter(param));
								}else if(method.getParameterTypes()[0].equals(String[].class)){
									String[] val=reqWarpper.getParameterValues(param);
									if(val==null){//ҳ��ֻ��һ�и��������
										if(reqWarpper.getParameter(param)!=null){
											val=new String[]{reqWarpper.getParameter(param)};
										}else{
											val=new String[0];
										}
									}
									method.invoke(form, new Object[]{val});//�����Ƕ�ά���ݾͽ������
								}
							}
						}
						reqWarpper.setAttribute(formName, form);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			    chain.doFilter(reqWarpper, response);
			}else if("finish".equals(multiLargeFile)){//����ļ��ϴ�
				session.removeAttribute(UPLOAD_INFO);
				return;
			}else if("percent".equals(multiLargeFile)){//��ȡ�ϴ�����
				String res;
				Object o=session.getAttribute(UPLOAD_INFO);
				if(o!=null){
					ProgressInfo upi=(ProgressInfo)o;
					res="{totalSize:"+upi.getTotalSize()+",bytesRead:"+upi.getBytesRead()+"}";
				}else{
					res="{totalSize:100,bytesRead:0}";
				}
				response.getWriter().print(res);
				return;
			}
		}else{
			chain.doFilter(req, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("LFileUpFilter init");
	}


}
