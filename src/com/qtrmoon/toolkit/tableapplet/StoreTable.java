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
 * �ִ���һ���Ρ���ͨ�ͻ��������ı��
 * 
 * @author ������
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
	 * ��ȡ��һ��ʹ�ñ����뵽ScrollPane��rowheader��ʹ����Ϊ�����ͷ
	 * 
	 * @param rowNames
	 * @return
	 */
	private static JViewport createRowHeader(String[] rowNames) {
		String[][] data = new String[rowNames.length][1];
		for (int i = 0; i < rowNames.length; i++) {
			data[i][0] = rowNames[i];
		}
		// ��ͷ���ȡһ������JViewport���ǲ�����ʾ��
		JTable table = new JTable(data, new String[] { "aa" });
		table.setEnabled(false);
		table.setBackground((Color) UIManager.get("TableHeader.background"));
		table.setRowHeight(20);
		JViewport view = new JViewport();
		view.setView(table);
		view.setPreferredSize(new Dimension(60, 54));
		return view;
	}
	// ��ͷ������*********************************************************************

	/**
	 * 
	 * @param headStr ��ͷ
	 * @param dataStr ����
	 * @param staticHead ��ͷ
	 * @param staticData ������
	 * @return
	 */
	public StoreTable(String headStr, String dataStr,String staticHead,String staticData) {
		if(headStr==null||headStr.equals("")){
			headStr="1,����,0!2,��������,0!3,�ͻ�����,0!4,�տ����,0!5,�ͻ�,0!6,����,5!7,����,5!8,���ص���,0!9,����,8!10,����,9!11,����,9!12,����,8!13,����,12!14,����,12!15,ҵ��Ա,0!16,����Ա,0!17,�����,0";
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
			}else{//���б�ͷ,���ŷָ
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
		this.getTableHeader().setReorderingAllowed(false);//��ֹ��ק��
		//this.getTableHeader().setResizingAllowed(false);//��ֹ�ı��п�
		
		jsp=new JScrollPane(this);
		if(staticHead!=null||!staticHead.equals("")){
			jsp.setRowHeader(createRowHeader(staticData.split(",")));
			HeadLabel corner = new HeadLabel(staticHead);
			corner.setBackground((Color) UIManager.get("TableHeader.background"));
			jsp.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
		}
	}
	
	/**
	 * ������������
	 * @return
	 */
	private List<Node> formatHead(List<Node> head){
		//��������ṹ
		for(Node node1:head){
			for(Node node2:head){
				if(node2.pid.equals(node1.id)){
					node1.addChild(node2);
					node2.pnode=node1;
				}
			}
		}
		//��ȡ��
		List<Node> roots=new ArrayList<Node>();
		for(Node node:head){
			if(node.pnode==null){
				roots.add(node);
			}
		}
		//��������Ե���ֱ�ӽ�����һ����
		if(roots.size()==1&&roots.get(0).children.size()>1){
			roots=roots.get(0).children;
		}
		//������ڵ�Ĳ���
		setLevel(roots,0);
		//������㼶
		for(Node node:head){
			if(maxLevel<node.level){
				maxLevel=node.level;
			}
		}
		//����x����
		int[] lineX=new int[maxLevel+1];
		setXIndex(roots,lineX);
		//������ڵ���colspan.
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
	 * �趨��Ԫ���x����
	 * @param children
	 * @param lineX
	 */
	private void setXIndex(List<Node> children,int[] lineX) {
		for(Node node:children){
			//����λ��
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
					new Dimension(StoreTable.this.getWidth(), (maxLevel+1)*HEAD_H));// ���ñ�ͷ��С������������㹻��
			// �������ֻ��Ʋ���ȫ�Լ���˸����

			buildHead(roots);
		}

		private void paintComponent(Node node, int x, int y, int w, int h) {
			rendererPane.paintComponent(g,node.component,header,x,y,w,h,true);
		}

		private void buildHead(List<Node> children) {
			for(Node node:children){
				//�����и�
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
				//����λ��
				paintComponent(node, 
						getX(node.xIndex), node.level*HEAD_H,
						w,h);
				buildHead(node.children);
			}
			
		}

		// �õ�ָ���е���ʼ����

		private int getX(int column) {
			int x = 0;
			for (int i = 0; i < column; i++)
				x += header.getColumnModel().getColumn(i).getWidth();
			return x;
		}

		// �õ�ָ���еĿ��

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
		//level:���Ĳ㼶���൱��y����λ��
		//xIndex:��Ҷ�ڵ������x����λ��
		//colspan:��Ԫ�����Ҷ�ڵ���
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
		
		frame.getContentPane().add(new StoreTable("", null,"�༶","����,����,����").getScrollPanel());
		frame.setVisible(true);
	}
	public JScrollPane getScrollPanel() {
		return jsp;
	}

}
