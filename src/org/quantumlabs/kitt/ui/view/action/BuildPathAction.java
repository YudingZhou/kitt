package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class BuildPathAction
    extends BaseSelectionListenerAction
{

    public BuildPathAction(String text)
    {
        super( text );
        // TODO Auto-generated constructor stub
    }

    public BuildPathAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this(text);
    }

}
