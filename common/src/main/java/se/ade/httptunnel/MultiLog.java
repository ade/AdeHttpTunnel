package se.ade.httptunnel;

/**
 * Utility logging class with configurable output level.
 * Created: adrnil Date: 2012-05-09 09:37
 */
public class MultiLog {
    public static final int LOG_LEVEL_0_DISABLE = 0;
    public static final int LOG_LEVEL_1_ASSERT = 1;
    public static final int LOG_LEVEL_2_ERROR = 2;
    public static final int LOG_LEVEL_3_WARN = 3;
    public static final int LOG_LEVEL_4_INFO = 4;
    public static final int LOG_LEVEL_5_DEBUG = 5;
    public static final int LOG_LEVEL_6_VERBOSE = 6;
	public static final int LOG_LEVEL_7_NETWORK = 7;

    private static int logLevel = LOG_LEVEL_7_NETWORK;

    public static String prefix = "MULTILOG: ";

    private static AbstractLogger logger = new SystemLogger();

    public static void setLogger(AbstractLogger logger) {
        MultiLog.logger = logger;
    }

    public static int getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(int mLogLevel) {
        MultiLog.logLevel = mLogLevel;
    }

    public static void e(String s, String msg) {
        if(logLevel >= LOG_LEVEL_2_ERROR)
            logger.e(prefix + s, msg);
    }
    public static void w(String s, String msg) {
        if(logLevel >= LOG_LEVEL_3_WARN)
            logger.w(prefix + s, msg);
    }
    public static void i(String s, String msg) {
        if(logLevel >= LOG_LEVEL_4_INFO)
            logger.i(prefix + s, msg);
    }
    public static void d(String s, String msg) {
        if(logLevel >= LOG_LEVEL_5_DEBUG)
            logger.d(prefix + s, msg);
    }
    public static void v(String s, String msg) {
        if(logLevel >= LOG_LEVEL_6_VERBOSE)
            logger.v(prefix + s, msg);
    }
    public static void network(String s, String msg) {
        if(logLevel >= LOG_LEVEL_7_NETWORK)
            logger.v(prefix + s, msg);
    }

    public static void v(Object o, String msg) {
        v(o.getClass().getSimpleName(), msg);
    }
    public static void d(Object o, String msg) {
        d(o.getClass().getSimpleName(), msg);
    }
    public static void e(Object o, String msg) {
        e(o.getClass().getSimpleName(), msg);
    }
    public static void i(Object o, String msg) {
        i(o.getClass().getSimpleName(), msg);
    }
    public static void w(Object o, String msg) {
        w(o.getClass().getSimpleName(), msg);
    }
    public static void network(Object o, String msg) {
        network(o.getClass().getSimpleName(), msg);
    }

    public static void v(Class o, String msg) {
        if(logLevel >= LOG_LEVEL_6_VERBOSE)
            logger.v(prefix + o.getSimpleName(), msg);
    }
    public static void d(Class o, String msg) {
        if(logLevel >= LOG_LEVEL_5_DEBUG)
            logger.d(prefix + o.getSimpleName(), msg);
    }
    public static void i(Class o, String msg) {
        if(logLevel >= LOG_LEVEL_4_INFO)
            logger.i(prefix + o.getSimpleName(), msg);
    }
    public static void w(Class o, String msg) {
        if(logLevel >= LOG_LEVEL_3_WARN)
            logger.w(prefix + o.getSimpleName(), msg);
    }
    public static void e(Class o, String msg) {
        if(logLevel >= LOG_LEVEL_2_ERROR)
            logger.e(prefix + o.getSimpleName(), msg);
    }

	/** Log network communication contents */
	public static void network(Class o, String msg) {
		if(logLevel >= LOG_LEVEL_7_NETWORK)
            logger.v(prefix + o.getSimpleName(), msg);
	}


    public static void epic_verbose(Object o, String msg) {
        v(o, "-----------------------------------------------------------------------");
        v(o, msg);
        v(o, "-----------------------------------------------------------------------");
    }
    public static void epic_verbose(Class c, String msg) {
        v(c, "-----------------------------------------------------------------------");
        v(c, msg);
        v(c, "-----------------------------------------------------------------------");
    }
}
