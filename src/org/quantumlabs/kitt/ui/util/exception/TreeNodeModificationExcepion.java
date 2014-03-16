package org.quantumlabs.kitt.ui.util.exception;

public class TreeNodeModificationExcepion
    extends StackTracableException
{
    public TreeNodeModificationExcepion(String message)
    {
        super( message );
    }

    private static final long serialVersionUID = 1L;
}
