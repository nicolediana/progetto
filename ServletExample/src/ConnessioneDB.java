import java.sql.*;

public class ConnessioneDB {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Connection conn = null;
	
	
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "calcio";
	String userName = "root";
	String password = "";

	
	/*
	String url = "jdbc:mysql://127.11.139.2:3306/";
	String dbName = "sirio";
	String userName = "adminlL8hBfI";
	String password = "HPZjQCQsnVG4";
	*/	
	
	public Connection openConnection(){
		try {
			conn = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connessione al DataBase stabilita!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}

	public void closeConnection(Connection conn){
		try {
			  conn.close();
			  System.out.println(" Chiusa connessione al DB!");
			  }
			  catch (SQLException e) {
				  e.printStackTrace();
			  }
			  }

}
