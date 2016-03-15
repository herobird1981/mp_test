package com.kevin.mmitestx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kevin.mmitestx.R;


public class OrientationSensorTest extends Activity {
	SensorManager mSensorManager;
	Sensor mOrientationSensor;
	//private int nSensorEventCnt = 0;
	private float [] values = {0, 0, 0};
	private String stringOthers; 
	private boolean bUpdateOthers = false;
	private boolean bFirstValues = false;
	private boolean bXChanged = false;
	private boolean bYChanged = false;
	private boolean bZChanged = false;
    private TextView field_model;
    private TextView field_accuracy;
    private TextView field_sensor;
    private TextView field_timestamp;
    private TextView field_values;
    private TextView field_others;
    private Button button_test_ng;
    private Button button_test_good;
	SharedPreferences result;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orientationsensor);
		
		result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		findViews();
		setListeners();
	}

	private void findViews(){
		field_model = (TextView)findViewById(R.id.textView3);
		field_accuracy = (TextView)findViewById(R.id.textView4);
		field_sensor = (TextView)findViewById(R.id.textView5);
		field_timestamp = (TextView)findViewById(R.id.textView6);
		field_values = (TextView)findViewById(R.id.textView7);
		field_others = (TextView)findViewById(R.id.textView8);
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if(mOrientationSensor != null){
			field_model.setText(mOrientationSensor.getName().toString());
		} else {
			this.finish();
		}
				
		// Set initial Test Good buttons state to "Disable"
		button_test_good.setEnabled(false);
	}
	
	public void onResume(){
		super.onResume();
		mSensorManager.registerListener(sensorEventListener, 
				mOrientationSensor, 
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void onPause(){
		super.onPause();
		mSensorManager.unregisterListener(sensorEventListener, mOrientationSensor);
	}
	
	SensorEventListener sensorEventListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy){
			
		}
		
		public void onSensorChanged(SensorEvent event){
			field_accuracy.setText(Integer.valueOf(event.accuracy).toString());
			field_sensor.setText(Integer.valueOf(event.sensor.getType()).toString());
			field_timestamp.setText(Long.valueOf(event.timestamp).toString());
			field_values.setText(Float.valueOf(Float.valueOf(event.values[0])).toString()
					+ ", " + Float.valueOf(Float.valueOf(event.values[1])).toString()
					+ ", " + Float.valueOf(Float.valueOf(event.values[2])).toString());
			
			if (!bFirstValues){
				bFirstValues = true;
				values[0] = event.values[0];
				values[1] = event.values[1];
				values[2] = event.values[2];
				bUpdateOthers = true;
			}
			
			if (Math.abs(values[0] - event.values[0]) > 15){
				bXChanged = true;
			}
			if (Math.abs(values[1] - event.values[1]) > 10){
				bYChanged = true;
			}
			if (Math.abs(values[2] - event.values[2]) > 10){
				bZChanged = true;
			}
			
			if (bUpdateOthers){
				bUpdateOthers = false;
				stringOthers = Float.valueOf(values[0]).toString() + ", "
						+ Float.valueOf(values[1]).toString() + ", "
						+ Float.valueOf(values[2]).toString();
				field_others.setText(stringOthers);
			}
			
			if (bXChanged && bYChanged && bZChanged){
				button_test_good.setEnabled(true);
			}
		}
	};
	
	private void setListeners(){
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("OrientationSensorTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			OrientationSensorTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("OrientationSensorTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			OrientationSensorTest.this.finish();
		}
	};
}
