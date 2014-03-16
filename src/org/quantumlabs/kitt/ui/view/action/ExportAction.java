package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ExportResourcesAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class ExportAction
    extends ExportResourcesAction
{

    public ExportAction(IWorkbenchWindow window)
    {
        super( window );
    }

    public ExportAction(IWorkbenchWindow window, NavigatorContextMenuManager menu)
    {
        this( window );
    }

}
