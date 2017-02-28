package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.chart.LineChartModel;

@ManagedBean
@SessionScoped
public class ChefService extends Employe implements LienDirChefSer {
	
	
	private LineChartModel abs1 = new LineChartModel() ;

	public LineChartModel getAbs1() {
		return abs1;
	}
	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
	
	ArrayList<String> services = defineService();
	
	
	public ArrayList<String> getServices() {
		return services;
	}
	public void setServices(ArrayList<String> services) {
		this.services = services;
	}
	
	public ArrayList<String> defineService()
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<String>	serviceList=	new	ArrayList<String>();
        connexion = Database.loadDatabase();        
       
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT nom_service  FROM service");
        	resultat = statement.executeQuery();
    		while(resultat.next())
    		{
    			serviceList.add(resultat.getString("nom_service"));
    		}

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	serviceList;
	}

	public ArrayList<Integer> employeeDeService(String service_nom)
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        int	id_service=0;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT id  FROM service WHERE nom_service  = ?;");
        	statement.setString(1,service_nom);
        	resultat = statement.executeQuery();
    		if(resultat.next())
    		{
    			id_service = resultat.getInt("id");
    		}
	///////

    		//chercherLesEployee

    		statement =connexion.prepareStatement("SELECT matricule  FROM employee WHERE service_id = ?;");
    		statement.setInt(1,id_service);
    		resultat=statement.executeQuery();

    		//replirLaListe
       		while(resultat.next())
       		{
     			listMatricule.add(resultat.getInt("matricule"));	  
       	   	 }
		//////

       		//ecrireDansLaConsole
       		for	(int	i=0;	i<listMatricule.size();i++)
       		{
       	     	System.out.print(listMatricule.get(i));
       		}
   	    	/////

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	listMatricule;
	}

	public	ArrayList<Chart>	tauxAbsenceServiceJour(String service_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(service_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceService = tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceService.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceService.get(k).getTaux_absence());
				absenceService.set(k,ch);
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employee	de	ce	service
		
		for	(int	k=0;k<absenceService.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence((float)absenceService.get(k).getTaux_absence()/(float)size);
			absenceService.set(k,ch);
			
			
		}	
		return	absenceService;
	}
	public void defineChart1() 
	{
			System.out.print(debut);
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
		 if(debut != null && fin != null && nomService!=null)
		 {
			ArrayList<Chart> charts = tauxAbsenceServiceJour(nomService,debut, fin);
			abs1 = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
	}
}
