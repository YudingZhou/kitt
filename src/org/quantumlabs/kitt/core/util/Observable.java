package org.quantumlabs.kitt.core.util;

import java.util.LinkedList;

public abstract class Observable<E>
{
    private final LinkedList<Observer<E>> observers;

    public Observable()
    {
        observers = new LinkedList<Observer<E>>();
    }

    public void attach( Observer<E> observer )
    {
        observers.add( observer );
    }

    public void detach( Observer<E> observer )
    {
        observers.remove( observer );
    }

    public int countObserver()
    {
        return observers.size();
    }

    protected final LinkedList<Observer<E>> getObservers()
    {
        return observers;
    }
}
