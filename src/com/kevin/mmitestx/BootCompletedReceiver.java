package com.kevin.mmitestx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootCompletedReceiver extends BroadcastReceiver
{
  private Boolean isAutoLaunched;
  private SharedPreferences sp;
  
  public void onReceive(Context context, Intent intent)
  {
    sp = context.getSharedPreferences("com.kevin.mmitestx_preferences", 1);
    isAutoLaunched = sp.getBoolean("Autolaunch", false);
    if (this.isAutoLaunched.booleanValue())
    {
      intent = new Intent(context, MMITestX.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }
}