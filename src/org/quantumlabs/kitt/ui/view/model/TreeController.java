package org.quantumlabs.kitt.ui.view.model;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.exception.TreeNodeAddException;
import org.quantumlabs.kitt.ui.util.exception.TreeNodeException;
import org.quantumlabs.kitt.ui.util.exception.TreeNodeModificationExcepion;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;
import org.quantumlabs.kitt.ui.view.ProjectTreeContentProvider;
import org.quantumlabs.kitt.ui.view.ProjectTreeLabelProvider;

/**
 * Tree controller is the bridge between view and model. view operates tree by tree controller
 * indirectly.
 * */
public class TreeController
    implements TreeOperation
{

    private static final int CURRENT_LEVEL = 1;
    private KittProjectNavigatorView view;
    private LinkedList<TreeControllerListener> listeners;
    private ProjectTreeContentProvider contentProvider;
    private ProjectTreeLabelProvider labelProvider;

    public TreeController(KittProjectNavigatorView view)
    {
        this.view = view;
        listeners = new LinkedList<TreeControllerListener>();
        contentProvider = view.getContentProvider();
        labelProvider = view.getLabelProvider();
    }

    public boolean addNode( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        return verifyValidSelection( selection ) && doAdd( selection, candidateNode );
    }

    /**
     * @return whether the selection is null or emputy.
     * */
    private boolean verifyValidSelection( IStructuredSelection selection )
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logWarning( "TreeController#addNode()", "selection is null or emputy : "
                + ( selection == null || selection.isEmpty() ) );
        }
        return selection != null && !selection.isEmpty();
    }

    private boolean doAdd( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {

        if( !verifyOnlyOneElement( selection ) )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logWarning( "TreeController#addNode()", "selected more than one items, can't add node!" );
            }
            throw new TreeNodeAddException( "More than one item selected for adding child." );
        }
        return addToNode( selection, candidateNode );
    }

    /**
     * Current the return value is not corresponding to listener's return.
     * */
    private boolean addToNode( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        TreeNode<IResource> selectedNode = extractNode( selection );

        if( selectedNode.addChild( candidateNode ) )
        {
            //fire event to listener after operation success on model.
            synchronized( listeners )
            {
                for( TreeControllerListener listener : listeners )
                {
                    if( !listener.addNode( selection, candidateNode ) )
                    {
                        if( Logger.isErrorEnable() )
                        {
                            Logger.logError( "TreeController#doAdd()", " fire event to listener fail : " + listener
                                + ",  RETURN FALSE or ROLL BACK?" );
                        }

                        // TODO: RETURN FALSE or ROLL BACK?
                        //return false;
                    }
                }
            }
            return true;
        }
        else
        {
            //TODO:if add node failed, what need to do? roll back?
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "TreeController#doAdd()", " add node failed, what need to do next? !" );
            }
            return false;
        }
    }

    @SuppressWarnings( "unchecked" )
    private TreeNode<IResource> extractNode( IStructuredSelection selection )
    {
        //FIXME : Is it ensured, the node is a  TreeNode<IResource>???
        return BasicSemanticValidator.validateInstanceOf( TreeNode.class, selection.getFirstElement() );
    }

    private boolean verifyOnlyOneElement( IStructuredSelection selection )
    {
        return selection.size() == 1;
    }

    @Override
    public boolean deleteNode( IStructuredSelection selection )
    {
        if( verifyValidSelection( selection ) )
        {
            //Delete one
            if( verifyOnlyOneElement( selection ) )
            {
                return doDeleteSingle( selection );
            }
            //Multiple deletion
            else
            {
                return doDeleteMultiple( selection );
            }
        }
        else
        {
            return false;
        }
    }

    private boolean doDeleteMultiple( IStructuredSelection selection )
    {
        boolean success = true;
        @SuppressWarnings( "unchecked" )
        Iterator<TreeNode<IResource>> iterator = selection.iterator();
        while( iterator.hasNext() )
        {
            TreeNode<IResource> selectedNode = iterator.next();
            TreeNode<IResource> parent = selectedNode.getParent();
            success = success & removeByParent( selection, selectedNode, parent );
        }

        if( !success )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "TreeController#doDeleteMultiple()", "failed to delete multiple node" );
            }
            //TODO : if failed to delete multiple; what need todo ? info user?
            //callBack();!!!!
        }
        return success;
    }

    private boolean doDeleteSingle( IStructuredSelection selection )
    {
        TreeNode<IResource> selectedNode = extractNode( selection );
        if( selectedNode.isRoot )
        {
            throw new TreeNodeException( "Current node should not be ROOT !" );
        }
        TreeNode<IResource> parent = selectedNode.getParent();
        return removeByParent( selection, selectedNode, parent );
    }

    private boolean removeByParent( IStructuredSelection selection, TreeNode<IResource> selectedNode,
        TreeNode<IResource> parent )
    {
        if( parent.removeChild( selectedNode ) )
        {
            synchronized( listeners )
            {
                for( TreeControllerListener listener : listeners )
                {
                    if( !listener.deleteNode( selection ) )
                    {
                        if( Logger.isErrorEnable() )
                        {
                            Logger.logError( "TreeController#doDelete()", " fire event to listener fail : " + listener
                                + ",  RETURN FALSE or ROLL BACK?" );
                        }

                        // TODO: RETURN FALSE or ROLL BACK?
                        //return false;
                    }
                }
            }
            return true;
        }
        else
        {
            //TODO:if delete node failed, what need to do? roll back?
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "TreeController#doDelete()", " delete node failed, what need to do next? !" );
            }
            return false;
        }
    }

    @Override
    public boolean modifyNode( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        return verifyValidSelection( selection ) && doModify( selection, candidateNode );
    }

    private boolean doModify( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        //        TreeNodeModificationExcepion
        if( !verifyOnlyOneElement( selection ) )
        {
            throw new TreeNodeModificationExcepion( "More than one items are selected to modify." );
        }
        return doRelaceNode( selection, candidateNode );
    }

    /**
     * Replace selected with candidate node, only replace name, description and value.
     * 
     * @param selectedNode
     *            the node to be replaced.
     * @param candidateNode
     *            the node for replacing.
     * @return success or not.
     * 
     * */
    private boolean doRelaceNode( IStructuredSelection selection, TreeNode<IResource> candidateNode )
    {
        TreeNode<IResource> selectedNode = extractNode( selection );
        selectedNode.setName( candidateNode.getName() );
        selectedNode.setDescription( candidateNode.getDescription() );
        selectedNode.setValue( candidateNode.getValue() );
        for( TreeControllerListener listener : listeners )
        {
            if( listener.modifyNode( selection, candidateNode ) )
            {
                if( Logger.isErrorEnable() )
                {
                    Logger.logError( "TreeController#doRelaceNode()", "can't fire modification event to listener : "
                        + listener );
                }

                // TODO: if fire event failed, RETURN FALSE or ROLL BACK?
                //return false;
            }
        }
        return true;
    }

    public boolean attachListener( TreeControllerListener listener )
    {
        return listeners.add( listener );
    }

    public boolean detachListener( TreeControllerListener listener )
    {
        if( listeners.contains( listener ) )
        {
            return listeners.remove( listener );
        }
        return false;
    }

    public void collapseTree()
    {
        view.collapseAll();
    }

    public void spreadTree( IStructuredSelection selection )
    {
        if( verifyValidSelection( selection ) )
        {
            expandOnSelection( selection );
        }
        else
        {
            view.expandAll();
        }
    }

    private void expandOnSelection( IStructuredSelection selection )
    {
        // FIXME : expand how deep to expand.
        //        view.expandCurrentNode( selection.getFirstElement(), level );

    }

    @Override
    public boolean addNode( TreeNode<IResource> parent, TreeNode<IResource> candidateNode )
    {
        if( parent.addChild( candidateNode ) )
        {
            //fire event to listener after operation success on model.
            synchronized( listeners )
            {
                for( TreeControllerListener listener : listeners )
                {
                    if( !listener.addNode( parent, candidateNode ) )
                    {
                        if( Logger.isErrorEnable() )
                        {
                            Logger.logError( "TreeController#doAdd()", " fire event to listener fail : " + listener
                                + ",  RETURN FALSE or ROLL BACK?" );
                        }

                        // TODO: RETURN FALSE or ROLL BACK?
                        //return false;
                    }
                }
            }
            return true;
        }
        else
        {
            //TODO:if add node failed, what need to do? roll back?
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "TreeController#doAdd()", " add node failed, what need to do next? !" );
            }
            return false;
        }
    }

    @Override
    public boolean deleteNode( TreeNode<IResource> candidateNode )
    {
        if( candidateNode.isRoot )
        {
            throw new TreeNodeException( "Current node should not be ROOT !" );
        }
        TreeNode<IResource> parent = candidateNode.getParent();
        if( parent.removeChild( candidateNode ) )
        {
            synchronized( listeners )
            {
                for( TreeControllerListener listener : listeners )
                {
                    if( !listener.deleteNode( candidateNode ) )
                    {
                        if( Logger.isErrorEnable() )
                        {
                            Logger.logError( "TreeController#doDelete()", " fire event to listener fail : " + listener
                                + ",  RETURN FALSE or ROLL BACK?" );
                        }

                        // TODO: RETURN FALSE or ROLL BACK?
                        //return false;
                    }
                }
            }
            return true;
        }
        else
        {
            //TODO:if delete node failed, what need to do? roll back?
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "TreeController#doDelete()", " delete node failed, what need to do next? !" );
            }
            return false;
        }
    }

    @Override
    public boolean modifyNode( TreeNode<IResource> originalNode, TreeNode<IResource> candidateNode )
    {
        originalNode.setName( candidateNode.getName() );
        originalNode.setDescription( candidateNode.getDescription() );
        originalNode.setValue( candidateNode.getValue() );
        for( TreeControllerListener listener : listeners )
        {
            if( listener.modifyNode( originalNode, candidateNode ) )
            {
                if( Logger.isErrorEnable() )
                {
                    Logger.logError( "TreeController#doRelaceNode()", "can't fire modification event to listener : "
                        + listener );
                }

                // TODO: if fire event failed, RETURN FALSE or ROLL BACK?
                //return false;
            }
        }
        return true;
    }
}
