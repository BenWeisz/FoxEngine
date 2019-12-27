package fox.main;

public class Logger {
    private static Logger mLogger = null;

    public static final byte ERROR = 0x01;
    public static final byte WARN = 0x02;
    public static final byte DEBUG = 0x04;
    public static final byte LOG = 0x08;
    public static final byte ALL = 0x0F;
    public static final byte NONE = 0x00;

    private static byte mLogLevel = ALL;

    /**
     *  The Logger Singleton.
     */
    private Logger(){

    }

    /**
     * Log an error message.
     * @param message The error message
     */
    public void error(String message){
        if ((mLogLevel & ERROR) > 0)
            System.err.println("ERROR: " + message);
    }

    /**
     * Log a warning message.
     * @param message The warning
     */
    public void warn(String message){
        if ((mLogLevel & WARN) > 0)
            System.err.println("WARNING: " + message);
    }

    /**
     * Log a debugging message.
     * @param message The debugging message.
     */
    public void debug(String message){
        if ((mLogLevel & DEBUG) > 0)
            System.out.println("DEBUG: " + message);
    }

    /**
     * Log a message.
     * @param message The message to log.
     */
    public void log(String message){
        if ((mLogLevel & LOG) > 0)
            System.out.println("LOG: " + message);
    }

    /**
     * Set the log level of the logger.
     * @param logLevel The new log level for the logger
     */
    public void setLogLevel(byte logLevel){
        mLogLevel = logLevel;
    }

    /**
     * Get the Logger singleton.
     * @return The Logger singleton
     */
    public static Logger getLogger(){
        if (mLogger != null)
            return mLogger;
        else return new Logger();
    }
}
