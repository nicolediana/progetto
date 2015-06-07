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
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ModificaProfiloActivity extends Activity {
	private String nomeServlet="/ServletExample/ServletProfilo";
		
	String idprofilo;
	private String nome;
	private String cognome;
	private String nickname;
	private String citta;
	private Integer annonascita;
	private String sesso;
	private Integer cellulare;
	//private String linkfotoprofilo;
	private String password;
	String tiporichiesta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifica_profilo);
		
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
		
		caricaProfilo();
	}
	
	public void onClickSalva(View v) throws UnsupportedEncodingException {
		TextView nomedig=(TextView)findViewById(R.id.nome);
		TextView cognomedig=(TextView)findViewById(R.id.cognome);
		TextView nicknamedig=(TextView)findViewById(R.id.nickname);
		TextView cittadig=(TextView)findViewById(R.id.citta);
		TextView annonascitadig=(TextView)findViewById(R.id.annonascita);
		TextView pwDigitato=(TextView)findViewById(R.id.psw);
		TextView celldig=(TextView)findViewById(R.id.cellulare);
		
		
		nome= nomedig.getText().toString();
		cognome= cognomedig.getText().toString();
		nickname= nicknamedig.getText().toString();
		citta= cittadig.getText().toString();
		password= (String)pwDigitato.getText().toString();
		
		String cellulareStr= (String)celldig.getText().toString();
		if(cellulareStr.equals(null)||cellulareStr.equals(""))
			cellulare=0;
		else
			cellulare=Integer.parseInt(cellulareStr);
		
		String annonascitaStr= (String)annonascitadig.getText().toString();
		if(annonascitaStr.equals(null)||annonascitaStr.equals(""))
			annonascita=0;
		else
			annonascita=Integer.parseInt(annonascitaStr);

		//nickname non può essere nullo
		if(nickname.equals("")||nickname.equals(null))
				Toast.makeText(getApplicationContext(), " Inserire un Nickname", Toast.LENGTH_LONG).show();	
		else{				
			//città non deve essere nullo
			if(citta.equals("")||citta.equals(null))
				Toast.makeText(getApplicationContext(), "Citta' non valida", Toast.LENGTH_LONG).show();
			else{
				JSONObject jsonobj= new JSONObject();
				try{
					String tiporichiesta="aggiorna";
					//creazione della Json
					jsonobj.put("tiporichiesta", tiporichiesta );
					jsonobj.put("nome", nome );
					jsonobj.put("cognome", cognome );
					jsonobj.put("nickname", nickname );
					jsonobj.put("citta", citta );
					jsonobj.put("annonascita", annonascita );
					jsonobj.put("cellulare", cellulare );
					jsonobj.put("sesso", sesso );
					jsonobj.put("password", password );
					jsonobj.put("idprofilo", idprofilo );
					
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
					String idprofilo=myjson.get("idprofilo").toString();
					String risposta=myjson.get("risposta").toString();
					
					if(risposta.equals("si")){
						Intent intent=new Intent(this,HomeActivity.class);
						Bundle b=new Bundle();
					    b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
					    intent.putExtras(b); //intent x passaggio parametri
					    startActivity(intent);
					}
					else Toast.makeText(this, "Nickname non valido, sceglierne un altro", Toast.LENGTH_SHORT).show();
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
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean sessochecked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_sessoM:
	            if (sessochecked)
	                sesso="M";
	            break;
	        case R.id.radio_sessoF:
	            if (sessochecked)
	            	sesso="F";
	            break;
	    }
	}
	
		public void caricaProfilo() {
				
		//LEGGI PROFILO
		tiporichiesta="leggi";
		JSONObject myjson= new JSONObject();
		try{
			//creazione della Json
			myjson.put("tiporichiesta", tiporichiesta );
			myjson.put("idprofilo", idprofilo );
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
			
			TextView nomedig=(TextView)findViewById(R.id.nome);
			TextView cognomedig=(TextView)findViewById(R.id.cognome);
			TextView nicknamedig=(TextView)findViewById(R.id.nickname);
			TextView cittadig=(TextView)findViewById(R.id.citta);
			
			sesso=myjson2.get("sesso").toString();
			
			nomedig.setText(myjson2.get("nome").toString());
			cognomedig.setText(myjson2.get("cognome").toString());
			nicknamedig.setText(myjson2.get("nickname").toString());
			cittadig.setText(myjson2.get("citta").toString());
			if(!myjson2.get("annonascita").toString().equals("0"))
			{
				TextView annonascitadig=(TextView)findViewById(R.id.annonascita);
				annonascitadig.setText(Integer.parseInt(myjson2.get("annonascita").toString()));
			}
			if(!myjson2.get("cellulare").toString().equals("0"))
			{
				TextView celldig=(TextView)findViewById(R.id.cellulare);
				celldig.setText(Integer.parseInt(myjson2.get("cellulare").toString()));
			}
			
			if(sesso.equals("M")){
				RadioButton radio1 = (RadioButton)findViewById(R.id.radio_sessoM);
				radio1.setChecked(true);
			}
			if(sesso.equals("F")){
				RadioButton radio1 = (RadioButton)findViewById(R.id.radio_sessoF);
				radio1.setChecked(true);
			}
			
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		catch(UnsupportedEncodingException e){e.printStackTrace();}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
		
}
