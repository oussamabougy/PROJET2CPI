package com.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;


public class ChartView {
	
	protected Date debut,fin;
	
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Date getDebut() {
		return debut;
	}
	public Date getFin() {
		return fin;
	}


	//Dessinger les chart
	public void defineChart() 
	{
		//System.out.print(getRole(1000001));
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String inputString1 = "2017-02-20";
		 String inputString2 = "2017-02-28";
		 Date d1 = null,d2 = null;
		 try {
		     d1 = myFormat.parse(inputString1);
		     d2 = myFormat.parse(inputString2);
		 } catch (ParseException e) {
		     e.printStackTrace();
		 }
		 if(debut != null && fin != null)
		 {
			ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,debut, fin);
			ArrayList<Chart> charts1 = tauxAbsenceEmployeeJourParHeur(matricule,debut, fin);
			ArrayList<Chart> charts2 = tauxAbscenceCumuleEmployeJour(matricule,debut, fin);
			abs = loadAbs(charts) ;
			abs1 = loadAbs(charts1);
			abs2 = loadAbsBar(charts2);
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
		
		//System.out.print(debut+" "+fin);
	}

	
	protected LineChartModel abs = new LineChartModel() ;

	protected LineChartModel abs1 = new LineChartModel() ;
	
	protected BarChartModel abs2 = new BarChartModel() ;
	
	
	
	public BarChartModel getAbs2() {
		return abs2;
	}
	public void setAbs2(BarChartModel abs2) {
		this.abs2 = abs2;
	}
	public LineChartModel getAbs1() {
		return abs1;
	}
	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
	public LineChartModel getAbs() {
		return abs;
	}

	public void setAbs(LineChartModel abs) {
		this.abs = abs;
	}
	
	public LineChartModel loadAbs(ArrayList<Chart> charts){
		
        LineChartModel model = new LineChartModel();
        
        model.setTitle("Category Chart");
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2017-02-30");
        axis.setTickFormat("%b %#d, %y");
         
        model.getAxes().put(AxisType.X, axis);
        //Axis yAxis = model.getAxis(AxisType.Y);
        //yAxis.setLabel("Births");
        //yAxis.setMin(0);
        //yAxis.setMax(10);
        
        
        ChartSeries series = new ChartSeries();
        
        series.setLabel("Series 1");
        
        for(Chart chart : charts)        	
        	series.set(chart.getDate(), chart.getTaux_absence());
        
        model.addSeries(series);
		
		
        return model;
	}
	
	
	
	public BarChartModel loadAbsBar(ArrayList<Chart> charts){
		
		BarChartModel model = new BarChartModel();
        
        model.setTitle("Category Chart");
        model.setLegendPosition("ne");
        model.setShowPointLabels(true);
        /*model.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2017-02-30");
        axis.setTickFormat("%b %#d, %y");
         
        model.getAxes().put(AxisType.X, axis);*/
        
        
        //Axis yAxis = model.getAxis(AxisType.Y);
        //yAxis.setLabel("Births");
        //yAxis.setMin(0);
        //yAxis.setMax(10);
        
        
        ChartSeries series = new ChartSeries();
        
        series.setLabel("Series 1");
        
        for(Chart chart : charts)        	
        	series.set(chart.getDate(), chart.getTaux_absence());
        
        model.addSeries(series);
		
		
        return model;
	}


}
