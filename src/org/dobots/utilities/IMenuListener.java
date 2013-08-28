package org.dobots.utilities;

import android.view.Menu;
import android.view.MenuItem;

public interface IMenuListener {
	
	public boolean onCreateOptionsMenu(Menu menu);
    public boolean onPrepareOptionsMenu(Menu menu);
	public boolean onOptionsItemSelected(MenuItem item);

}
