package com.github.nicolediana.siriofootballfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InfoActivity extends Activity {
	String idprofilo="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		Intent i=getIntent();
		idprofilo= i.getStringExtra("idprofilo");
	}
}
