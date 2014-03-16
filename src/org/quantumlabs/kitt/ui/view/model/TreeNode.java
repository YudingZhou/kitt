package org.quantumlabs.kitt.ui.view.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.quantumlabs.kitt.core.util.ComparableBuilder;
import org.quantumlabs.kitt.core.util.Observable;
import org.quantumlabs.kitt.core.util.Observer;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView.HashedTreeViewer;

public class TreeNode<V>
    extends Observable<NodeEvent>
    implements Comparable<TreeNode<V>>
{
    public final boolean isRoot;
    public NodeType type;
    private String name;
    private LinkedList<TreeNode<V>> children;
    private TreeNode<V> parent;
    /**
     * Node value should be org.eclipse.core.resource.IResource
     * 
     * <pre>
     * Type hierarchy:
     * IResource
     *      >>Resource
     *          >>Container
     *          >>File
     *      >>IContainer
     *          >>Container
     *              >>Folder
     *              >>Project
     *              >>WorkspaceRoot
     *          >>IFolder
     *              >>Folder
     *          >>IProject
     *              >>Project
     *          >>IWorkspaceRoot
     *              >>WorkspaceRoot
     *      >>IFile
     *          >>File
     * </pre>
     * */
    private V value;
    private ComparableBuilder comparableBuilder;
    private NodeDescription description;
    private HashedTreeViewer viewer;

    public NodeDescription getDescription()
    {
        return description;
    }

    public void setDescription( NodeDescription description )
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setType( NodeType type )
    {
        this.type = type;
    }

    @Override
    public int compareTo( TreeNode<V> o )
    {
        return comparableBuilder.append( name, o.name ).append( type, o.type ).append( value, o.getValue() )
            .toComparison();
    }

    @SuppressWarnings( "unchecked" )
    public TreeNode<V>[] getChildren()
    {
        return children.toArray( new TreeNode[children.size()] );
    }

    public V getValue()
    {
        return value;
    }

    public void setValue( V value )
    {
        this.value = value;
    }

    public TreeNode(TreeNode<V> parent, String name, NodeType type, V value, HashedTreeViewer treeViewer)
    {
        this( parent, name, type, value, false, treeViewer );
    }

    @SuppressWarnings( "unchecked" )
    public TreeNode(TreeNode<V> parent, String name, NodeType type, V value, boolean isRoot, HashedTreeViewer treeViewer)
    {
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.value = value;
        this.isRoot = isRoot;
        children = new LinkedList<TreeNode<V>>();
        comparableBuilder = new ComparableBuilder();
        description = new NodeDescription();
        description.type = type;
        description.realName = name;
        viewer = treeViewer;
        treeViewer.cache( (TreeNode<IResource>) this );
    }

    @Override
    public String toString()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public boolean addChild( TreeNode<V> node )
    {
        if( null == node )
        {
            return false;
        }

        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "add tree node : " + node.name );
        }
        node.parent = this;
        if( children.add( node ) )
        {
            for( Observer<NodeEvent> observer : getObservers() )
            {
                observer.fireEvent( this, NodeEvent.ADD );
            }
            return true;
        }
        if( Logger.isErrorEnable() )
        {
            Logger.logError( "add tree node failed : " + node.name );
        }
        return false;
    }

    public void addChildren( List<TreeNode<V>> children )
    {
        for( TreeNode<V> child : children )
        {
            addChild( child );
        }
    }

    public TreeNode<V> getParent()
    {
        return parent;
    }

    public boolean hasChildren()
    {
        return children.size() > 0;
    }

    public boolean removeChild( TreeNode<V> child )
    {
        if( children.remove( child ) )
        {
            for( Observer<NodeEvent> observer : getObservers() )
            {
                observer.fireEvent( child, NodeEvent.DELETE );
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public HashedTreeViewer getViewer()
    {
        return viewer;
    }

    @Override
    public boolean equals( Object o )
    {
        if( o == this )
        {
            return true;
        }
        if( !( o instanceof TreeNode ) )
        {
            return false;
        }

        return compareTo( (TreeNode<V>) o ) == 0 ? true : false;
    }
}
