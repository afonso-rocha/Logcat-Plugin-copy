package org.apache.cordova.logcat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.Environment;
import android.app.Activity;
import org.apache.cordova.logcat.MyForegroundService;
import android.util.Log;

public class LogCat extends CordovaPlugin { //LogCatPlugin 
	
	private static final String TAG = "LogCatPlugin";
    
	/*
    protected void pluginInitialize() {
	    super.pluginInitialize();
    }*/

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) 
	      throws JSONException {
	    if (action.equals("sendLogs")) {
		     Log.i(TAG, "Sendlogs received");
            if(!foregroundServiceRunning()) {
		Log.i(TAG, "NO foreground service running");
		Activity activity = cordova.getActivity();
                Intent serviceIntent = new Intent(activity, MyForegroundService.class);
                activity.getApplicationContext().startForegroundService(serviceIntent);
		Log.i(TAG, "Foreground service LogCat Plugin running");
            }  
		    //create a new Intent to send the logs
		    //Intent serviceIntent = new Intent(cordova.getActivity(), MyForegroundService.class);
		    //serviceIntent.setaction(""); //string of the action that want to execute
             return true;
	    }else{        
	    return false;
	    }
      }	
    
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if(!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this, MyForegroundService.class);
            startForegroundService(serviceIntent);
        }
    }*/

    public boolean foregroundServiceRunning() {
    	Context context = cordova.getActivity().getApplicationContext();
    	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
        	if (MyForegroundService.class.getName().equals(service.service.getClassName())) {
            		return true;
        	}
    	}
    	return false;
     }

}
