/*
 Copyright 2014 Modern Alchemists OG

 Licensed under MIT.

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

package com.scala.cordova.plugin.cache;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.app.AlarmManager;

@TargetApi(19)
public class Cache extends CordovaPlugin {

	private static final String LOG_TAG = "Cache";
	private CallbackContext callbackContext;

	/**
	 * Constructor.
	 */
	public Cache() {

	}

	@Override
	public boolean execute (String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		final Activity activity = cordova.getActivity();

		if( action.equals("clear") ) {
			Log.v(LOG_TAG, "Cordova Android Cache.clear() called.");
			this.callbackContext = callbackContext;
			
			final Cache self = this;

			activity.runOnUiThread( new Runnable() {
				public void run() {
					try {
						// clear the cache
						self.webView.clearCache(true);
						
						// send success result to cordova
						PluginResult result = new PluginResult(PluginResult.Status.OK);
						result.setKeepCallback(false); 
						self.callbackContext.sendPluginResult(result);
					} catch( Exception e ) {
						String msg = "Error while clearing webview cache.";
						Log.e(LOG_TAG, msg);
						
						// return error answer to cordova
						PluginResult result = new PluginResult(PluginResult.Status.ERROR, msg);
						result.setKeepCallback(false); 
						self.callbackContext.sendPluginResult(result);
					}
				}

			});
			return true;
		}

		if( action.equals("clearAllData") ) {
			Log.v(LOG_TAG, "Cordova Android Cache.clear() called.");
			this.callbackContext = callbackContext;

			final String appDataDir = activity.getFilesDir().getParent();
			final File defaultUserDataDir = new File(appDataDir + File.separator + "app_xwalkcore" + File.separator + "Default");
			
			final Cache self = this;

			activity.runOnUiThread(new Runnable() {

				public void run() {
					try {
						// clear the cache
						self.webView.clearCache(true);
						
						// clear the all data
						if (defaultUserDataDir.exists()) {
							delete(defaultUserDataDir);
							
							// schedule a restart of the app
							Context context = activity.getApplicationContext();
							Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
							PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
							AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
							alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
							System.exit(0);
						}

						
						// send success result to cordova
						PluginResult result = new PluginResult(PluginResult.Status.OK);
						result.setKeepCallback(false); 
						self.callbackContext.sendPluginResult(result);
					} catch(Exception e) {
						String msg = "Error while clearing all data.";
						Log.e(LOG_TAG, msg);
						
						// return error answer to cordova
						PluginResult result = new PluginResult(PluginResult.Status.ERROR, msg);
						result.setKeepCallback(false); 
						self.callbackContext.sendPluginResult(result);
					}
				}

			});
			return true;
		}

		return false;

	}

	public static void delete(File file) throws IOException {

		if(file.isDirectory()){
			//directory is empty, then delete it
			if(file.list().length == 0) {
				file.delete();
			} else {

				//list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					//construct the file structure
					File fileDelete = new File(file, temp);

					//recursive delete
					delete(fileDelete);
				}

				//check the directory again, if empty then delete it
				if(file.list().length == 0){
					file.delete();
				}
			}
		} else {
			//if file, then delete it
			file.delete();
		}
	}
}
