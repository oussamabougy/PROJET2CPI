

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

















import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.connect.Database;

@ManagedBean
@SessionScoped

public class EmployeGeneral1 
{
	protected int matricule = 5;
	protected String nom = "Mohamed";
	protected String prenom = "WAHIB";
	protected String motPasse;
	protected double salaire;
	protected String role = "Directeur";
	protected String grade;

	private int abs = 150;
	private int absJustifie = 53;
	private int absNonJustifie = 97;
	private int absMoy = 5;
	
	private String chartLine;
	private String chartBar;
	
	private List<String> listeService = defineService();
	private List<String> listeDepartement = defineDepartement();
	
	
	public List<String> getListeService() {
		return listeService;
	}

	public void setListeService(List<String> listeService) {
		this.listeService = listeService;
	}

	public List<String> getListeDepartement() {
		return listeDepartement;
	}

	public void setListeDepartement(List<String> listeDepartement) {
		this.listeDepartement = listeDepartement;
	}

	@PostConstruct
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
		 Map<String,Float> abse = tauxAbsenceEmployeeJour(1000010,d1, d2);

		 Map<String,Float> cumul =  tauxAbscenceCumuleEmployeJour(1000010,d1, d2);

		 chartLine = defineChart(abse);
		 chartBar = defineBar(cumul);

	}
	
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
	
	public String defineChart(Map<String,Float> charts)	
	{
		String k = "";
		for(String i: charts.keySet())
		{

			k+="{y: '"+i+"' , item1:"+ charts.get(i)+"},";
			
		}	
		System.out.print(k);
		return k;
	}
	
	public String defineBar(Map<String,Float> charts)	
	{
		String k = "";
		for(String i: charts.keySet())
		{
			k+="{y: '"+i+"' , a:"+ charts.get(i)+", b:0},";
			
		}	
		System.out.print(k);
		return k;
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
	public int getAbs() {
		return abs;
	}
	public void setAbs(int abs) {
		this.abs = abs;
	}
	public int getAbsJustifie() {
		return absJustifie;
	}
	public void setAbsJustifie(int absJustifie) {
		this.absJustifie = absJustifie;
	}
	public int getAbsNonJustifie() {
		return absNonJustifie;
	}
	public void setAbsNonJustifie(int absNonJustifie) {
		this.absNonJustifie = absNonJustifie;
	}
	public int getAbsMoy() {
		return absMoy;
	}
	public void setAbsMoy(int absMoy) {
		this.absMoy = absMoy;
	}

	
    public String getChartLine() {
		return chartLine;
	}
	public void setChartLine(String chartLine) {
		this.chartLine = chartLine;
	}
	

	public String getChartBar() {
		return chartBar;
	}

	public void setChartBar(String chartBar) {
		this.chartBar = chartBar;
	}

	
	public ArrayList<String> defineService()
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<String>	serviceList=	new	ArrayList<String>();
        connexion = Database.loadDatabase();        
       
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT nom_service  FROM service");
        	resultat = statement.executeQuery();
    		while(resultat.next())
    		{
    			serviceList.add(resultat.getString("nom_service"));
    		}

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	serviceList;
	}







	private List<Enter> entrants = loadusers(); // liste de pointage d'un seul employee
	
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
            
        	/*HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);
        	
        	int mat = (int) session.getAttribute("matricule") ;
    		System.out.print("matriculedazdazs = "+mat) ;*/
   
    		statement.setInt(1,1000010) ;

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

	
private List<Enter> entrants1 = loadusers1(); // liste de pointage d'un seul employee
	
	private List<Enter> filteredEntrants1 ;
	
	
	public List<Enter> getFilteredEntrants1() {
		return filteredEntrants1;
	}

	public void setFilteredEntrants1(List<Enter> filteredEntrants1) {
		this.filteredEntrants1 = filteredEntrants1;
	}

	public List<Enter> getEntrants1() {
		return entrants1;
	}

	public void setEntrants1(List<Enter> entrants1) {
		this.entrants1 = entrants1;
	}
	
	
	public int IdService(int matricule) //get the service_id of employee
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

	public List<Enter> loadusers1()
	{
		
		List<Enter> tab = new ArrayList<Enter>();
		PreparedStatement statement = null;
        ResultSet resultat = null;
        Connection connexion = null;
        connexion = Database.loadDatabase();      
        try {
            statement = connexion.prepareStatement("SELECT * FROM pointage ORDER BY matricule ;");

            
        	/*HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
    				.getExternalContext().getSession(false);*/
        	
        	int mat = 1000010;//(int) session.getAttribute("matricule") ;
    	//	System.out.print("matricule = "+mat) ;

    		int serviceId = IdService(mat) ;
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

            	Enter user = new Enter ();
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

	public ArrayList<String> defineDepartement()
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<String>	departementList=	new	ArrayList<String>();
        connexion = Database.loadDatabase();        
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT nom_departement  FROM departement");
        	resultat = statement.executeQuery();
    		while(resultat.next())
    		{
    			departementList.add(resultat.getString("nom_departement"));
    		}

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	departementList;
	}
}
