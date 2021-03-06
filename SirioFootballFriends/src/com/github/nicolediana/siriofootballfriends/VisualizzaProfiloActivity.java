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
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizzaProfiloActivity extends Activity {
	private String nomeServlet="/ServletExample/ServletProfilo";
	
	String idprofilo;
	private String nickname;
	//private String linkfotoprofilo;
	String tiporichiesta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizza_profilo);
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo"); //idprofilo dell'utente che visualizza i profili degli altri giocatori,mi serve se voglio tornare indietro
		nickname= i.getStringExtra("nickname");
		caricaProfilo();
	}
	
	public void caricaProfilo() {
		
		//LEGGI PROFILO
		tiporichiesta="vediprofilo";
		JSONObject myjson= new JSONObject();
		try{
			//creazione della Json
			myjson.put("tiporichiesta", tiporichiesta );
			myjson.put("nickname", nickname );
			//creazione pacchetto post
			StringEntity entity = new StringEntity(myjson.toString());
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse httpresponse = httpclient.execute(httppostreq);
			//risposta
			String line = "";
			InputStream inputstream = httpresponse.getEntity().getContent();
			line = convertStreamToString(inputstream);
			JSONObject myjson2 = new JSONObject(line);	 
			
			TextView nicknamedig=(TextView)findViewById(R.id.nickname);
			TextView cittadig=(TextView)findViewById(R.id.citta);
			TextView livellodig=(TextView)findViewById(R.id.livello2);
			TextView votodig=(TextView)findViewById(R.id.voto);
			TextView partitegiocatedig=(TextView)findViewById(R.id.partite_giocate);
			TextView partitevintedig=(TextView)findViewById(R.id.partite_vinte);
			TextView partitepersedig=(TextView)findViewById(R.id.partite_perse);
			TextView bidonidig=(TextView)findViewById(R.id.bidoni);
			TextView annonascitadig=(TextView)findViewById(R.id.eta);
			
			nicknamedig.setText(myjson2.get("nickname").toString());
			cittadig.setText(myjson2.get("citta").toString());
			livellodig.setText(myjson2.get("livello").toString());
			partitegiocatedig.setText(myjson2.get("partitegiocate").toString());
			partitevintedig.setText(myjson2.get("partitevinte").toString());
			partitepersedig.setText(myjson2.get("partiteperse").toString());
			bidonidig.setText(myjson2.get("bidoni").toString());			
			votodig.setText(myjson2.get("voto").toString());
			
			String temp=myjson2.get("eta").toString();
			if(!temp.equals("0"))
			{
				annonascitadig.setText(myjson2.get("eta").toString());			
			}
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		catch(UnsupportedEncodingException e){e.printStackTrace();}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
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
		
}
