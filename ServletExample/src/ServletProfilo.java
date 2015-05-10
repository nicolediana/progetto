import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Calendar;

public class ServletProfilo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ConnessioneDB connect=new ConnessioneDB();
	  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		ResultSet rs2;
		PrintWriter writer = response.getWriter();
		String sql ="";
		
		// Recupero dei dati dalla Json in ingresso
		try {
			System.out.println("servlet profilo");
			StringBuilder sb = new StringBuilder();
		    String s;
		    while ((s = request.getReader().readLine()) != null) {
		    	sb.append(s);
		    }
		    
		    JSONObject jObj = new JSONObject(sb.toString());
		    String tiporichiesta= jObj.get("tiporichiesta").toString();
		    
		    String password,nome,cognome,nickname,citta,sesso,livello;
		    String linkfotoprofilo="";
		    Integer annonascita,cellulare=0;
		    Integer voto,idcredenziali,idprofilo;
		    
		    conn = connect.openConnection();
			stmt = conn.createStatement();
			
			// Esegui una query SQL, ottieni un ResultSet
//--------------------------------------CREA PROFILO--------------------------------------------------------------			
//c'è anche il controllo per veder se il nickname è stato già assegnato a qualcun altro
			if(tiporichiesta.equals("crea"))
			{
				System.out.println("crea");
				nome= jObj.get("nome").toString();
			    cognome= jObj.get("cognome").toString();
			    nickname= jObj.get("nickname").toString();
			    citta= jObj.get("citta").toString();
			    annonascita= Integer.parseInt(jObj.get("annonascita").toString());			    
			    cellulare= Integer.parseInt(jObj.get("cellulare").toString());
			    sesso= jObj.get("sesso").toString();
			    //linkfotoprofilo
			    voto=100; //x ogni partita vinta=+10 pareggio=+5 perdita=-5 bidone=-10
			    idcredenziali= Integer.parseInt(jObj.get("idcredenziali").toString());
			    idprofilo=0;
			    
			    JSONObject jsonObj = new JSONObject();
				sql = "SELECT * FROM profilo WHERE nickname='"+nickname+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					jsonObj.put("risposta", "no");
				}
				else {
			  
			    //memorizzare profilo
					sql = "INSERT INTO profilo (nome, cognome, nickname, sesso, citta, annonascita, voto, cellulare, linkfotoprofilo, idcredenziali) VALUES ('"+nome+"', '"+cognome+"', '"+nickname+"', '"+sesso+"', '"+citta+"', '"+annonascita+"', '"+voto+"', '"+cellulare+"', '"+linkfotoprofilo+"', '"+idcredenziali+"')";
					stmt.executeUpdate(sql);
				
					sql = "SELECT idprofilo FROM profilo WHERE idcredenziali='"+idcredenziali+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						idprofilo=Integer.parseInt(rs.getString("idprofilo"));
						sql = "UPDATE credenziali SET idprofilo = '"+idprofilo+"' WHERE idcredenziali='"+idcredenziali+"'";
						stmt.executeUpdate(sql);				
					}
					//writer.println(idcredenziali.toString().toJson());
					jsonObj.put("risposta", "si");
				}
				jsonObj.put("idprofilo", idprofilo);
				writer.write(jsonObj.toString());
			}
//--------------------------------------LEGGI PROFILO--------------------------------------------------------------
			//GET
			if(tiporichiesta.equals("leggi"))
			{
				System.out.println("leggi");
				idprofilo= Integer.parseInt(jObj.get("idprofilo").toString());
				
				sql = "SELECT * FROM profilo WHERE idprofilo='"+idprofilo+"'";
				rs = stmt.executeQuery(sql);
				
				JSONObject jsonObj = new JSONObject();
				if(rs.next()) {
					nome=rs.getString("nome");
					cognome=rs.getString("cognome");
					nickname=rs.getString("nickname");
					annonascita=(Integer) rs.getObject("annonascita");
					sesso=rs.getString("sesso");
					citta=rs.getString("citta");
					cellulare=(Integer) rs.getObject("cellulare");
					linkfotoprofilo=rs.getString("linkfotoprofilo");					
				    
					jsonObj.put("nome", nome);
					jsonObj.put("cognome", cognome);
					jsonObj.put("nickname", nickname);
					jsonObj.put("annonascita", annonascita);
					jsonObj.put("sesso", sesso);
					jsonObj.put("citta", citta);
					jsonObj.put("cellulare", cellulare);
					jsonObj.put("linkfotoprofilo", linkfotoprofilo);
				}
				//writer.println(idcredenziali.toString().toJson());
				jsonObj.put("idprofilo", idprofilo);
				
				writer.write(jsonObj.toString());
			}

