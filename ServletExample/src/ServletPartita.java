import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Vector;

public class ServletPartita extends HttpServlet {
	
	
	private String nomecampo;
	private String indirizzocampo;
	private String citta;
	private String provincia;
	private Float costo;
	private String data;
	private String ora;
	private Integer amministratore;
	private String tiporichiesta;
	private Integer idpartita;
	private String linkfotocampo;
	private String terreno;
	private String coperto;
	private String note;
	private String tipopartita;
	private Integer idtipopartita;
	private Integer numsquadre;
	private Integer numgiocatori;
	private String contatto;
	private static final long serialVersionUID = 1L;
	String sql="";
	int idprofilo;
	
	private ConnessioneDB connect=new ConnessioneDB();
	  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Connection conn = null;
		Statement stmt = null;		
		ResultSet rs,rs2;
		PrintWriter writer = response.getWriter();

		// Recupero i dati dalla richiesta
		try {
			System.out.println("Servlet partita");
			StringBuilder sb = new StringBuilder();
		    String s;
		    while ((s = request.getReader().readLine()) != null) {
		    	sb.append(s);
		    }		    
		    JSONObject jObj = new JSONObject(sb.toString());
		    tiporichiesta= jObj.get("tiporichiesta").toString();	
			citta= jObj.get("citta").toString();
		    provincia= jObj.get("provincia").toString();		    
		    
		    conn = connect.openConnection();
			stmt = conn.createStatement();			
								
//-----------------------------CREA PARTITA----------------------------------------
			//PUT ->crea partita. PartitaActivity
			if(tiporichiesta.equals("crea"))
			{
				nomecampo= jObj.get("nomecampo").toString();
			    indirizzocampo= jObj.get("indirizzocampo").toString();
			    data= jObj.get("data").toString();
			    ora= jObj.get("ora").toString();
			    costo= Float.parseFloat(jObj.get("costo").toString());
			    amministratore = Integer.parseInt(jObj.get("amministratore").toString());
			    contatto = jObj.get("contatto").toString();
			    linkfotocampo="";
			    terreno= jObj.get("terreno").toString();
			    coperto= jObj.get("coperto").toString();
			    note= jObj.get("note").toString();
			    tipopartita= jObj.get("tipopartita").toString();
			    sql = "SELECT * FROM tipopartita WHERE tipopartita='"+tipopartita+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					idtipopartita=Integer.parseInt(rs.getString("idtipopartita"));
					numsquadre=Integer.parseInt(rs.getString("numsquadre"));
					numgiocatori=Integer.parseInt(rs.getString("numgiocatori"));					
				}
			    
				//Memorizza parita
				sql = "INSERT INTO partita (idtipopartita, nomecampo, indirizzocampo, citta, provincia, costo, data, ora, terreno, coperto, note, linkfotocampo,amministratore, contatto) VALUES ('"+idtipopartita+"','"+nomecampo+"', '"+indirizzocampo+"', '"+citta.toUpperCase()+"', '"+provincia.toUpperCase()+"', '"+costo+"', '"+data+"', '"+ora+"', '"+terreno+"', '"+coperto+"', '"+note+"', '"+linkfotocampo+"', '"+amministratore+"', '"+contatto+"')";
				stmt.executeUpdate(sql);
				//potrei fare la select col timestamp creazione della partita
				sql = "SELECT idpartita FROM partita WHERE idtipopartita='"+idtipopartita+"' and nomecampo='"+nomecampo+"' and indirizzocampo='"+indirizzocampo+"' and citta='"+citta+"' and provincia='"+provincia+"' and data='"+data+"' and ora='"+ora+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					idpartita=Integer.parseInt(rs.getString("idpartita"));
				}
				for(int i=0;i<numsquadre;i++){
					//crea squadre
					Integer squadra=i+1;
					sql = "INSERT INTO squadra (idpartita, idtipopartita, squadra) VALUES ('"+idpartita+"','"+idtipopartita+"','"+squadra+"')";
					stmt.executeUpdate(sql);					
				}
				sql = "SELECT idsquadra FROM squadra WHERE idpartita='"+idpartita+"' and idtipopartita='"+idtipopartita+"'";		    
				rs = stmt.executeQuery(sql);
				
