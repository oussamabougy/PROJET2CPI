package com.beans;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;



@ManagedBean
@RequestScoped
@SessionScoped

@ViewScoped
public class ChefService extends Employe implements LienDirChefSer,Serializable {
	
    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public void ChefSrvice(){};

	private List<Enter> entrants ;  // liste de pointage d'un service
	
	private List<Enter> filteredEntrants ;     //   liste filtrer de pointage d'un service
	
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


	public int idService(int matricule) //get the service_id of employee
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

	public ArrayList<Integer> employeeDeService(String service_nom)
	{
		PreparedStatement statement;
        ResultSet resultat;
        Connection connexion = null;
        int	id_service=0;
        ArrayList<Integer>	listMatricule=	new	ArrayList<Integer>();
        connexion = Database.loadDatabase();        
       
        try
        {	
        	//chercher	id_service	
        	statement =connexion.prepareStatement("SELECT id  FROM service WHERE nom_service  = ?;");
        	statement.setString(1,service_nom);
        	resultat = statement.executeQuery();
    		if(resultat.next())
    		{
    			id_service = resultat.getInt("id");
    		}
	///////

    		//chercherLesEployee

    		statement =connexion.prepareStatement("SELECT matricule  FROM employee WHERE service_id = ?;");
    		statement.setInt(1,id_service);
    		resultat=statement.executeQuery();

    		//replirLaListe
       		while(resultat.next())
       		{
     			listMatricule.add(resultat.getInt("matricule"));	  
       	   	 }
		//////

       		//ecrireDansLaConsole
       		/*for	(int	i=0;	i<listMatricule.size();i++)
       		{
       	     	System.out.print(listMatricule.get(i));
       		}*/
   	    	/////

        }
        catch(SQLException e) 
        {
            e.printStackTrace();

        }        
        return	listMatricule;
	}

	public	ArrayList<Chart>	tauxAbsenceServiceJour(String service_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(service_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceService = tauxAbsenceEmployeeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbsenceEmployeeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceService.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceService.get(k).getTaux_absence());
				absenceService.set(k,ch);
				
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		
		for	(int	k=0;k<absenceService.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence((float)absenceService.get(k).getTaux_absence()/(float)size);
			absenceService.set(k,ch);
			
			
		}	
		return	absenceService;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsenceServiceMois(String service_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(service_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absenceService = tauxAbscenceeEmployeMoi(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeMoi(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceService.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceService.get(k).getTaux_absence());
				absenceService.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absenceService.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absenceService.get(k).getTaux_absence()/(float)size);
			absenceService.set(k,ch);
			
			
		}	
		return	absenceService;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsenceServiceAnnee(String service_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(service_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absenceService = tauxAbscenceeEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceService.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceService.get(k).getTaux_absence());
				absenceService.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absenceService.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absenceService.get(k).getTaux_absence()/(float)size);
			absenceService.set(k,ch);
			
			
		}	
		return	absenceService;
	}

	
	public ArrayList<Chart> tauxAbsenceServiceCumuleJour(String nomService,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(nomService);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceService = tauxAbscenceCumuleEmployeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceService.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceService.get(k).getTaux_absence());
				absenceService.set(k,ch);
				
				
			}	
		}	
		
		
		
		for	(int	k=1;k<absenceService.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceService.get(k).getTaux_absence()+ absenceService.get(k-1).getTaux_absence());
			absenceService.set(k,ch);
			
			
		}	
		return	absenceService;
	}
	
	public ArrayList<ChartMois> tauxAbsenceServiceCumuleMois(String nomService,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(nomService);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absenceServicemois = tauxAbscenceCumuleEmployemois(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployemois(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceServicemois.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absenceServicemois.get(k).getJour_absence());
				absenceServicemois.set(k,ch);
				
				
			}
		}
		return absenceServicemois;
	}
		
	public ArrayList<ChartMois> tauxAbsenceServiceCumuleAnnee(String nomService,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeService(nomService);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absenceServiceannee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceServiceannee.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absenceServiceannee.get(k).getJour_absence());
				absenceServiceannee.set(k,ch);
				
				
			}
		}
		return absenceServiceannee;
	}
	
	
	
	
	
	
	public void defineChart1() 
	{
			System.out.print(debut);
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
		 if(debut != null && fin != null && nomService!=null)
		 {
			ArrayList<Chart> charts = tauxAbsenceServiceJour(nomService,debut, fin);
			abs1 = loadAbs(charts) ;
			for(Chart chart:charts)
			{
				System.out.print("jour:"+chart.getDate()+"nb absence:"+chart.getTaux_absence());
			}
		 }
	}
	public void tester1() 
	{
		//System.out.print(getRole(1000001));
		 SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String inputString1 = "2017-02-20";
		 String inputString2 = "2017-02-23";
		 Date d1 = null,d2 = null;
		 try {
		     d1 = myFormat.parse(inputString1);
		     d2 = myFormat.parse(inputString2);
		 } catch (ParseException e) {
		     e.printStackTrace();
		 }
		 ArrayList<Chart> jour = tauxAbsenceServiceJour("acquisition et traitement",d1, d2);
		 ArrayList<ChartmoisTaux> mois =  tauxAbsenceServiceMois("acquisition et traitement",d1, d2);
		 ArrayList<ChartmoisTaux> annee = tauxAbsenceServiceAnnee("acquisition et traitement",d1, d2);
		 for(Chart chart:jour)
		{
			System.out.print("jour : "+chart.getDate()+" nb absence : "+chart.getTaux_absence() +"%");
		}
		for(ChartmoisTaux chartmoistaux:mois)
		{
			System.out.print("mois : "+chartmoistaux.getMois()+" nb absence : "+chartmoistaux.getTaux_absence()+" %");
		}
		for(ChartmoisTaux chartmoistaux:annee)
		{
			System.out.print("annee : "+chartmoistaux.getMois()+" nb absence : "+chartmoistaux.getTaux_absence()+" %");
		}
	}

}
