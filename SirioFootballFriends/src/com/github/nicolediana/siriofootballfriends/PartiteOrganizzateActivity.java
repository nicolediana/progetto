package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PartiteOrganizzateActivity extends Activity {

	private String idprofilo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partite_organizzate);
		
		//Recupera parametro passato
		Intent i = getIntent();
		idprofilo = i.getStringExtra("idprofilo");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		JSONObject jsonobj = new JSONObject();
		String tiporichiesta = "partiteOrganizzate";
		
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
			 String[] partite = new String[size];
			 final Integer[] idpart = new Integer[size]; //Associa l'id della riga del ListView all'idpartita
			 
			 // Recupero delle singole Json dall'array di Json
			 for (int j = 0; j < size; j++) {
			    JSONObject another_json_object = json_array.getJSONObject(j);
			    partite[j] = another_json_object.get("nomecampo").toString() + " - " +
			    		     another_json_object.get("data").toString();
			    idpart[j] = Integer.parseInt(another_json_object.get("idpartita").toString()); 
			 }
			 
			 // Creazione del Listview - definisco un ArrayList
			 final ArrayList <String> partitelist = new ArrayList<String>();  
			 for (int i1 = 0; i1 < partite.length; ++i1) {  
			 	partitelist.add(partite[i1]);  
			 }  		    
			 // Recupero la lista dal layout 
			 ListView mylist = (ListView) findViewById(R.id.elencoPartiteOrganizzate); 
			 mylist.setTextFilterEnabled(true);			  
			 // Creo e istruisco l'adattatore  
			 ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partitelist); 
			 // Inietto i dati  
			 mylist.setAdapter(adapter); 
			 final Intent intent = new Intent(this, ConfermaPartecipaActivity.class);
			 // Definisco un listener per l'adattatore
			 mylist.setOnItemClickListener(new OnItemClickListener() {  
			 	@Override  
			 	public void onItemClick(AdapterView<?> adapter, final View view, int pos, long id){  
			 		String indicepartita = idpart[pos].toString();
			 		intent.putExtra("idpartita", indicepartita); //intent x passaggio parametri
			 		intent.putExtra("idprofilo", idprofilo);
			 		intent.putExtra("visibilitàPulsanti", "partiteOrganizzate");
			 		startActivity(intent);
			 	}    
			 });
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
