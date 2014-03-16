package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;
import org.quantumlabs.kitt.ui.view.model.NodeType;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class NewAction
    extends MenuManager
    implements ISelectionChangedListener
{

    private TreeNode<IResource> container;
    private KittProjectNavigatorView view;

    public NewAction(String text, MenuManager parent)
    {
        super( text );
        setParent( parent );
        view = NavigatorContextMenuManager.class.cast( parent ).getView();
        init();
    }

    private void init()
    {
        add( new NewActionMenuManager( view.getSite().getWorkbenchWindow() ) );
    }

    @Override
    public void selectionChanged( SelectionChangedEvent event )
    {
        IStructuredSelection selection = BasicSemanticValidator.validateInstanceOf( IStructuredSelection.class,
            event.getSelection() );
        @SuppressWarnings( "unchecked" )
        TreeNode<IResource> selectedNode = (TreeNode<IResource>) selection.getFirstElement();

        if( Logger.isTraceEnable() )
        {
            if( selectedNode != null )
            {
                Logger.logTrace( "NewAction#selection : " + selectedNode.getName() + ", " + selectedNode.getValue() );
            }
        }
        if( selectedNode == null )
        {
            setContainer( view.getContentProvider().getRoot() );
        }
        else if( selectedNode.type == NodeType.FILE || selectedNode.getDescription().type == NodeType.FILE )
        {
            setContainer( selectedNode.getParent() );
        }
        else
        {
            setContainer( selectedNode );
        }
    }

    public TreeNode<IResource> getContainer()
    {
        return container;
    }

    public void setContainer( TreeNode<IResource> contrainer )
    {
        this.container = contrainer;
    }

}
