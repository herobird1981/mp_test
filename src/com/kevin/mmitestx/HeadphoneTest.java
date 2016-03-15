package com.kevin.mmitestx;

import com.kevin.mmitestx.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class HeadphoneTest extends Activity {
	private final boolean DBG = false;
	private final String TAG = "Headphone_Test";
	private AudioManager am = null;
	//SharedPreferences result;
	BroadcastsHandler bh = new BroadcastsHandler();
	private Button button_test_ng;
	private Button button_test_good;
	private Button button_listen_again;
	private CheckBox checkbox_Headphone;
	private Ringtone r;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headphone);
		
		//result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();

		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		if(alert == null) {
			if (DBG) Log.d(TAG, "RingtoneManager.TYPE_RINGTONE is null");
			// alert is null, using backup
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
				if (DBG) Log.d(TAG, "RingtoneManager.TYPE_NOTIFICATION=null");
				// alert backup is null, using 2nd backup
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);               
	       		}
	    	}
		r = RingtoneManager.getRingtone(HeadphoneTest.this, alert);
		r.setStreamType(AudioManager.STREAM_MUSIC);
	}
	
	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button1);
		button_test_good = (Button)findViewById(R.id.button2);
		button_listen_again = (Button)findViewById(R.id.button3);
		checkbox_Headphone = (CheckBox)findViewById(R.id.checkBox1);
		
		checkbox_Headphone.setEnabled(false);
		
		// Detect whether wired-headset/headphone is plugged
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		if (am.isWiredHeadsetOn()) {
			checkbox_Headphone.setChecked(true);
			button_test_good.setEnabled(true);
		} else {
			checkbox_Headphone.setChecked(false);
			button_test_good.setEnabled(false);
		}
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
            setResult(MMITestX.TEST_NOT_GOOD);
            HeadphoneTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			if (r.isPlaying()) {
				r.stop();
			}
            setResult(MMITestX.TEST_GOOD);
            HeadphoneTest.this.finish();
		}
	};
	
	private Button.OnClickListener listenAgain = new Button.OnClickListener() {
		public void onClick(View v) {
			if (am.isWiredHeadsetOn()) {
				r.play();
			} else {
				showToast(R.string.insert_headphone, Toast.LENGTH_SHORT);
			}
		}
	};
	
	protected void onResume() {
		super.onResume();
		if (DBG) Log.d(TAG, "onResume, registerReceiver");
		am.setSpeakerphoneOn(false);
    	this.registerReceiver(bh, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	}
	
	protected void onPause() {
		if (r.isPlaying()) {
			r.stop();
		}
		this.unregisterReceiver(bh);
		if (DBG) Log.d(TAG, "onPause, unregisterReceiver");
		super.onPause();
	}
	
	private void showToast(int strRes,int duration){
		Toast.makeText(getApplicationContext(), strRes, duration).show();
	}
	
	class BroadcastsHandler extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		    if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
		        //String data = intent.getDataString();
		        //Bundle extraData = intent.getExtras();
	
		        int st = intent.getIntExtra("state", -1);
                if (DBG) {
                    String nm = intent.getStringExtra("name");
                    int mic = intent.getIntExtra("microphone", -1);
                    Log.d(TAG, "st=" + Integer.toString(st) + "; nm=" + nm + "; mic=" + Integer.toString(mic));
                }	
		        if (st == 0) {
		        	checkbox_Headphone.setChecked(false);
		        	button_test_good.setEnabled(false);
		        	if (r.isPlaying()) {
		        		r.stop();
		        	}
		        } else if (st == 1) {
		        	checkbox_Headphone.setChecked(true);
		        	button_test_good.setEnabled(true);
		        	if (!r.isPlaying()) {
		        		r.play();
		        	}
		        }
		    }
		}
	}
}
