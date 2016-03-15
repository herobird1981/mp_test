package com.kevin.mmitestx;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SpeakerTest_Setting extends Activity
{
    private TextView soundTextView;
    private Spinner soundSpinner; 
    private ArrayAdapter<?> soundAdapter;
    SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker_setting);
		
		sp = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);
		soundTextView = (TextView) findViewById(R.id.sound_prompt);
		soundSpinner = (Spinner) findViewById(R.id.soundspinner); 
	    
	    final String[] soundType = { getResources().getString(R.string.soundtype_ringtone),
	    							 getResources().getString(R.string.soundtype_notification),
	    							 getResources().getString(R.string.soundtype_alarm)};
	    
	    soundAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_dropdown_item, soundType);
	    soundSpinner.setAdapter(soundAdapter);
	    soundSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
	    { 
	    	 
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	        {
	          soundTextView.setText(R.string.sound_prompt);
	          switch (arg2)
	          {
	          	case 0: 
	          		sp.edit().putString("sound_type", "Ringtone").commit();
	          		break;
	          	case 1:
	          		sp.edit().putString("sound_type", "Notification").commit();
	          		break;
	          	case 2:
	          		sp.edit().putString("sound_type", "Alarm").commit();
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
