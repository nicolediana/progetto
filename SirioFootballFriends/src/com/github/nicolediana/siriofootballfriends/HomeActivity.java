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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	String idprofilo="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);		
		//recupera parametro passato
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		//Toast.makeText(getApplicationContext(), "Benvenuto !", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
	
	public void onClickCreaPartita(View v) {
		Intent intent=new Intent(this,PartitaActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}

	public void onClickPartecipa(View v) {
		Intent intent=new Intent(this,PartecipaActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}
	
	public void onClickModificaProfilo(View v) {
		Intent intent=new Intent(this,ModificaProfiloActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}
	
	public void onClickProssimaPartita(View v) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		JSONObject jsonobj= new JSONObject();
		String tiporichiesta="prossimaPartita";
		String idPartita;
		
		try {
			jsonobj.put("tiporichiesta", tiporichiesta );
			jsonobj.put("idprofilo", idprofilo);
			jsonobj.put("citta", "");
			jsonobj.put("provincia", "");
			StringEntity entity = new StringEntity(jsonobj.toString());
			String nomeServlet="/ServletExample/ServletPartita";
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
			entity.setContentType("application/json;charset=UTF-8");
			httppostreq.setEntity(entity);
			HttpResponse response = httpclient.execute(httppostreq);			
			
			// Recupero della risposta HTTP
			String line = "";
			InputStream inputstream = response.getEntity().getContent();
			line = convertStreamToString(inputstream);
			JSONObject myjson = new JSONObject(line);
			boolean esito = (boolean) myjson.get("esito");
			if(!esito) Toast.makeText(getApplicationContext(), "Non hai partite da giocare, cercane una!", Toast.LENGTH_LONG).show();
			else {
			idPartita = myjson.get("idPartita").toString();

// Passaggio all'activity Dettaglio partita
			Intent intent = new Intent(this, ConfermaPartecipaActivity.class);
			Bundle b = new Bundle();
			b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			b.putString("idpartita", idPartita);
			b.putString("visibilit‡Pulsanti", "invisibili");
			intent.putExtras(b); //intent x passaggio parametri
			startActivity(intent);	
			}
		} 
		catch (JSONException | IOException e) {
			  e.printStackTrace();
	    }
	}
	
	public void onClickLeMiePartite(View v) {
		Intent intent = new Intent(this, LeMiePartiteActivity.class);
		Bundle b = new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);	
	}
	
	public void onClickLogout(View v) {
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	
	public void onClickInfo(View v) {
		Intent intent=new Intent(this,InfoActivity.class);
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
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
