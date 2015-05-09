package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class PartiteInProgrammaActivity extends Activity {
	
	private String idprofilo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partite_in_programma);
		//recupera parametro passato
		Intent i = getIntent();
	    idprofilo = i.getStringExtra("idprofilo");
	    
		JSONObject jsonobj= new JSONObject();
		try {
			 String tiporichiesta="partiteInProgramma";
			 jsonobj.put("idprofilo", idprofilo);
			 jsonobj.put("tiporichiesta", tiporichiesta );
			 
			 // Creazione pacchetto post
			 StringEntity entity = new StringEntity(jsonobj.toString());
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			 DefaultHttpClient httpclient = new DefaultHttpClient();
			 String nomeServlet = "ServletPartita";
			 HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			 entity.setContentType("application/json;charset=UTF-8");
			 httppostreq.setEntity(entity);
			 HttpResponse httpresponse = httpclient.execute(httppostreq);
			 
			// Recupero della risposta
			 String line = "";
			 InputStream inputstream = httpresponse.getEntity().getContent();
			 line = convertStreamToString(inputstream);
			 JSONObject myjson = new JSONObject(line);	 
			 Toast.makeText(getApplicationContext(), myjson.toString(), Toast.LENGTH_LONG).show();				
		}
		catch(IOException | JSONException e) {
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
}