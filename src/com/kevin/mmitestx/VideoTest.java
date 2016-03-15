package com.kevin.mmitestx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class VideoTest extends Activity implements SurfaceHolder.Callback
{
	  private static final String TAG = "VideoTest";
	  private boolean DBG = true;
	  private boolean bIsPaused = false;
	  private boolean bIsReleased = false;
	  private Button button_test_good;
	  private Button button_test_ng;
	  File f = new File("/mnt/sdcard/test.3gp");
	  private boolean isPlaying = false;
	  private MediaPlayer mMediaPlayer;
	  private ImageButton mPause;
	  private ImageButton mPlay;
	  private ImageButton mReset;
	  private ImageButton mStop;
	  private SurfaceHolder mSurfaceHolder;
	  private SurfaceView mSurfaceView;
	  private String strVideoPath = "/mnt/sdcard/test.3gp";
	  
	  private boolean checkSDCard()
	  {
	    if (Environment.getExternalStorageState().equals("mounted"))
	    {
	    	return true;
	    }
	    else
	    	return false;
	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		if(!checkSDCard()){
			mMakeTextToast("Please insert a SD card!", true);
		}
		findView();
		setListeners();
	}
		
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		 if (!f.exists())
		    {
		      try
		      {
		        copyTestVideoToSD("/mnt/sdcard/test.3gp");
		        Log.d(TAG, "Writing Done!");
		      }
		      catch (IOException e)
		      {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }
		    }
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(isPlaying)
		{
			mMediaPlayer.stop();
			mMediaPlayer.release();
			if(DBG)Log.d(TAG, "MediaPlayer Released!");
		}
		super.onPause();
	}

	private void findView()
	{
		button_test_ng = (Button)findViewById(R.id.button1);
		button_test_good = (Button)findViewById(R.id.button2);
		mPause = (ImageButton)findViewById(R.id.pause);
		mPlay = (ImageButton)findViewById(R.id.play);
		mReset = (ImageButton)findViewById(R.id.reset);
		mStop = (ImageButton)findViewById(R.id.stop);
		mSurfaceView = (SurfaceView)findViewById(R.id.mSurfaceView1);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		if(!checkSDCard()){
			mPlay.setEnabled(false);
			mPause.setEnabled(false);
			mReset.setEnabled(false);
			mStop.setEnabled(false);
		}
	}
	
	private void setListeners() {
		button_test_ng.setOnClickListener(pressTestNG);
		button_test_good.setOnClickListener(pressTestGood);
		mPlay.setOnClickListener(pressPlay);
		mPause.setOnClickListener(pressPause);
		mReset.setOnClickListener(pressReset);
		mStop.setOnClickListener(pressStop);
	}
	
	private ImageButton.OnClickListener pressPlay = new ImageButton.OnClickListener()
	{

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(checkSDCard())
	        {
	          playVideo(strVideoPath);
	          isPlaying = true;
	        }
			if(isPlaying)
			{
				mPlay.setEnabled(false);
			}

		}
	};
	
	private void playVideo(String strPath) 
	{
			// TODO Auto-generated method stub
			mMediaPlayer = new MediaPlayer();
		    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    mMediaPlayer.setDisplay(mSurfaceHolder);
		    try {
				mMediaPlayer.setDataSource(strVideoPath);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    mMediaPlayer.start();
		    bIsReleased = false;
	}
		
	
	private ImageButton.OnClickListener pressPause = new ImageButton.OnClickListener()
	{
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(checkSDCard())
	        {
	          if (mMediaPlayer != null)
	          {
	            if(bIsReleased == false)
	            {
	              if(bIsPaused==false)
	              {
	                mMediaPlayer.pause();
	                bIsPaused = true;
	              }
	              else if(bIsPaused==true)
	              {
	                mMediaPlayer.start();
	                bIsPaused = false;
	              }
	            }
	          }
	        }		
		}	
	};
	
	private ImageButton.OnClickListener pressReset = new ImageButton.OnClickListener()
	{

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(checkSDCard())
	        {
	          if(bIsReleased == false)
	          {
	            if (mMediaPlayer != null)
	            { 
	              mMediaPlayer.seekTo(0); 
	            }
	          }
	        }
		}
		
	};
	
	private ImageButton.OnClickListener pressStop = new ImageButton.OnClickListener()
	{
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(checkSDCard())
	        {
	          if (mMediaPlayer != null)
	          {
	            if(bIsReleased==false)
	            {
	              mMediaPlayer.stop();
	              mMediaPlayer.release();
	              bIsReleased = true;
	              isPlaying = false;
	              mPlay.setEnabled(true);
	            }          
	          }
	        }
		}	
	};
	
	private Button.OnClickListener pressTestNG = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_NOT_GOOD);
			VideoTest.this.finish();
		}
	};
	
	private Button.OnClickListener pressTestGood = new Button.OnClickListener() {
		public void onClick(View v) {
            setResult(MMITestX.TEST_GOOD);
			VideoTest.this.finish();
		}
	};
	
	public void mMakeTextToast(String str, boolean isLong)
	{
	    if(isLong==true)
	    {
	      Toast.makeText(VideoTest.this, str, Toast.LENGTH_LONG).show();
	    }
	    else
	    {
	      Toast.makeText(VideoTest.this, str, Toast.LENGTH_SHORT).show();
	    }
	}
	
	private void copyTestVideoToSD(String strOutFileName) throws IOException 
	{  
	      InputStream myInput;  
	      OutputStream myOutput = new FileOutputStream(strOutFileName);  
	      myInput = getResources().openRawResource(R.raw.test);
	      byte[] buffer = new byte[1024];  
	      int length = myInput.read(buffer);
	      while(length > 0)
	      {
	          myOutput.write(buffer, 0, length); 
	          length = myInput.read(buffer);
	      }    
	      myOutput.flush();  
	      myInput.close();  
	      myOutput.close();        
	 }

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
}
