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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PartitaActivity extends Activity {
	private String nomeServlet="/ServletExample/ServletPartita";
			
	String idprofilo;
	private String nomecampo;
	private String indirizzocampo;
	private String provincia;
	private String citta;
	private Float costo;
	private String coperto;
	private String terreno;
	private String note;
	private String data;
	private String ora;
	private String amministratore;
	private String contatto;
	//ArrayList<String> tipopartitalist;
	private String tipopartita;
	private String tipoPartitaList;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partita);
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		//Toast.makeText(getApplicationContext(), "Organizza una partita !", Toast.LENGTH_LONG).show();
		amministratore=i.getStringExtra("idprofilo");
		//da idprofilo prendere num cell dell'amministratore else salvare il num inserito nel db in tab profilo
		//caricare lo spinner
		//caricaTipoPartito();
		Spinner spinner_tipopartita = (Spinner) findViewById(R.id.spinnerTipoPartita);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipoPartitaList, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_tipopartita.setAdapter(adapter);
		
		spinner_tipopartita.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapter, View view, int pos,long id) 
			{
				tipoPartitaList= adapter.getItemAtPosition(pos).toString();	
			}
			public void onNothingSelected(AdapterView<?> arg0) { }});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partita, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_info) {
			Intent intent=new Intent(this,InfoActivity.class);
			Bundle b=new Bundle();
			b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			intent.putExtras(b); //intent x passaggio parametri
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
public void onClickSalva(View v) throws UnsupportedEncodingException {

		TextView nomedig=(TextView)findViewById(R.id.nomeCampo);
		nomecampo= nomedig.getText().toString();
		TextView indirizzodig=(TextView)findViewById(R.id.indirizzoCampo);
		indirizzocampo= indirizzodig.getText().toString();
		TextView cittadig=(TextView)findViewById(R.id.citta);
		citta= cittadig.getText().toString();
		TextView provinciadig=(TextView)findViewById(R.id.provincia);
		provincia= provinciadig.getText().toString();
		TextView datadig=(TextView)findViewById(R.id.data);
		data= datadig.getText().toString();
		TextView oradig=(TextView)findViewById(R.id.ora);
		ora= oradig.getText().toString();
		TextView costodig=(TextView)findViewById(R.id.costo);
		String costoStr=costodig.getText().toString();
		if(costoStr.equals("")||costoStr.equals(null))
			costo= (float) 0;
		else
			costo= Float.parseFloat(costodig.getText().toString());
		TextView celldig=(TextView)findViewById(R.id.cellulare);
		String cellulareStr= (String)celldig.getText().toString();
		TextView tipoterrenodig=(TextView)findViewById(R.id.tipoTerreno);
		terreno= tipoterrenodig.getText().toString();
		TextView copertodig=(TextView)findViewById(R.id.coperto);
		coperto= copertodig.getText().toString();
		TextView notedig=(TextView)findViewById(R.id.note);
		note= notedig.getText().toString();
		
		//città non deve essere nullo
		if(citta.equals("")||provincia.equals("")||indirizzocampo.equals("")||data.equals("")||ora.equals("")||cellulareStr.equals(""))
			Toast.makeText(getApplicationContext(), "Inserire tutti i campi contrassegnati con * ", Toast.LENGTH_LONG).show();
		else{
			JSONObject jsonobj= new JSONObject();
			try{
				String tiporichiesta="crea";
				//creazione della Json
				jsonobj.put("tiporichiesta", tiporichiesta );
				jsonobj.put("nomecampo", nomecampo );
				jsonobj.put("indirizzocampo", indirizzocampo );
				jsonobj.put("provincia", provincia );
				jsonobj.put("citta", citta );
				jsonobj.put("contatto", cellulareStr );
				jsonobj.put("costo", costo.toString() );
				jsonobj.put("data", data );
				jsonobj.put("ora", ora );
				jsonobj.put("amministratore", amministratore );
				jsonobj.put("idprofilo", idprofilo );
				jsonobj.put("terreno", terreno );
				jsonobj.put("coperto", coperto );
				jsonobj.put("note", note );
				jsonobj.put("tipopartita", tipoPartitaList );
				
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
				String idpartita=myjson.get("idpartita").toString();
				
				/*if(tipopartita.equals("Calcio a 5"))
					Intent intent=new Intent(this,CalcioA5Activity.class);
				if(tipopartita.equals("Calcio a 11"))
					Intent intent=new Intent(this,CalcioA11Activity.class);
				*/
				Intent intent=new Intent(this,HomeActivity.class);
				Bundle b=new Bundle();
			    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
			    b.putString("idpartita", idpartita);
			    intent.putExtras(b); //intent x passaggio parametri
			    startActivity(intent);
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
	}
	
	public void onClickAnnulla(View v){
		Intent intent=new Intent(this,HomeActivity.class);
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
	
/*	private void caricaTipoPartito(){
		//LEGGI PROFILO
				String tiporichiesta="caricaTipoPartita";
				JSONObject myjson= new JSONObject();
				try{
					//creazione della Json
					myjson.put("tiporichiesta", tiporichiesta );
					myjson.put("citta", "citta" );
					myjson.put("provincia", "provincia" );
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
					JSONArray json_array = myjson2.getJSONArray("jsonVector");
					int size = json_array.length();
					for (int i = 0; i < size; i++) {
						String tipo = json_array.getJSONObject(i).toString();
						tipopartitalist.add(tipo);
						Toast.makeText(getApplicationContext(), "Tipo:" + tipo , Toast.LENGTH_LONG).show();
						
					}				
					
					Spinner mySpinner = (Spinner) findViewById(R.id.spinnerTipoPartita);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartitaActivity.this,android.R.layout.simple_spinner_dropdown_item,tipopartitalist);
					mySpinner.setAdapter(adapter);
					mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> adapter, View view, int pos,long id) 
						{
							tipopartita= adapter.getItemAtPosition(pos).toString();	
						}
						public void onNothingSelected(AdapterView<?> arg0) { }});
	
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
			}*/

}
