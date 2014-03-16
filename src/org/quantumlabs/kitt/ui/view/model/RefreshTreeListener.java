package org.quantumlabs.kitt.ui.view.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class RefreshTreeListener
    implements TreeControllerListener
{

    private Viewer viewer;
    private TreeRefresher refresher;

    public RefreshTreeListener(Viewer viewer)
    {
        this.viewer = viewer;
        refresher = new TreeRefresher();
        refresher.addViewer( viewer );
    }

    @Override
    public boolean addNode( final IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        //        viewer.refresh();
        refresher.refresh( new TreeSelection()
        {
            @SuppressWarnings( "unchecked" )
            @Override
            public Object getFirstElement()
            {
                return ( (TreeNode<IResource>) selection.getFirstElement() ).getParent();
            }
        } );
        logInfo( "fresh after add" );
        return true;
    }

    @Override
    public boolean deleteNode( final IStructuredSelection selection )
    {
        //        viewer.refresh();
        refresher.refresh( new TreeSelection()
        {
            @SuppressWarnings( "unchecked" )
            @Override
            public Object getFirstElement()
            {
                return ( (TreeNode<IResource>) selection.getFirstElement() ).getParent();
            }
        } );
        logInfo( "fresh after delete" );
        return true;
    }

    @Override
    public boolean modifyNode( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        viewer.refresh();
        logInfo( "fresh after modify" );
        return true;
    }

    private void logInfo( String message )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "RefreshTreeLisenter : " + message );
        }
    }

    @Override
    public boolean addNode( TreeNode<IResource> parent, TreeNode<IResource> candidateNode )
    {
        refresher.refresh( parent );
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "RefreshTreeLisenter : after add" );
        }
        return true;
    }

    @Override
    public boolean deleteNode( TreeNode<IResource> candidateNode )
    {
        refresher.refresh( candidateNode.getParent() );
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "RefreshTreeLisenter : suceed after delete" );
        }
        return true;
    }

    @Override
    public boolean modifyNode( TreeNode<IResource> originalNode, TreeNode<IResource> candidateNode )
    {
        refresher.refresh( candidateNode.getParent() );
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "RefreshTreeLisenter : after modify" );
        }
        return false;
    }
}
