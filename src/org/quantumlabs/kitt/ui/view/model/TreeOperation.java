package org.quantumlabs.kitt.ui.view.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * TreeOperation is an abstraction for basic tree operation.Also see {@link TreeController},
 * {@link TreeControllerListener}.
 * 
 * */
public interface TreeOperation
{
    boolean addNode( IStructuredSelection selection, TreeNode<IResource> candidateNode );

    boolean addNode( TreeNode<IResource> parent, TreeNode<IResource> candidateNode );

    boolean deleteNode( IStructuredSelection selection );

    boolean deleteNode( TreeNode<IResource> candidateNode );

    boolean modifyNode( IStructuredSelection selection, TreeNode<IResource> candidateNode );

    boolean modifyNode( TreeNode<IResource> originalNode, TreeNode<IResource> candidateNode );
}
