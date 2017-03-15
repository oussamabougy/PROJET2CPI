package com.beans;
import com.connect.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped

public class ChefDepartement extends EmployeGeneral implements LienDirChefDep {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nomDepartement;
	
	public int idDepartement(int matricule) //get the departement_id of employee
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

	
	
	
	public String getNomDepartement() {
		return nomDepartement;
	}
	public void setNomDepartement(String nomDepartement) {
		this.nomDepartement = nomDepartement;
	}

	
	
	


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
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	depatement
		
		for	(int	k=0;k<absenceDepa.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceDepa.get(k).getTaux_absence()/size);
			absenceDepa.set(k,ch);
			
			
		}	
		return	absenceDepa;
	}
	 
	public	ArrayList<ChartmoisTaux>	tauxAbsenceDepartementMois(String departement_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(departement_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absenceDepartement = tauxAbscenceeEmployeMoi(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeMoi(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceDepartement.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceDepartement.get(k).getTaux_absence());
				absenceDepartement.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absenceDepartement.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absenceDepartement.get(k).getTaux_absence()/(float)size);
			absenceDepartement.set(k,ch);
			
			
		}	
		return	absenceDepartement;
	}

	public	ArrayList<ChartmoisTaux>	tauxAbsenceDepartementAnnee(String departement_nom,Date debut, Date fin)
	{			
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(departement_nom);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartmoisTaux> absenceDepartement = tauxAbscenceeEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartmoisTaux> absenceEmployee=new	ArrayList<ChartmoisTaux>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceeEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceDepartement.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartmoisTaux ch=new	ChartmoisTaux() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceDepartement.get(k).getTaux_absence());
				absenceDepartement.set(k,ch);
					
				
			}	
		}	
		
		int	size=listEmployee.size();//le	nombre	des	employee	de	ce	service
		System.out.println(size);
		for	(int	k=0;k<absenceDepartement.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			ChartmoisTaux ch=new	ChartmoisTaux() ;
			ch.setMois(absenceEmployee.get(k).getMois());
			ch.setTaux_absence((float)absenceDepartement.get(k).getTaux_absence()/(float)size);
			absenceDepartement.set(k,ch);
			
			
		}	
		return	absenceDepartement;
	}

	
	
	
	
	public ArrayList<Chart> tauxAbsenceDepartementCumuleJour(String nomdepartement,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(nomdepartement);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<Chart> absenceDepartement = tauxAbscenceCumuleEmployeJour(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<Chart> absenceEmployee=new	ArrayList<Chart>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeJour(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceDepartement.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				Chart ch=new	Chart() ;
				ch.setDate(absenceEmployee.get(k).getDate());
				ch.setTaux_absence(absenceEmployee.get(k).getTaux_absence()	+	absenceDepartement.get(k).getTaux_absence());
				absenceDepartement.set(k,ch);
				
				
			}	
		}	
		
		
		
		for	(int	k=1;k<absenceDepartement.size();k++)//boucle	pour	faire	la	devision	sur	le	nombre	d'employee
		{
			Chart ch=new	Chart() ;
			ch.setDate(absenceEmployee.get(k).getDate());
			ch.setTaux_absence(absenceDepartement.get(k).getTaux_absence()+ absenceDepartement.get(k-1).getTaux_absence());
			absenceDepartement.set(k,ch);
			
			
		}	
		return	absenceDepartement;
	}
	
	public ArrayList<ChartMois> tauxAbsenceDepartementCumuleMois(String nomdepartement,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(nomdepartement);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absenceDepartementemois = tauxAbscenceCumuleEmployemois(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployemois(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceDepartementemois.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absenceDepartementemois.get(k).getJour_absence());
				absenceDepartementemois.set(k,ch);
				
				
			}
		}
		return absenceDepartementemois;
	}
		
	public ArrayList<ChartMois> tauxAbsenceDepartementCumuleAnnee(String nomdepartement,Date d1,Date d2)
	{
		ArrayList<Integer>	listEmployee=new	ArrayList<Integer>();
		listEmployee=employeeDeDepartement(nomdepartement);//remplir la liste par les matricule	des	employee de service pour 
		//faire la somme des taux la liste va contenir	/matricule0/matricule1/...../matriculei/....
						
					
		ArrayList<ChartMois> absenceDepartementannee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(0), debut,  fin);//initialiser	la	liste	
			//qui	va	contenir	le	taux	d'absecne	de	service	par	le	taux	d'absence	d'employee	0
		
		ArrayList<ChartMois> absenceEmployee=new	ArrayList<ChartMois>();
	
		
		
		for(int	i=1	;i<listEmployee.size();i++)
		{			
			absenceEmployee = tauxAbscenceCumuleEmployeAnnee(listEmployee.get(i),debut,fin);//affecter le taux	d'absence	de	chaque
																				//	employee	a	la	liste	absenceEployee

			for	(int	k=0;k<absenceDepartementannee.size();k++)	//boucle	pour	faire	la	somme	des	taux	
															//et	le	mettre	dans	absence	Employee
			{
				ChartMois ch=new	ChartMois() ;
				ch.setMois(absenceEmployee.get(k).getMois());
				ch.setJour_absence(absenceEmployee.get(k).getJour_absence()	+	absenceDepartementannee.get(k).getJour_absence());
				absenceDepartementannee.set(k,ch);
				
				
			}
		}
		return absenceDepartementannee;
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
