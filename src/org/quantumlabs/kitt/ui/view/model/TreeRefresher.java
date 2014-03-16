package org.quantumlabs.kitt.ui.view.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.quantumlabs.kitt.core.serivce.PlatformServiceProvider;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.IRefresher;

public class TreeRefresher
    implements IRefresher
{

    public TreeRefresher()
    {
    }

    public TreeRefresher(Viewer viewer)
    {
        addViewer( viewer );
    }

    private TreeViewer viewer;

    @Override
    public void addViewer( Viewer viewer )
    {
        this.viewer = (TreeViewer) viewer;
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public void refresh( ISelection selection )
    {
        if( selection == null || needRefreshRoot( extractNode( selection ) ) )
        {
            refresh();
        }
        else
        {
            refresh( (TreeNode) ( (TreeSelection) selection ).getFirstElement() );
        }
    }

    private boolean needRefreshRoot( TreeNode<IResource> node )
    {
        return node.isRoot || node.getParent().isRoot;
    }

    @SuppressWarnings( "unchecked" )
    private TreeNode<IResource> extractNode( ISelection selection )
    {
        return TreeNode.class.cast( TreeSelection.class.cast( selection ).getFirstElement() );
    }

    public void refresh( final TreeNode<IResource> node )
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( getClass().getSimpleName(), "refresh : " + node.toString() + ", is root : " + node.isRoot );
        }
        if( !needRefreshRoot( node ) )
        {
            PlatformServiceProvider.syncExecOnUI( new Runnable()
            {
                @Override
                public void run()
                {
                    viewer.refresh( node );
                }
            } );
        }
        else
        {
            refresh();
        }
    }

    @Override
    public void refresh()
    {
        PlatformServiceProvider.syncExecOnUI( new Runnable()
        {
            @Override
            public void run()
            {
                viewer.refresh();
            }
        } );
    }
}
