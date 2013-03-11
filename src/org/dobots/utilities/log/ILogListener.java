package org.dobots.utilities.log;

public interface ILogListener {

	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage);
	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage, Throwable i_oObj);
	
}
