package com.example.karthik_cust;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends Activity {

	CabDriver[] drivers = new CabDriver[10];
	String phoneNumForMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		for(int i=0;i<10;i++)
		{
			drivers[i] = new CabDriver("Karthik","Route "+i,"9035673873"); 
			Log.i("test",drivers[i].name);
		}



		Spinner spinner = (Spinner)findViewById(R.id.spinner);
		List<String>vehicleNos = new ArrayList<String>();

		for(int i=0;i<drivers.length;i++)
		{
			vehicleNos.add(drivers[i].vehicle_no);
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,vehicleNos);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

	
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				phoneNumForMsg= drivers[pos].phone_no;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				phoneNumForMsg= drivers[0].phone_no;
			}
		});


		ImageButton getLoc=(ImageButton)findViewById(R.id.button1);
		getLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SmsManager smsMngr= SmsManager.getDefault();
				smsMngr.sendTextMessage(phoneNumForMsg,null,"LOC",null,null);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
