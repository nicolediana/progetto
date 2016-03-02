package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfermaPartecipaActivity extends Activity {
	
	private String nomeServlet="/ServletExample/ServletPartita";
		
	private String idpartita;
	private String idprofilo;
	private String tiporichiesta;
	private String visibilit‡Pulsanti;
	private String mappa;
	private Uri indmappa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conferma_partecipa);
		Intent i = getIntent();
		idpartita= i.getStringExtra("idpartita");
		idprofilo= i.getStringExtra("idprofilo");
		visibilit‡Pulsanti = i.getStringExtra("visibilit‡Pulsanti");
		 Button mappaButton = (Button) findViewById(R.id.bottoneMappa);
			mappaButton.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					indmappa= Uri.parse("geo:0,0?q="+mappa);
					Intent i2 = new Intent(Intent.ACTION_VIEW,indmappa);
					startActivity(i2);				
				}
			});
		// Visibilit‡ dei pulsanti
		Button partecipaButton = (Button) findViewById(R.id.bottoneConfermaPartecipa);
		Button annullaButton = (Button) findViewById(R.id.bottoneAnnulla);		
		
		if(visibilit‡Pulsanti.equals("partiteOrganizzate")) {
			partecipaButton.setVisibility(View.VISIBLE); // GONE: pulsanti completamente rimossi dall'activity
			partecipaButton.setText("MODIFICA");	
			partecipaButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					onClickModifica(v);					
				}				
			});			
			annullaButton.setVisibility(View.VISIBLE);
			annullaButton.setText("ELIMINA");	
			annullaButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					onClickElimina(v);					
				}				
			});
		}
		else{
			if(visibilit‡Pulsanti.equals("partiteInProgramma")) {
			partecipaButton.setVisibility(View.GONE); // GONE: pulsanti completamente rimossi dall'activity
			annullaButton.setVisibility(View.VISIBLE);
			annullaButton.setText("ABBANDONA");	
			annullaButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					onClickAbbandona(v);					
				}				
			});}
	        else {
				partecipaButton.setVisibility(View.VISIBLE);
				annullaButton.setVisibility(View.VISIBLE);
			}
		}
		// Legge i campi della tabella 'partita'
		tiporichiesta = "leggi";
		try{
			JSONObject myjson = new JSONObject();		
			myjson.put("tiporichiesta", tiporichiesta);
			myjson.put("idpartita", idpartita);
			myjson.put("citta", "null");
			myjson.put("provincia", "null");
			
			// Richiesta HTTP
			StringEntity entity = new StringEntity(myjson.toString());		
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse response = httpclient.execute(httppostreq);
			
			// Recupero dati dalla ServletPartita
			if(response!=null){
    		 // Settaggio dei Textview
    		 String line = "";
		     InputStream inputstream = response.getEntity().getContent();
		     line = convertStreamToString(inputstream);		     
		     JSONObject myjson1 = new JSONObject(line);
		     
		     TextView nomedig=(TextView)findViewById(R.id.nomeCampo);
		     nomedig.setText(myjson1.get("nomecampo").toString());
			 
		     TextView indirizzodig=(TextView)findViewById(R.id.indirizzoCampo);
		     String ind1=myjson1.get("indirizzocampo").toString();
			 indirizzodig.setText(ind1);
			 TextView cittadig=(TextView)findViewById(R.id.citta);
			 String cit1=myjson1.get("citta").toString();
		     cittadig.setText(cit1);
		     TextView provinciadig=(TextView)findViewById(R.id.provincia);
		     String prov1=myjson1.get("provincia").toString();
		     provinciadig.setText(prov1);		     
		     
		     mappa=(ind1+" "+cit1+" "+prov1).replace(" ", "+");
		    				
		     TextView datadig=(TextView)findViewById(R.id.data);
		     datadig.setText(myjson1.get("data").toString());
		     TextView oradig=(TextView)findViewById(R.id.ora);
		     oradig.setText(myjson1.get("ora").toString());		     
		     
		     TextView costodig=(TextView)findViewById(R.id.costo);
			 costodig.setText(myjson1.get("costo").toString());
		     TextView tipoterrenodig=(TextView)findViewById(R.id.tipoTerreno);
			 tipoterrenodig.setText(myjson1.get("terreno").toString());
			 TextView copertodig=(TextView)findViewById(R.id.coperto);
			 copertodig.setText(myjson1.get("coperto").toString());
			 TextView notedig=(TextView)findViewById(R.id.note);
			 notedig.setText(myjson1.get("note").toString());
			 TextView amministratoredig=(TextView)findViewById(R.id.amministratore);
			 amministratoredig.setText(myjson1.get("amministratore").toString());
			 TextView tipopartitadig=(TextView)findViewById(R.id.tipoPartita);
			 tipopartitadig.setText(myjson1.get("tipopartita").toString());
			 TextView celldig=(TextView)findViewById(R.id.cellulare);
			 celldig.setText(myjson1.get("cellulare").toString());
			 }
		} catch(Exception e) {
    		e.printStackTrace();
        }	
		
	}
	
	private String convertStreamToString(InputStream is) {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try {
	        while ((line = rd.readLine()) != null) {
	            total.append(line);
	        }
	    } catch (Exception e) {
	    Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
	    }
	return total.toString();
	}
	
	public void onClickSalva(View v) {
		TextView tipopartitadig=(TextView)findViewById(R.id.tipoPartita);
		String tipopartita=tipopartitadig.getText().toString();
		Bundle b=new Bundle();
	    b.putString("idprofilo", idprofilo); 
	    b.putString("idpartita", idpartita); 
	    Intent intent = null;
	    if(tipopartita.equals("Calcio a 5"))
	    	intent=new Intent(this,CalcioA5Activity.class);
	    else if(tipopartita.equals("Calcio a 11"))
	    	intent=new Intent(this,CalcioA11Activity.class);
	    else
	    	intent=new Intent(this,HomeActivity.class);
	    intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
		}
	
	public void onClickAnnulla(View v) {
		Intent intent=new Intent(this,HomeActivity.class);
		Bundle b=new Bundle();
	    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
	    intent.putExtras(b); //intent x passaggio parametri
	    startActivity(intent);	
	}

	public void onClickElimina(View v) {
		JSONObject jsonobj= new JSONObject();
		String tiporichiesta="eliminaPartita";			
		try{
			jsonobj.put("tiporichiesta", tiporichiesta);
			jsonobj.put("idprofilo", idprofilo);
			jsonobj.put("idpartita", idpartita);
			jsonobj.put("citta", "");
			jsonobj.put("provincia", "");		
			
			//creazione pacchetto post
			StringEntity entity = new StringEntity(jsonobj.toString());
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse httpresponse = httpclient.execute(httppostreq);
			
			String line = "";
			InputStream inputstream = httpresponse.getEntity().getContent();
			line = convertStreamToString(inputstream);			    
			JSONObject myjson = new JSONObject(line);	        	     
			String risposta=myjson.get("risposta").toString();
			
			if(risposta.equals("si"))
			{
				Toast.makeText(this, "Partita eliminata", Toast.LENGTH_SHORT).show();
				
				Intent intent=new Intent(this,HomeActivity.class);
				Bundle b=new Bundle();
			    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			    intent.putExtras(b); //intent x passaggio parametri
			    startActivity(intent);
			}
			else
				 Toast.makeText(getApplicationContext(), "Permesso negato, partita non eliminata", Toast.LENGTH_LONG).show();
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		catch(UnsupportedEncodingException e){e.printStackTrace();}
		catch (ClientProtocolException e) {	e.printStackTrace();} 
		catch (IOException e2) {e2.printStackTrace();}
	}
	
	public void onClickAbbandona(View v) {
		JSONObject jsonobj= new JSONObject();
		String tiporichiesta="abbandonaPartita";			
		try{
			jsonobj.put("tiporichiesta", tiporichiesta);
			jsonobj.put("idprofilo", idprofilo);
			jsonobj.put("idpartita", idpartita);
			jsonobj.put("citta", "");
			jsonobj.put("provincia", "");		
					
			//creazione pacchetto post
			StringEntity entity = new StringEntity(jsonobj.toString());
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse httpresponse = httpclient.execute(httppostreq);
			
			Toast.makeText(this, "Partita abbandonata", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(this,HomeActivity.class);
			Bundle b=new Bundle();
		    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		    intent.putExtras(b); //intent x passaggio parametri
		    startActivity(intent);					
		}
		catch (JSONException ex) {ex.printStackTrace();	}
		catch(UnsupportedEncodingException e){e.printStackTrace();}
		catch (ClientProtocolException e) {e.printStackTrace();} 
		catch (IOException e2) { e2.printStackTrace(); }
	}
	
	public void onClickModifica(View v) {
		TextView tipopartitadig=(TextView)findViewById(R.id.tipoPartita);
		String tipopartita=tipopartitadig.getText().toString();
		Bundle b=new Bundle();
		b.putString("tiporichiesta", "modifica"); 
	    b.putString("idprofilo", idprofilo); 
	    b.putString("idpartita", idpartita); 
	    Intent intent = null;
	   // intent=new Intent(this,ModificaPartitaActivity.class);
	    intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
		}	
}