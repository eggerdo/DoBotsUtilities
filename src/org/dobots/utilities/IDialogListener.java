package org.dobots.utilities;

import android.app.Activity;
import android.app.Dialog;

public interface IDialogListener {
	
	public Dialog onCreateDialog(Activity activity, int id);
	public void onPrepareDialog(Activity activity, int id, Dialog dialog);
	
}
