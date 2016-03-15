package com.kevin.mmitestx;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.app.AlertDialog;

public class ClearReport extends Activity
{
	private boolean DBG = false;
	private static final String TAG = "ClearReport";
	public static final int RESET = 0;
	SharedPreferences reset;
	Editor ed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		reset = getSharedPreferences("MMITestX_TestResult",MODE_WORLD_READABLE);
		new AlertDialog.Builder(ClearReport.this)
		.setIcon(android.R.drawable.ic_input_delete)
		.setTitle(R.string.clear_dialog_title)
		.setMessage(R.string.clear_dialog_message)
		.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener(){
		
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				clearReport();
				if (DBG) Log.d(TAG, "Test results cleared!!!");
				showToast(R.string.clear_report_Toast, Toast.LENGTH_LONG);
				ClearReport.this.finish();
			}
		})
		.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				ClearReport.this.finish();
			}
		})
		.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				ClearReport.this.finish();
			}
		}).show();
	}
	
	private void showToast(int str,int duration){
		Toast.makeText(ClearReport.this, str, duration).show();
	}
	
	private void clearReport(){
		ed=reset.edit();
		ed.putInt("LCDTest", RESET);
		ed.putInt("TouchPanelTest", RESET);
		ed.putInt("BacklightTest", RESET);
		ed.putInt("FlashlightTest", RESET);
		ed.putInt("KeypadTest", RESET);
		ed.putInt("SARADCTest", RESET);
		ed.putInt("SpeakerTest", RESET);
		ed.putInt("HeadphoneTest", RESET);
		ed.putInt("VideoTest", RESET);
		ed.putInt("VibratorTest", RESET);
		ed.putInt("ReceiverMicTest", RESET);
		ed.putInt("FrontCameraTest", RESET);
		ed.putInt("RearCameraTest", RESET);
		ed.putInt("AccelerometerSensorTest", RESET);
		ed.putInt("MagneticSensorTest", RESET);
		ed.putInt("OrientationSensorTest", RESET);
		ed.putInt("LightSensorTest", RESET);
		ed.putInt("ProximitySensorTest", RESET);
		ed.putInt("BluetoothTest", RESET);
		ed.putInt("WiFiTest", RESET);
		ed.putInt("GPSTest", RESET);
		ed.putInt("SDCardTest", RESET);
		ed.putLong("ProdLineModeElapsedTime", RESET);
		ed.commit();
	}
}
