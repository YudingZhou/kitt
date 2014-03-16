package org.quantumlabs.kitt.core.serivce;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchSite;
import org.quantumlabs.kitt.Activator;

public class PlatformServiceProvider
{
    private IWorkbenchSite site;

    private PlatformServiceProvider(IWorkbenchSite site)
    {
        this.site = site;
    }

    public ISelectionService getSelectionService()
    {
        return site.getWorkbenchWindow().getSelectionService();
    }

    public static PlatformServiceProvider getProvider( IWorkbenchSite site )
    {
        //TODO : do like log for j
        return new PlatformServiceProvider( site );
    }

    public static void syncExecOnUI( Runnable task )
    {
        Display display = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
        display.syncExec( task );
    }

}
