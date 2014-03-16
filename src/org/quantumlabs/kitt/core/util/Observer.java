package org.quantumlabs.kitt.core.util;

public interface Observer<T>
{
    void fireEvent( Observable<T> source, T event );
}
