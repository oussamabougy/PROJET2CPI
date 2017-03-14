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

public interface LienDirChefDep extends LienDirChefSer {
	public default void test(){
		
	}
	public default ArrayList<String> defineService()
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

	public default	Map<String,Float>	tauxAbsenceDepartementJour(String departement_nom,Date debut, Date fin)
	{	
	
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(departement_nom);//remplir la liste par les matricule des	employee de departement pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
		EmployeGeneral employe = new EmployeGeneral();
		Map<String,Float> absenceDepa=employe.tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//absenceDepa <qui va	contenir le	taux d'absecne	de	departement	>par	le	taux	d'absence	d'employee	0
		
		Map<String,Float> absenceEmployee=new	HashMap<String,Float>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = employe.tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEmployee

			for	(String k: absenceDepa.keySet())	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absenceDepa
			{
				absenceDepa.put(k,absenceEmployee.get(k)	+	absenceDepa.get(k));
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employee	de	ce	depatement
		
		for	(String k: absenceDepa.keySet())//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			absenceDepa.put(k,absenceDepa.get(k)/size);
			
		}	
		return	absenceDepa;
	}
	
	public default ArrayList<Integer> employeeDeDepartement(String departement_nom)

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
	//public <type> taux_abscence_parDepartement_par_jour();
	//public <type> taux_abscence_parDepartement_par_mois();
	//public <type> taux_abscence_parDepartement_par_annee();
//public <type> taux_abscence_justifier_Departement_par_jour();
//public <type> taux_abscence_justifier_Departement_par_mois();
//public <type> taux_abscence_justifier_Departement_par_annee();
//public <type> taux_abscence_cumile_parservice_par_jours();
//public <type> taux_abscence_cumile_parservice_par_mois();
//public <type> taux_abscence_cumule_parservice_par_annee();

//public <type> infoDepartement();//facultatif
//public <type> historique_pointage_Departement();
//public <type> liste_employe_departement();
}
