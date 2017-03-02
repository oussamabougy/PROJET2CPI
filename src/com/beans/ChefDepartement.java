package com.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.chart.LineChartModel;

@ManagedBean
@SessionScoped

public class ChefDepartement extends EmployeGeneral implements LienDirChefDep {
	
	private String nomDepartement;
	
    private List<Enter> entrants = loadusers(); // liste de pointage d'un departement
	
	private List<Enter> filteredEntrants ;     //  liste filtrer de pointage d'un departement
	
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

	public int IdDepartement(int matricule) //get the departement_id of employee
	{
		PreparedStatement statement = null;
        Connection connexion = null;
        int IdDepartement = 1 ;
        
        try 
        {
            connexion = Database.loadDatabase();
	    	statement = connexion.prepareStatement("SELECT departement_id FROM employee WHERE matricule = ? ;");
	    	statement.setInt(1,matricule);

			ResultSet resultat = statement.executeQuery();
    	
	    	if(resultat.next())
	    	{

	    		IdDepartement = resultat.getInt("departement_id") ;
	    		System.out.print("IdDepartement = " + IdDepartement) ;

	    	}
    		
        }catch (SQLException e) {}
        finally 
        {
               try { if (connexion != null) connexion.close( );}
               catch (SQLException ignore) {}
        }
  	
        
        return IdDepartement ;
	}

	public List<Enter> loadusers()
	{
		
		List<Enter> tab = new ArrayList<Enter>();
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

    		int departementId = IdDepartement(mat) ;
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
	
	
	public String getNomDepartement() {
		return nomDepartement;
	}
	public void setNomDepartement(String nomDepartement) {
		this.nomDepartement = nomDepartement;
	}

	private LineChartModel abs1 = new LineChartModel() ;

	public LineChartModel getAbs1() {
		return abs1;
	}
	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
	
	ArrayList<String> departements = defineDepartement();
	
	
	public ArrayList<String> getDepartements() {
		return departements;
	}
	public void setDepartements(ArrayList<String> departements) {
		this.departements = departements;
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
	
	public ArrayList<Integer> employeeDeDepartement(String departement_nom)

	{
		PreparedStatement statement;
        ResultSet resultat;
        int	id_departement=0;
        Connection connexion = null;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       
        try
        {	
	//chercher	id_depatemente	
        	statement =connexion.prepareStatement("SELECT id  FROM departement WHERE nom_departement  = ?;");
        	statement.setString(1,departement_nom);
        	resultat=statement.executeQuery();
    		while(resultat.next())
    		{
    			id_departement=resultat.getInt("id");
    		}
	///////

    		//chercherLesEmployee

    		statement =connexion.prepareStatement("SELECT matricule  FROM employee WHERE departement_id = ?;");
    		statement.setInt(1,id_departement);
    		resultat=statement.executeQuery();

    		//replirLaListe
       		while(resultat.next())
       		{
     			listMatricule.add(resultat.getInt("matricule"));	  
       	   	 }
		//////

       		//ecrireDansLaConsole
       		for	(int	i=0;	i<listMatricule.size();i++)
       		{
       	     	System.out.print(listMatricule.get(i));
       		}
   	    	/////

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }
        
        return	listMatricule;
	}

	public	ArrayList<Chart>	tauxAbsenceDepartementJour(String departement_nom,Date debut, Date fin)
	{	
	
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(departement_nom);//remplir la liste par les matricule des	employee de departement pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceDepa=tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//absenceDepa <qui va	contenir le	taux d'absecne	de	departement	>par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEmployee

			for	(int	k=0;k<absenceDepa.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absenceDepa
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceDepa.get(k).getTaux_absence());
				absenceDepa.set(k,ch);
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employee	de	ce	depatement
		
		for	(int	k=0;k<absenceDepa.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceDepa.get(k).getTaux_absence()/size);
			absenceDepa.set(k,ch);
			
			
		}	
		return	absenceDepa;
	}
	
	public void defineChart2() 
	{
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
		 if(debut != null && fin != null && nomDepartement!=null)
		 {
			ArrayList<Chart> charts = tauxAbsenceDepartementJour(nomDepartement,debut, fin);
			abs1 = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
	}
}
