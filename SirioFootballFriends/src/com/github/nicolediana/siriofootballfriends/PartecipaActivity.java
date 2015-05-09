package com.github.nicolediana.siriofootballfriends;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PartecipaActivity extends Activity {

	private String nomeServlet="/ServletExample/ServletPartita";
	
	private String idprofilo;
	private String citta;
	private String provincia;
	private String indicepartita;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partecipa);
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		//Toast.makeText(getApplicationContext(), "Sei pronto a giocare? Scegli la partita a cui partecipare!!!", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partecipa, menu);
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
	
	public void onClickCerca(View v) throws UnsupportedEncodingException {
		TextView cercaCitta=(TextView)findViewById(R.id.cercaPerCitta);
		citta= cercaCitta.getText().toString();
		TextView cercaProv=(TextView)findViewById(R.id.cercaPerProvincia);
		provincia= cercaProv.getText().toString();
		
		//citt‡ e provincia non devono essere entrambi nulli
		if((citta.equals("")||citta.equals(null))&&(provincia.equals("")||provincia.equals(null)))
				Toast.makeText(getApplicationContext(), "Inserire Citta' e/o Provincia", Toast.LENGTH_LONG).show();
		else{
			JSONObject jsonobj= new JSONObject();
			try{
				String tiporichiesta="leggi";
				String idpartita="";
				
				//creazione della Json
				jsonobj.put("tiporichiesta", tiporichiesta );
				if(!citta.equals("")&&!citta.equals(null))
					jsonobj.put("citta", citta );
				else jsonobj.put("citta", "null");
				if(!provincia.equals("")&&!provincia.equals(null))
					jsonobj.put("provincia", provincia );
				else jsonobj.put("provincia", "null" );
				jsonobj.put("idpartita", "");
								
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
				
				JSONArray json_array = myjson.getJSONArray("jsonVector");
				int size = json_array.length();
			    String[] partite = new String[size];
			    final Integer[] idpart = new Integer[size]; //Associa l'id della riga del ListView all'idpartita
			    
			    // Recupero delle singole Json dall'array di Json
			    for (int i = 0; i < size; i++) {
			        JSONObject another_json_object = json_array.getJSONObject(i);
			        partite[i] = another_json_object.get("nomecampo").toString() + " - " +
			        		     another_json_object.get("data").toString();
			        idpart[i] = Integer.parseInt(another_json_object.get("idpartita").toString()); 
				}
			    // Creazione del Listview - definisco un ArrayList
			    final ArrayList <String> partitelist = new ArrayList<String>();  
		        for (int i = 0; i < partite.length; ++i) {  
		             partitelist.add(partite[i]);  
		        }  
			    // recupero la lista dal layout  
			    ListView mylist = (ListView) findViewById(R.id.listView1); 
			    mylist.setTextFilterEnabled(true);			    
			    // creo e istruisco l'adattatore  
			    ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partitelist);  
		        // inietto i dati  
		        mylist.setAdapter(adapter); 
		        intent=new Intent(this, ConfermaPartecipaActivity.class);
			    // Definisco un listener per l'adattatore
			    mylist.setOnItemClickListener(new OnItemClickListener() {  
			    	@Override  
				    public void onItemClick(AdapterView<?> adapter, final View view, int pos, long id){  
			    		indicepartita = idpart[pos].toString();
				        //Toast.makeText(getApplicationContext(), indicepartita, Toast.LENGTH_LONG).show();
				        intent.putExtra("idpartita", indicepartita); //intent x passaggio parametri
				        intent.putExtra("idprofilo", idprofilo);
				        intent.putExtra("visibilit‡Pulsanti", "");
				        startActivity(intent);
				     }    
			    });  
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
