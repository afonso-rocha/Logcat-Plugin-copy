package org.apache.cordova.logcat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.onesignal.OneSignal;

import java.io.File;
import java.io.IOException;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;

public class LogCat extends CordovaPlugin { //LogCatPlugin

    private static final String TAG = "LogCatPlugin";

    private static final String ADD_SUBSCRIPTION_OBSERVER = "addSubscriptionObserver";

    public boolean init(JSONArray data) {
        try {
            String appId = data.getString(0);

            OneSignal.sdkType = "cordova";

            OneSignal.setAppId(appId);
            OneSignal.initWithContext(this.cordova.getActivity());

            return true;
        } catch (JSONException e) {
            Log.e(TAG, "execute: Got JSON Exception " + e.getMessage());
            return false;
        }
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        switch(action) {
                
            case ADD_SUBSCRIPTION_OBSERVER:
                result = OneSignalObserverController.addSubscriptionObserver(callbackContext);
                break;

            default:
                break;

        }
        
        if (action.equals("sendLogs")) {
            if(!foregroundServiceRunning()) {
                Activity activity = cordova.getActivity();
                Intent serviceIntent = new Intent(activity, MyForegroundService.class);
                activity.getApplicationContext().startForegroundService(serviceIntent);
            }
            return true;
        } else if (action.equals("uploadPlugin")) {
                Activity activity = cordova.getActivity();
                new LogcatHistoryFile().generateZipFile(activity, args.getString(0));
                return true;
        } else if (action.equals("registerDevice")) {
                Activity activityCordova = cordova.getActivity();
                OneSignal.initWithContext(activityCordova);
                OneSignal.setAppId(args.getString(0));
                return true;
        } else {
            return false;
        }
    }        //create a new Intent to send the logs
             //Intent serviceIntent = new Intent(cordova.getActivity(), MyForegroundService.class);
             //serviceIntent.setaction(""); //string of the action that want to execute

    //Checks if the foreground service is running
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
