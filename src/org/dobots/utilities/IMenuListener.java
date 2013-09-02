package org.dobots.utilities;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public interface IMenuListener {
	
	public boolean onCreateOptionsMenu(Activity activity, Menu menu);
    public boolean onPrepareOptionsMenu(Activity activity, Menu menu);
	public boolean onOptionsItemSelected(Activity activity, MenuItem item);

}
