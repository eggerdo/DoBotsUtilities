package org.dobots.utilities;

import android.app.Application;

public class BaseApplication extends Application {

	private BaseActivity mCurrentActivity = null;
	
	public BaseActivity getCurrentActivity() {
		return mCurrentActivity;
	}
	
	public void setCurrentActivity(BaseActivity activity) {
		mCurrentActivity = activity;
	}
	
}
