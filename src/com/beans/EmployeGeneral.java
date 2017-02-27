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
	private String oldpasswrd;
	private String newpasswrd;
	private String confirmpasswrd;
	
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

	public String getOldpasswrd() {
		return oldpasswrd;
	}
	public void setOldpasswrd(String oldp) {
		this.oldpasswrd = oldp;
	}
	public String getNewpasswrd() {
		return newpasswrd;
	}
	public void setNewpasswrd(String newp) {
		this.newpasswrd = newp;
	}
	public String getConfirmpasswrd() {
		return confirmpasswrd;
	}
	public void setConfirmpasswrd(String confirmp) {
		this.confirmpasswrd = confirmp;
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
	public void changemot_passe()
	{
		Connection connexion = null;
		PreparedStatement statement;// pour charger la requete 
		
		connexion = Database.loadDatabase();//charger la bd
		try
		{
			if (virifyPass() && confirmPass())
			{
				statement = connexion.prepareStatement("update employee set mot_pass =? where matricule = ?;");
				statement.setInt(2, getMatricule() );
				statement.setString(1, getNewpasswrd() );
				statement.executeUpdate();
			}
			else 
			{
				if(!virifyPass())
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "votre mot de passe est incorrect", "PrimeFaces Rocks."));
					System.out.println("votre mot de passe est incorrect");
				}
				else
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "votre mot de passe de confirmation ne ressemble pas o neauveau mot passe", "PrimeFaces Rocks."));
					System.out.println("votre mot de passe de confirmation ne ressemble pas o neauveau mot passe");
				}
			}
			
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public boolean virifyPass()
	{
		boolean vrfy = false;
		Connection connexion = null;
		PreparedStatement statement;// pour charger la requete 
		ResultSet result = null;
		connexion = Database.loadDatabase();//charger la bd
		try
		{
			statement=connexion.prepareStatement("select mot_pass from employee where matricule = ?;");
			statement.setInt(1, getMatricule());
			result=statement.executeQuery();
			if (result.next())
			{
				String pass = result.getString("mot_pass");
				if ( pass.equals(getOldpasswrd()))
				{
					vrfy=true;
				}
				else
				{
					vrfy=false;
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return vrfy;
	}
	public boolean confirmPass()
	{
		boolean con;
		if (getConfirmpasswrd().equals(getNewpasswrd()))
		{
			con=true;
		}
		else
		{
			con=false;
		}
		return con;
	}
	//public <type> salaire_a_avoir();
	//public <type> historique_de_monpointage();
	//public <type> get_role();

}
