package org.dobots.utilities.log;

public class Logger implements ILogListener {
	
	public static Logger INSTANCE;
	
	private ILogListener mListener;
	
	public static Logger getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Logger();
		}
		
		return INSTANCE;
	}

	public static void setLogger(ILogListener logger) {
		getInstance().mListener = logger;
	}

	@Override
	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage) {
		if (mListener != null) {
			mListener.onTrace(i_eType, i_strTag, i_strMessage);
		}
	}

	@Override
	public void onTrace(LogTypes i_eType, String i_strTag, String i_strMessage,
			Throwable i_oObj) {
		if (mListener != null) {
			mListener.onTrace(i_eType, i_strTag, i_strMessage, i_oObj);
		}
	}
}
