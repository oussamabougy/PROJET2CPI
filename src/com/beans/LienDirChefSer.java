package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.connect.Database;

public interface LienDirChefSer {
	public default void test(){
		
	}
	public default ArrayList<Integer> employeeDeService(String service_nom)
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

	public default	Map<String,Float>	tauxAbsenceServiceJour(String service_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(service_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
		EmployeGeneral employe = new EmployeGeneral();
		Map<String,Float> absenceService = employe.tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		Map<String,Float> absenceEmployee=new	HashMap<String,Float>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = employe.tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(String k: absenceService.keySet())	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				absenceService.put(k,absenceEmployee.get(k)	+	absenceService.get(k));
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employee	de	ce	service
		
		for	(String k: absenceService.keySet())//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{

			absenceService.put(k,absenceService.get(k)/size);
			
			
		}	
		return	absenceService;
	}
	//public <type> taux_abscence_parservice_par_jour();
	//public <type> taux_abscence_employe_par_mois();
	//public <type> taux_abscence_employe_par_annee();
//public <type> taux_abscence_justifier_service_par_jour();
	//public <type> taux_abscence_justifier_service_par_mois();
	//public <type> taux_abscence_justifier_servicce_par_annee();
	//public <type> infoservice();//facultatif
	//public <type> historique_pointage_service();
	//public <type> liste_employe_service();
}
