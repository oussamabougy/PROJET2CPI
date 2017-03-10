package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
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
	private String oldpasswrd;
	private String newpasswrd;
	private String confirmpasswrd;
	
	
	
	
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
			ArrayList<Chart> charts1 = tauxAbsenceEmployeeJourParHeur(matricule,debut, fin);
			ArrayList<Chart> charts2 = tauxAbscenceCumuleEmployeJour(matricule,debut, fin);
			abs = loadAbs(charts) ;
			abs1 = loadAbs(charts1);
			abs2 = loadAbsBar(charts2);
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
		
		//System.out.print(debut+" "+fin);
	}

	
	protected LineChartModel abs = new LineChartModel() ;

	protected LineChartModel abs1 = new LineChartModel() ;
	
	protected BarChartModel abs2 = new BarChartModel() ;
	
	
	
	public BarChartModel getAbs2() {
		return abs2;
	}
	public void setAbs2(BarChartModel abs2) {
		this.abs2 = abs2;
	}
	public LineChartModel getAbs1() {
		return abs1;
	}
	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
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
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2017-02-30");
        axis.setTickFormat("%b %#d, %y");
         
        model.getAxes().put(AxisType.X, axis);
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
	
	
	
	public BarChartModel loadAbsBar(ArrayList<Chart> charts){
		
		BarChartModel model = new BarChartModel();
        
        model.setTitle("Category Chart");
        model.setLegendPosition("ne");
        model.setShowPointLabels(true);
        /*model.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        model.setZoom(true);
        model.getAxis(AxisType.Y).setLabel("Values");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2017-02-30");
        axis.setTickFormat("%b %#d, %y");
         
        model.getAxes().put(AxisType.X, axis);*/
        
        
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
		
		ArrayList<Chart> charts = new ArrayList<Chart>(); //creer liste de type chart(journnï¿½e,taux absnece)
		
		Time t1=null,t2=null; // t1 l"heure de debut de travail, t2 heure de fin
		long diffHour = 0 ; // la difference entre t1 et t2;
		
		PreparedStatement statement = null;
	    ResultSet resultat = null;
	    Connection connexion = null;
	    connexion = Database.loadDatabase(); //Connecter ï¿½ la base de donner
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
				date.setTime(debut.getTime());		// on declare la variable "date", on l'intialize a debut et aprï¿½s chaque boucle on l'increment d'une journnï¿½e
		
		for (int i=0;i <= (int)days; i++) //boucle qui va repeter "days" fois
		{
			if(dateVerify(date)) // verifier si la date "date" et une jounnï¿½e de travail, pas une jour de ferie ou weekend
			{
				Chart chart = new Chart(); //creer un chart(journï¿½e,toux absence)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"

				chart.setDate(date1);			//aprés on l'insère dans la variable "chart"

				int nb_absence =(int)diffHour; // nombre d'absence maximale
				
				connexion = Database.loadDatabase();
		        try 
		        {
			    	statement =connexion.prepareStatement("SELECT heure_pointage FROM pointage WHERE jour = ? AND matricule = ? ORDER BY heure_pointage;"); //recupere le donnï¿½e de pointage
			    	statement.setString(1,date1);						// de la mydb.pointagtable pointage specifiï¿½ par le matricule de l'employee
			    	statement.setInt(2,matricule);					// et la date "date1"
			    	resultat=statement.executeQuery();
		    		if(resultat.next())		// si l'employee a pointï¿½ dans la date "date1" (l'entrer)
		    		{
			    		long diffHours = (resultat.getTime("heure_pointage").getTime()- t1.getTime()) / (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de debut de travail rï¿½el et l'heure de debut de travail theorique 
			    		
			    		nb_absence = (int)diffHours;
			    		
			    		if(resultat.next())	// si l'employee a pointï¿½ pour la 2eme fois (la sortie)
			    		{
				    		diffHours = (t2.getTime() - resultat.getTime("heure_pointage").getTime() )/ (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de fin de travail rï¿½el et l'heure de fin de travail theorique
				    		
				    		nb_absence += (int)(diffHours); //on ajoute la difference a la variable nb_absence

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
		        	        
		        chart.setTaux_absence((float)nb_absence/(float)diffHour); //on calcule le taux d'absence (nombre d'absence divisï¿½ par le nombre theorique de travail) et l'ajoute ï¿½ la "chart"
		        charts.add(chart);	// on ajoute la variable "chart" a la list "charts"
			}
			date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000); // on increment  la variable "date" d'une journnï¿½e  (24 * 60 * 60 * 1000 ms = 1 jour)
		}

	    // Fermeture de la connexion

	    try {


	         if (connexion != null)

	              connexion.close();

	        } catch (SQLException ignore) {

	        
	    }
		return charts;
	}


	public ArrayList<Chart> tauxAbsenceEmployeeJourParHeur(int matricule,Date debut, Date fin)// Calculer le taux d'absence d'un employee(matricule) de la date "debut" jusqua la date "fin"
	{				
		long diff = fin.getTime() - debut.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); // Claculer le nombre de jour entre la date "debut" et la date "fin"
		
		ArrayList<Chart> charts = new ArrayList<Chart>(); //creer liste de type chart(journnée,taux absnece)
		
		Time t1=null,t2=null; // t1 l"heure de debut de travail, t2 heure de fin
		long diffHour = 0 ; // la difference entre t1 et t2;
		
		PreparedStatement statement = null;
	    ResultSet resultat = null;
	    Connection connexion = null;
	    connexion = Database.loadDatabase(); //Connecter à la base de donner
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
				date.setTime(debut.getTime());		// on declare la variable "date", on l'intialize a debut et aprés chaque boucle on l'increment d'une journnée
		
		for (int i=0;i <= (int)days; i++) //boucle qui va repeter "days" fois
		{
			if(dateVerify(date)) // verifier si la date "date" et une jounnée de travail, pas une jour de ferie ou weekend
			{
				Chart chart = new Chart(); //creer un chart(journée,toux absence)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"
				chart.setDate(date1);			//aprés on l'insère dans la variable "chart"
				int nb_absence =(int)diffHour; // nombre d'absence maximale
				
				connexion = Database.loadDatabase();
		        try 
		        {
			    	statement =connexion.prepareStatement("SELECT heure_pointage FROM pointage WHERE jour = ? AND matricule = ? ORDER BY heure_pointage;"); //recupere le donnée de pointage
			    	statement.setString(1,date1);						// de la mydb.pointagtable pointage specifié par le matricule de l'employee
			    	statement.setInt(2,matricule);					// et la date "date1"
			    	resultat=statement.executeQuery();
		    		if(resultat.next())		// si l'employee a pointé dans la date "date1" (l'entrer)
		    		{
			    		long diffHours = (resultat.getTime("heure_pointage").getTime()- t1.getTime()) / (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de debut de travail réel et l'heure de debut de travail theorique 
			    		
			    		nb_absence = (int)diffHours;
			    		
			    		if(resultat.next())	// si l'employee a pointé pour la 2eme fois (la sortie)
			    		{
				    		diffHours = (t2.getTime() - resultat.getTime("heure_pointage").getTime() )/ (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de fin de travail réel et l'heure de fin de travail theorique
				    		
				    		nb_absence += (int)(diffHours+1); //on ajoute la difference a la variable nb_absence

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
		        	        
		        chart.setTaux_absence((float)nb_absence); //on calcule le taux d'absence (nombre d'absence divisé par le nombre theorique de travail) et l'ajoute à la "chart"
		        charts.add(chart);	// on ajoute la variable "chart" a la list "charts"
			}
			date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000); // on increment  la variable "date" d'une journnée  (24 * 60 * 60 * 1000 ms = 1 jour)
		}

	    // Fermeture de la connexion

	    try {


	         if (connexion != null)

	              connexion.close();

	        } catch (SQLException ignore) {

	        
	    }
		return charts;
	}
	
	public Boolean dateVerify(Date date) // verifier si la date "date" et une jounnée de travail, ou bien une jour de ferie ou weekend
	{
		String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);// si la date "date" et une journnï¿½e de weekend
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

	
	//public <type> tauxAbscenceEmployeAnnee();
	public ArrayList<Chart> tauxAbscenceCumuleEmployeJour(int matricule,Date d1,Date d2)
	{
		ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);
		ArrayList<Chart> cumul = new ArrayList<Chart>(charts.size());
		float taux_cumule=0;
		int i=0;
		Chart ch = new Chart();
		ch=charts.get(0);
		ch.setTaux_absence(ch.getTaux_absence()*8);
		cumul.add(0, ch );
		taux_cumule=ch.getTaux_absence();
		for(i=1;i<charts.size();i++)
		{
			ch = new Chart();
			taux_cumule+=(charts.get(i).getTaux_absence() * 8);
			System.out.println("allllleze : " + taux_cumule + " " + charts.get(i).getTaux_absence() * 8 );
			ch.setTaux_absence(taux_cumule);
			ch.setDate(charts.get(i).getDate());
			cumul.add(i, ch);
			
		}	
		return cumul;
	}
	public ArrayList<ChartMois> tauxAbscenceCumuleEmployeMois(int matricule,Date d1,Date d2)
	{
		Calendar calendarMin = new GregorianCalendar();
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 int Month1 = 0;
		 int i=0;
		 Date d = null;
		 ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);//avoir la liste des taux absence employe
		 ArrayList<ChartMois> cumul_mois = new ArrayList<ChartMois>();//
		 try {
			 d=myFormat.parse(charts.get(0).getDate());
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }
		 calendarMin.setTime(d);
		 int Month =calendarMin.get(Calendar.MONTH);// avoir le mois de la premiere date
		 int absence_cumul = (int)(charts.get(0).getTaux_absence()*8) ;//nombre de journee absente de premier jour
		
		 for(i=1;i<charts.size();i++)
		 {
			 try {
				 d=myFormat.parse(charts.get(i).getDate());
			 } catch (ParseException e) {
				 e.printStackTrace();
			 }
			 calendarMin.setTime(d);
			 Month1 =calendarMin.get(Calendar.MONTH);
			 if (Month != Month1)
			 {
				 ChartMois mois = new ChartMois();
				 mois.setMois(Month+1);
				 mois.setJour_absence(absence_cumul/8);
				 Month=Month1;
				 cumul_mois.add(mois);
				 absence_cumul=(int)(charts.get(i).getTaux_absence()*8);
			 }
			 else
			 {
				 absence_cumul=absence_cumul + (int)(charts.get(i).getTaux_absence()*8);
				
			 }
			 
		 }
		 ChartMois mois = new ChartMois();
		 mois.setMois(Month+1);
		 mois.setJour_absence(absence_cumul/8);
		 cumul_mois.add(mois);
		return cumul_mois;
 	}
	//public <type> tauxAbscenceCumuleEmployeAnnee();
	//public <type> taux_abscence_justifier_employe_par_jour();
	//public <type> taux_abscence_justifier_employe_par_mois();
	//public <type> taux_abscence_justifier_employe_par_annee();
	//public <type> afficher_info_employe();
	//public <type> Temps_de_travail();
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
	//public <type> salaire_a_avoir();
	//public <type> historique_de_monpointage();
	//public <type> get_role();
	
	/*public ArrayList<Chart> tauxAbsenceEmployeeJourJustifie(int matricule,Date debut, Date fin)// Calculer le taux d'absence d'un employee(matricule) de la date "debut" jusqua la date "fin"
	{				
		long diff = fin.getTime() - debut.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); // Claculer le nombre de jour entre la date "debut" et la date "fin"
		
		ArrayList<Chart> charts = new ArrayList<Chart>(); //creer liste de type chart(journnée,taux absnece)
		
		Time t1=null,t2=null; // t1 l"heure de debut de travail, t2 heure de fin
		long diffHour = 0 ; // la difference entre t1 et t2;
		
		PreparedStatement statement = null;
	    ResultSet resultat = null;
	    Connection connexion = null;
	    connexion = Database.loadDatabase(); //Connecter à la base de donner
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
				date.setTime(debut.getTime());		// on declare la variable "date", on l'intialize a debut et aprés chaque boucle on l'increment d'une journnée
		
		for (int i=0;i <= (int)days; i++) //boucle qui va repeter "days" fois
		{
			if(dateVerify(date)) // verifier si la date "date" et une jounnée de travail, pas une jour de ferie ou weekend
			{
				Chart chart = new Chart(); //creer un chart(journée,toux absence)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"
				chart.setDate(date1);			//aprés on l'insère dans la variable "chart"
				int nb_absence =(int)diffHour; // nombre d'absence maximale
				
				connexion = Database.loadDatabase();
		        try 
		        {
			    	statement =connexion.prepareStatement("SELECT heure_pointage FROM pointage WHERE jour = ? AND matricule = ? ORDER BY heure_pointage;"); //recupere le donnée de pointage
			    	statement.setString(1,date1);						// de la mydb.pointagtable pointage specifié par le matricule de l'employee
			    	statement.setInt(2,matricule);					// et la date "date1"
			    	resultat=statement.executeQuery();
		    		if(resultat.next())		// si l'employee a pointé dans la date "date1" (l'entrer)
		    		{
			    		long diffHours = (resultat.getTime("heure_pointage").getTime()- t1.getTime()) / (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de debut de travail réel et l'heure de debut de travail theorique 
			    		
			    		nb_absence = (int)diffHours;
			    		
			    		if(resultat.next())	// si l'employee a pointé pour la 2eme fois (la sortie)
			    		{
				    		diffHours = (t2.getTime() - resultat.getTime("heure_pointage").getTime() )/ (60 * 60 * 1000) % 24;// on calcule la difference entre l'heure de fin de travail réel et l'heure de fin de travail theorique
				    		
				    		nb_absence += (int)(diffHours+1); //on ajoute la difference a la variable nb_absence

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
		        	        
		        chart.setTaux_absence((float)nb_absence); //on calcule le taux d'absence (nombre d'absence divisé par le nombre theorique de travail) et l'ajoute à la "chart"
		        charts.add(chart);	// on ajoute la variable "chart" a la list "charts"
			}
			date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000); // on increment  la variable "date" d'une journnée  (24 * 60 * 60 * 1000 ms = 1 jour)
		}

	    // Fermeture de la connexion

	    try {


	         if (connexion != null)

	              connexion.close();

	        } catch (SQLException ignore) {

	        
	    }
		return charts;
	}*/
	
	public void tester() 
	{
		//System.out.print(getRole(1000001));
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String inputString1 = "2017-02-20";
		 String inputString2 = "2017-02-27";
		 Date d1 = null,d2 = null;
		 try {
		     d1 = myFormat.parse(inputString1);
		     d2 = myFormat.parse(inputString2);
		 } catch (ParseException e) {
		     e.printStackTrace();
		 }
		 ArrayList<Chart> abse = tauxAbsenceEmployeeJour(1000010,d1, d2);
		 ArrayList<Chart> cumuli = tauxAbscenceCumuleEmployeJour(1000010,d1, d2);

		 ArrayList<ChartMois> cumul =  tauxAbscenceCumuleEmployeMois(1000010,d1, d2);
		for(ChartMois chartmois:cumul)
		{
			System.out.print("Mois : "+chartmois.getMois()+" nb absence : "+chartmois.getJour_absence()+" jours");
		}

		 
		 for(Chart chart:abse)
			{
				System.out.print("Mois : "+chart.getDate()+" nb absence : "+chart.getTaux_absence()+" jours");
			}

		for(Chart chart:cumuli)
		{
			System.out.print("jour : "+chart.getDate()+" nb absence : "+chart.getTaux_absence()+" heurs");
		}
		String day = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(d1);
		System.out.print(day);
		System.out.print(new SimpleDateFormat("MMMM").format(d2));
	}
}
