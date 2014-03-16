package org.quantumlabs.kitt.core.util.trace;

public enum LogLevel
{
    SYSTEM( 5 ), TRACE( 4 ), DEBUG( 3 ), WARNING( 2 ), ERROR( 1 );

    private LogLevel(int id)
    {
        this.id = id;
    }

    int id;

    public int id()
    {
        return id;
    }
}