				Vector<String> quer = new Vector<String>();
				Vector<Integer> idsquad = new Vector<Integer>();
				Vector<String> quer2 = new Vector<String>();
				while(rs.next()) {
					//per ogni squadra crea ruolo ->tab squadra-ruolo
					Integer idsquadra=Integer.parseInt(rs.getString("idsquadra"));
			    	String sql2 = "SELECT idruolo FROM ruolo WHERE idtipopartita='"+idtipopartita+"'";		    
			    	quer.add(sql2);
			    	idsquad.add(idsquadra);
				}
				for(int k=0;k<quer.size();k++)
				{
					Integer idsquadra=idsquad.get(k);
					rs2 = stmt.executeQuery(quer.get(k));
					while(rs2.next()) {
						Integer idruolo=Integer.parseInt(rs2.getString("idruolo"));	
						String sql3 = "INSERT INTO squadra_ruolo (idsquadra, idruolo) VALUES ('"+idsquadra+"','"+idruolo+"')";
						quer2.add(sql3);
					}
				} 
				for(int z=0;z<quer2.size();z++)
				{
					stmt.executeUpdate(quer2.get(z));	
				}
				//ritornare idpartita
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("idpartita", idpartita);
				writer.write(jsonObj.toString()); 				
			}
			
//------------------------------CERCA PARTITE-----------------------------------------
			//GET ->cerca partite->elenco PartecipaActivity
			if(tiporichiesta.equals("leggi")&&(!citta.equals("null") || !provincia.equals("null")))
			{
				Vector<Partita> v = new Vector<Partita>();
				Vector<Partita> v2 = new Vector<Partita>();
				Vector<String> quer = new Vector<String>();
			    
			    if(citta.equals("null")){
			    	System.out.println("Ricerca per provincia");
			    	sql = "SELECT * FROM partita WHERE provincia='"+provincia.toUpperCase()+"'";
			    }
			    if(provincia.equals("null")){
			    	System.out.println("Ricerca per città");
			    	sql = "SELECT * FROM partita WHERE citta='"+citta.toUpperCase()+"'";
				}
			    if(!provincia.equals("null")&&!citta.equals("null")){
			    	System.out.println("Ricerca per provincia e citta");
			    	sql = "SELECT * FROM partita WHERE provincia='"+provincia.toUpperCase()+"' and citta='"+citta.toUpperCase()+"'" ;
			    }
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
			    	Partita part = new Partita();
					part.setIdpartita(Integer.parseInt(rs.getString("idpartita")));	
					part.setIdtipopartita(Integer.parseInt(rs.getString("idtipopartita")));					
					part.setNomecampo(rs.getString("nomecampo"));
					part.setIndirizzocampo(rs.getString("indirizzocampo"));
					part.setCosto(Float.parseFloat(rs.getString("costo")));
					part.setData(rs.getString("data"));
					part.setOra(rs.getString("ora"));
					part.setCitta(rs.getString("citta"));
					part.setProvincia(rs.getString("provincia"));
					part.setLinkfotocampo(rs.getString("linkfotocampo"));
					part.setTerreno(rs.getString("terreno"));
					part.setCoperto(rs.getString("coperto"));
					part.setNote(rs.getString("note"));
					part.setAmministratore(Integer.parseInt(rs.getString("amministratore")));
					part.setContatto(rs.getString("contatto"));					
					
					idpartita=Integer.parseInt(rs.getString("idpartita"));
					idtipopartita=Integer.parseInt(rs.getString("idtipopartita"));
					sql = "SELECT idprofilo FROM profilo_partita WHERE idpartita='"+idpartita+"'";
					quer.add(sql);
					v.add(part);
				}
				//vedere se è al completo
				Integer cont=0;
				for(int i=0;i<quer.size();i++)
				{
					rs = stmt.executeQuery(quer.get(i));
					if(rs.next()) {
					while(rs.next()) {
						cont +=1;
					}}
					else System.out.println("nessun giocatore");
					sql = "SELECT numgiocatori FROM tipopartita WHERE idtipopartita='"+idtipopartita+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						numgiocatori=Integer.parseInt(rs.getString("numgiocatori"));					
					}
					if(cont<numgiocatori)
					{
						v2.add(v.get(i));		
					}				
					//if (part.getNmancanti()>0)
					//	v.add(part);
				}				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("jsonVector", v2);
				writer.write(jsonObj.toString());
			}
			
			
