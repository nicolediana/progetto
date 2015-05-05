package com.github.nicolediana.siriofootballfriends;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{	
	//public static String urlServlet="http://app-siriofootball.rhcloud.com";
	//public static String urlServlet="http://192.168.1.111:8080";
	public static String urlServlet="http://192.168.1.107:8080";
	private String nomeServlet="/ServletExample/ServletCredenziali";
	
	private String email="";
	private String password="";
	private String email_regular="^[A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@SuppressLint("NewApi")
	public void onClickAccedi(View v) throws UnsupportedEncodingException {
		TextView emaildig=(TextView)findViewById(R.id.email);
		email= (String)emaildig.getText().toString();
		TextView pwDigitato=(TextView)findViewById(R.id.psw);
		password= (String)pwDigitato.getText().toString();
		
		//controllo se email è vuota e se rispetta la regular expression
		if(email.equals("")||email.equals(null)||(!email.matches(email_regular)))
			Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_LONG).show();
		else{
			//controllo se pw è vuota 
			if(password.equals("")||password.equals(null))
				Toast.makeText(getApplicationContext(), "Inserire Password", Toast.LENGTH_LONG).show();
			else{
				
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				
				JSONObject jsonobj= new JSONObject();
				try{
					String tiporichiesta="leggi";
					//convertire in Json
					jsonobj.put("tiporichiesta", tiporichiesta );
					jsonobj.put("email", email );
					jsonobj.put("password", password );
					StringEntity entity = new StringEntity(jsonobj.toString());
					
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httppostreq = new HttpPost(urlServlet+nomeServlet);
					entity.setContentType("application/json;charset=UTF-8");
					httppostreq.setEntity(entity);
					HttpResponse response = httpclient.execute(httppostreq);
					
		        	if(response!=null){
		        		 String line = "";
		        	     InputStream inputstream = response.getEntity().getContent();
		        	     line = convertStreamToString(inputstream);
		        	     JSONObject myjson = new JSONObject(line);
		        	     String id=myjson.get("idcredenziali").toString();
		        	     String idpr=myjson.get("idprofilo").toString();
			        	 Integer idcred=Integer.parseInt(id);
				    	 Integer idprof=Integer.parseInt(idpr);
				    	
				    	if(idcred!=0){
				    		if(idprof!=0)
				    		{
				    			Intent intent=new Intent(this,HomeActivity.class);
				        		Bundle b=new Bundle();
				        		b.putString("idprofilo", idpr); //passa chiave valore a activity_home
				        		intent.putExtras(b); //intent x passaggio parametri
				        		startActivity(intent);
				        	}
				    		else{
				    			// deve accedere a activity Profilo
				    			Intent intent=new Intent(this,ProfiloActivity.class);
				        		Bundle b=new Bundle();
				        		b.putString("idcredenziali", id); //passa chiave valore a activity_home
				        		intent.putExtras(b); //intent x passaggio parametri
				        		startActivity(intent);
				        		
				    			Toast.makeText(getApplicationContext(), "Crea il tuo Profilo", Toast.LENGTH_LONG).show(); 
					    		
				    			}
				    		}
		        	    
				    else Toast.makeText(getApplicationContext(), "Utente Inesistente!!!", Toast.LENGTH_LONG).show(); //(id=0)
			    		
		        	}//if
		        	else Toast.makeText(getApplicationContext(), "Utente Inesistente", Toast.LENGTH_LONG).show();
		    		
		        }//try
		        	catch(Exception e){
		        		Toast.makeText(getApplicationContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
		        	}//catch 
			}
		}
		
		
		//Toast.makeText(getApplicationContext(), "funziona "+email+" "+password, Toast.LENGTH_LONG).show();
		
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
	
	public void onClickRegistrati(View v) {
		TextView emaildig=(TextView)findViewById(R.id.email);
		email= (String)emaildig.getText().toString();
		TextView pwDigitato=(TextView)findViewById(R.id.psw);
		password= (String)pwDigitato.getText().toString();
	
		Intent intent=new Intent(this, RegistrazioneActivity.class);
		Bundle b=new Bundle();
		b.putString("email", email); //passa chiave valore a activity_home
		intent.putExtras(b);
		b.putString("password", password); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}

	
}
