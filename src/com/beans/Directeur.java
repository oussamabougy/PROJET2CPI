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




import com.connect.*;

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
