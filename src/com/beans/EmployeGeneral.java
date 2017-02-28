package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;



@ManagedBean
@SessionScoped

public class EmployeGeneral 
{
	protected int matricule;
	protected String nom;
	protected String prenom;
	protected String motPasse;
	protected double salaire;
	protected String role;
	protected String grade;
	
	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
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

	
	
	public double getSalaire() {
		return salaire;
	}
	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}



	protected Date debut,fin;
	
	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Date getDebut() {
		return debut;
	}

	public Date getFin() {
		return fin;
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
				nom = resultat.getString("nom");
				prenom = resultat.getString("prenom");
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
	
	//Dessinger les chart
	public void defineChart() 
	{
		//System.out.print(getRole(1000001));
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String inputString1 = "2017-02-20";
		 String inputString2 = "2017-02-28";
		 Date d1 = null,d2 = null;
		 try {
		     d1 = myFormat.parse(inputString1);
		     d2 = myFormat.parse(inputString2);
		 } catch (ParseException e) {
		     e.printStackTrace();
		 }
		 if(debut != null && fin != null)
		 {
			ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,debut, fin);
			abs = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
		
		//System.out.print(debut+" "+fin);
	}

	
	protected LineChartModel abs = new LineChartModel() ;

	public LineChartModel getAbs() {
		return abs;
	}

	public void setAbs(LineChartModel abs) {
		this.abs = abs;
	}
	
	public LineChartModel loadAbs(ArrayList<Chart> charts){
		
        LineChartModel model = new LineChartModel();
        
        model.setTitle("Category Chart");
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        //Axis yAxis = model.getAxis(AxisType.Y);
        //yAxis.setLabel("Births");
        //yAxis.setMin(0);
        //yAxis.setMax(10);
        
        
        ChartSeries series = new ChartSeries();
        
        series.setLabel("Series 1");
        
        for(Chart chart : charts)        	
        	series.set(chart.getDate(), chart.getTaux_absence());
        
        model.addSeries(series);
		
		
        return model;
	}

	
	public ArrayList<Chart> tauxAbsenceEmployeeJour(int matricule,Date debut, Date fin)// Calculer le taux d'absence d'un employee(matricule) de la date "debut" jusqua la date "fin"
	{				
		long diff = fin.getTime() - debut.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); // Claculer le nombre de jour entre la date "debut" et la date "fin"
		
		ArrayList<Chart> charts = new ArrayList<Chart>(); //creer liste de type chart(journn�e,taux absnece)
		
		Time t1=null,t2=null; // t1 l"heure de debut de travail, t2 heure de fin
		long diffHour = 0 ; // la difference entre t1 et t2;
		
		PreparedStatement statement = null;
	    ResultSet resultat = null;
	    Connection connexion = null;
	    connexion = Database.loadDatabase(); //Connecter � la base de donner
	    try 
	    {
	    	statement =connexion.prepareStatement("SELECT planning_id FROM employee WHERE matricule = ? ;");// Selectionner
	    	statement.setInt(1,matricule);					//l' id de planning de l'empolyee
	    	resultat=statement.executeQuery();
			resultat.next();
			
			
	    	statement =connexion.prepareStatement("SELECT debut,fin FROM planning WHERE id = ?;");// chercher l'heure de
	    	statement.setInt(1,resultat.getInt("planning_id"));		//debut et fin du planning 
	    	resultat=statement.executeQuery();
	    	resultat.next();

	    	t1 = resultat.getTime("debut");		//t1 prend la valeur de l'heur de debut de travail 
	    	t2 = resultat.getTime("fin");		//t2 prend la valeur de l'heur de fin de travail 
	    	
	    	
	    	diffHour = (t2.getTime()- t1.getTime()) / (60 * 60 * 1000) % 24; // on calcule la difference entre t1 et t2 en nombre d'heure
	    	
	    }
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
	    finally {

	        // Fermeture de la connexion

	        try {


	            if (connexion != null)

	                connexion.close();

	        } catch (SQLException ignore) {

	        }
	    }
	    
		Date date = new	Date();
				date.setTime(debut.getTime());		// on declare la variable "date", on l'intialize a debut et apr�s chaque boucle on l'increment d'une journn�e
		
		for (int i=0;i <= (int)days; i++) //boucle qui va repeter "days" fois
		{
			if(dateVerify(date)) // verifier si la date "date" et une jounn�e de travail, pas une jour de ferie ou weekend
			{
				Chart chart = new Chart(); //creer un chart(journ�e,toux absence)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"
				chart.setDate(date1);			//apr�s on l'ins�re dans la variable "chart"

				int nb_absence =(int)diffHour; // nombre d'absence maximale
				
				connexion = Database.loadDatabase();
		        try 
		        {
			    	statement =connexion.prepareStatement("SELECT heure_pointage FROM pointage WHERE jour = ? AND matricule = ? ORDER BY heure_pointage;"); //recupere le donn�e de pointage
			    	statement.setString(1,date1);						// de la mydb.pointagtable pointage specifi� par le matricule de l'employee
			    	statement.setInt(2,matricule);					// et la date "date1"
			    	resultat=statement.executeQuery();
		    		if(resultat.next())		// si l'employee a point� dans la date "date1" (l'entrer)
		    		{
			    		long diffHours = (resultat.getTime("heure_pointage").getTime()- t1.getTime()) / (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de debut de travail r�el et l'heure de debut de travail theorique 
			    		
			    		nb_absence = (int)diffHours;
			    		
			    		if(resultat.next())	// si l'employee a point� pour la 2eme fois (la sortie)
			    		{
				    		diffHours = (t2.getTime() - resultat.getTime("heure_pointage").getTime() )/ (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de fin de travail r�el et l'heure de fin de travail theorique
				    		
				    		nb_absence += (int)diffHours; //on ajoute la difference a la variable nb_absence

			    		}
		    		}		    		
		        }
		        catch (SQLException e) 
		        {
		            e.printStackTrace();
		        }
		        finally {

		            // Fermeture de la connexion

		            try {


		                if (connexion != null)

		                    connexion.close();

		            } catch (SQLException ignore) {

		            }
		        }
		        	        
		        chart.setTaux_absence((float)nb_absence/(float)diffHour); //on calcule le taux d'absence (nombre d'absence divis� par le nombre theorique de travail) et l'ajoute � la "chart"
		        charts.add(chart);	// on ajoute la variable "chart" a la list "charts"
			}
			date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000); // on increment  la variable "date" d'une journn�e  (24 * 60 * 60 * 1000 ms = 1 jour)
		}

	    // Fermeture de la connexion

	    try {


	         if (connexion != null)

	              connexion.close();

	        } catch (SQLException ignore) {

	        
	    }
		return charts;
	}

	public Boolean dateVerify(Date date) // verifier si la date "date" et une jounn�e de travail, ou bien une jour de ferie ou weekend
	{
		String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);// si la date "date" et une journn�e de weekend
		if(day.equals("Friday") || day.equals("Saturday"))			//samedi ou b1 vendredi
			return false;					//retourener faux
		else		//sinon
		{
			Connection connexion = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Set your date format
			String date1 = sdf.format(date);		// transformer le type date de la variable "date"
													//au type string dans la variable "date1"
			
			PreparedStatement statement = null;
	        ResultSet resultat = null;
	        connexion = Database.loadDatabase();
	        try 
	        {
		    	statement =connexion.prepareStatement("SELECT id FROM jours_ferie WHERE jour = ?;"); // on cherche si la date "date1"
		    	statement.setString(1,date1);					//dans la table jour_ferie 
		    	resultat=statement.executeQuery();
	    		while(resultat.next())			//si date "date1" est un jour ferie retourner faux
	    		{
	    			return false;	    			
	    		}
	        }
	        catch (SQLException e) 
	        {
	            e.printStackTrace();
	        }
	        finally {

	            // Fermeture de la connexion

	            try {


	                if (connexion != null)

	                    connexion.close();

	            } catch (SQLException ignore) {

	            }
	        }
			
		}
		return true;		//sinon retourner vrai
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
