package org.dobots.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	protected BaseApplication mApplication = null;
	private IActivityResultListener m_oListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getApplicationContext() instanceof BaseApplication) {
			mApplication = (BaseApplication)getApplicationContext();
		}
	}
	
	public void startActivityForResult(Intent intent, int requestCode, IActivityResultListener listener) {
		m_oListener = listener;
		super.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (m_oListener != null) {
			m_oListener.onActivityResult(requestCode, resultCode, data);
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
}
