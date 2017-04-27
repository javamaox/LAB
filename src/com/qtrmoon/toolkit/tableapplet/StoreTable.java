package com.qtrmoon.toolkit.tableapplet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/*******************************************************************************
 * 
 * 仓储（一日游、普通客户）操作的表格。
 * 
 * @author 胡海波
 * 
 * 
 * 
 */

public class StoreTable extends JTable {
	
	private static Font font=new Font("",Font.PLAIN,12);
	private static final int HEAD_H=20;
	private int maxLevel=0;
	private JScrollPane jsp;
	
	/**
	 * 获取到一个使用表格加入到ScrollPane的rowheader，使其作为表的行头
	 * 
	 * @param rowNames
	 * @return
	 */
	private static JViewport createRowHeader(String[] rowNames) {
		String[][] data = new String[rowNames.length][1];
		for (int i = 0; i < rowNames.length; i++) {
			data[i][0] = rowNames[i];
		}
		// 列头随便取一个，在JViewport里是不能显示的
		JTable table = new JTable(data, new String[] { "aa" });
		table.setEnabled(false);
		table.setBackground((Color) UIManager.get("TableHeader.background"));
		table.setRowHeight(20);
		JViewport view = new JViewport();
		view.setView(table);
		view.setPreferredSize(new Dimension(60, 54));
		return view;
	}
	// 表头绘制器*********************************************************************

	/**
	 * 
	 * @param headStr 表头
	 * @param dataStr 数据
	 * @param staticHead 行头
	 * @param staticData 行数据
	 * @return
	 */
	public StoreTable(String headStr, String dataStr,String staticHead,String staticData) {
		if(headStr==null||headStr.equals("")){
			headStr="1,分类,0!2,到货日期,0!3,送货日期,0!4,收款代理,0!5,客户,0!6,上游,5!7,下游,5!8,报关单号,0!9,上游,8!10,进口,9!11,出口,9!12,下游,8!13,进口,12!14,出口,12!15,业务员,0!16,操作员,0!17,审核人,0";
		}
		List<Node> head=new ArrayList<Node>();
		List<String[]> datas=new ArrayList<String[]>();

		if(headStr!=null&&!headStr.equals("")){
			if(headStr.indexOf("!")>0){
				String[] headArr=headStr.split("!");
				for(String nodeStr:headArr){
					String[] nodeArr=nodeStr.split(",");
					head.add(new Node(nodeArr[0],nodeArr[1],nodeArr[2]));//id,label,pid
				}
			}else{//单行表头,逗号分割。
				String[] headArr=headStr.split(",");
				for(String nodeStr:headArr){
					head.add(new Node("x",nodeStr,"0"));//id,label,pid
				}
			}
		}
		List<Node> roots=formatHead(head);
		this.getTableHeader().setUI(new Head(roots));
		
		if(dataStr!=null&&!dataStr.equals("")){
			String[] dataArr=dataStr.split("!");
			for(String oneRes:dataArr){
				datas.add(oneRes.split(","));
			}
		}else{
			int leafNum=0;
			for(Node node:head){
				if(node.children.size()==0){
					leafNum++;
				}
			}
			String[] oneData=new String[leafNum];
			for(int i=0;i<leafNum;i++){
				oneData[i]=i+"";
			}
			for(int i=0;i<100;i++){
				datas.add(oneData);
			}
		}
		
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setModel(new MTableModel(datas,datas.get(0)));
		this.setRowHeight(20);
		this.getTableHeader().setReorderingAllowed(false);//禁止拖拽列
		//this.getTableHeader().setResizingAllowed(false);//禁止改变行宽
		
		jsp=new JScrollPane(this);
		if(staticHead!=null||!staticHead.equals("")){
			jsp.setRowHeader(createRowHeader(staticData.split(",")));
			HeadLabel corner = new HeadLabel(staticHead);
			corner.setBackground((Color) UIManager.get("TableHeader.background"));
			jsp.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
		}
	}
	
	/**
	 * 构建单链层树
	 * @return
	 */
	private List<Node> formatHead(List<Node> head){
		//构造层树结构
		for(Node node1:head){
			for(Node node2:head){
				if(node2.pid.equals(node1.id)){
					node1.addChild(node2);
					node2.pnode=node1;
				}
			}
		}
		//提取根
		List<Node> roots=new ArrayList<Node>();
		for(Node node:head){
			if(node.pnode==null){
				roots.add(node);
			}
		}
		//单根则忽略单根直接进入下一级。
		if(roots.size()==1&&roots.get(0).children.size()>1){
			roots=roots.get(0).children;
		}
		//查出各节点的层数
		setLevel(roots,0);
		//查出最大层级
		for(Node node:head){
			if(maxLevel<node.level){
				maxLevel=node.level;
			}
		}
		//设置x索引
		int[] lineX=new int[maxLevel+1];
		setXIndex(roots,lineX);
		//计算各节点跨度colspan.
		setColspan(roots);
		return roots;
	}
	
