package org.quantumlabs.kitt.core.util.collection;

import java.util.Map.Entry;

import org.quantumlabs.kitt.core.util.ComparableBuilder;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

public class Pair<L, R>
    implements Entry<L, R>, Comparable<Pair<L, R>>
{
    private L l;
    private R r;
    private ComparableBuilder comparableBuilder;

    public Pair(L l, R r)
    {
        if( l == null || r == null )
        {
            throw new StackTracableException( new NullPointerException(
                SackConstant.MESSAGE_PAIR_INIT_WITH_NULL_EXCEPTION ) );
        }
        this.l = l;
        this.r = r;
        comparableBuilder = new ComparableBuilder();
    }

    public L getLeft()
    {
        return l;
    }

    public R getRight()
    {
        return r;
    }

    @Override
    public L getKey()
    {
        return l;
    }

    @Override
    public R getValue()
    {
        return r;
    }

    @Override
    public R setValue( R value )
    {
        r = value;
        return r;
    }

    @Override
    public int compareTo( Pair<L, R> o )
    {
        return comparableBuilder.append( this, o ).append( l, o.getLeft() ).append( r, o.getRight() ).toComparison();
    }
}
