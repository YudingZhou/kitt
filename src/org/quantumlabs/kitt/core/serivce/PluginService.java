package org.quantumlabs.kitt.core.serivce;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class PluginService
{
    private static PluginService instance;
    private ViewSelectionChangedListener navigatorSelectionChangedListener;

    public static PluginService instance()
    {
        if( instance == null )
        {
            synchronized( PluginService.class )
            {
                if( instance == null )
                {
                    if( Logger.isSystemEnable() )
                    {
                        Logger.logSystem( PluginService.class.getSimpleName(), "create PluginService" );
                    }
                    instance = new PluginService();
                }
            }
        }
        return instance;
    }

    private class ViewSelectionChangedListener
        implements ISelectionChangedListener
    {
        private ISelection selection;

        @Override
        public void selectionChanged( SelectionChangedEvent event )
        {
            selection = event.getSelection();
        }

        public ISelection getCurrentSelection()
        {
            return selection;
        }
    }

    public void initialize()
    {
        navigatorSelectionChangedListener = new ViewSelectionChangedListener();
    }

    public ISelection getCurrentSelectionFromNavigatorView()
    {
        if( Activator.instance().getProjectNavigator() != null )
        {
            return Activator.instance().getProjectNavigator().getviewer().getSelection();
        }
        return null;
    }

    public String[] getTTCNPartitionTypes()
    {
        return null;
    }
}
