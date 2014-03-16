package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class RunAsAction
    extends BaseSelectionListenerAction
{

    public RunAsAction(String text)
    {
        super( text );
        // TODO Auto-generated constructor stub
    }

    public RunAsAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this( text );
    }

}
