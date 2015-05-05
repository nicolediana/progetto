package com.github.nicolediana.siriofootballfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class InfoActivity extends Activity {

	String idprofilo="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		//recupera parametro passato
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
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
		if (id == R.id.action_home) {
			Intent intent=new Intent(this,HomeActivity.class);
			Bundle b=new Bundle();
			b.putString("idprofilo", i.getStringExtra("idprofilo")); 
			intent.putExtras(b); 
			startActivity(intent);			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
