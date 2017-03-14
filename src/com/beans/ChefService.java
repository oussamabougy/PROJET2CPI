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
public class ChefService extends Employe implements LienDirChefSer {

	public int idService(int matricule) //get the service_id of employee
	{
		PreparedStatement statement = null;
        Connection connexion = null;
        int IdService = 1 ;
        
        try 
        {
            connexion = Database.loadDatabase();
	    	statement = connexion.prepareStatement("SELECT service_id FROM employee WHERE matricule = ? ;");
	    	statement.setInt(1,matricule);

			ResultSet resultat = statement.executeQuery();
    	
	    	if(resultat.next())
	    	{

	    		IdService = resultat.getInt("service_id") ;
	    		System.out.print("IdService = " + IdService) ;

	    	}
    		
        }catch (SQLException e) {}
        finally 
        {
               try { if (connexion != null) connexion.close( );}
               catch (SQLException ignore) {}
        }
  	
        
        return IdService ;
	}

	
	
	
	
	
}
