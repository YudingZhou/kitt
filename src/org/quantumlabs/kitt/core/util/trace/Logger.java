package org.quantumlabs.kitt.core.util.trace;

import java.util.concurrent.ConcurrentHashMap;

import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;

public class Logger
{
    //TODO : to be continue, for log thread handling.
    private static boolean systemEnable = false;
    private static boolean traceEnable = false;
    private static boolean debugEnable = false;
    private static boolean waringEnable = false;
    private static boolean errorEnable = false;
    static String NULL = "";
    //TODO : Ilooger has no implementation
    private static ILogger logger;

    private static ConcurrentHashMap<Class<?>, Logger> loggerManager;

    static
    {
        loggerManager = new ConcurrentHashMap<Class<?>, Logger>();
    }

    public static void setLogger( ILogger iLogger )
    {
        logger = iLogger;
    }

    public static void initialize()
    {
        int level = KITTParameter.getLogLevel();
        if( level == LogLevel.ERROR.id() )
        {
            Logger.setLevel( LogLevel.ERROR );
        }
        else if( level == LogLevel.WARNING.id() )
        {
            Logger.setLevel( LogLevel.WARNING );
        }
        else if( level == LogLevel.DEBUG.id() )
        {
            Logger.setLevel( LogLevel.DEBUG );
        }
        else if( level == LogLevel.TRACE.id() )
        {
            Logger.setLevel( LogLevel.TRACE );
        }
        else if( level == LogLevel.SYSTEM.id() )
        {
            Logger.setLevel( LogLevel.SYSTEM );
        }
        else
        {
            Logger.setLevel(LogLevel.NONE);
        }
    }

    public static void setLevel( LogLevel level )
    {
        switch( level )
        {
            case SYSTEM:
                systemEnable = true;
            case TRACE:
                traceEnable = true;
            case DEBUG:
                debugEnable = true;
            case WARNING:
                waringEnable = true;
            case ERROR:
                errorEnable = true;
                break;
            default:
            	systemEnable = traceEnable = debugEnable = waringEnable = errorEnable = false;
        }
    }

    public static void disableLogger()
    {
        systemEnable = traceEnable = debugEnable = waringEnable = errorEnable = false;
    }

    /**
     * System level means some <strong>system related actions is performing</strong>, e.g. native
     * configuration loading, plugin starting up etc.
     * */
    public static boolean isSystemEnable()
    {
        return systemEnable;
    }

    /**
     * Trace level is the most usual way to log something.
     * */
    public static boolean isTraceEnable()
    {
        return traceEnable;
    }

    /**
     * Debug level means current execution is initializing, configuration loading, component status
     * changing etc.. Somewhat for tracing key actions.
     * */
    public static boolean isDebugEnable()
    {
        return debugEnable;
    }

    /**
     * Warning level means current execution is somehow unpredicted which <strong>may cause some
     * unexpected behaviors</strong> or some faults which <strong>can be recovered</strong>.
     * */
    public static boolean isWarningEnable()
    {
        return waringEnable;
    }

    /**
     * Error level means a severity fault happened, you <strong>can't recover</strong> it and
     * <strong>must notify system that by logging it at least</strong>. The common way for using it
     * is to log an exception.
     * */
    public static boolean isErrorEnable()
    {
        return errorEnable;
    }

    public static void logTrace( String message )
    {
        logTrace( "", message );
    }

    public static void logTrace( String model, String... messages )
    {
        if( isTraceEnable() )
        {
            logger.log( LoggerUtil.format( LogLevel.TRACE, model, messages ) );
        }
    }

    public static void logSystem( String message )
    {
        logSystem( "", message );
    }

    public static void logSystem( String model, String... messages )
    {
        if( isSystemEnable() )
        {
            logger.log( LoggerUtil.format( LogLevel.SYSTEM, model, messages ) );
        }
    }

    public static void logDebug( String message )
    {
        logDebug( "", message );
    }

    public static void logDebug( String model, Throwable fault, String... messages )
    {
        if( isDebugEnable() )
        {
            logger.log( LoggerUtil.format( fault, LogLevel.DEBUG, model, messages ) );
        }
    }

    public static void logDebug( String model, String... messages )
    {
        if( isDebugEnable() )
        {
            logger.log( LoggerUtil.format( LogLevel.DEBUG, model, messages ) );
        }
    }

    public static void logWarning( String message )
    {
        logWarning( "", message );
    }

    public static void logWarning( String subModel, String... messages )
    {
        if( isWarningEnable() )
        {
            logger.log( LoggerUtil.format( LogLevel.WARNING, subModel, messages ) );
        }
    }

    public static void logError( String subModel, String message )
    {
        logError( subModel, null, message );
    }

    public static void logError( String message, Throwable cause )
    {
        logError( NULL, cause, message );
    }

    public static void logError( String message )
    {
        logError( NULL, message );
    }

    public static void logError( Throwable fault )
    {
        logError( "", fault, "" );
    }

    public static void logError( String subModel, Throwable cause, String... messages )
    {
        if( isTraceEnable() )
        {
            logger.log( LoggerUtil.format( cause, LogLevel.ERROR, subModel, messages ) );
        }
    }

    public static Logger getLogger( Class<?> clazz )
    {
        Logger logger = null;
        if( loggerManager.containsKey( clazz ) )
        {
            logger = new Logger();
            loggerManager.putIfAbsent( clazz, logger );
        }
        else
        {
            logger = loggerManager.get( clazz );
        }
        return logger;
    }

    public static Logger getLogger( Object o )
    {
        return getLogger( o.getClass() );
    }

	public static void debug(String c) {
		System.out.println(c);
	}
}
