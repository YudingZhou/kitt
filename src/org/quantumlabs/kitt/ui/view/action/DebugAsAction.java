package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class DebugAsAction
    extends BaseSelectionListenerAction
{

    public DebugAsAction(String text)
    {
        super( text );
        // TODO Auto-generated constructor stub
    }

    public DebugAsAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this( text );
    }

}
