package com.beans;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	public static Connection loadDatabase() 
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/mydb", "root", "admin");
			return con;
		} 
		catch (Exception ex) 
		{
			System.out.println("Database.getConnection() Error -->"
					+ ex.getMessage());
			return null;
		}
	}

}
