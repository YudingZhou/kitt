package org.quantumlabs.kitt.core.util;

import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

/**
 * BasicSemanticValidator is essential semantic validator for validating basic element. Basicly,
 * exception raised while error occurs, it's preferred to designate as a singleton for holding in a
 * manager. It also can be extended easily for further. validating functionalities.
 * */
public class BasicSemanticValidator
{
    @SuppressWarnings( "serial" )
    static class TracableNullPointException
        extends StackTracableException
    {
        TracableNullPointException(String message)
        {
            super( message );
        }
    }

    @SuppressWarnings( "serial" )
    static class TracableIllegalArgumentException
        extends StackTracableException
    {
        TracableIllegalArgumentException(String message)
        {
            super( message );
        }
    }

    public static <T> T validateNotNull( T candidate )
    {
        if( candidate == null )
        {
            throw new TracableNullPointException( candidate + " should not be NULL." );
        }
        return candidate;
    }

    public static void fail( String message )
    {
        throw new TracableIllegalArgumentException( message );
    }

    public static <T> T validateInstanceOf( Class<T> clazz, Object o )
    {
        if( !clazz.isInstance( o ) )
        {
            throw new TracableIllegalArgumentException( o + " is not a " + clazz.getName() );
        }
        return clazz.cast( o );
    }

    public static <T> boolean isInstanceOf( Class<T> clazz, Object o )
    {
        return clazz.isInstance( o );
    }

}
