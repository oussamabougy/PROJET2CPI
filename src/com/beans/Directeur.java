	package com.beans;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;







@ManagedBean


public class Directeur extends EmployeGeneral  {

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
	
	public	Map<String,Float>	tauxAbsenceGeneraleJour(Date debut, Date fin)
	{	
	
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee = tousLesEmployee();;//remplir la liste par les matricule de	tous	les	employee  pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		Map<String,Float> absenceGene = tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//absenceGene <qui va	contenir le	taux d'absecne	generale	>par	le	taux	d'absence	d'employee	0
		
		Map<String,Float> absenceEmployee = new	HashMap<String,Float>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																		

		//	employee	a	la	liste	absenceEmployee

			for	(String k: absenceGene.keySet())	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absenceGene
			{

				absenceGene.put(k,absenceEmployee.get(k)	+	absenceGene.get(k));
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employees	
		for	(String k: absenceGene.keySet())//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{

			absenceGene.put(k,absenceGene.get(k)/size);
			
			
		}	
		return	absenceGene;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsenceGeneralMois(Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absencegeneral = tauxAbscenceeEmployeMoi(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeMoi(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absencegeneral.get(k).getTaux_absence()/(float)size);
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsencegeneralAnnee(Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absencegeneral = tauxAbscenceeEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absencegeneral.get(k).getTaux_absence()/(float)size);
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}

	
	public ArrayList<Chart> tauxAbsenceGeneralCumuleJour(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absencegeneral = tauxAbscenceCumuleEmployeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
				
				
			}	
		}	
		
		
		
		for	(int	k=1;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absencegeneral.get(k).getTaux_absence()+ absencegeneral.get(k-1).getTaux_absence());
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}
	
	public ArrayList<ChartMois> tauxAbsenceGeneralCumuleMois(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absencegeneralemois = tauxAbscenceCumuleEmployemois(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployemois(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneralemois.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absencegeneralemois.get(k).getJour_absence());
				absencegeneralemois.set(k,ch);
				
				
			}
		}
		return absencegeneralemois;
	}
		
	public ArrayList<ChartMois> tauxAbsenceGeneralCumuleAnnee(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absencegeneralannee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneralannee.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absencegeneralannee.get(k).getJour_absence());
				absencegeneralannee.set(k,ch);
				
				
			}
		}
		return absencegeneralannee;
	}
	
	public ArrayList<Integer> tousLesEmployee()

	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       int	mat=0;
       try
        {	
	
	///////

	//chercherLesEmployee

    	statement =connexion.prepareStatement("SELECT matricule  FROM employee ;");
       	resultat=statement.executeQuery();

	//replirLaListe
       		while(resultat.next())
       		{
     			mat=resultat.getInt("matricule");
     			listMatricule.add(mat);	  
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
	
}
