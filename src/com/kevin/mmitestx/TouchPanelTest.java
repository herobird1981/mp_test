package com.kevin.mmitestx;

import com.kevin.mmitestx.R;

import android.app.Activity;
import android.content.SharedPreferences;
//import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TouchPanelTest extends Activity {
	private Button button_test_ng;
	private Button button_test_good;
	private int pointerCount;
	private int gCenterX = 160;
    private int gCenterY = 240;
    private int gRadius = 100;
    private int Designated_Color = -1;
    private int fontSize;
    View v;
    Paint vPaint;
    RelativeLayout rl;
    SharedPreferences getKey;
   
    private int gTouchX1, gTouchY1, gTouchX2, gTouchY2;
   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        //result = getSharedPreferences("MMITestX_TestResult", MODE_WORLD_WRITEABLE);        
        setContentView(R.layout.touchpanel);
        getKey = getSharedPreferences("Key", MODE_WORLD_WRITEABLE);
        findViews();
        setListeners();
        if (getKey.getString("draw_color", "") == "")
		{
			Designated_Color = Color.WHITE;
		}
		else if(getKey.getString("draw_color", "") == "red")
		{
			Designated_Color = Color.RED;
		}
		else if (getKey.getString("draw_color", "") == "blue")
		{
			Designated_Color = Color.BLUE;
		}
		else if (getKey.getString("draw_color", "") == "yellow")
		{
			Designated_Color = Color.YELLOW;
		}
		else if (getKey.getString("draw_color", "") == "green")
		{
			Designated_Color = Color.GREEN;
		}
		else if (getKey.getString("draw_color", "") == "white")
		{
			Designated_Color = Color.WHITE;
		}
        
        if (getKey.getString("font_size", "") == "small")
        {
        	fontSize = 10;
        }
        else if (getKey.getString("font_size", "") == "medium")
        {
        	fontSize = 20;
        }
        else if (getKey.getString("font_size", "") == "large")
        {
        	fontSize = 35;
        }
    } 
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		v = new View(this) {
        	Paint vPaint = new Paint();
        	//private int i = 0;
        	protected void onDraw(Canvas canvas) {
        		super.onDraw(canvas);
        		Log.d("TouchPanel_Setting2", Designated_Color+"");
        		vPaint.setColor(Designated_Color);		
        		vPaint.setAntiAlias( true );   
        		vPaint.setStyle( Paint.Style.STROKE );
        		canvas.drawCircle( gCenterX, gCenterY, gRadius, vPaint );
        		String SomeInfo;
        		SomeInfo = Integer.toString(pointerCount) + " pts";
        		vPaint.setTextSize(fontSize);
        		canvas.drawText(SomeInfo, 0, SomeInfo.length(), gCenterX, gCenterY, vPaint);
        	}
        };
        rl.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void findViews() {
		button_test_ng = (Button)findViewById(R.id.button2);
		button_test_good = (Button)findViewById(R.id.button3);
		rl = (RelativeLayout)findViewById(R.id.relativeLayout);
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
	}
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("TouchPanelTest", MMITestX.TEST_NOT_GOOD).commit();
            setResult(MMITestX.TEST_NOT_GOOD);
			TouchPanelTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
			//result.edit().putInt("TouchPanelTest", MMITestX.TEST_GOOD).commit();
            setResult(MMITestX.TEST_GOOD);
			TouchPanelTest.this.finish();
		}
	};
	
    public boolean onTouchEvent(MotionEvent event) {
      pointerCount = event.getPointerCount();
     
      switch( event.getAction() )
      {
          case MotionEvent.ACTION_DOWN:
              break;
         
          case MotionEvent.ACTION_MOVE:
             
              if( pointerCount == 1 )
              {
                  gCenterX = (int) event.getX();
                  gCenterY = (int) event.getY();
              }
              else if( pointerCount == 2 )
              {
                  gTouchX1 = (int) event.getX(0); 
                  gTouchY1 = (int) event.getY(0);
                  gTouchX2 = (int) event.getX(1);  
                  gTouchY2 = (int) event.getY(1);
                 
                  gCenterX = (int)((gTouchX1 + gTouchX2)/2);
                  gCenterY = (int)((gTouchY1 + gTouchY2)/2);
                  gRadius = (int)(Math.sqrt( Math.pow(gTouchX2-gTouchX1, 2)
                                           + Math.pow(gTouchY2-gTouchY1, 2)
                                           )/2);
              }
             
              rl.invalidate();
              break;
             
          case MotionEvent.ACTION_UP:    
              break;
      }
     
      // TODO Auto-generated method stub
      return true;
    }
}
