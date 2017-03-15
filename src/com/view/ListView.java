package com.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.beans.ChefDepartement;
import com.beans.ChefService;
import com.beans.Pointage;
import com.connect.Database;

public class ListView {


    private List<Pointage> entrants = loadusers(); // liste de pointage d'un seul employee
	
	private List<Pointage> filteredEntrants ;
	
	
	public List<Pointage> getFilteredEntrants() {
		return filteredEntrants;
	}

	public void setFilteredEntrants(List<Pointage> filteredEntrants) {
		this.filteredEntrants = filteredEntrants;
	}

	public List<Pointage> getEntrants() {
		return entrants;
	}

	public void setEntrants(List<Pointage> entrants) {
		this.entrants = entrants;
	}

	public List<Pointage> loadusers()
	{
		List<Pointage> tab = new ArrayList<Pointage>();
		PreparedStatement statement = null;
        ResultSet resultat = null;
        Connection connexion = null;
        connexion = Database.loadDatabase();      
        try {
            statement = connexion.prepareStatement("SELECT * FROM pointage where matricule=? ;");
            
        	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);
        	
        	int mat = (int) session.getAttribute("matricule") ;
    		System.out.print("matriculedazdazs = "+mat) ;
   
    		statement.setInt(1,mat) ;

            resultat = statement.executeQuery();
        	System.out.print("ok");

                    	
            while (resultat.next())
            {
            	System.out.print("yaw");
            	Pointage user = new Pointage ();
                user.setMatricule(resultat.getInt("matricule"));
                user.setDate(resultat.getDate("jour"));
                user.setTime(resultat.getTime("heure_pointage"));
                statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                
                statement.setInt(1, resultat.getInt("matricule"));

                // Exécution de la requête

                ResultSet resultat1 = statement.executeQuery();
                if(resultat1.next())
                {
                    user.setNom(resultat1.getString("nom"));
                    user.setPrenom(resultat1.getString("prenom"));
                }

                tab.add(user) ;
                
          }
                 
        }
        catch (SQLException e) {

        } finally {

            // Fermeture de la connexion

            try {

                if (connexion != null)
                    connexion.close();

            } catch (SQLException ignore) {
            }
        }
		return tab;
	}

	public List<Pointage> loadusersService()
	{
		
		List<Pointage> tab = new ArrayList<Pointage>();
		PreparedStatement statement = null;
        ResultSet resultat = null;
        Connection connexion = null;
        connexion = Database.loadDatabase();      
        try {
            statement = connexion.prepareStatement("SELECT * FROM pointage ORDER BY matricule ;");

            
        	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);
        	
        	int mat = (int) session.getAttribute("matricule") ;
    	//	System.out.print("matricule = "+mat) ;

        	ChefService chefSer = new ChefService();
    		int serviceId = chefSer.idService(mat) ;
    	//	System.out.print("service_id = "+serviceId) ;
        	
    		
            resultat = statement.executeQuery();
            
            // Récupération des données
        	
            while (resultat.next())
            {
            	 statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                 statement.setInt(1, resultat.getInt("matricule"));
                 ResultSet resultat2 = statement.executeQuery();
                 
                if(resultat2.next())
                {
                if(resultat2.getInt("service_id")==serviceId)
                {

            	Pointage user = new Pointage ();
                user.setMatricule(resultat.getInt("matricule"));
                user.setDate(resultat.getDate("jour"));
                user.setTime(resultat.getTime("heure_pointage"));
                statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                
                statement.setInt(1, resultat.getInt("matricule"));

                // Exécution de la requête

                ResultSet resultat1 = statement.executeQuery();
                if(resultat1.next())
                {
                    user.setNom(resultat1.getString("nom"));
                    user.setPrenom(resultat1.getString("prenom"));
                }

                tab.add(user) ;
                
                }
                }
            }
                 
        }
        catch (SQLException e) {

        } finally {

            // Fermeture de la connexion

            try {

                if (resultat != null)
                    resultat.close();

                if (statement != null)
                    statement.close();

                if (connexion != null)
                    connexion.close();

            } catch (SQLException ignore) {
            }
        }
		return tab;
	}


	public List<Pointage> loadusersDepartement()
	{
		
		List<Pointage> tab = new ArrayList<Pointage>();
		PreparedStatement statement = null;
        ResultSet resultat = null;
        Connection connexion = null;
        connexion = Database.loadDatabase();      
        try {
            statement = connexion.prepareStatement("SELECT * FROM pointage ORDER BY matricule ;");

            
        	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);
        	
        	int mat = (int) session.getAttribute("matricule") ;
    	//	System.out.print("matricule = "+mat) ;

        	ChefDepartement chefDep = new ChefDepartement();
    		int departementId = chefDep.idDepartement(mat) ;
    	//	System.out.print("dep = "+departementId) ;
        	
    		
            resultat = statement.executeQuery();
            
            // Récupération des données
        	
            while (resultat.next())
            {
            	 statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                 
                 statement.setInt(1, resultat.getInt("matricule"));
                 ResultSet resultat2 = statement.executeQuery();
                if(resultat2.next())
                {
                if(resultat2.getInt("departement_id")==departementId)
                {

            	Pointage user = new Pointage ();
                user.setMatricule(resultat.getInt("matricule"));
                user.setDate(resultat.getDate("jour"));
                user.setTime(resultat.getTime("heure_pointage"));
                statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                
                statement.setInt(1, resultat.getInt("matricule"));

                // Exécution de la requête

                ResultSet resultat1 = statement.executeQuery();
                if(resultat1.next())
                {
                    user.setNom(resultat1.getString("nom"));
                    user.setPrenom(resultat1.getString("prenom"));
                }

                tab.add(user) ;
                
                }
                }
            }
                 
        }
        catch (SQLException e) {

        } finally {

            // Fermeture de la connexion

            try {

                if (resultat != null)
                    resultat.close();

                if (statement != null)
                    statement.close();

                if (connexion != null)
                    connexion.close();

            } catch (SQLException ignore) {
            }
        }
		return tab;
	}
}
