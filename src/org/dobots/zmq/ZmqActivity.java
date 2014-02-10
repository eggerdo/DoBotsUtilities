/**
* 456789------------------------------------------------------------------------------------------------------------120
*
* @brief:	Base class for Zmq Activities
* @file: 	ZmqActivity.java
*
* @desc:	Retrieves the ZmqHandler and Setings. Registers for menu and dialog creation.
* 			If this activity is the first (main) activity, then the ZmqHandler is created
* 			with reference to this activity.
*
*
* Copyright (c) 2013 Dominik Egger <dominik@dobots.nl>
*
* @author:		Dominik Egger
* @date:		2.9.2013
* @project:		Robot-Lib
* @company:		Distributed Organisms B.V.
*/
package org.dobots.zmq;

import org.dobots.utilities.BaseActivity;
import org.dobots.utilities.Utils;
import org.dobots.zmq.ZmqSettings.SettingsChangeListener;

import android.os.Bundle;

public abstract class ZmqActivity extends BaseActivity {
	
	protected ZmqHandler mZmqHandler;
	protected ZmqSettings mSettings;
	
	public abstract void onZmqReady();
	
	public abstract void onZmqFailed();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mZmqHandler = ZmqHandler.initialize(this);
		
		mSettings = mZmqHandler.getSettings();
		mSettings.setSettingsChangeListener(new SettingsChangeListener() {
			
			@Override
			public void onChange() {
	        	onZmqReady();
			}

			@Override
			public void onCancel() {
				if (!mSettings.checkSettings()) {
					onZmqFailed();
				}
			}
		});
		
		addMenuListener(mSettings);
		addDialogListener(mSettings);
		
		if (checkSettings()) {
			// don't use runOnUiThread() or onZmqReady will be
			// executed before activity is fully created!!
			Utils.runAsyncUiTask(new Runnable() {
				
				@Override
				public void run() {
					onZmqReady();
				}
			});
		}
	}
	
	protected boolean checkSettings() {
		
		if (!mSettings.checkSettings()) {
			mSettings.showDialog(this);
			return false;
        }
		return true;
	}
	
}

