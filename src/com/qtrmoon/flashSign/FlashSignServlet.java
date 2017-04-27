package com.qtrmoon.flashSign;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qtrmoon.toolkit.FileUpDownUtil;

public class FlashSignServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FlashSignServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String colors=request.getParameter("colors");
		String act=request.getParameter("act");
		if("create".equals(act)){
			String[] arr=colors.split(",");
			int width=Integer.parseInt(arr[0]);
			int height=Integer.parseInt(arr[1]);
			colors=arr[2];
			BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g=bi.createGraphics();
			//g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.setColor(Color.white);
			g.fillRect(0,0,width,height);
			g.setColor(Color.black);
			
			for (int j = 0; j<height; j++) {
				for (int i = 0; i<width; i++) {
					if(colors.charAt(j*width+i)=='0'){
						//g.drawImage(dot,i,j,null);
						g.drawLine(i-1,j-1,i,j);
					}
				}
			}
			File f=File.createTempFile("sign",".png");
			String path=f.getAbsolutePath();
			FileOutputStream fout=new FileOutputStream(f);
			ImageIO.write(bi,"png",fout);
			fout.close();
			request.getSession().setAttribute("SIGN_PATH",path);
			response.getWriter().print("success");
		}else if("view".equals(act)){
			String path=(String)request.getSession().getAttribute("SIGN_PATH");
			FileUpDownUtil.fileDown(path,response);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
