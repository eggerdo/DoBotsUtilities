package org.dobots.utilities.log;

import android.util.Log;

public class AndroidLogger implements ILogListener {

	@Override
	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage) {
		switch(i_eType) {
		case tt_Debug:
			Log.d(i_strTag, i_strMessage);
			break;
		case tt_Error:
			Log.e(i_strTag, i_strMessage);
			break;
		case tt_Info:
			Log.i(i_strTag, i_strMessage);
			break;
		case tt_Warning:
			Log.w(i_strTag, i_strMessage);
			break;
		}
	}

	@Override
	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage,
			Throwable i_oObj) {
		switch(i_eType) {
		case tt_Debug:
			Log.d(i_strTag, i_strMessage, i_oObj);
			break;
		case tt_Error:
			Log.e(i_strTag, i_strMessage, i_oObj);
			break;
		case tt_Info:
			Log.i(i_strTag, i_strMessage, i_oObj);
			break;
		case tt_Warning:
			Log.w(i_strTag, i_strMessage, i_oObj);
			break;
		}
	}

}
