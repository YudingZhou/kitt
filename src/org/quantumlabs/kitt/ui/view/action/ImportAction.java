package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ImportResourcesAction;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class ImportAction
    extends ImportResourcesAction
{

    private KittProjectNavigatorView view;

    public ImportAction(IWorkbenchWindow window, KittProjectNavigatorView view)
    {
        super( window );
        this.view = view;
    }

    public ImportAction(IWorkbenchWindow window, NavigatorContextMenuManager menu)
    {
        this( window, menu.getView() );
    }

    public void run()
    {
        super.run();
        postRun();
    }

    private void postRun()
    {
        view.refreshViewer();
    }
}
