package org.dobots.utilities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ThreadHandler extends Thread {
	
	private Handler mHandler = null;
	private Looper mLooper = null;

	public ThreadHandler(String name) {
		super(name);
		start();
	}
	
	public Handler getHandler() {
		// stall until the handler is available
		if (!interrupted()) {
			while (mHandler == null) {
				Utils.waitSomeTime(1);
			}
			return mHandler;
		} else {
			return null;
		}
	}
	
	public void run() {
		
		Looper.prepare();
		mLooper = Looper.myLooper();
		mHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				handleIncomingMessage(msg);
			}
				
		};
		Looper.loop();
		
	};
	
	public void destroy() {
		interrupt();
		mLooper.quit();
	}
	
	protected void handleIncomingMessage(Message msg) {
		
	}

}
