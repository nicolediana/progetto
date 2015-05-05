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
import android.widget.TextView;
import android.widget.Toast;

public class RegistrazioneActivity extends Activity {
	private String nomeServlet="/ServletExample/ServletCredenziali";
			
	private String email;
	private String password;
	private String email_regular="^[A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private String password2="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrazione);
		//recupera parametro passato
		Intent i=getIntent();
		TextView emaildig1=(TextView)findViewById(R.id.email);
		String email1= i.getStringExtra("email");
		emaildig1.setText(email1);
		TextView pwDigitato1=(TextView)findViewById(R.id.psw);
		String psw1= i.getStringExtra("password");
		pwDigitato1.setText(psw1);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registrazione, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickSalvaReg(View v) throws UnsupportedEncodingException {
		TextView emaildig=(TextView)findViewById(R.id.email);
		email= (String)emaildig.getText().toString();
		TextView pwDigitato=(TextView)findViewById(R.id.psw);
		password= (String)pwDigitato.getText().toString();
		TextView pw2Digitato=(TextView)findViewById(R.id.psw2);
		password2= (String)pw2Digitato.getText().toString();
		
		//controllo se email è vuota e se rispetta la regular expression
		if(email.equals("")||email.equals(null)||(!email.matches(email_regular)))
			Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_LONG).show();
		else{
			//controllo se pw è vuota 
			if(password.equals("")||password.equals(null))
				Toast.makeText(getApplicationContext(), "Inserire Password", Toast.LENGTH_LONG).show();
			else{
				if(!password2.equals(password))
					Toast.makeText(getApplicationContext(), "Le Password non coincidono", Toast.LENGTH_LONG).show();
				else{
					JSONObject jsonobj= new JSONObject();
					try{
						String tiporichiesta="crea";
						//convertire in Json
						jsonobj.put("tiporichiesta", tiporichiesta );
						jsonobj.put("email", email );
						jsonobj.put("password", password );
						StringEntity entity = new StringEntity(jsonobj.toString());
					
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
						StrictMode.setThreadPolicy(policy);
					
						DefaultHttpClient httpclient = new DefaultHttpClient();
						//HttpPost httppostreq = new HttpPost(urlServlet+"/ServletExample/ServletCredenziali");
						HttpPost httppostreq = new HttpPost(MainActivity.urlServlet+nomeServlet);
						entity.setContentType("application/json;charset=UTF-8");
						httppostreq.setEntity(entity);
						HttpResponse httpresponse = httpclient.execute(httppostreq);
						
						String line = "";
						InputStream inputstream = httpresponse.getEntity().getContent();
						line = convertStreamToString(inputstream);
						JSONObject myjson = new JSONObject(line);	        	     
						String id=myjson.get("idcredenziali").toString();
						Integer idcred=Integer.parseInt(id);
		
					if(idcred!=0){
						Intent intent=new Intent(this,ProfiloActivity.class);
						Bundle b=new Bundle();
			        		b.putString("idcredenziali", id); //passa chiave valore a activity_home
			        		intent.putExtras(b); //intent x passaggio parametri
			        		startActivity(intent);
			        	}
					else
						Toast.makeText(getApplicationContext(), "Email già registrata", Toast.LENGTH_LONG).show();
					
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
