package org.dobots.utilities.log;



public class Loggable {

	// Should only be true when debugging
	protected boolean m_bDebug = true;
	
	// receives debug events
	protected ILogListener m_oLogListener = null;
	
	protected static Loggable INSTANCE = null;
	
	public static Loggable getInstance() {
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
	
	
	private static Entry[] list = new Entry[10];
	private static class Entry {
		public long start;
		public long max = 0;
		public long min = Long.MAX_VALUE;
		public double avg = 0;
		public int count = 0;
	}
	
	public static void startTimeMeasurement(int id) {
		if (list[id] == null) {
			list[id] = new Entry();
		}
		list[id].start = System.currentTimeMillis();
	}
	
	public static void stopTimeMeasurement(int id) {
		long end = System.currentTimeMillis();
		Entry entry = list[id];
		long start = entry.start;
		long d = end - start;
		entry.max = Math.max(entry.max, d);
		entry.min = Math.min(entry.min, d);
		entry.avg = (double) (entry.avg * entry.count + d) / (entry.count + 1);
		++entry.count;
		getInstance().error("TimeClock", "time clock [%d]: %d ms, min: %d, max: %d, avg: %.0f", id, d, entry.min, entry.max, entry.avg);
	}

}
