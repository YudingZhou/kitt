package org.quantumlabs.kitt.ui.util.exception;

public class CheckableException
    extends Exception
{
    public CheckableException(String message)
    {
        this( message, null );
    }

    public CheckableException(String message, Throwable cause)
    {
        super( message, cause );
    }

    private static final long serialVersionUID = 1L;
}
