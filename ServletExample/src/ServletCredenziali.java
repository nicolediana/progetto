import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.*;

public class ServletCredenziali extends HttpServlet { 
	
	private static final long serialVersionUID = 1L;
	private ConnessioneDB connect=new ConnessioneDB();
	  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		PrintWriter writer = response.getWriter();
		
		// Recupero dei dati dalla Json in ingresso
		try {
			StringBuilder sb = new StringBuilder();
		    String s;
		    while ((s = request.getReader().readLine()) != null) {
		    	sb.append(s);
		    }
		    
		    JSONObject jObj = new JSONObject(sb.toString());
		    String email= jObj.get("email").toString();
		    String password= jObj.get("password").toString();
		    String tiporichiesta= jObj.get("tiporichiesta").toString();
		    Integer idcredenziali=0;
		    
		    conn = connect.openConnection();
			stmt = conn.createStatement();
			System.out.println("servlet credenziali");
			// Esegui una query SQL, ottieni un ResultSet
			
			if(tiporichiesta.equals("crea"))
			{
				//controllo email prima di memorizzare
				String sql = "SELECT * FROM credenziali WHERE email='"+email+"'";
			
				ResultSet rs1=stmt.executeQuery(sql);
				
				if(rs1.next()) {
					idcredenziali=0;
				}
				else {
					sql = "Insert INTO credenziali (email, password) VALUES ('"+email+"', '"+password+"')";
					stmt.executeUpdate(sql);
			
					sql = "SELECT idcredenziali FROM credenziali WHERE email='"+email+"'and password='"+password+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						idcredenziali=Integer.parseInt(rs.getString("idcredenziali"));
					}
				}
				//writer.println(idcredenziali.toString().toJson());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("idcredenziali", idcredenziali);
				writer.write(jsonObj.toString());
			}
			//GET
			if(tiporichiesta.equals("leggi"))
			{
				Integer idprofilo=0;
				String sql = "SELECT idcredenziali, idprofilo  FROM credenziali WHERE email='"+email+"'and password='"+password+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					idcredenziali=Integer.parseInt(rs.getString("idcredenziali"));
					idprofilo=(Integer) rs.getObject("idprofilo");
					if(idprofilo==null)
						idprofilo=0;
				}
				else {
					idcredenziali=0;
				}
				//writer.println(idcredenziali.toString().toJson());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("idcredenziali", idcredenziali);
				jsonObj.put("idprofilo", idprofilo);
				writer.write(jsonObj.toString());
			}
				
		} catch (SQLException | JSONException e) {
			  e.printStackTrace();
	  }
	  finally {
	  // Chiudere sempre la connessione alla base di dati
	  if (conn != null)
	  connect.closeConnection(conn);
	  }
	 } 
	
	
/*	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		//response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String id = request.getParameter("idcredenziali");
		
		try {
		// Crea un oggetto Statement
		conn = connect.openConnection();
		stmt = conn.createStatement();
		System.out.println("sono in post");
		// Esegui una query SQL, ottieni un ResultSet
		String sql = "Update credenziali SET email='"+email+"',password= '"+password+"' WHERE idcredenziali='"+id+"'";
		int result=stmt.executeUpdate(sql);
		
		} catch (SQLException e) {
			  e.printStackTrace();
		  }
	  finally {
	  // Chiudere sempre la connessione alla base di dati
	  if (conn != null)
	  connect.closeConnection(conn);
	  }	  
	 } 
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		//response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("idcredenziali");
		
		try {
		// Crea un oggetto Statement
		conn = connect.openConnection();
		stmt = conn.createStatement();
		System.out.println("sono in post");
		// Esegui una query SQL, ottieni un ResultSet
		String sql = "DELETE FROM credenziali WHERE idcredenziali='"+id+"'";
		int result=stmt.executeUpdate(sql);
		
		} catch (SQLException e) {
			  e.printStackTrace();
		  }
	  finally {
	  // Chiudere sempre la connessione alla base di dati
	  if (conn != null)
	  connect.closeConnection(conn);
	  }
	 } 
	*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		PrintWriter writer = response.getWriter();
		Credenziali cred=new Credenziali();
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		try {
		conn = connect.openConnection();
		stmt = conn.createStatement();
		
		// Esegui una query SQL, ottieni un ResultSet
		String sql = "SELECT idcredenziali, idprofilo  FROM credenziali WHERE email='"+email+"'and password='"+password+"'";
		rs = stmt.executeQuery(sql);
		if(rs.next()) {
				cred.setIdcredenziali(Integer.parseInt(rs.getString("idcredenziali")));
				Integer idprof=(Integer) rs.getObject("idprofilo");
				if(idprof==null)
					cred.setIdprofilo(0);
				else
					cred.setIdprofilo(Integer.parseInt(rs.getString("idprofilo")));						
			}
		else {
			cred.setIdcredenziali(0);
		}
		
		writer.println(cred.toJson());
		
		}catch (SQLException e) {
			  e.printStackTrace();
		  }
	  finally {
	  // Chiudere sempre la connessione alla base di dati
	  if (conn != null)
	  connect.closeConnection(conn);
	  }
	  
	 } 
	
  	}  