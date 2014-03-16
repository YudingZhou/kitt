package org.quantumlabs.kitt.ui.view;

import java.util.LinkedList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.IResourceFilter;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;
import org.quantumlabs.kitt.ui.view.model.NavigatorResourceFilter;
import org.quantumlabs.kitt.ui.view.model.NodeType;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class ProjectTreeContentProvider
    implements ITreeContentProvider, IPropertyChangeListener
{
    private KittProjectNavigatorView view;
    private volatile TreeNode<IResource> invisibleRoot;
    private LinkedList<IProject> projects;
    private IResourceFilter fileter;

    public ProjectTreeContentProvider(KittProjectNavigatorView view)
    {
        this.view = view;
        projects = new LinkedList<IProject>();
        fileter = new NavigatorResourceFilter();
    }

    @Override
    public void dispose()
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( "ProjectTreeContentProvider#dispose()" );
        }
    }

    //
    @Override
    public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( getClass().getSimpleName(),
                String.format( "on inputChanged : view : %s    old : %s   new : %s", viewer, oldInput, newInput ) );
        }
    }

    @Override
    public Object[] getChildren( Object o )
    {
        return BasicSemanticValidator.validateInstanceOf( TreeNode.class, o ).getChildren();
    }

    @Override
    public Object[] getElements( Object parent )
    {
        if( parent.equals( view.getViewSite() ) )
        {
            if( invisibleRoot == null )
            {
                initializeRoot();
            }
            return getChildren( invisibleRoot );
        }
        return getChildren( parent );
    }

    public void reinitializeRoot( Object input )
    {
        if( input == null )
        {
            initializeRoot();
        }
        else
        {
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( getClass().getSimpleName(), "reinitialize root  : input : %s " + input );
            }
        }
    }

    /**
     * Initialize current workspace navigator.
     * 
     * @throws StackTracableException
     *             which encapsulates CoreException
     * */
    private void initializeRoot()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( SackConstant.LOG_RESOURCE_LOADING_IMAGE_PAIR );
        }
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        //NULL as the parent of root
        invisibleRoot = new TreeNode<IResource>( SackConstant.NULL, SackConstant.VIEW_WORKSPACE_ROOT_C, NodeType.ROOT,
            workspaceRoot, true, view.getviewer() );
        //StructuredViewer.setInput -> super.setInput /*ContentViewer.setInput*/ -> IContentProvider.inputChanged /*this*/
        view.getviewer().setInput( invisibleRoot );
        IProject[] projectsInWorkspace = workspaceRoot.getProjects();
        for( IProject project : projectsInWorkspace )
        {
            try
            {
                if( Logger.isDebugEnable() )
                {
                    Logger.logDebug( "setup project branch : " + project.getName() );
                }
                //show only KITT project or not
                setUpTree( project );
            }
            catch( CoreException e )
            {
                if( Logger.isErrorEnable() )
                {
                    Logger.logError( "ProjectTreeContentProvider#initializeRoot()", e, project.getName()
                        + "project loading failed : " );
                }
                if( KITTParameter.isBETA() )
                {
                    throw new StackTracableException( e );
                }
            }
        }
    }

    private void setUpTree( IProject project ) throws CoreException
    {
        if( KITTParameter.isCONFIG_SHOW_ONLY_KITT_PROJECTS() )
        {
            IProjectDescription description = project.getDescription();
            if( description.hasNature( SackConstant.NATRURE_KITT_PROJECT ) )
            {
                //TODO: add KITT nature related description
                //addDescriptionDetail(project)
                setUpProjectBranch( project );
            }
        }
        else
        {
            setUpProjectBranch( project );
        }
    }

    private void setUpProjectBranch( IProject project )
    {
        TreeNode<IResource> projectBranch = traverseProjectResources( project );
        projectBranch.type = NodeType.PROJECT;
        projects.add( project );
        invisibleRoot.addChild( projectBranch );
    }

    private TreeNode<IResource> traverseProjectResources( IProject project )
    {
        return traverse( project, fileter, new NodeCreatorDelegateImpl<IResource>() );
    }

    interface NodeCreatorDelegate<T>
    {
        public TreeNode<T> create( TreeNode<T> parent, String nodeName, T value, Class<?> adapter );
    }

    class NodeCreatorDelegateImpl<T>
        implements NodeCreatorDelegate<T>
    {
        @Override
        public TreeNode<T> create( TreeNode<T> parent, String nodeName, T value, Class<?> adapter )
        {
            TreeNode<T> node = null;
            if( IFile.class == adapter )
            {
                node = new TreeNode<T>( parent, nodeName, NodeType.FILE, value, view.getviewer() );
                node.getDescription().icon = ImageHolder.instance().checkout(
                    SackConstant.IMG_PROJECT_NAV_NODE_FILE_IMG_S );
            }
            else if( IFolder.class == adapter )
            {
                node = new TreeNode<T>( parent, nodeName, NodeType.FOLDER, value, view.getviewer() );
                node.getDescription().icon = ImageHolder.instance().checkout(
                    SackConstant.IMG_PROJECT_NAV_NODE_FOLDER_IMG_S );
            }
            else if( IProject.class == adapter )
            {
                node = new TreeNode<T>( parent, nodeName, NodeType.PROJECT, value, view.getviewer() );
                node.getDescription().icon = ImageHolder.instance().checkout(
                    SackConstant.IMG_PROJECT_NAV_NODE_PROJECT_IMG_S );
            }
            else
            {
                node = new TreeNode<T>( parent, nodeName, NodeType.OTHER, value, view.getviewer() );
                node.getDescription().icon = ImageHolder.instance().checkout(
                    SackConstant.IMG_PROJECT_NAV_NODE_OTHER_IMG_S );
            }

            return node;
        }
    }

    /**
     * Traverse IContainer and create TreeNode against on it.
     * 
     * @param resource
     *            The IContainer which will be traversed.
     * @param filter
     *            Filter for filtering IResource.
     * @param filter
     *            Delegate is used to create specific TreeNode which based on adapter.
     * @return TreeNode against the given IContainer.
     * */
    private TreeNode<IResource> traverse( IContainer resource, IResourceFilter filter,
        NodeCreatorDelegate<IResource> delegate )
    {
        if( !filter.accept( resource ) )
        {
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( "contentprovider#traverse() filtered : " + resource.getName() );
            }
            return null;
        }

        TreeNode<IResource> me = delegate.create( invisibleRoot, resource.getName(), resource, IFolder.class );
        traverseChildren( resource, filter, delegate, me );

        return me;
    }

    private void handleError( CoreException e, IContainer container )
    {
        //TODO : mark current resource as unavailible and do rescue...
    }

    private void traverseChildren( IContainer resource, IResourceFilter filter,
        NodeCreatorDelegate<IResource> delegate, TreeNode<IResource> me )
    {
        try
        {
            if( resource.members() != null && resource.members().length > 0 )
            {
                for( IResource childResource : resource.members() )
                {
                    appendChild( filter, delegate, me, childResource );
                }
            }
        }
        catch( CoreException e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "ProjectTreeContentProvider#initializeRoot()", e, resource.getName()
                    + " traverseChildrent() : " );
            }
            handleError( e, resource );
        }
    }

    private void appendChild( IResourceFilter filter, NodeCreatorDelegate<IResource> delegate, TreeNode<IResource> me,
        IResource childResource )
    {
        if( BasicSemanticValidator.isInstanceOf( IFile.class, childResource ) )
        {
            me.addChild( delegate.create( me, childResource.getName(), childResource, IFile.class ) );
        }
        else if( BasicSemanticValidator.isInstanceOf( IFolder.class, childResource ) )
        {
            TreeNode<IResource> branch = traverse( (IContainer) childResource, filter, delegate );
            if( null != branch )
            {
                me.addChild( branch );
            }
        }
        else
        {
            if( Logger.isWarningEnable() )
            {
                Logger.logWarning( "contentprovider#traverse() : unknown IResource type : " + childResource.getName()
                    + " : " + childResource.getClass().getSimpleName() );
            }
            me.addChild( delegate.create( me, childResource.getName(), childResource, null ) );
        }
    }

    @Override
    public Object getParent( Object o )
    {
        return BasicSemanticValidator.validateInstanceOf( TreeNode.class, o ).getParent();
    }

    @Override
    public boolean hasChildren( Object o )
    {
        return BasicSemanticValidator.validateInstanceOf( TreeNode.class, o ).hasChildren();
    }

    public ViewPart getViewPart()
    {
        return view;
    }

    @Override
    public void propertyChange( PropertyChangeEvent event )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "ProjectContentProvider#propertyChange()" );
        }
        // TODO Auto-generated method stub

    }

    public TreeNode<IResource> getRoot()
    {
        return invisibleRoot;
    }
}