//------------------------------DETTAGLIO PARTITA----------------------------------
			// riepilogo/dettaglio di una partita ConfermaPartecipaActivity
			if(tiporichiesta.equals("leggi")&& citta.equals("null") && provincia.equals("null"))
			{
				idpartita = Integer.parseInt(jObj.get("idpartita").toString());
				System.out.println("Leggi Partita per Id");
				// Prelevo dati dal database
				sql = "SELECT * FROM partita WHERE idpartita='"+idpartita+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					citta=rs.getString("citta");
					provincia=rs.getString("provincia");					
				    nomecampo= rs.getString("nomecampo");
				    indirizzocampo= rs.getString("indirizzocampo");
				    data= rs.getString("data");
				    ora= rs.getString("ora");
				    costo= Float.parseFloat(rs.getString("costo"));
				    amministratore = Integer.parseInt(rs.getString("amministratore"));
				    linkfotocampo="";
				    terreno= rs.getString("terreno");
				    coperto= rs.getString("coperto");
				    note= rs.getString("note");
				    contatto= rs.getString("contatto");				    
				    idtipopartita= Integer.parseInt(rs.getString("idtipopartita"));
				    				    
				    sql = "SELECT nickname FROM profilo WHERE idprofilo='"+amministratore+"'";
					rs = stmt.executeQuery(sql);
					String nickname="";
					if(rs.next()) {
						nickname = rs.getString("nickname");
					}
					sql = "SELECT tipopartita FROM tipopartita WHERE idtipopartita='"+idtipopartita+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						tipopartita = rs.getString("tipopartita");
					}
					
				    jObj.put("nomecampo", nomecampo);
					jObj.put("indirizzocampo", indirizzocampo);
					jObj.put("citta", citta);
					jObj.put("provincia", provincia);
					jObj.put("data", data);
					jObj.put("ora", ora);
					jObj.put("costo", costo);//costo diviso num giocatori					
					jObj.put("terreno", terreno);
					jObj.put("coperto", coperto);
					jObj.put("note", note);
					jObj.put("cellulare", contatto);
					//jObj.put("linkfotocampo", linkfotocampo);
					jObj.put("amministratore", nickname);
					jObj.put("tipopartita", tipopartita);
					
					writer.write(jObj.toString()); 
				}
				   
			}
			
			
//------------------------PARTECIPA ALLA PARTITA------------------------------------
			//POST -update
	//fa anche il controllo se un utente già si è registrato x quella partita
			if(tiporichiesta.equals("aggiorna"))
			{
				//inserire idprofilo in tab correlate
				System.out.println("Registrazione alla partita");
				idpartita = Integer.parseInt(jObj.get("idpartita").toString());
				Integer idprofilo = Integer.parseInt(jObj.get("idprofilo").toString());
				idtipopartita = Integer.parseInt(jObj.get("idtipopartita").toString());
				String ruolo = jObj.get("ruolo").toString();
				Integer squadra = Integer.parseInt(jObj.get("squadra").toString());
				Integer idruolo=0,idsquadra=0;
				JSONObject jsonObj = new JSONObject();
				
				sql = "SELECT * FROM profilo_partita WHERE idprofilo='"+idprofilo+"' and idpartita='"+idpartita+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					jsonObj.put("risposta", "no");
				}
				else {
					sql = "SELECT idruolo FROM ruolo WHERE ruolo='"+ruolo+"' and idtipopartita='"+idtipopartita+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						idruolo=Integer.parseInt(rs.getString("idruolo"));
					}
					sql = "SELECT idsquadra FROM squadra WHERE squadra='"+squadra+"' and idpartita='"+idpartita+"' and idtipopartita='"+idtipopartita+"'";
					rs = stmt.executeQuery(sql);
					if(rs.next()) {
						idsquadra=Integer.parseInt(rs.getString("idsquadra"));
					}
					sql = "INSERT INTO squadra_ruolo (idsquadra, idruolo) VALUES ('"+idsquadra+"', '"+idruolo+"')";
					stmt.executeUpdate(sql);
					sql = "INSERT INTO profilo_partita (idprofilo, idpartita, idruolo, idsquadra) VALUES ('"+idprofilo+"','"+idpartita+"', '"+idruolo+"', '"+idsquadra+"')";
					stmt.executeUpdate(sql);
					jsonObj.put("risposta", "si");
				}
				writer.write(jsonObj.toString());
			
				//potrei fare la select col timestamp creazione della partita
				
				/*
				String sql = "UPDATE partita SET nmancanti='"+nmancanti+"' WHERE idpartita='"+idpartita+"'";
				stmt.executeUpdate(sql);
				// Ritorna a ConfermaPartecipaActivity il n° di giocatori che mancano ancora
				sql = "SELECT nmancanti FROM partita WHERE idpartita='"+idpartita+"'";
				rs = stmt.executeQuery(sql);
				if(rs.next()) 
					nmancanti = Integer.parseInt(rs.getString("nmancanti"));
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("nmancanti", nmancanti);
				writer.write(jsonObj.toString());*/
			}
			//fine put
			
