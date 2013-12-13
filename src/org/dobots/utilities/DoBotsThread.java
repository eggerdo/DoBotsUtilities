package org.dobots.utilities;

import android.util.Log;

public abstract class DoBotsThread extends Thread {
	
	private static final String TAG = "DoBotsThread";

	protected volatile boolean m_bStopped = false;
	protected volatile boolean m_bPaused = false;
	
	public DoBotsThread(String threadName) {
		super(threadName);
	}
	
	public void stopThread() {
		m_bStopped = true;
		interrupt();
		shutDown();
	}
	
	public void destroy() {
		stopThread();
	}
	
	public void pauseThread() {
		m_bPaused = true;
	}
	
	public void startThread() {
		m_bPaused = false;
	}

	@Override
	public void run() {

//		while (!m_bStopped && !Thread.interrupted()) {
		while (!m_bStopped) {
			if (!m_bPaused) {
				execute();
			}
		
			try {
				sleep(1);
			} catch (InterruptedException e) {
				if (!m_bStopped) {
					e.printStackTrace();
				}
			}			
		}
		
		Log.d(TAG, "thread exit");
	}
	
	public abstract void shutDown();
	
	protected abstract void execute();
	
}