	private int setColspan(List<Node> children) {
		if(children.size()==0){
			return 1;
		}
		int colspan=0;
		for(Node node:children){
			node.colspan=setColspan(node.children);
			colspan+=node.colspan;
		}
		return colspan;
	}

	private void setLevel(List<Node> children,int level) {
		if(children!=null){
			for(Node node:children){
				node.level=level;
				setLevel(node.children,level+1);
			}
		}
	}
	
	/**
	 * 设定单元格的x索引
	 * @param children
	 * @param lineX
	 */
	private void setXIndex(List<Node> children,int[] lineX) {
		for(Node node:children){
			//计算位置
			node.xIndex=lineX[node.level];
			if(node.children.size()==0){
				for(int i=0;i<lineX.length;i++){
					lineX[i]+=1;
				}
			}
			setXIndex(node.children,lineX);
		}
		
	}
	
	private class Head extends BasicTableHeaderUI {

		private JTableHeader header;
		List<Node> roots;
		Graphics g;
		
		public Head(List<Node> head){
			this.roots=head;
		}

		public void paint(Graphics g, JComponent c) {
			this.g=g;
			header = (JTableHeader) c;

			getTableHeader().setPreferredSize(
					new Dimension(StoreTable.this.getWidth(), (maxLevel+1)*HEAD_H));// 设置表头大小。横坐标必须足够大，
			// 否则会出现绘制不完全以及闪烁现象

			buildHead(roots);
		}

		private void paintComponent(Node node, int x, int y, int w, int h) {
			rendererPane.paintComponent(g,node.component,header,x,y,w,h,true);
		}

		private void buildHead(List<Node> children) {
			for(Node node:children){
				//计算行高
				int h=0;
				if(node.children.size()>0){
					h=HEAD_H;
				}else{
					h=(maxLevel-node.level+1)*HEAD_H;
				}
				int w=getWidth(node.xIndex);
				for(int i=1;i<node.colspan;i++){
					w+=getWidth(node.xIndex+i);
				}
				//计算位置
				paintComponent(node, 
						getX(node.xIndex), node.level*HEAD_H,
						w,h);
				buildHead(node.children);
			}
			
		}

		// 得到指定列的起始坐标

		private int getX(int column) {
			int x = 0;
			for (int i = 0; i < column; i++)
				x += header.getColumnModel().getColumn(i).getWidth();
			return x;
		}

		// 得到指定列的宽度

		private int getWidth(int column) {
			return header.getColumnModel().getColumn(column).getWidth();
		}

	}
	
	private class HeadLabel extends JLabel{
		private Color color=new Color(150,50,50);
		public HeadLabel(String l){
			super(l,JLabel.CENTER);
			this.setFont(font);
			this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		}
		
		public void paints(Graphics g) {
			super.paint(g);
			g.setColor(color);
			g.fillRect(1, 1, getWidth()-2, getHeight()-2);
			g.setColor(new Color(50,150,50));
			g.drawString(getText(), 0, 10);
		}
		
		
	}

	private class MTableModel extends AbstractTableModel {
		private List<String[]> rowData;
		private String[] columnNames;
		
		public MTableModel(List<String[]> data,String[] columnNames){
			this.rowData=data;
			this.columnNames=columnNames;
		}

		public String getColumnName(int column) {return columnNames[column];}
	    public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
	    public boolean isCellEditable(int row, int col) {if(col!=0){return true;}return false;}
	    public void setValueAt(Object aValue, int row, int column) {
	    	rowData.get(row)[column]=(String)aValue;
	    }
	    
		public int getColumnCount() {
			return rowData.get(0).length;
		}

		public int getRowCount() {
			return rowData.size();
		}

		public Object getValueAt(int row, int col) {
			return rowData.get(row)[col];
		}
	}
	
	private class Node{
		String id,pid,label;
		List<Node> children=new ArrayList<Node>();
		Node pnode;
		HeadLabel component;
		//level:树的层级，相当于y索引位置
		//xIndex:按叶节点排序的x索引位置
		//colspan:单元格的子叶节点跨度
		int level,xIndex,colspan;
		
		public Node(String id,String label,String pid){
			this.id=id;
			this.label=label;
			this.pid=pid;
			component=new HeadLabel(label);
		}
		
		public void addChild(Node node){
			children.add(node);
		}
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 300);
		frame.setDefaultCloseOperation(3);
		
		frame.getContentPane().add(new StoreTable("", null,"班级","张三,李四,王二").getScrollPanel());
		frame.setVisible(true);
	}
	public JScrollPane getScrollPanel() {
		return jsp;
	}

}
