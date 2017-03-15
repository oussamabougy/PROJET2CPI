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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;




@ManagedBean
@RequestScoped
@SessionScoped
@ViewScoped

public abstract class  EmployeGeneral implements Serializable  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	
	public Map<String,Float> tauxAbsenceEmployeeJour(int matricule,Date debut, Date fin)// Calculer le taux d'absence d'un employee(matricule) de la date "debut" jusqua la date "fin"
	{				
		long diff = fin.getTime() - debut.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); // Claculer le nombre de jour entre la date "debut" et la date "fin"
		
		Map<String,Float> charts = new HashMap<String,Float>(); //creer liste de type chart(journnï¿½e,taux absnece)
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"

								//aprés on l'insère dans la variable "chart"

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
		        	        
		        		//on calcule le taux d'absence (nombre d'absence divisï¿½ par le nombre theorique de travail) et l'ajoute ï¿½ la "chart"
		        charts.put(date1,(float)nb_absence/(float)diffHour);	// on ajoute la variable "chart" a la list "charts"
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

<<<<<<< HEAD
	public ArrayList<ChartmoisTaux> tauxAbscenceeEmployeMoi(int matricule,Date d1,Date d2)
	{
		 Calendar calendarMin = new GregorianCalendar();
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 int Month=0;	
		 int Month1 = 0;
		 int days=1;
		 float absence=0;
		 int i=0;
		 Date d = null;
		 
		 ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);//avoir la liste des taux absence employe
		 ArrayList<ChartmoisTaux> tauxMois = new ArrayList<ChartmoisTaux>();//
		
		 try 
		 {
			 d=myFormat.parse(charts.get(0).getDate());
		 }
		 catch (ParseException e) {
			 e.printStackTrace();
		 }
		 calendarMin.setTime(d);
		 Month =calendarMin.get(Calendar.MONTH);// avoir le mois de la premiere date
		 absence = charts.get(0).getTaux_absence() ;//nombre de heure absente de premier jour
		

		 for(i=1;i<charts.size();i++)
		 {
			 try 
			 {
				 d=myFormat.parse(charts.get(i).getDate());
			 }
			 catch (ParseException e) 
			 {
				 e.printStackTrace();
			 }
			 
			 calendarMin.setTime(d);
			 Month1 =calendarMin.get(Calendar.MONTH);
			 
				 if (Month != Month1) //verifier est ce que c'est un  nouveau moi
				 {
				 	 //enregestrer le moi dans la liste
					 ChartmoisTaux mois = new ChartmoisTaux();
					 mois.setMois(Month+1);
					 mois.setTaux_absence(absence/days);
					 tauxMois.add(mois); 
					
 					 //renitialiser les var
					 Month=Month1;		
					 days=1;
				 	 absence=charts.get(i).getTaux_absence();
			 	}
				 else
				 {
					 //incrementer le nombre de jour et ajouter le taux d'absence de ce jour
					 days++;
					 absence+=charts.get(i).getTaux_absence();
				 }
			 
		 }
		 
		 //enregestrer le dernier  moi dans la liste
		 ChartmoisTaux mois = new ChartmoisTaux();
		 mois.setMois(Month+1);
		 mois.setTaux_absence(absence/days);
		 tauxMois.add(mois); 
		 
		 
		return tauxMois;
	}
	
	public ArrayList<ChartmoisTaux> tauxAbscenceeEmployeAnnee(int matricule,Date d1,Date d2)
	{
		Calendar calendarMin = new GregorianCalendar();
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 int year=0;	
		 int year1 = 0;
		 int days=1;
		 float absence=0;
		 int i=0;
		 Date d = null;
		 
		 ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);//avoir la liste des taux absence employe
		 ArrayList<ChartmoisTaux> tauxAnnee = new ArrayList<ChartmoisTaux>();//
		
		 try 
		 {
			 d=myFormat.parse(charts.get(0).getDate());
		 }
		 catch (ParseException e) {
			 e.printStackTrace();
		 }
		 calendarMin.setTime(d);
		 year =calendarMin.get(Calendar.YEAR);// avoir le mois de la premiere date
		 absence = charts.get(0).getTaux_absence() ;//nombre de heure absente de premier jour
		

		 for(i=1;i<charts.size();i++)
		 {
			 try 
			 {
				 d=myFormat.parse(charts.get(i).getDate());
			 }
			 catch (ParseException e) 
			 {
				 e.printStackTrace();
			 }
			 
			 calendarMin.setTime(d);
			 year1 =calendarMin.get(Calendar.YEAR);
			 
				 if (year != year1) //verifier est ce que c'est un  nouveau moi
				 {
				 	 //enregestrer le moi dans la liste
					 ChartmoisTaux annee = new ChartmoisTaux();
					 annee.setMois(year);
					 annee.setTaux_absence(absence/days);
					 tauxAnnee.add(annee); 
					
					 //renitialiser les var
					 year=year1;		
					 days=1;
				 	 absence=charts.get(i).getTaux_absence();
			 	}
				 else
				 {
					 //incrementer le nombre de jour et ajouter le taux d'absence de ce jour
					 days++;
					 absence+=charts.get(i).getTaux_absence();
				 }
			 
		 }
		 
		 //enregestrer le dernier  moi dans la liste
		 ChartmoisTaux annee = new ChartmoisTaux();
		 annee.setMois(year);
		 annee.setTaux_absence(absence/days);
		 tauxAnnee.add(annee); 
		 
		 
		return tauxAnnee;
	}
	


	public Map<String,Integer> tauxAbsenceEmployeeJourParHeur(int matricule,Date debut, Date fin)// Calculer le taux d'absence d'un employee(matricule) de la date "debut" jusqua la date "fin"
	{				
		long diff = fin.getTime() - debut.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); // Claculer le nombre de jour entre la date "debut" et la date "fin"
		
		Map<String,Integer> charts = new HashMap<String,Integer>(); //creer liste de type chart(journnée,taux absnece)
		
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
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // transformer le type date de la variable "date"
				String date1 = sdf.format(date);		//au type string dans la variable "date1"
				
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
		        	        
		         //on calcule le taux d'absence (nombre d'absence divisé par le nombre theorique de travail) et l'ajoute à la "chart"
		        charts.put(date1,nb_absence);	// on ajoute la variable "chart" a la list "charts"
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
	public Map<String,Float> tauxAbscenceCumuleEmployeJour(int matricule,Date d1,Date d2)
	{
		Map<String,Float> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);
		Map<String,Float> cumul = new HashMap<String,Float>();
		float taux_cumule=0;
		for(String i: charts.keySet())
		{

			taux_cumule+=(charts.get(i) * 8);

			cumul.put(i, taux_cumule);
			
		}	
		return cumul;
	}
	public Map<String,Integer> tauxAbscenceCumuleEmployeMois(int matricule,Date d1,Date d2)
	{
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String Month1;
		 Date d = null;
		 Map<String,Float> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);//avoir la liste des taux absence employe
		 Map<String,Integer> cumul_mois = new HashMap<String,Integer>();//


		 String Month =new SimpleDateFormat("MMMM", Locale.ENGLISH).format(d1);// avoir le mois de la premiere date
		 int absence_cumul = 0 ;//nombre de journee absente de premier jour
		
		 for(String i: charts.keySet())
		 {
			 try {
				 d=myFormat.parse(i);
			 } catch (ParseException e) {
				 e.printStackTrace();
			 }
			 Month1 =new SimpleDateFormat("MMMM", Locale.ENGLISH).format(d);
			 if (!Month.equals(Month1))
			 {

				 cumul_mois.put(Month,absence_cumul/8);
				 Month = Month1;
				 
				 absence_cumul=(int)(charts.get(i)*8);
			 }
			 else
			 {
				 absence_cumul+=(int)(charts.get(i)*8);
				
			 }
			 
		 }

		 cumul_mois.put(Month,absence_cumul/8);
		return cumul_mois;
 	}
	public ArrayList<ChartMois> tauxAbscenceCumuleEmployeAnnee(int matricule,Date d1,Date d2)
	{
		Calendar calendarMin = new GregorianCalendar();
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 int year1 = 0;
		 int i=0;
		 Date d = null;
		 ArrayList<Chart> charts = tauxAbsenceEmployeeJour(matricule,d1, d2);//avoir la liste des taux absence employe
		 ArrayList<ChartMois> cumul_annee = new ArrayList<ChartMois>();//
		 try {
			 d=myFormat.parse(charts.get(0).getDate());
		 } catch (ParseException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 calendarMin.setTime(d);
		 int year =calendarMin.get(Calendar.YEAR);// avoir le mois de la premiere date
		 int absence_cumul = (int)(charts.get(0).getTaux_absence()*8) ;//nombre de journee absente de premier jour
		
		 for(i=1;i<charts.size();i++)
		 {
			 try {
				 d=myFormat.parse(charts.get(i).getDate());
			 } catch (ParseException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 calendarMin.setTime(d);
			 year1 =calendarMin.get(Calendar.YEAR);
			 if (year != year1)
			 {
				 ChartMois annee = new ChartMois();
				 annee.setMois(year);
				 annee.setJour_absence(absence_cumul/8);
				 year=year1;
				 cumul_annee.add(annee);
				 absence_cumul=(int)(charts.get(i).getTaux_absence()*8);
			 }
			 else
			 {
				 absence_cumul=absence_cumul + (int)(charts.get(i).getTaux_absence()*8);
				
			 }
			 
		 }
		 ChartMois annee = new ChartMois();
		 annee.setMois(year);
		 annee.setJour_absence(absence_cumul/8);
		 cumul_annee.add(annee);
		return cumul_annee;
 	}
	//public <type> taux_abscence_justifier_employe_par_jour();
	//public <type> taux_abscence_justifier_employe_par_mois();
	//public <type> taux_abscence_justifier_employe_par_annee();
	//public <type> afficher_info_employe();
	//public <type> Temps_de_travail();
	
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
	

}
