package com.beans;
import com.connect.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped

public class ChefDepartement extends EmployeGeneral implements LienDirChefDep {
	
	private String nomDepartement;
	
	public int idDepartement(int matricule) //get the departement_id of employee
	{
		PreparedStatement statement = null;
        Connection connexion = null;
        int IdDepartement = 1 ;
        
        try 
        {
            connexion = Database.loadDatabase();
	    	statement = connexion.prepareStatement("SELECT departement_id FROM employee WHERE matricule = ? ;");
	    	statement.setInt(1,matricule);

			ResultSet resultat = statement.executeQuery();
    	
	    	if(resultat.next())
	    	{

	    		IdDepartement = resultat.getInt("departement_id") ;
	    		System.out.print("IdDepartement = " + IdDepartement) ;

	    	}
    		
        }catch (SQLException e) {}
        finally 
        {
               try { if (connexion != null) connexion.close( );}
               catch (SQLException ignore) {}
        }
  	
        
        return IdDepartement ;
	}

	
	
	
	public String getNomDepartement() {
		return nomDepartement;
	}
	public void setNomDepartement(String nomDepartement) {
		this.nomDepartement = nomDepartement;
	}

	
	
	

}
