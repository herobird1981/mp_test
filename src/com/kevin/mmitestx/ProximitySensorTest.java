package com.kevin.mmitestx;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.kevin.mmitestx.R;

public class ProximitySensorTest extends Activity {
	SensorManager mSensorManager;
	Sensor mProximitySensor;
	private int nSensorEventCnt = 0;
	private float value;
    private TextView field_model;
    private TextView field_accuracy;
    private TextView field_sensor;
    private TextView field_timestamp;
    private TextView field_values;
    private Button button_test_ng;
    private Button button_test_good;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.proximitysensor);
		findViews();
		setListeners();
	}

	private void findViews(){
		field_model = (TextView)findViewById(R.id.textView3);
		field_accuracy = (TextView)findViewById(R.id.textView4);
		field_sensor = (TextView)findViewById(R.id.textView5);
		field_timestamp = (TextView)findViewById(R.id.textView6);
		field_values = (TextView)findViewById(R.id.textView7);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if(mProximitySensor != null){
			field_model.setText(mProximitySensor.getName().toString());
		} else {
			showToast(R.string.no_proximitysensor, Toast.LENGTH_SHORT);
			//this.finish();
		}
		
		// Set initial Test Good buttons state to "Disable"
		button_test_good.setEnabled(false);
	}
	
	public void onResume(){
		super.onResume();
		mSensorManager.registerListener(proximitySensorEventListener, 
				mProximitySensor, 
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void onPause(){
		super.onPause();
		mSensorManager.unregisterListener(proximitySensorEventListener, mProximitySensor);
	}
	
	SensorEventListener proximitySensorEventListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy){
			
		}
		
		public void onSensorChanged(SensorEvent event){
			field_accuracy.setText(Integer.valueOf(event.accuracy).toString());
			field_sensor.setText(Integer.valueOf(event.sensor.getType()).toString());
			field_timestamp.setText(Long.valueOf(event.timestamp).toString());
			field_values.setText(Float.valueOf(Float.valueOf(event.values[0])).toString());
			
			if (value != event.values[0]){
				nSensorEventCnt++;
				
				if (nSensorEventCnt >= 2){
					button_test_good.setEnabled(true);
				}
			}
		}
	};
	
	private void setListeners(){
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("ProximitySensorTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			ProximitySensorTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("ProximitySensorTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			ProximitySensorTest.this.finish();
		}
	};
	
	private void showToast(int strRes, int duration){
		Toast.makeText(getApplicationContext(), strRes, duration).show();
	}
}
