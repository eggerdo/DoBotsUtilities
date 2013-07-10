package org.dobots.utilities;

import android.content.Intent;

public interface IActivityResultListener {
	
	public void onActivityResult(int requestCode, int resultCode, Intent data);

}
