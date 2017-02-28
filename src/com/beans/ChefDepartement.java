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
public class ChefDepartement extends EmployeGeneral implements LienDirChefDep {
	private String nomDepartement;
	
	
	
	public String getNomDepartement() {
		return nomDepartement;
	}
	public void setNomDepartement(String nomDepartement) {
		this.nomDepartement = nomDepartement;
	}

	private LineChartModel abs1 = new LineChartModel() ;

	public LineChartModel getAbs1() {
		return abs1;
	}
	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
	
	ArrayList<String> departements = defineDepartement();
	
	
	public ArrayList<String> getDepartements() {
		return departements;
	}
	public void setDepartements(ArrayList<String> departements) {
		this.departements = departements;
	}
	
	public ArrayList<String> defineDepartement()
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<String>	departementList=	new	ArrayList<String>();
        connexion = Database.loadDatabase();        
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT nom_departement  FROM departement");
        	resultat = statement.executeQuery();
    		while(resultat.next())
    		{
    			departementList.add(resultat.getString("nom_departement"));
    		}

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	departementList;
	}
	
	public ArrayList<Integer> employeeDeDepartement(String departement_nom)

	{
		PreparedStatement statement;
        ResultSet resultat;
        int	id_departement=0;
        Connection connexion = null;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       
        try
        {	
	//chercher	id_depatemente	
        	statement =connexion.prepareStatement("SELECT id  FROM departement WHERE nom_departement  = ?;");
        	statement.setString(1,departement_nom);
        	resultat=statement.executeQuery();
    		while(resultat.next())
    		{
    			id_departement=resultat.getInt("id");
    		}
	///////

    		//chercherLesEmployee

    		statement =connexion.prepareStatement("SELECT matricule  FROM employee WHERE departement_id = ?;");
    		statement.setInt(1,id_departement);
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

	public	ArrayList<Chart>	tauxAbsenceDepartementJour(String departement_nom,Date debut, Date fin)
	{	
	
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(departement_nom);//remplir la liste par les matricule des	employee de departement pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceDepa=tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//absenceDepa <qui va	contenir le	taux d'absecne	de	departement	>par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEmployee

			for	(int	k=0;k<absenceDepa.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absenceDepa
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceDepa.get(k).getTaux_absence());
				absenceDepa.set(k,ch);
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employee	de	ce	depatement
		
		for	(int	k=0;k<absenceDepa.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceDepa.get(k).getTaux_absence()/size);
			absenceDepa.set(k,ch);
			
			
		}	
		return	absenceDepa;
	}
	
	public void defineChart2() 
	{
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
		 if(debut != null && fin != null && nomDepartement!=null)
		 {
			ArrayList<Chart> charts = tauxAbsenceDepartementJour(nomDepartement,debut, fin);
			abs1 = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
	}
}
