package com.qtrmoon.toolkit.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.TableOrder;

import com.qtrmoon.common.Constant;

public class ChartTaglib extends TagSupport {
	private String vals;//��ֵ�������ŷָ�
	private String _kinds;//���മ�����ŷָ�
	private String title="";//����
	private String type="bar3d";//��ʽ��[bar3d,pie3d]
	private String style="";
	private String colors;//��ɫ����ָ����ʹ��jfreechartĬ��ɫ
	private int[][] color;//��ɫ��{{r,g,b},{r,g,b}}
	private int width=800,height=400;//��������ֵ
	
	public int doStartTag() {
		vals=vals.replaceAll(" ", vals);
		_kinds=_kinds.replaceAll(" ", _kinds);
		if(vals.endsWith(",")){
			vals=vals.substring(0,vals.length()-1);
		}
		if(_kinds.endsWith(",")){
			_kinds=_kinds.substring(0,_kinds.length()-1);
		}
		if(vals.startsWith(",")){
			vals=vals.substring(1);
		}
		if(_kinds.startsWith(",")){
			_kinds=_kinds.substring(1);
		}
		JFreeChart chart=null;
		if(type.equalsIgnoreCase("bar3d")){
			chart=createBar3dChart();
		}else if(type.equals("pie3d")){
			chart=createPie3dChart();
		}
		/*
		 * Write the chart image to the temporary directory
		 */
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		String filename = "";
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height, info,pageContext.getSession());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			JspWriter out = pageContext.getOut();
			out.print("<img src='/"+Constant.getConstant("projectName")+"/DisplayChart?filename="+filename+"' border='0' />");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}
	
	private JFreeChart createBar3dChart(){
		String[] values=vals.split(",");
		String[] kinds=_kinds.split(",");
		DefaultCategoryDataset barData = new DefaultCategoryDataset();
		for(int i=0;i<kinds.length; i++){
			barData.addValue(Double.parseDouble(values[i]), title, kinds[i]);
		}
		JFreeChart chart = ChartFactory.createBarChart3D("", "","", barData,
				PlotOrientation.VERTICAL, true, true, false);
		chart.getLegend().setPosition(RectangleEdge.LEFT);
		chart.getLegend().setVerticalAlignment(VerticalAlignment.TOP);
		// chart.getCategoryPlot().getRenderer().setBasePositiveItemLabelPosition(
		// new ItemLabelPosition(ItemLabelAnchor.OUTSIDE5,
		// TextAnchor.CENTER, TextAnchor.CENTER, 30d), true);

		Font font = new Font("����",Font.BOLD,14);
		CategoryPlot plot=chart.getCategoryPlot();
		// set up gradient paints for series...
		GradientPaint gp0 = new GradientPaint(0.1f, 0.1f, Color.blue, 0.0f,0.0f, Color.lightGray);
		GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, new Color(7798646),0.0f, 0.0f, Color.lightGray);
		GradientPaint gp2 = new GradientPaint(0.1f, 0.1f, Color.yellow, 0.0f,0.0f, Color.lightGray);
		GradientPaint gp3 = new GradientPaint(0.1f, 0.1f, new Color(16735838),0.0f, 0.0f, Color.black);
		CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);
		renderer.setSeriesPaint(3, gp3);
		//����
//		TextTitle subtitle=new TextTitle(title,font);
//		chart.setTitle(subtitle);
		//X�ᴦ��
		CategoryAxis domainAxis=plot.getDomainAxis();
		//domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//����x�����ֵĽǶ�
		domainAxis.setTickLabelFont(font);// ����x�������ϵ�����
		domainAxis.setLabelFont(font);//����X��ı�������
		//Y�ᴦ��
		NumberAxis numberaxis=(NumberAxis)plot.getRangeAxis();
		numberaxis.setTickLabelFont(font);//����Y�������ϵ�����
		numberaxis.setLabelFont(font);//����Y���������
		//ͼ������
		chart.getLegend().setItemFont(font);
		//��������
		plot.setBackgroundPaint(Color.white);//���񱳾�ɫ
		plot.setDomainGridlinePaint(Color.pink);//����������ɫ
		plot.setRangeGridlinePaint(Color.pink);//���������ɫ
		//��ʾÿ��������ֵ�����޸ĸ���ֵ����������
		//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);


		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);

		return chart;
	}
	
	private JFreeChart createPie3dChart() {
		String[] values=vals.split(",");
		String[] kinds=_kinds.split(",");
		DefaultPieDataset barData = new DefaultPieDataset();
		for(int i=0;i<kinds.length; i++){
			barData.insertValue(i, kinds[i], Double.parseDouble(values[i]));
		}
		Font font=new Font("����",Font.BOLD,14);
		JFreeChart chart = ChartFactory.createPieChart3D("", barData,true, true, false);
		chart.getLegend().setPosition(RectangleEdge.LEFT);
		chart.getLegend().setVerticalAlignment(VerticalAlignment.TOP);
		//����
//		TextTitle subtitle=new TextTitle(title,font);
		//chart.addSubtitle(new TextTitle("������",font));
//		chart.setTitle(subtitle);
		//ͼ��
		chart.getLegend().setItemFont(font);
		
		//����ٷֱ�
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}{1}({2})",NumberFormat.getNumberInstance(),new DecimalFormat("0.00%")));
		//����ɫ
		if(style.indexOf("background")>=0){
			
			int[] background=buildColor("#eeeeee");
			plot.setBackgroundPaint(new Color(background[0],background[1],background[2]));//���񱳾�ɫ
		}
		//ָ�� section ��ɫ��
		if(colors!=null&&!colors.equals("")){
			for(int i=0;i<kinds.length;i++){
				int idx=i%color.length;
				plot.setSectionPaint(kinds[i], new Color(color[idx][0],color[idx][1],color[idx][2]));
			}
		}
		plot.setLabelFont(font);

		return chart;
	}

	public String getKinds() {
		return _kinds;
	}

	public void setKinds(String _kinds) {
		this._kinds = _kinds;
	}

	public String getVals() {
		return vals;
	}

	public void setVals(String vals) {
		this.vals = vals;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		if(colors!=null&&!colors.equals("")){
			String[] cols=colors.split(",");
			color=new int[cols.length][];
			for(int i=0;i<cols.length;i++){
				color[i]=buildColor(cols[i]);
			}
		}
		this.colors = colors;
	}
	
	/**
	 * "#ffee00" to int[rgb]
	 * @param color
	 * @return
	 */
	private int[] buildColor(String color){
		color=color.replaceAll("#", "");
		return new int[]{
				Integer.parseInt(color.substring(0,2), 16),
				Integer.parseInt(color.substring(2,4), 16),
				Integer.parseInt(color.substring(4,6), 16)};
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
