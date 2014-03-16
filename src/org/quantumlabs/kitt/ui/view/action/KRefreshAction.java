package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.RefreshAction;
import org.quantumlabs.kitt.core.serivce.PlatformServiceProvider;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;
import org.quantumlabs.kitt.ui.view.model.TreeRefresher;

public class KRefreshAction
    extends RefreshAction
{

    private NavigatorContextMenuManager menu;
    private TreeRefresher refresher;
    private IStructuredSelection selection;

    private TreeViewer viewer;

    public KRefreshAction(IShellProvider provider)
    {
        super( provider );
        // TODO Auto-generated constructor stub
    }

    public KRefreshAction(Shell shell, NavigatorContextMenuManager menu)
    {
        super( shell );
        this.menu = menu;
        associateWithViewer();
        setEnabled( true );
        refresher = new TreeRefresher();
        refresher.addViewer( menu.getView().getviewer()  );
    }

    private void associateWithViewer()
    {
        viewer = menu.getView().getviewer();
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public void run()
    {
        setCurrentSelection();
        refresh();
    }

    private void setCurrentSelection()
    {
        selection = (TreeSelection) PlatformServiceProvider.getProvider( menu.getView().getSite() )
            .getSelectionService().getSelection();
    }

    public void refresh()
    {
        refresher.refresh( selection );
    }

}
