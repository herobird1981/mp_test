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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;

public class ProductionLineModeTest extends Activity 
{
    private static final String TAG = "ProductionLineModeTest";
    private static final boolean DBG = true;
	private List<TestItem> list,list2;
	private boolean bDone;
	private int nIndex;
	private long ElapsedTime;
    private boolean isCommitOK = false;
	SharedPreferences result,sp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		nIndex = 0;
		bDone = false;
		list = readXml(this);
		//if (DBG) Log.d(TAG, "list2 size=" + Integer.toString(list2.size()));
		result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);
		sp = getSharedPreferences("Config_Item", MODE_WORLD_READABLE);
		ElapsedTime = System.currentTimeMillis();
	}
	
	public void onResume() 
	{
		super.onResume();
		//if (DBG) Log.d(TAG, "indexCount="+indexCount());
		list2 = configuedList();
		if (DBG) Log.d(TAG, "list2 size: "+list2.size());
		if(nIndex < list2.size()) 
		{
			TestItem ti = list2.get(nIndex);
			if (DBG) Log.d(TAG, "list"+nIndex+" ="+list.get(nIndex).toString());            
			Intent intent = new Intent();
			try {
					String clsnm = "com.kevin.mmitestx." + ti.getName();
					intent.setClass(this, Class.forName(clsnm));
					startActivityForResult(intent, nIndex);
					if (DBG) Log.d(TAG, "ClassName=" + clsnm + "; nIndex=" + Integer.toString(nIndex));
					bDone = true;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}  			
			//}
			if (DBG) Log.d(TAG, "nIndex = "+nIndex);
			nIndex++;
		} 
		else if (bDone) {
			Intent intent = new Intent();
			try {
				ElapsedTime = System.currentTimeMillis() - ElapsedTime;
				result.edit().putLong("ProdLineModeElapsedTime", ElapsedTime).commit();
				intent.setClass(this, Class.forName("com.kevin.mmitestx.ShowTestReport"));
				startActivity(intent);
				if (DBG) Log.d(TAG, "Show test report");
				bDone = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			this.finish();
		}
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode >= 0 && requestCode < list2.size()) {
        	if(resultCode>0){
				setResult(RESULT_OK);
				if(DBG) Log.d(TAG, "set RESULT_OK");
			}
            TestItem ti = list2.get(requestCode);
            isCommitOK = result.edit().putInt(ti.getName(), resultCode).commit();
            if (DBG) Log.d(TAG, "onActivityResult: isCommitOK=" + isCommitOK + 
                    ", ti.getName()=" + ti.getName() +
                    ", requestCode=" + requestCode +
                    ", resultCode=" + resultCode);
        }
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
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Generate a new List including those devices added in configuration list.
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
}
