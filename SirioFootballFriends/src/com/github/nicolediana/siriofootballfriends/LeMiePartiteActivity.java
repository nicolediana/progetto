package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class LeMiePartiteActivity extends Activity {
	
	private String idprofilo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_le_mie_partite);		
		//recupera parametro passato
		Intent i = getIntent();
	    idprofilo = i.getStringExtra("idprofilo");
	}
	
	public void onClick_PartiteInProgramma(View v) {
		String tiporichiesta = "partiteInProgramma";
		controllo(tiporichiesta);
	}
	
	public void onClick_PartiteGiocate(View v) {
		String tiporichiesta = "partiteGiocate";
		controllo(tiporichiesta);
	}
	
	public void onClick_PartiteOrganizzate(View v) {
		String tiporichiesta = "partiteOrganizzate";
		controllo(tiporichiesta);
	}
	
	public void controllo(String tiporichiesta) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		JSONObject jsonobj = new JSONObject();
		try {			 
			 jsonobj.put("idprofilo", idprofilo);
			 jsonobj.put("tiporichiesta", tiporichiesta );
			 jsonobj.put("citta", "");
			 jsonobj.put("provincia", "");
			 
			 // Creazione pacchetto post
			 StringEntity entity = new StringEntity(jsonobj.toString());
			 DefaultHttpClient httpclient = new DefaultHttpClient();
			 String nomeServlet = "/ServletExample/ServletPartita";
			 HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			 entity.setContentType("application/json;charset=UTF-8");
			 httppostreq.setEntity(entity);
			 HttpResponse httpresponse = httpclient.execute(httppostreq);
			 
			 // Recupero della risposta
			 String line = "";
			 InputStream inputstream = httpresponse.getEntity().getContent();
			 line = convertStreamToString(inputstream);
			 JSONObject myjson = new JSONObject(line);			 
			 JSONArray json_array = myjson.getJSONArray("elencoPartite");
			 int size = json_array.length();
			 if(size==0)
					Toast.makeText(this, "Nessuna partita presente", Toast.LENGTH_SHORT).show();
					
			 else{
				 if(tiporichiesta.equals("partiteInProgramma")){
					 	Intent intent=new Intent(this,PartiteInProgrammaActivity.class);
					 	Bundle b=new Bundle();
					 	b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
					 	intent.putExtras(b); //intent x passaggio parametri
					 	startActivity(intent);
				 }
				 if(tiporichiesta.equals("partiteGiocate")){
					 	Intent intent=new Intent(this,PartiteGiocateActivity.class);
						Bundle b=new Bundle();
						b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
						intent.putExtras(b); //intent x passaggio parametri
						startActivity(intent);
					 }
				 if(tiporichiesta.equals("partiteOrganizzate")){
					 	Intent intent=new Intent(this,PartiteOrganizzateActivity.class);
						Bundle b=new Bundle();
						b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
						intent.putExtras(b); //intent x passaggio parametri
						startActivity(intent);
					 }
			 	}
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