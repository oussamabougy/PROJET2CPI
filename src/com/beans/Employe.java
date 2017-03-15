package com.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.connect.*;

@ManagedBean
@RequestScoped
@SessionScoped
@ViewScoped
public class Employe extends EmployeGeneral implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String nomService;
	protected String nomDepartement;
	
	


    private List<Enter> entrants; // liste de pointage d'un seul employee
	
	private List<Enter> filteredEntrants ;
	
	
	public List<Enter> getFilteredEntrants() {
		return filteredEntrants;
	}

	public void setFilteredEntrants(List<Enter> filteredEntrants) {
		this.filteredEntrants = filteredEntrants;
	}

	public List<Enter> getEntrants() {
		return entrants;
	}

	public void setEntrants(List<Enter> entrants) {
		this.entrants = entrants;
	}

	public List<Enter> loadusers()
	{
		List<Enter> tab = new ArrayList<Enter>();
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
            	Enter user = new Enter ();
                user.setMatricule(resultat.getInt("matricule"));
                user.setDate(resultat.getDate("jour"));
                user.setTime(resultat.getTime("heure_pointage"));
                statement = connexion.prepareStatement("SELECT * FROM employee WHERE matricule = ? ;");
                
                statement.setInt(1, resultat.getInt("matricule"));

                // Ex�cution de la requ�te

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

	public String getNomService() {
		return nomService;
	}
	public void setNomService(String nomService) {
		this.nomService = nomService;
	}
	public String getNomDepartement() {
		return nomDepartement;
	}
	public void setNomDepartement(String nomDepartement) {
		this.nomDepartement = nomDepartement;
	}
	

	
	public void showInfo(){
		PreparedStatement statement = null;
        ResultSet resultat = null;
        Connection connexion = null;
        connexion = Database.loadDatabase();      
        try 
        {
            statement = connexion.prepareStatement("SELECT * FROM employee where matricule= ?;");
    		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);
    		
    		statement.setString(1, session.getAttribute("matricule").toString());
            resultat = statement.executeQuery();
            
        	
        	    int service_id=1 ,grade_id=0,role_id=0,departement_id=0;


                if(resultat.next() );  
                {
                	matricule = (resultat.getInt("matricule"));
                    nom = (resultat.getString("nom"));
                    prenom = (resultat.getString("prenom"));
                    service_id=resultat.getInt("service_id");
                    grade_id=resultat.getInt("grade_id");
                    departement_id=resultat.getInt("departement_id") ;
                    role_id = resultat.getInt("role_id") ;
                    salaire = (resultat.getFloat("salaire"));              

                    
	                if (service_id != 0)
	                {
	                	
	                   statement =connexion.prepareStatement("SELECT *  FROM service WHERE id = ? ;");
	            	
	                 	statement.setInt(1,service_id);           
	                   resultat = statement.executeQuery();
	                
	                                        
	                    resultat.next() ;                          
	                    nomService = (resultat.getString("nom_service")) ;
	                }
	                else
	                {
	                	nomService = "aucun service";
	                }
                
                
	               if (grade_id != 0)
	                {
	                   statement =connexion.prepareStatement("SELECT *  FROM grade WHERE id = ? ;");
	            	
	                 	statement.setInt(1,grade_id);           
	                   resultat = statement.executeQuery();
	                                                    
	                    resultat.next() ;                          
	                    grade = resultat.getString("grade") ;
	                }
	                else
	                {
	                    grade = "aucun grade" ;
	                }
               
               
	               if (role_id != 0)
	               {
	                  statement =connexion.prepareStatement("SELECT * FROM role WHERE id = ? ;");
	           	
	                	statement.setInt(1,role_id);           
	                  resultat = statement.executeQuery();
	                                                   
	                   resultat.next() ;                          
	                   role = resultat.getString("nom_role");
	               }
	               else
	               {
	                   role = "aucun role" ;
	               }
               
               
	               if (departement_id != 0)
	               {
	                  statement =connexion.prepareStatement("SELECT *  FROM departement WHERE id = ? ;");
	           	
	                	statement.setInt(1,departement_id);           
	                  resultat = statement.executeQuery();
	                                                   
	                   resultat.next() ;                          
	                   nomDepartement= resultat.getString("nom_departement") ;
	               }
	               else
	               {
	                   nomDepartement = "aucun departement" ;
	               }
                }                                      
                                    
      }catch (SQLException e) {}
      finally 
      {
             try 
    	        {
                 
                if (connexion != null) connexion.close();                   
                } catch (SQLException ignore) {}
        }
	}
	//public <type> monService();
	//public <type> monDepartement();
	
	

}
