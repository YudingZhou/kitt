package org.quantumlabs.kitt.core.resource.resourcechange;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.model.NodeType;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class NavigatorTreeVisitor
    implements IResourceDeltaVisitor
{

    @Override
    public boolean visit( IResourceDelta delta ) throws CoreException
    {
        IResource resource = delta.getResource();
        int kind = delta.getKind();

        if( kind == IResourceDelta.ADDED )
        {
            createNode( resource );
        }
        else if( kind == IResourceDelta.REMOVED )
        {
            deleteNode( resource );
        }
        else if( kind == IResourceDelta.CHANGED )
        {
            modifyNode( resource );
        }
        else
        {

        }
        return true;

    }

    private void modifyNode( IResource resource )
    {
        // TODO Auto-generated method stub

    }

    private void deleteNode( IResource resource )
    {
        if( Activator.instance().getProjectNavigator() != null )
        {
            TreeNode<IResource> candidate = Activator.instance().getProjectNavigator().getviewer()
                .getNode( resource.getFullPath() );
            Activator.instance().getProjectNavigator().getTreeController().deleteNode( candidate );
            if( Logger.isTraceEnable() )
            {
                Logger.logTrace( getClass().getSimpleName(), "delete : " + resource.getFullPath() );
            }
        }
    }

    private NodeType extractNodeType( IResource resource )
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( getClass().getSimpleName(), "extractNodeType() resource type", resource.getClass()
                .toString() );
        }
        if( resource instanceof IFile )
        {
            return NodeType.FILE;
        }
        else if( resource instanceof IFolder )
        {
            return NodeType.FOLDER;
        }
        else if( resource instanceof IProject )
        {
            return NodeType.PROJECT;
        }
        else
        {
            return NodeType.OTHER;
        }
    }

    private void createNode( IResource resource )
    {
        if( Activator.instance().getProjectNavigator() != null )
        {
            IResource parentResource = resource.getParent();
            TreeNode<IResource> parentNode = Activator.instance().getProjectNavigator().getviewer()
                .getNode( parentResource.getFullPath() );
            TreeNode<IResource> candidateNode = new TreeNode<IResource>( parentNode, resource.getName(),
                extractNodeType( resource ), resource, parentNode.getViewer() );
            Activator.instance().getProjectNavigator().getTreeController().addNode( parentNode, candidateNode );
        }
    }
}
