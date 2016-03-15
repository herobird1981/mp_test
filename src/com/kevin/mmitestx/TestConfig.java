package com.kevin.mmitestx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.kevin.mmitestx.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.Toast;

public class TestConfig extends Activity {
	private static final boolean DBG = false;
	private boolean isChecked;
	private boolean isNull = false;
	private static final String TAG = "Test_Config";
	private static List<TestItem> list;
	private CheckBox LCD;
	private CheckBox TP;
	private CheckBox Backlight;
	private CheckBox Flashlight;
	private CheckBox KP;
	private CheckBox SARADC;
	private CheckBox Speaker;
	private CheckBox Headphone;
	private CheckBox Video;
	private CheckBox Vib;
	private CheckBox Receiver;
	private CheckBox FC;
	private CheckBox RC;
	private CheckBox Asensor;
	private CheckBox Msensor;
	private CheckBox Osensor;
	private CheckBox Lsensor;
	private CheckBox Psensor;
	private CheckBox BT;
	private CheckBox Wifi;
	private CheckBox GPS;
	private CheckBox SD;
	ProgressDialog progressdialog;
	SharedPreferences sp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testconfig);
		list = readXml(this);
		sp = getSharedPreferences("Config_Item", MODE_WORLD_WRITEABLE);
		TableLayout tl = (TableLayout) findViewById(R.id.maintable);
		
		LCD = (CheckBox) findViewById(R.id.LCDTest_Checkbox);
		TP = (CheckBox) findViewById(R.id.TouchPanelTest_Checkbox);
		Backlight = (CheckBox) findViewById(R.id.BacklightTest_Checkbox);
		Flashlight = (CheckBox) findViewById(R.id.Flashlight_Checkbox);
		KP = (CheckBox) findViewById(R.id.KeypadTest_Checkbox);
		SARADC = (CheckBox) findViewById(R.id.SARADCTest_Checkbox);
		Speaker = (CheckBox) findViewById(R.id.SpeakerTest_Checkbox);
		Headphone = (CheckBox) findViewById(R.id.HeadphoneTest_Checkbox);
		Video = (CheckBox) findViewById(R.id.VideoTest_Checkbox);
		Vib = (CheckBox) findViewById(R.id.VibratorTest_Checkbox);
		Receiver = (CheckBox) findViewById(R.id.ReceiverMicTest_Checkbox);
		FC = (CheckBox) findViewById(R.id.FrontCameraTest_Checkbox);
		RC = (CheckBox) findViewById(R.id.RearCameraTest_Checkbox);
		Asensor = (CheckBox) findViewById(R.id.AccelerometerSensorTest_Checkbox);
		Msensor = (CheckBox) findViewById(R.id.MagneticSensorTest_Checkbox);
		Osensor = (CheckBox) findViewById(R.id.OrientationSensorTest_Checkbox);
		Lsensor = (CheckBox) findViewById(R.id.LightSensorTest_Checkbox);
		Psensor = (CheckBox) findViewById(R.id.ProximitySensorTest_Checkbox);
		BT = (CheckBox) findViewById(R.id.BluetoothTest_Checkbox);
		Wifi = (CheckBox) findViewById(R.id.WiFiTest_Checkbox);
		GPS = (CheckBox) findViewById(R.id.GPSTest_Checkbox);
		SD = (CheckBox) findViewById(R.id.SDCardTest_Checkbox);
		
			
		for (TestItem ti : list) 
		{			
				//nTestResult = result.getInt(ti.getName(), MMITestX.TEST_NONE);
				if (ti.getName().equals("LCDTest")) 
				{
					if(sp.getBoolean("LCDTest", false)){
						LCD.setChecked(true);
						isChecked = true;	
					}
					else
					{
						LCD.setChecked(false);
						isChecked = false;
					}
				} 
				else if (ti.getName().equals("TouchPanelTest"))
				{
					if(sp.getBoolean("TouchPanelTest", false)){
						TP.setChecked(true);
						isChecked = true;	
					}
					else
					{
						TP.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("BacklightTest"))
				{
					if(sp.getBoolean("BacklightTest", false)){
						Backlight.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Backlight.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("FlashlightTest"))
				{
					if(sp.getBoolean("FlashlightTest", false)){
						Flashlight.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Flashlight.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("KeypadTest"))
				{
					if(sp.getBoolean("KeypadTest", false)){
						KP.setChecked(true);
						isChecked = true;	
					}
					else
					{
						KP.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("SARADCTest"))
				{
					if(sp.getBoolean("SARADCTest", false)){
						SARADC.setChecked(true);
						isChecked = true;	
					}
					else
					{
						SARADC.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("SpeakerTest"))
				{
					if(sp.getBoolean("SpeakerTest", false)){
						Speaker.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Speaker.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("HeadphoneTest"))
				{
					if(sp.getBoolean("HeadphoneTest", false)){
						Headphone.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Headphone.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("VideoTest"))
				{
					if(sp.getBoolean("VideoTest", false)){
						Video.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Video.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("VibratorTest"))
				{
					if(sp.getBoolean("VibratorTest", false)){
						Vib.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Vib.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("ReceiverMicTest"))
				{
					if(sp.getBoolean("ReceiverMicTest", false)){
						Receiver.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Receiver.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("FrontCameraTest"))
				{
					if(sp.getBoolean("FrontCameraTest", false)){
						FC.setChecked(true);
						isChecked = true;	
					}
					else
					{
						FC.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("RearCameraTest"))
				{
					if(sp.getBoolean("RearCameraTest", false)){
						RC.setChecked(true);
						isChecked = true;	
					}
					else
					{
						RC.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("AccelerometerSensorTest"))
				{
					if(sp.getBoolean("AccelerometerSensorTest", false)){
						Asensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Asensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("MagneticSensorTest"))
				{
					if(sp.getBoolean("MagneticSensorTest", false)){
						Msensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Msensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("OrientationSensorTest"))
				{
					if(sp.getBoolean("OrientationSensorTest", false)){
						Osensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Osensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("LightSensorTest"))
				{
					if(sp.getBoolean("LightSensorTest", false)){
						Lsensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Lsensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("ProximitySensorTest"))
				{
					if(sp.getBoolean("ProximitySensorTest", false)){
						Psensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Psensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("ProximitySensorTest"))
				{
					if(sp.getBoolean("ProximitySensorTest", false)){
						Psensor.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Psensor.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("BluetoothTest"))
				{
					if(sp.getBoolean("BluetoothTest", false)){
						BT.setChecked(true);
						isChecked = true;	
					}
					else
					{
						BT.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("WiFiTest"))
				{
					if(sp.getBoolean("WiFiTest", false)){
						Wifi.setChecked(true);
						isChecked = true;	
					}
					else
					{
						Wifi.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("GPSTest"))
				{
					if(sp.getBoolean("GPSTest", false)){
						GPS.setChecked(true);
						isChecked = true;	
					}
					else
					{
						GPS.setChecked(false);
						isChecked = false;
					}
				}
				else if (ti.getName().equals("SDCardTest"))
				{
					if(sp.getBoolean("SDCardTest", false)){
						SD.setChecked(true);
						isChecked = true;	
					}
					else
					{
						SD.setChecked(false);
						isChecked = false;
					}
				}			
				sp.edit().putBoolean(ti.getName(), isChecked).commit();
		}
		
		View lineV = new View(this);
		lineV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lineV.setBackgroundColor(Color.argb(255, 255, 182, 75));
		tl.addView(lineV);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,Menu.FIRST,1,R.string.config_save).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, Menu.FIRST+1, 2, R.string.config_select_all).setIcon(R.drawable.select_all);
		menu.add(0, Menu.FIRST+2, 3, R.string.config_select_none).setIcon(R.drawable.select_none);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
		{
			case Menu.FIRST:
				showSaveDialog();
				saveConfig();
				mhandler.sendEmptyMessageDelayed(100, 1500);
				break;
			case Menu.FIRST+1:
				LCD.setChecked(true);
				TP.setChecked(true);
				Backlight.setChecked(true);
				Flashlight.setChecked(true);
				KP.setChecked(true);
				SARADC.setChecked(true);
				Speaker.setChecked(true);
				Headphone.setChecked(true);
				Video.setChecked(true);
				Vib.setChecked(true);
				Receiver.setChecked(true);
				FC.setChecked(true);
				RC.setChecked(true);
				Asensor.setChecked(true);
				Msensor.setChecked(true);
				Osensor.setChecked(true);
				Lsensor.setChecked(true);
				Psensor.setChecked(true);
				BT.setChecked(true);
				Wifi.setChecked(true);
				GPS.setChecked(true);
				SD.setChecked(true);
				break;
			case Menu.FIRST+2:
				LCD.setChecked(false);
				TP.setChecked(false);
				Backlight.setChecked(false);
				Flashlight.setChecked(false);
				KP.setChecked(false);
				SARADC.setChecked(false);
				Speaker.setChecked(false);
				Headphone.setChecked(false);
				Video.setChecked(false);
				Vib.setChecked(false);
				Receiver.setChecked(false);
				FC.setChecked(false);
				RC.setChecked(false);
				Asensor.setChecked(false);
				Msensor.setChecked(false);
				Osensor.setChecked(false);
				Lsensor.setChecked(false);
				Psensor.setChecked(false);
				BT.setChecked(false);
				Wifi.setChecked(false);
				GPS.setChecked(false);
				SD.setChecked(false);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	static List<TestItem> readXml(Context context) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			InputSource is = null;
			String loc = Locale.getDefault().toString();
			if (DBG) Log.d(TAG, loc);
			if(loc.endsWith("US")||loc.endsWith("CA")||loc.endsWith("AU")||loc.endsWith("GB")||loc.endsWith("NZ")||loc.endsWith("en_SG"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-en.xml")));
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
			else if (loc.endsWith("CN"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-zh.xml")));
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
			else if (loc.endsWith("TW"))
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("settings-zh-tw.xml")));
			    is = new InputSource(br);
			    TestHandler dh = new TestHandler();
			    try {
			    	parser.parse(is, dh);
			    	return dh.getList();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			}
			}catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			if (msg.what==100)
			{
				progressdialog.dismiss();
				if (!isNull)
				{
					Toast.makeText(getApplicationContext(), R.string.save_toast, Toast.LENGTH_LONG).show();
				}
				TestConfig.this.finish();
			}
			super.handleMessage(msg);
		}
		
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{	
			Toast.makeText(getApplication(), R.string.not_saved, Toast.LENGTH_SHORT).show();
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showSaveDialog()
	{
		String loc = Locale.getDefault().toString();
		progressdialog = new ProgressDialog(TestConfig.this);
		progressdialog.setTitle(R.string.save_progressdialog_title);
		progressdialog.setIcon(android.R.drawable.ic_menu_save);
		progressdialog.setCancelable(false);
		progressdialog.setIndeterminate(true);
		if(loc.endsWith("US")||loc.endsWith("CA")||loc.endsWith("AU")||loc.endsWith("GB")||loc.endsWith("NZ")||loc.endsWith("en_SG"))
		{
			progressdialog.setMessage("Please wait...");
		}
		else if (loc.endsWith("CN"))
		{
			progressdialog.setMessage("«Î…‘∫Û...");
		}
		else if (loc.endsWith("TW"))
		{
			progressdialog.setMessage("’à…‘··...");
		}
		progressdialog.show();
	}

	// A method for writing the changes to XML file.
	private void saveConfig()
	{
		boolean lcd = LCD.isChecked();
		boolean tp = TP.isChecked();
		boolean backlight = Backlight.isChecked();
		boolean flashlight = Flashlight.isChecked();
		boolean kp = KP.isChecked();
		boolean sar = SARADC.isChecked();
		boolean speaker = Speaker.isChecked();
		boolean headphone = Headphone.isChecked();
		boolean video = Video.isChecked();
		boolean vib = Vib.isChecked();
		boolean receiver = Receiver.isChecked();
		boolean fc = FC.isChecked();
		boolean rc = RC.isChecked();
		boolean asensor = Asensor.isChecked();
		boolean msensor = Msensor.isChecked();
		boolean osensor = Osensor.isChecked();
		boolean lsensor = Lsensor.isChecked();
		boolean psensor = Psensor.isChecked();
		boolean bt = BT.isChecked();
		boolean wifi = Wifi.isChecked();
		boolean gps = GPS.isChecked();
		boolean sd = SD.isChecked();		
		
		if(!(lcd || tp || backlight || flashlight || kp || sar || speaker || headphone || video || vib || receiver || fc || rc || asensor || msensor || osensor || lsensor || psensor || bt || wifi || gps || sd))
		{
			Toast.makeText(getApplication(), R.string.save_fail, Toast.LENGTH_SHORT).show();
			isNull = true;
		}
		else
		{
			sp.edit().putBoolean("LCDTest", lcd).commit();
			sp.edit().putBoolean("TouchPanelTest", tp).commit();
			sp.edit().putBoolean("BacklightTest", backlight).commit();
			sp.edit().putBoolean("FlashlightTest", flashlight).commit();
			sp.edit().putBoolean("KeypadTest", kp).commit();
			sp.edit().putBoolean("SARADCTest", sar).commit();
			sp.edit().putBoolean("SpeakerTest", speaker).commit();
			sp.edit().putBoolean("HeadphoneTest", headphone).commit();
			sp.edit().putBoolean("VideoTest", video).commit();
			sp.edit().putBoolean("VibratorTest", vib).commit();
			sp.edit().putBoolean("ReceiverMicTest", receiver).commit();
			sp.edit().putBoolean("FrontCameraTest", fc).commit();
			sp.edit().putBoolean("RearCameraTest", rc).commit();
			sp.edit().putBoolean("AccelerometerSensorTest", asensor).commit();
			sp.edit().putBoolean("MagneticSensorTest", msensor).commit();
			sp.edit().putBoolean("OrientationSensorTest", osensor).commit();
			sp.edit().putBoolean("LightSensorTest", lsensor).commit();
			sp.edit().putBoolean("ProximitySensorTest", psensor).commit();
			sp.edit().putBoolean("BluetoothTest", bt).commit();
			sp.edit().putBoolean("WiFiTest", wifi).commit();
			sp.edit().putBoolean("GPSTest", gps).commit();
			sp.edit().putBoolean("SDCardTest", sd).commit();
			if(DBG) Log.d(TAG,"CONFIG SAVED!");
		}				
	}
}
