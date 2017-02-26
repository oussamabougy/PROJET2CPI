package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped

public class EmployeGeneral {
	protected int matricule;
	protected String nom;
	protected String prenom;
	protected String motPasse;
	protected double salaire;
	protected String role;
	protected String grade;
	
	//hi
	public int getMatricule() {
		return matricule;
	}


	public void setMatricule(int matricule) {
		this.matricule = matricule;
	}


	public String getMotPasse() {
		return motPasse;
	}


	public void setMotPasse(String motPasse) {
		this.motPasse = motPasse;
	}


	//validate login
	public String login() 
	{
		Connection connexion = null;
		PreparedStatement statement = null;

		try 
		{
			connexion = Database.loadDatabase();
			statement = connexion.prepareStatement("Select * from employee where matricule = ? and mot_pass = ?");
			statement.setInt(1, matricule);
			statement.setString(2, motPasse);

			ResultSet resultat = statement.executeQuery();

			if (resultat.next()) 
			{
				//result found, means valid inputs
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("matricule", matricule);
				return "home";
			}
		} 
		catch (SQLException ex) 
		{
			System.out.println("Login error -->" + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login";
		} 
		finally 
		{
			try 
			{
				if (connexion != null)
                    connexion.close();
            } 
			catch (SQLException e) 
			{
				e.printStackTrace();
            }
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Incorrect Username and Passowrd",
						"Please enter correct username and Password"));
		return "login";
	}


	//logout event, invalidate session
	public String logout() 
	{
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}	
	
	
	//public <type> tauxAbscenceEmployeJour();
	//public <type> tauxAbscenceEmployeMois();
	//public <type> tauxAbscenceEmployeAnnee();
	//public <type> tauxAbscenceCumuleEmployeJour();
	//public <type> tauxAbscenceCumuleEmployeMois();
	//public <type> tauxAbscenceCumuleEmployeAnnee();
	//public <type> taux_abscence_justifier_employe_par_jour();
	//public <type> taux_abscence_justifier_employe_par_mois();
	//public <type> taux_abscence_justifier_employe_par_annee();
	//public <type> afficher_info_employe();
	//public <type> Temps_de_travail();
	//public <type> changer_mot_passe();
	//public <type> salaire_a_avoir();
	//public <type> historique_de_monpointage();
	//public <type> get_role();

}
