package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class PropertiesAction
    extends BaseSelectionListenerAction
{

    public PropertiesAction(String text)
    {
        super( text );
    }

    public PropertiesAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this( text );
    }
}
