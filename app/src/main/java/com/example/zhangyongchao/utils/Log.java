package com.example.zhangyongchao.utils;

/**
 * Created by zhangyongchao on 2015/12/16.
 */
public final class Log {
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean printLogs = true; //

    private Log(){

    }
    public static void setPrintLogs(boolean printLogs){
        Log.printLogs = printLogs;
    }

    public static void d(String tag, String message){
        log(android.util.Log.DEBUG, tag, message, null);
    }
    public static void i(String tag, String message){
        log(android.util.Log.INFO, tag, message, null);
    }
    public static void w(String tag, String message){
        log(android.util.Log.WARN, tag, message, null);
    }
    public static void e(String tag, String message){
        log(android.util.Log.ERROR, tag, message, null);
    }
    public static void e(String tag, String message, Throwable ex){
        log(android.util.Log.ERROR, tag, message, ex);
    }

    private static void log(int priority, String tag, String message, Throwable ex){
        if ( ! printLogs) return;
        String log;
        if (ex == null){
            log = message;
        } else {
            String logMessage = message == null ? ex.getMessage() : message;
            String logBody = android.util.Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        android.util.Log.println(priority, tag, log);
    }
    /**
     * println_native(LOG_ID_MAIN, ERROR, tag, msg + '\n' + getStackTraceString(tr));
     * println_native(LOG_ID_MAIN, ERROR, tag, msg);
     */
}
