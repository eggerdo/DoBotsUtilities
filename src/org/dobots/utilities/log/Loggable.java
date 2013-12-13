package org.dobots.utilities.log;

public class Loggable {

	// Should only be true when debugging
	protected boolean m_bDebug = true;
	
	// receives debug events
	protected ILogListener m_oLogListener = null;
	
	protected static Loggable INSTANCE = null;
	
	protected static Loggable getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Loggable();
		}
		return INSTANCE;
	}

	public void setDebug(boolean i_bDebug) {
		m_bDebug = i_bDebug;
	}

	// wrapper for info logs -------------------------------------------------------------------
	protected void info(String tag, String message) {
		Logger.getInstance().onTrace(LogTypes.tt_Info, tag, message);
	}
	
	protected void info(String tag, String fmt, Object ... args) {
		Logger.getInstance().onTrace(LogTypes.tt_Info, tag, String.format(fmt, args));
	}

	protected void info(String tag, String message, Throwable obj) {
		Logger.getInstance().onTrace(LogTypes.tt_Info, tag, message, obj);
	}

	// wrapper for debug logs ------------------------------------------------------------------
	protected void debug(String tag, String message) {
		if (m_bDebug) {
			Logger.getInstance().onTrace(LogTypes.tt_Debug, tag, message);
		}
	}

	protected void debug(String tag, String fmt, Object ... args) {
		if (m_bDebug) {
			Logger.getInstance().onTrace(LogTypes.tt_Debug, tag, String.format(fmt, args));
		}
	}

	protected void debug(String tag, String message, Throwable obj) {
		if (m_bDebug) {
			Logger.getInstance().onTrace(LogTypes.tt_Debug, tag, message, obj);
		}
	}

	// wrapper for warning logs ----------------------------------------------------------------
	protected void warn(String tag, String message) {
		Logger.getInstance().onTrace(LogTypes.tt_Warning, tag, message);
	}

	protected void warn(String tag, String fmt, Object ... args) {
		Logger.getInstance().onTrace(LogTypes.tt_Warning, tag, String.format(fmt, args));
	}
	
	protected void warn(String tag, String message, Throwable obj) {
		Logger.getInstance().onTrace(LogTypes.tt_Warning, tag, message, obj);
	}

	// wrapper for error logs  -----------------------------------------------------------------
	protected void error(String tag, String message) {
		Logger.getInstance().onTrace(LogTypes.tt_Error, tag, message);
	}

	protected void error(String tag, String fmt, Object ... args) {
		Logger.getInstance().onTrace(LogTypes.tt_Error, tag, String.format(fmt, args));
	}
	
	protected void error(String tag, String message, Throwable obj) {
		Logger.getInstance().onTrace(LogTypes.tt_Error, tag, message, obj);
	}

}