//-------------------------RUOLI GIA' ASSEGNATI--------------------------------------
			if(tiporichiesta.equals("leggiRuoli")){
				idpartita = Integer.parseInt(jObj.get("idpartita").toString());				
				Vector<Ruolo2> v = new Vector<Ruolo2>();
				
				//prende solo i nickname, ruolo,squadra di chi partecipa alla partita
				sql = "SELECT profilo.nickname,squadra.squadra, ruolo.ruolo FROM (profilo_partita "
						+ "inner join profilo ON profilo.idprofilo=profilo_partita.idprofilo) "
						+ "inner join squadra ON (squadra.idsquadra=profilo_partita.idsquadra) "
						+ "inner join ruolo ON (ruolo.idruolo=profilo_partita.idruolo) "
						+ "WHERE profilo_partita.idpartita='"+idpartita+"'";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
			    	Ruolo2 temp = new Ruolo2();
					temp.setNickname(rs.getString("nickname"));	
					String ruoloscelto=rs.getString("ruolo")+rs.getString("squadra");
					System.out.println(ruoloscelto);
					temp.setRuolo(ruoloscelto);	
					v.add(temp);
				}
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("jsonVector", v);
				writer.write(jsonObj.toString());	
			}

//------------------------PROSSIMA PARTITA------------------------------------			
			if(tiporichiesta.equals("prossimaPartita")) {
				
				int idprofilo = Integer.parseInt(jObj.get("idprofilo").toString());
				ResultSet result;
				int idPartita = 0;
				boolean esito = false;				
		    	
		    	//String sql = "SELECT idpartita FROM profilo_partita WHERE idprofilo='" + idprofilo + "'";
		    	String sql = "SELECT * FROM partita " +
		    				 "INNER JOIN profilo_partita ON partita.idpartita = profilo_partita.idpartita " +
		    				 "WHERE partita.data > (SELECT NOW()) AND profilo_partita.idprofilo = " + idprofilo +
		    				 " ORDER BY partita.data ASC LIMIT 1;";
		    	result = stmt.executeQuery(sql);		    	
		    	Partita partita = new Partita();
		    	if(result.next()) {
		    		esito = true;
		    		partita.setIdpartita(Integer.parseInt(result.getString("idpartita")));	
		    		partita.setIdtipopartita(Integer.parseInt(result.getString("idtipopartita")));					
		    		partita.setNomecampo(result.getString("nomecampo"));
		    		partita.setIndirizzocampo(result.getString("indirizzocampo"));
		    		partita.setCosto(Float.parseFloat(result.getString("costo")));
		    		partita.setData(result.getString("data"));
		    		partita.setOra(result.getString("ora"));
		    		partita.setCitta(result.getString("citta"));
		    		partita.setProvincia(result.getString("provincia"));
		    		partita.setLinkfotocampo(result.getString("linkfotocampo"));
		    		partita.setTerreno(result.getString("terreno"));
		    		partita.setCoperto(result.getString("coperto"));
		    		partita.setNote(result.getString("note"));
					partita.setAmministratore(Integer.parseInt(result.getString("amministratore")));
					partita.setContatto(result.getString("contatto"));					
				}
		    	else esito = false;
		    	
		    	JSONObject jsonObj = new JSONObject();
				jsonObj.put("prossimaPartita", partita);
				jsonObj.put("esito", esito);
				writer.write(jsonObj.toString());
		    }
			
			/*
			if(tiporichiesta.equals("caricaTipoPartita"))
			{
				Vector<String> v = new Vector<String>();
				
			    String sql="";
			    sql = "SELECT * FROM tipopartita";			    
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
			    	v.add(rs.getString("tipopartita"));
				}
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("jsonVector", v);
				writer.write(jsonObj.toString());
			}*/
		
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
