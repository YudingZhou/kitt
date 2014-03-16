package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.jface.action.Action;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.core.util.trace.LoggerUtil;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;

public class LinkEditorToEditorAction
    extends Action
{
    public LinkEditorToEditorAction(KittProjectNavigatorView view, String text)
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), LoggerUtil.TEXT.CONSTRUCTING.toString() );
        }
        setToolTipText( SackConstant.ACTION_LINK_TO_EDITOR_TOOLTIP );
        setImageDescriptor( ImageHolder.instance().checkoutDescriptor( SackConstant.IMG_ACTION_LINK_TO_EDITOR_IMG_S ) );
    }
}
