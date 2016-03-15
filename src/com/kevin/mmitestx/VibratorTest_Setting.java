package com.kevin.mmitestx;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class VibratorTest_Setting extends Activity
{
    private TextView vibTextView;
    private Spinner vibSpinner; 
    private ArrayAdapter<?> vibAdapter;
    SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vibrator_setting);
		
		sp = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);
		vibTextView = (TextView) findViewById(R.id.vib_prompt);
		vibSpinner = (Spinner) findViewById(R.id.vibspinner); 
	    
	    final String[] soundType = { getResources().getString(R.string.vibmode_always),
	    							 getResources().getString(R.string.vibmode_pattern)};
	    
	    vibAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_dropdown_item, soundType);
	    vibSpinner.setAdapter(vibAdapter);
	    vibSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
	    { 
	    	 
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	        {
	          vibTextView.setText(R.string.vib_prompt);
	          switch (arg2)
	          {
	          	case 0:
	          		sp.edit().putString("vibration_mode", "Always").commit();
	          		break;
	          	case 1:
	          		sp.edit().putString("vibration_mode", "Pattern").commit();
	          		break;
	          }
	          arg0.setVisibility(View.VISIBLE);
	        } 
	        public void onNothingSelected(AdapterView<?> arg0)
	        { 
	          // TODO Auto-generated method stub 
	        }	         
	    });
}
}
