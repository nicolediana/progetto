package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CalcioA5Activity extends Activity {
	private String nomeServlet="/ServletExample/ServletPartita";
	
	private String idpartita;
	private String idprofilo;
	private String tiporichiesta;
	private Integer idtipopartita = 1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calcio_a5);		
		Intent i = getIntent();
		idpartita= i.getStringExtra("idpartita");
		idprofilo= i.getStringExtra("idprofilo");		
		leggiRuoli();
	}

	public void onClickAttaccante1(View v){
		TextView temp=(TextView)findViewById(R.id.btnAttaccante1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="attaccante";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo gi� assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickAttaccante2(View v){
		TextView temp=(TextView)findViewById(R.id.btnAttaccante2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="attaccante";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);			
	}
	
	public void onClickAlaDx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnAlaDx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="ala destra";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickAlaDx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnAlaDx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="ala destra";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickAlaSx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnAlaSx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="ala sinistra";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
	}
	
	public void onClickAlaSx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnAlaSx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="ala sinistra";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickDifensore1(View v){
		TextView temp=(TextView)findViewById(R.id.btnDifensore1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="difensore";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickDifensore2(View v){
		TextView temp=(TextView)findViewById(R.id.btnDifensore2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="difensore";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickPortiere1(View v){
		TextView temp=(TextView)findViewById(R.id.btnPortiere1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="portiere";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}
	
	public void onClickPortiere2(View v){
		TextView temp=(TextView)findViewById(R.id.btnPortiere2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="portiere";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp); 
	}

	public void visualizzaProfilo(String ttemp){
		Intent intent=new Intent(this,VisualizzaProfiloActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); 
	    b.putString("nickname", ttemp);
	    intent.putExtras(b); 
	    startActivity(intent);
	}
	
	public void inviaRichiesta(String ruolo, Integer numsquadra){
		try{
			tiporichiesta = "aggiorna";
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("tiporichiesta", tiporichiesta);
			jsonobj.put("idprofilo", idprofilo);
			jsonobj.put("idpartita", idpartita);
			jsonobj.put("citta", "");
			jsonobj.put("provincia", "");
			jsonobj.put("idtipopartita", idtipopartita);
			jsonobj.put("ruolo", ruolo);
			jsonobj.put("squadra", numsquadra);			
			
			//creazione pacchetto post
			StringEntity entity = new StringEntity(jsonobj.toString());
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse response = httpclient.execute(httppostreq);
			//risposta si=ok prenotazione effettuata con successo,no=nick gi� presente x qst partita
			String line = "";
			InputStream inputstream = response.getEntity().getContent();
			line = convertStreamToString(inputstream);
			JSONObject myjson = new JSONObject(line);	        	     
			String risposta=myjson.get("risposta").toString();
			if(risposta.equals("si")){
				Toast.makeText(this, "Registrazione effettuata con successo", Toast.LENGTH_SHORT).show();
				leggiRuoli();
			}
			else Toast.makeText(this, "Nickname gi� presente in questa partita", Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	public void leggiRuoli(){
		try{
			tiporichiesta = "leggiRuoli";
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("tiporichiesta", tiporichiesta);
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
			
			 //recupero della risposta
			String line = "";
			InputStream inputstream = httpresponse.getEntity().getContent();
			line = convertStreamToString(inputstream);
			JSONObject myjson = new JSONObject(line);	        	     
			//mi deve passare quali ruoli sono disponibili o il nickname di quelli non disponibili
			//oggetti:attaccante1,nick; attacante2, disponibile
			JSONArray json_array = myjson.getJSONArray("jsonVector");
			//Toast.makeText(this, json_array.toString(), Toast.LENGTH_SHORT).show();
			int size = json_array.length();
		    // Recupero delle singole Json dall'array di Json
		    for (int i = 0; i < size; i++) {
		        JSONObject another_json_object = json_array.getJSONObject(i);
		        String ruoloscelto=another_json_object.get("ruolo").toString();
		        String nick=another_json_object.get("nickname").toString();
		        
		        Button temp = null;
		        if(ruoloscelto.equals("attaccante1"))
		        	temp=(Button)findViewById(R.id.btnAttaccante1);
				if(ruoloscelto.equals("attaccante2"))
					temp=(Button)findViewById(R.id.btnAttaccante2);
				if(ruoloscelto.equals("ala destra1"))
			        temp=(Button)findViewById(R.id.btnAlaDx1);
				if(ruoloscelto.equals("ala destra2"))
			        temp=(Button)findViewById(R.id.btnAlaDx2);
				if(ruoloscelto.equals("ala sinistra1"))
					temp=(Button)findViewById(R.id.btnAlaSx1);
				if(ruoloscelto.equals("ala sinistra2"))
					temp=(Button)findViewById(R.id.btnAlaSx2);
				if(ruoloscelto.equals("difensore1"))
			        temp=(Button)findViewById(R.id.btnDifensore1);
				if(ruoloscelto.equals("difensore2"))
					temp=(Button)findViewById(R.id.btnDifensore2);
				if(ruoloscelto.equals("portiere1"))
					temp=(Button)findViewById(R.id.btnPortiere1);
				if(ruoloscelto.equals("portiere2"))
					temp=(Button)findViewById(R.id.btnPortiere2);
				temp.setText(nick);	
				temp.setTextColor(getResources().getColor(R.color.nero));
			}			
		}catch (Exception e) {e.printStackTrace();}
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
