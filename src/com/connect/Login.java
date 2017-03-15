package com.connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Login {
	private int matricule;
	private String motPasse;
	
	private String oldpasswrd;
	private String newpasswrd;
	private String confirmpasswrd;
	

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
			statement = connexion.prepareStatement("Select * from employee WHERE matricule = ? AND mot_pass = ?");
			statement.setInt(1, matricule);
			statement.setString(2, motPasse);

			ResultSet resultat = statement.executeQuery();

			if (resultat.next()) 
			{
				//result found, means valid inputs
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("matricule", matricule);
				return "Menu";
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
			return null;
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
		return null;
	}


	//logout event, invalidate session
	public String logout() 
	{
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}	
	

	public void changeMotPasse()
	{
		Connection connexion = null;
		PreparedStatement statement;// pour charger la requete 
		
		connexion = Database.loadDatabase();//charger la bd
		try
		{
			if (verifyPass() && confirmPass())
			{
				statement = connexion.prepareStatement("update employee set mot_pass =? where matricule = ?;");
				statement.setInt(2, getMatricule() );
				statement.setString(1, getNewpasswrd() );
				statement.executeUpdate();
			}
			else 
			{
				if(!verifyPass())
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
	public boolean verifyPass()
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

}
