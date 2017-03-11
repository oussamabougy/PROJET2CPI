package com.beans;

import java.io.Serializable;
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
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.LineChartModel;

@ManagedBean


public class Directeur extends EmployeGeneral  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Enter> entrants = loadusers();   // liste de pointage general (tout les employees)
	
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
            statement = connexion.prepareStatement("SELECT * FROM pointage ORDER BY matricule ;");

            // Exécution de la requête

            resultat = statement.executeQuery();
            
            // Récupération des données
        	
            while (resultat.next())
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
	
	public	ArrayList<Chart>	tauxAbsenceGeneraleJour(Date debut, Date fin)
	{	
	
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee = tousLesEmployee();;//remplir la liste par les matricule de	tous	les	employee  pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceGene = tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//absenceGene <qui va	contenir le	taux d'absecne	generale	>par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee = new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																		

		//	employee	a	la	liste	absenceEmployee

			for	(int	k=0;k<absenceGene.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absenceGene
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceGene.get(k).getTaux_absence

());
				absenceGene.set(k,ch);
				
				
			}	
		}	
		
		int	size=listEmployee.size()+1;//le	nombre	des	employees	
		for	(int	k=0;k<absenceGene.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceGene.get(k).getTaux_absence()/size);
			absenceGene.set(k,ch);
			
			
		}	
		return	absenceGene;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsenceGeneralMois(Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absencegeneral = tauxAbscenceeEmployeMoi(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeMoi(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absencegeneral.get(k).getTaux_absence()/(float)size);
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsencegeneralAnnee(Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absencegeneral = tauxAbscenceeEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absencegeneral.get(k).getTaux_absence()/(float)size);
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}

	
	public ArrayList<Chart> tauxAbsenceGeneralCumuleJour(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absencegeneral = tauxAbscenceCumuleEmployeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneral.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absencegeneral.get(k).getTaux_absence());
				absencegeneral.set(k,ch);
				
				
			}	
		}	
		
		
		
		for	(int	k=1;k<absencegeneral.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absencegeneral.get(k).getTaux_absence()+ absencegeneral.get(k-1).getTaux_absence());
			absencegeneral.set(k,ch);
			
			
		}	
		return	absencegeneral;
	}
	
	public ArrayList<ChartMois> tauxAbsenceGeneralCumuleMois(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absencegeneralemois = tauxAbscenceCumuleEmployemois(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployemois(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneralemois.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absencegeneralemois.get(k).getJour_absence());
				absencegeneralemois.set(k,ch);
				
				
			}
		}
		return absencegeneralemois;
	}
		
	public ArrayList<ChartMois> tauxAbsenceGeneralCumuleAnnee(Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=tousLesEmployee();//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absencegeneralannee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absencegeneralannee.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absencegeneralannee.get(k).getJour_absence());
				absencegeneralannee.set(k,ch);
				
				
			}
		}
		return absencegeneralannee;
	}
	
	public ArrayList<Integer> tousLesEmployee()

	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       int	mat=0;
       try
        {	
	
	///////

	//chercherLesEmployee

    	statement =connexion.prepareStatement("SELECT matricule  FROM employee ;");
       	resultat=statement.executeQuery();

	//replirLaListe
       		while(resultat.next())
       		{
     			mat=resultat.getInt("matricule");
     			listMatricule.add(mat);	  
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
	
	protected LineChartModel abs1 = new LineChartModel() ;
	
	public void defineChart1() 
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
			ArrayList<Chart> charts = tauxAbsenceGeneraleJour(debut, fin);
			abs1 = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
		
		//System.out.print(debut+" "+fin);
	}
	
	public LineChartModel getAbs1() {
		return abs1;
	}

	public void setAbs1(LineChartModel abs1) {
		this.abs1 = abs1;
	}
}
