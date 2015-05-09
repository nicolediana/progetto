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
		Intent intent=new Intent(this,PartiteInProgrammaActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}
	
	public void onClick_PartiteGiocate(View v) {
		Intent intent=new Intent(this,PartiteGiocateActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}
	
	public void onClick_PartiteOrganizzate(View v) {
		Intent intent=new Intent(this,PartiteOrganizzateActivity.class);
		Bundle b=new Bundle();
		b.putString("idprofilo", idprofilo); //passa chiave valore a activity_home
		intent.putExtras(b); //intent x passaggio parametri
		startActivity(intent);
	}
}