//--------------------------------------AGGIORNA PROFILO--------------------------------------------------------------
			//PUT -update
			if(tiporichiesta.equals("aggiorna"))
			{
				System.out.println("aggiorna");
				//per modificaProfilo ho anche password da aggiornare solo se non nullo
				password= jObj.get("password").toString();
			    nome= jObj.get("nome").toString();
			    cognome= jObj.get("cognome").toString();
			    nickname= jObj.get("nickname").toString();
			    citta= jObj.get("citta").toString();
			    annonascita= Integer.parseInt(jObj.get("annonascita").toString());
			    sesso= jObj.get("sesso").toString();
			    cellulare= Integer.parseInt(jObj.get("cellulare").toString());
			    idprofilo= Integer.parseInt(jObj.get("idprofilo").toString());
			    //linkfotoprofilo
			    String risposta="si";
			    
			    //controllo che nickname non sia già esistente
			    JSONObject jsonObj = new JSONObject();
				sql = "SELECT * FROM profilo WHERE nickname='"+nickname+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					Integer temp=Integer.parseInt(rs.getString("idprofilo"));	
					if(idprofilo!=temp)
						risposta="no";	
				}
				if(risposta.equals("si")) {
					//memorizzare profilo
					sql = "UPDATE profilo SET nome='"+nome+"', cognome='"+cognome+"', nickname='"+nickname+"', sesso='"+sesso+"', citta='"+citta+"', annonascita='"+annonascita
							+"', cellulare='"+cellulare+"', linkfotoprofilo='"+linkfotoprofilo+"' WHERE idprofilo='"+idprofilo+"'";
					stmt.executeUpdate(sql);
					System.out.println("psw:"+password);
					if(!password.equals(null)||password!=" "){
						if(!password.isEmpty())
						{sql = "UPDATE credenziali SET password='"+password+"' WHERE idprofilo='"+idprofilo+"'";
						stmt.executeUpdate(sql);	}			
					}					
				}
				jsonObj.put("risposta", risposta);
				jsonObj.put("idprofilo", idprofilo);
				writer.write(jsonObj.toString());
			}
			//fine put
//--------------------------------------VISUALIZZA PROFILO--------------------------------------------------------------
			//GET
			if(tiporichiesta.equals("vediprofilo"))
			{
				System.out.println("vediprofilo");
				idprofilo= Integer.parseInt(jObj.get("idprofilo").toString());
				
				sql = "SELECT * FROM profilo WHERE idprofilo='"+idprofilo+"'";
				rs = stmt.executeQuery(sql);
				
				JSONObject jsonObj = new JSONObject();
				if(rs.next()) {
					nickname=rs.getString("nickname");
					annonascita=(Integer) rs.getObject("annonascita");
					citta=rs.getString("citta");
					linkfotoprofilo=rs.getString("linkfotoprofilo");
					voto=(Integer) rs.getObject("voto");
					//livello principiante esperto professionista serie A B C
					livello="";
				    if(voto <= 200)
				    	livello="principiante di serie C";
				    if(voto <= 300 && voto >= 200)
				    	livello="esperto di serie C";
				    if(voto <= 400 && voto >= 300)
				    	livello="professionista di serie C";
				    if(voto <= 500 && voto >= 400)
					    livello="principiante di serie B";
				    if(voto <= 600 && voto >= 500)
				    	livello="esperto di serie B";
				    if(voto <= 700 && voto >= 600)
				    	livello="professionista di serie B";
				    if(voto <= 800 && voto >= 700)
				    	livello="principiante di serie A";
				    if(voto <= 900 && voto >= 800)
				    	livello="esperto di serie A";
				    if(voto <= 1000 && voto >= 900)
				    	livello="professionista di serie A";
				    
					Integer currentYear=Calendar.getInstance().get(Calendar.YEAR);
					Integer eta= currentYear - annonascita;
					jsonObj.put("nickname", nickname);
					jsonObj.put("eta", eta);
					jsonObj.put("citta", citta);
					jsonObj.put("linkfotoprofilo", linkfotoprofilo);
					jsonObj.put("livello", livello);
				}
				
				Integer bidone=0;
				Integer partite_giocate=0;
				Integer partite_vinte=0;
				Integer partite_perse=0;
				Integer pareggi=0;
				
				sql = "SELECT * FROM profilo-partita WHERE idprofilo='"+idprofilo+"'";
				rs = stmt.executeQuery(sql);				
				while(rs.next()) {
					bidone +=(Integer) rs.getObject("bidone");
					partite_giocate += 1;
					Integer idpartita=(Integer) rs.getObject("idpartita");
					
					String sql2 = "SELECT * FROM squadra WHERE idpartita='"+idpartita+"'";
					rs2 = stmt.executeQuery(sql2);					
					if(rs2.getString("esito") == "VITTORIA")
						partite_vinte += 1;
					if(rs2.getString("esito") == "SCONFITTA")
						partite_perse += 1;
					if(rs2.getString("esito") == "PAREGGIO")
						pareggi += 1;				
					
				}
				//writer.println(idcredenziali.toString().toJson());
				jsonObj.put("idprofilo", idprofilo);
				jsonObj.put("bidoni", bidone);
				jsonObj.put("partite_giocare", partite_giocate);
				jsonObj.put("partite_vinte", partite_vinte);
				jsonObj.put("partite_perse", partite_perse);
				
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
	

}
