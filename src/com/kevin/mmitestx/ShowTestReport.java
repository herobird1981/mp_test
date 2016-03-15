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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTestReport extends Activity {
	private static final boolean DBG = true;
	private static final String TAG = "ShowTestReport";
	public static final int RESET = 0;
	private static List<TestItem> list,list2;
	ProgressDialog progressdialog;
	long ElapsedTime;
	int nTestResult;
	SharedPreferences sp,result;
	TableLayout tl;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testreport);	
		result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		//reset = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		sp = getSharedPreferences("Config_Item", MODE_WORLD_READABLE);
		list2 = configuedList();
		tl = (TableLayout) findViewById(R.id.maintable);
		updateResult();
	}
	
	private void updateResult()
	{
		for(TestItem ti : list2) 
		{
				TableRow tr = new TableRow(this);
				tr.setLayoutParams(new LayoutParams(
	                    LayoutParams.MATCH_PARENT,
	                    LayoutParams.WRAP_CONTENT)); 
	
				TextView labelTV = new TextView(this);
				labelTV.setText(ti.getTitle());
				labelTV.setTextColor(Color.WHITE);
				tr.addView(labelTV);
	
				TextView valueTV = new TextView(this);
				nTestResult = result.getInt(ti.getName(), MMITestX.TEST_NONE);
				if (DBG)
					Log.d(TAG, "Test Item (" + ti.getName() + ")=" + nTestResult);
				if (nTestResult == MMITestX.TEST_GOOD) {
					valueTV.setText(R.string.test_good);
					valueTV.setTextColor(Color.GREEN);
				} else if (nTestResult == MMITestX.TEST_NOT_GOOD) {
					valueTV.setText(R.string.test_ng);
					valueTV.setTextColor(Color.RED);
				} else if (nTestResult == MMITestX.TEST_NONE) {
					valueTV.setText(R.string.test_none);
					valueTV.setTextColor(Color.DKGRAY);
				}
				tr.addView(valueTV);
				
				// Add the TableRow to the TableLayout
	            tl.addView(tr, new TableLayout.LayoutParams(
	                    LayoutParams.MATCH_PARENT,
	                    LayoutParams.WRAP_CONTENT));
	            
				View lineV = new View(this);
				lineV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
				lineV.setBackgroundColor(Color.argb(200, 255, 182, 75));
				tl.addView(lineV);			
		}	
					
		View lineV = new View(this);
		lineV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lineV.setBackgroundColor(Color.argb(255, 255, 182, 75));
		tl.addView(lineV);


		// Print elapsed time
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)); 
		// Set label
		TextView labelTV = new TextView(this);
		labelTV.setText(R.string.report_elapsed_time);
		labelTV.setTextColor(Color.YELLOW);
		tr.addView(labelTV);	
		// Set elapsed time
		TextView valueTV = new TextView(this);
		ElapsedTime = result.getLong("ProdLineModeElapsedTime", 0);
		valueTV.setText((Float.toString(((float)ElapsedTime)/1000)));
		valueTV.setTextColor(Color.YELLOW);
		tr.addView(valueTV);
		// Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,Menu.FIRST,1,R.string.clear_report).setIcon(R.drawable.clear_report);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
		{
			case Menu.FIRST:
				clearReport();
				showClearDialog();
				mhandler.sendEmptyMessageDelayed(100, 2000);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case 100:
					ShowTestReport.this.finish();
					startActivity(new Intent(ShowTestReport.this, ShowTestReport.class));
					progressdialog.cancel();
					Toast.makeText(getApplicationContext(), R.string.clear_report_Toast, Toast.LENGTH_SHORT).show();
					break;
			}
			super.handleMessage(msg);
		}		
	};

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
	
	public List<TestItem> configuedList()
	{
		List<TestItem> blist = null;
		list = readXml(this);
		int mIndex;	
		if (DBG) Log.d(TAG, "Original list size: "+ list.size());
		for (mIndex=0; mIndex<list.size(); mIndex++)
		{
			TestItem ti1 = list.get(mIndex);
			if(!sp.getBoolean(ti1.getName(), false))
			{
				list.remove(mIndex);
				mIndex--;
			}
	    }
		if (DBG) Log.d(TAG, "Modified list size: "+ list.size());
		blist = list;
		return blist;	
	}
	
	private void showClearDialog()
	{
	    String str = Locale.getDefault().toString();
	    if(DBG)Log.d(TAG, str);
	    progressdialog = new ProgressDialog(ShowTestReport.this);
	    progressdialog.setTitle(R.string.clear_progressdialog_title);
	    progressdialog.setIcon(R.drawable.clear_report);
	    progressdialog.setCancelable(false);
	    progressdialog.setIndeterminate(true);
	    if ((str.endsWith("US")) || (str.endsWith("CA")) || (str.endsWith("AU")) || (str.endsWith("GB")) || (str.endsWith("NZ")) || (str.endsWith("en_SG")))
	    {
	    	progressdialog.setMessage("Please wait...");
	    }
	    else if(str.endsWith("CN"))
	    {
	        progressdialog.setMessage("ÇëÉÔºó...");
	    }
	    else if (str.endsWith("TW"))
	    {
	    	progressdialog.setMessage("ÕˆÉÔáá...");
	    }
	    progressdialog.show();    
	 }

	
	private void clearReport()
	{
		result.edit().putInt("LCDTest", RESET).commit();
		result.edit().putInt("TouchPanelTest", RESET).commit();
		result.edit().putInt("BacklightTest", RESET).commit();
		result.edit().putInt("FlashlightTest", RESET).commit();
		result.edit().putInt("KeypadTest", RESET).commit();
		result.edit().putInt("SARADCTest", RESET).commit();
		result.edit().putInt("SpeakerTest", RESET).commit();
		result.edit().putInt("HeadphoneTest", RESET).commit();
		result.edit().putInt("VideoTest", RESET).commit();
		result.edit().putInt("VibratorTest", RESET).commit();
		result.edit().putInt("ReceiverMicTest", RESET).commit();
		result.edit().putInt("FrontCameraTest", RESET).commit();
		result.edit().putInt("RearCameraTest", RESET).commit();
		result.edit().putInt("AccelerometerSensorTest", RESET).commit();
		result.edit().putInt("MagneticSensorTest", RESET).commit();
		result.edit().putInt("OrientationSensorTest", RESET).commit();
		result.edit().putInt("LightSensorTest", RESET).commit();
		result.edit().putInt("ProximitySensorTest", RESET).commit();
		result.edit().putInt("BluetoothTest", RESET).commit();
		result.edit().putInt("WiFiTest", RESET).commit();
		result.edit().putInt("GPSTest", RESET).commit();
		result.edit().putInt("SDCardTest", RESET).commit();
		result.edit().putLong("ProdLineModeElapsedTime", RESET).commit();
		if (DBG) Log.d(TAG, "Report Cleared!");
	}
}
