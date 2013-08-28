package org.dobots.utilities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	
	protected BaseApplication mApplication = null;
	private IActivityResultListener m_oActivityResultListener;
	
	private ArrayList<IMenuListener> m_oMenuListeners;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getApplicationContext() instanceof BaseApplication) {
			mApplication = (BaseApplication)getApplicationContext();
		}
		
		m_oMenuListeners = new ArrayList<IMenuListener>();
	}
	
	public void startActivityForResult(Intent intent, int requestCode, IActivityResultListener listener) {
		m_oActivityResultListener = listener;
		super.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (m_oActivityResultListener != null) {
			m_oActivityResultListener.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mApplication != null) {
			mApplication.setCurrentActivity(this);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		clearActivityReferences();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearActivityReferences();
	}
	
	private void clearActivityReferences() {
		if (mApplication != null) {
			Activity currentActivity = mApplication.getCurrentActivity();
			if (currentActivity != null && currentActivity.equals(this)) {
				mApplication.setCurrentActivity(null);
			}
		}
	}
	
	public void addMenuListener(IMenuListener listener) {
		m_oMenuListeners.add(listener);
	}
	
	public void removeMenuListener(IMenuListener listener) {
		m_oMenuListeners.remove(listener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		if (!m_oMenuListeners.isEmpty()) {
			for (IMenuListener listener : m_oMenuListeners) {
				listener.onCreateOptionsMenu(menu);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		if (!m_oMenuListeners.isEmpty()) {
			for (IMenuListener listener : m_oMenuListeners) {
				listener.onPrepareOptionsMenu(menu);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (!m_oMenuListeners.isEmpty()) {
			for (IMenuListener listener : m_oMenuListeners) {
				listener.onOptionsItemSelected(item);
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
}
