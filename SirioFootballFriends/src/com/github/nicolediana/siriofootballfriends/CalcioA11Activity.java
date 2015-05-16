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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CalcioA11Activity extends Activity {
	private String nomeServlet="/ServletExample/ServletPartita";
	
	private String idpartita;
	private String idprofilo;
	private String tiporichiesta;
	private Integer idtipopartita = 2; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calcio_a11);
		Intent i = getIntent();
		idpartita= i.getStringExtra("idpartita");
		idprofilo= i.getStringExtra("idprofilo");
		
		leggiRuoli();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calcio_a11, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_info) {
			Intent intent=new Intent(this,InfoActivity.class);
			Intent i=getIntent();
			idprofilo= i.getStringExtra("idprofilo");
			Bundle b=new Bundle();
			b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			intent.putExtras(b); //intent x passaggio parametri
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		        if(ruoloscelto.equals("portiere1"))
		        	temp=(Button)findViewById(R.id.btnPortiere1);
				if(ruoloscelto.equals("terzino destro1"))
					temp=(Button)findViewById(R.id.btnTerzinoDx1);
				if(ruoloscelto.equals("terzino sinistro1"))
			        temp=(Button)findViewById(R.id.btnTerzinoSx1);
				if(ruoloscelto.equals("mediano destro1"))
			        temp=(Button)findViewById(R.id.btnMedianoDx1);
				if(ruoloscelto.equals("mediano sinistro1"))
					temp=(Button)findViewById(R.id.btnMedianoSx1);
				if(ruoloscelto.equals("centromediano1"))
					temp=(Button)findViewById(R.id.btnCentromediano1);
				if(ruoloscelto.equals("ala destra1"))
			        temp=(Button)findViewById(R.id.btnAlaDx1);
				if(ruoloscelto.equals("ala sinistra1"))
					temp=(Button)findViewById(R.id.btnAlaSx1);
				if(ruoloscelto.equals("mezzala destra1"))
					temp=(Button)findViewById(R.id.btnMezzalaDx1);
				if(ruoloscelto.equals("mezzala sinistra1"))
					temp=(Button)findViewById(R.id.btnMezzalaSx1);
				if(ruoloscelto.equals("centravanti1"))
					temp=(Button)findViewById(R.id.btnCentravanti1);
				
				if(ruoloscelto.equals("portiere2"))
		        	temp=(Button)findViewById(R.id.btnPortiere2);
				if(ruoloscelto.equals("terzino destro2"))
					temp=(Button)findViewById(R.id.btnTerzinoDx2);
				if(ruoloscelto.equals("terzino sinistro2"))
			        temp=(Button)findViewById(R.id.btnTerzinoSx2);
				if(ruoloscelto.equals("mediano destro2"))
			        temp=(Button)findViewById(R.id.btnMedianoDx2);
				if(ruoloscelto.equals("mediano sinistro2"))
					temp=(Button)findViewById(R.id.btnMedianoSx2);
				if(ruoloscelto.equals("centromediano2"))
					temp=(Button)findViewById(R.id.btnCentromediano2);
				if(ruoloscelto.equals("ala destra2"))
			        temp=(Button)findViewById(R.id.btnAlaDx2);
				if(ruoloscelto.equals("ala sinistra2"))
					temp=(Button)findViewById(R.id.btnAlaSx2);
				if(ruoloscelto.equals("mezzala destra2"))
					temp=(Button)findViewById(R.id.btnMezzalaDx2);
				if(ruoloscelto.equals("mezzala sinistra2"))
					temp=(Button)findViewById(R.id.btnMezzalaSx2);
				if(ruoloscelto.equals("centravanti2"))
					temp=(Button)findViewById(R.id.btnCentravanti2);
				
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
			//risposta si=ok prenotazione effettuata con successo,no=nick già presente x qst partita
			String line = "";
			InputStream inputstream = response.getEntity().getContent();
			line = convertStreamToString(inputstream);
			JSONObject myjson = new JSONObject(line);	        	     
			String risposta=myjson.get("risposta").toString();
			if(risposta.equals("si")){
				Toast.makeText(this, "Registrazione effettuata con successo", Toast.LENGTH_SHORT).show();
				leggiRuoli();
				// Ritorno alla Homepage
				/*Intent intent=new Intent(this,HomeActivity.class);
				Bundle b=new Bundle();
			    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			    intent.putExtras(b); //intent x passaggio parametri
			    startActivity(intent);*/
			}
			else Toast.makeText(this, "Nickname già presente in questa partita", Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {e.printStackTrace();}
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
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
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
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
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
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMezzalaDx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnMezzalaDx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mezzala destra";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMezzalaSx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnMezzalaSx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mezzala sinistra";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickTerzinoDx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnTerzinoDx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="terzino destro";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickTerzinoSx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnTerzinoSx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="terzino sinistro";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMedianoDx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnMedianoDx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mediano destro";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMedianoSx1(View v){
		TextView temp=(TextView)findViewById(R.id.btnMedianoSx1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mediano sinistro";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
		
	public void onClickCentromediano1(View v){
		TextView temp=(TextView)findViewById(R.id.btnCentromediano1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="centromediano";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}	
	
	public void onClickCentravanti1(View v){
		TextView temp=(TextView)findViewById(R.id.btnCentravanti1);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="centravanti";
			Integer numsquadra=1;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	//-------------------------------------------------
	public void onClickPortiere2(View v){
		TextView temp=(TextView)findViewById(R.id.btnPortiere2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="portiere";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
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
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
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
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMezzalaDx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnMezzalaDx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mezzala destra";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMezzalaSx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnMezzalaSx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mezzala sinistra";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickTerzinoDx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnTerzinoDx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="terzino destro";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickTerzinoSx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnTerzinoSx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="terzino sinistro";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMedianoDx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnMedianoDx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mediano destro";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
	
	public void onClickMedianoSx2(View v){
		TextView temp=(TextView)findViewById(R.id.btnMedianoSx2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="mediano sinistro";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
		
	public void onClickCentromediano2(View v){
		TextView temp=(TextView)findViewById(R.id.btnCentromediano2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="centromediano";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}	
	
	public void onClickCentravanti2(View v){
		TextView temp=(TextView)findViewById(R.id.btnCentravanti2);		
		String ttemp= temp.getText().toString();
		if(ttemp.equals("Disponibile")){
			String ruolo="centravanti";
			Integer numsquadra=2;
			inviaRichiesta(ruolo,numsquadra);
		}
		else visualizzaProfilo(ttemp);
		//Toast.makeText(this, "Ruolo già assegnato", Toast.LENGTH_SHORT).show(); 
	}
		
}
