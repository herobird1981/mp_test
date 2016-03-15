package com.kevin.mmitestx;

import com.kevin.mmitestx.R;
import android.app.Activity;
import android.content.SharedPreferences;
//import android.content.SharedPreferences;
//import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SpeakerTest extends Activity {
	private final boolean DBG = false;
	private final String TAG = "Speaker_Test";
	//SharedPreferences result;
	private Button button_test_ng;
	private Button button_test_good;
	private Button button_listen_again;
	private Ringtone r;
	Uri alert;
	SharedPreferences getSoundType;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker);	
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
		getSoundType = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);	
		alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		r = RingtoneManager.getRingtone(SpeakerTest.this, alert);	
	}
	
	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button1);
		button_test_good = (Button)findViewById(R.id.button2);
		button_listen_again = (Button)findViewById(R.id.button3);
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		button_listen_again.setOnClickListener(listenAgain);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			if (r.isPlaying()) {
				r.stop();
			}
			//result.edit().putInt("SpeakerTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			SpeakerTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			if (r.isPlaying()) {
				r.stop();
			}
			//result.edit().putInt("SpeakerTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			SpeakerTest.this.finish();
		}
	};
	
	private Button.OnClickListener listenAgain = new Button.OnClickListener() {
		public void onClick(View v) {
			r.play();
		}
	};
	
	protected void onResume() {
		super.onResume();
		if(getSoundType.getString("sound_type", "") == "Ringtone")
		{
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			r = RingtoneManager.getRingtone(SpeakerTest.this, alert);
		}
		else if(getSoundType.getString("sound_type", "") == "Notification")
		{
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);   
			r = RingtoneManager.getRingtone(SpeakerTest.this, alert);
	    }
		else if (getSoundType.getString("sound_type", "") == "Alarm")
		{
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			r = RingtoneManager.getRingtone(SpeakerTest.this, alert);
		}
		r.play();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		r.stop();
		if (DBG) Log.d(TAG, "onPause: Ringtone stopped!");
		super.onPause();
	}
}
