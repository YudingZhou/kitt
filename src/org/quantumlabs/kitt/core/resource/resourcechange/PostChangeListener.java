package org.quantumlabs.kitt.core.resource.resourcechange;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionService;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.resource.ResourcesManager;
import org.quantumlabs.kitt.core.serivce.PlatformServiceProvider;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class PostChangeListener
    implements IResourceChangeListener
{
    private ResourcesManager manager;

    public PostChangeListener(ResourcesManager manager)
    {
        this.manager = manager;
        manager.addResourceChangeListener( this, IResourceChangeEvent.POST_CHANGE );
    }

    @Override
    public void resourceChanged( IResourceChangeEvent event )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName() + " : on post change" );
        }
        try
        {
            event.getDelta().accept( new NavigatorTreeVisitor() );
        }
        catch( CoreException e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( getClass().getSimpleName(), e, "resourceChanged exception : " + e.getMessage() );
            }
        }
    }
}
