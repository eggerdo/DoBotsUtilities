package org.dobots.utilities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

public abstract class ThreadMessenger extends Thread {
	
	private Messenger mMessenger = null;
	private Looper mLooper = null;
	
	public ThreadMessenger(String name) {
		super(name);
		start();
	}
	
	public Messenger getMessenger() {
		// stall until the messenger is available
		if (!interrupted()) {
			while (mMessenger == null) {
				Utils.waitSomeTime(1);
			}
			return mMessenger;
		} else {
			return null;
		}
	}
	
	public void run() {
		
		Looper.prepare();
		mLooper = Looper.myLooper();
		mMessenger = new Messenger(new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				if (!handleIncomingMessage(msg)) {
					super.handleMessage(msg);
				}
			}
				
		});
		Looper.loop();
		
	};
	
	public void destroy() {
		interrupt();
		mLooper.quit();
	}
	
	protected abstract boolean handleIncomingMessage(Message msg);

}
