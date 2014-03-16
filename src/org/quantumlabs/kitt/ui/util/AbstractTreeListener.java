package org.quantumlabs.kitt.ui.util;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class AbstractTreeListener
    implements SelectionListener, TreeListener
{

    @Override
    public void treeCollapsed( TreeEvent event )
    {
        if( Logger.isWarningEnable() )
        {
            Logger
                .logWarning( "sub class of AbstractTreeListener should overwritten current method AbstractTreeListener#treeCollapsed." );
        }
    }

    @Override
    public void treeExpanded( TreeEvent event )
    {
        if( Logger.isWarningEnable() )
        {
            Logger
                .logWarning( "sub class of AbstractTreeListener should overwritten current method AbstractTreeListener#treeExpanded." );
        }
    }

    @Override
    public void widgetDefaultSelected( SelectionEvent event )
    {
        if( Logger.isWarningEnable() )
        {
            Logger
                .logWarning( "sub class of AbstractTreeListener should overwritten current method AbstractTreeListener#widgetDefaultSelected." );
        }
    }

    @Override
    public void widgetSelected( SelectionEvent event )
    {
        if( Logger.isWarningEnable() )
        {
            Logger
                .logWarning( "sub class of AbstractTreeListener should overwritten current method AbstractTreeListener#widgetSelected." );
        }
    }

}
