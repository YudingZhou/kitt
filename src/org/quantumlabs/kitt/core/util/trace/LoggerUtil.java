package org.quantumlabs.kitt.core.util.trace;

import org.quantumlabs.kitt.core.util.SackConstant;

public class LoggerUtil
{

    public enum TEXT
    {
        CONSTRUCTING;
    }

    /**
     * Format strings like following rule:
     * 
     * <pre>
     *   format(cause,submodel,message)
     *   
     *   DATE : M-D-Y h:m:s      PID : XXX      CATEGORY : XXX      MODEL : $submodel
     *   TEXT : $message
     *   EXCEPTION : $cause
     * 
     * </pre>
     * */
    public static String format( Throwable fault, LogLevel level, String model, String... messages )
    {
        StringBuilder stringBuilder = new StringBuilder();
        String exceptionStack = null;
        if( fault != null )
        {
            exceptionStack = getExceptionStackTraceElementAsString( fault );
        }

        for( String message : messages )
        {
            stringBuilder.append( "\n" );
            stringBuilder.append( message );
        }

        return String.format( SackConstant.LOG_TEMPLATE_WITHEXCEPTION, "//TODO", "//TODO", level.toString(), model,
            stringBuilder.toString(), exceptionStack );
    }

    /**
     * Format strings like following rule:
     * 
     * <pre>
     *   format(submodel,message)
     *   
     *   DATE : M-D-Y h:m:s      PID : XXX      CATEGORY : XXX      MODEL : $submodel
     *   TEXT : $message
     * 
     * </pre>
     * */
    public static String format( LogLevel level, String model, String... messages )
    {

        StringBuilder stringBuilder = new StringBuilder();
        for( String message : messages )
        {
            stringBuilder.append( "\n" );
            stringBuilder.append( message );
        }
        return String.format( SackConstant.LOG_TEMPLATE_WITHOUTEXCEPTION, "//TODO", "//TODO", level.toString(), model,
            stringBuilder.toString() );
    }

    public static String format( Throwable cause )
    {
        if( null == cause )
        {
            return Logger.NULL;
        }
        return format( cause, LogLevel.ERROR, "", "" );
    }

    private static String getExceptionStackTraceElementAsString( Throwable cause )
    {
        StringBuilder builder = new StringBuilder();
        for( StackTraceElement stack : cause.getStackTrace() )
        {
            builder.append( "\n" );
            builder.append( stack.toString() );
        }
        return builder.toString();
    }
}
