package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class PasteAction
    extends BaseSelectionListenerAction
{

    public PasteAction(String text)
    {
        super( text );
    }

    public PasteAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this( text );
    }

}
