package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.jface.action.Action;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.core.util.trace.LoggerUtil;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;

public class CollapseAllAction
    extends Action
{

    private KittProjectNavigatorView view;

    public CollapseAllAction(KittProjectNavigatorView view, String text)
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), LoggerUtil.TEXT.CONSTRUCTING.toString() );
        }
        this.view = view;
        setToolTipText( text );
        setImageDescriptor( ImageHolder.instance().checkoutDescriptor( SackConstant.IMG_ACTION_COLLAPSE_ALL_IMG_S ) );
    }

    public void run()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), "collapse all run" );
        }
        view.collapseAll();
    }
}
