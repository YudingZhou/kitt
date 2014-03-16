package org.quantumlabs.kitt.core.util;

/**
 * Aspect are numbers of actions before or after an operation. Aspect should be independent to the
 * operations, the operation <strong>should not</strong> reference to the result of its aspects.
 * */
public interface Aspect
    extends Comparable<Aspect>
{
    /**
     * Do action on the operation.
     * */
    void doAction( Object... args );
}
