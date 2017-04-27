package com.qtrmoon.manualsign;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import org.apache.struts.util.ImageButtonBean;

public class SignPanel extends Applet {
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() {
		this.setLayout(null);
		int width=500,height=200;
//		String widthStr=getParameter("width");
//		String heightStr=getParameter("height");
//		if(widthStr!=null){
//			width=Integer.parseInt(widthStr);
//		}
//		if(heightStr!=null){
//			height=Integer.parseInt(heightStr);
//		}
		this.setLayout(new BorderLayout());
		this.add(new DrawPanel(width,height),BorderLayout.CENTER);
		System.out.println(width+":"+height);
		super.init();
	}

	

	@Override
	public void start() {
		super.start();
	}

	class DrawPanel extends Panel implements MouseListener,MouseMotionListener{
		private Graphics2D g;
		private int ox,oy,ost,max=10;
		private Stroke[] st;
		private BufferedImage bi;
		
		public DrawPanel(int width,int height){
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			bi=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			g=bi.createGraphics();
			g.setColor(new Color(0,0,0));
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			st=new Stroke[max];
			for(int i=0;i<max;i++){
				st[i]=new BasicStroke(max-i,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
			}
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(bi, 0, 0, this);
		}

		private void drawStroke(int x,int y) {
			if(Math.abs(ox-x)+Math.abs(oy-y)>=1){
				int s1=Math.abs(ox-x);
				int s2=Math.abs(oy-y);
				int s=(int)Math.sqrt(s1*s1+s2*s2);
				s=s/2;
				if(s>=max){
					s=max-1;
				}
				if(g==null){
					g=(Graphics2D)this.getGraphics();
				}
				//²åÖµ
				int chaz=Math.abs(ost-s);
				if(chaz>1){
					int cx=(x-ox)/chaz;
					int cy=(y-oy)/chaz;
					int add=(s-ost)/chaz;
					for(int i=0;i<chaz-1;i++){
						int currst=ost+add;
						g.setStroke(st[currst]);
						g.drawLine(ox, oy, ox+cx, oy+cy);
						ox=ox+cx;
						oy=oy+cy;
						ost=currst;
					}
				}
				g.setStroke(st[s]);
				g.drawLine(ox, oy, x, y);
				ox=x;
				oy=y;
				ost=s;
			}
			repaint();
		}
		
		public void mouseClicked(MouseEvent e) {
		}
		
		public void mouseEntered(MouseEvent e) {
		}
		
		public void mouseExited(MouseEvent e) {
		}
		
		public void mousePressed(MouseEvent e) {
			ox=e.getX();
			oy=e.getY();
			ost=1;
		}
		
		public void mouseReleased(MouseEvent e) {
		}
		
		
		public void mouseDragged(MouseEvent e) {
			int x=e.getX();
			int y=e.getY();
			drawStroke(x,y);
		}
		
		public void mouseMoved(MouseEvent e) {
		}
	}
}