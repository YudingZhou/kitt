package org.quantumlabs.kitt.core.util;

/**
 * Aspectable is an ability for Object which can do something before or after its own operation. The
 * Object should not reference or use the result of aspect.
 * */
public interface Aspectable
{
    /**
     * Add new aspects which will be executed before one operation.
     * */
    void setBefore( Aspect... aspects );

    /**
     * Add new aspects which will be executed after one operation.
     * */
    void setAfter( Aspect... aspects );
}
