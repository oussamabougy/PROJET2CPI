package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class Employe extends EmployeGeneral {
	protected String nomService;
	protected String nomDepartement;
	
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
	

	@PostConstruct
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
