package org.quantumlabs.kitt.ui.view;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.AbstractTreeListener;
import org.quantumlabs.kitt.ui.view.action.CollapseAllAction;
import org.quantumlabs.kitt.ui.view.action.LinkEditorToEditorAction;
import org.quantumlabs.kitt.ui.view.model.RefreshTreeListener;
import org.quantumlabs.kitt.ui.view.model.TreeController;
import org.quantumlabs.kitt.ui.view.model.TreeNode;
import org.quantumlabs.kitt.ui.view.model.TreeRefresher;

/**
 * Navigator should separate to two area : <br>
 * 1. <strong>model operation</strong>. Which provide tree viewer operation abilities and info
 * Navigator that tree is changed. <br>
 * 2. <strong>Project abstract</strong>. Which provide the project portrait. e.g. which file is TTCN
 * source, which is a package, what properties does file or folder own, that's related to menu
 * operation. E.g. right click on a tree node, the menu shown based on the project level abstract of
 * the selected item.
 * */
public class KittProjectNavigatorView
    extends ViewPart
{
    public static final String ID = SackConstant.K_PROJECT_NAV_VIEW;

    private Activator kitt;
    private HashedTreeViewer viewer;
    private TreeController treeController;

    private Action collapseAction;
    private Action linkToEditorAction;

    private boolean linkingEnable = false;

    private NavigatorContextMenuManager menuMgr;

    private ProjectTreeContentProvider contentProvider;
    private ProjectTreeLabelProvider labelProvider;

    private TreeRefresher treeRefresher;

    public class HashedTreeViewer
        extends TreeViewer
    {
        private ConcurrentHashMap<IPath, TreeNode<IResource>> nodeCache;

        public HashedTreeViewer(Composite parent, int style)
        {
            super( parent, style );
            nodeCache = new ConcurrentHashMap<IPath, TreeNode<IResource>>();
        }

        public TreeNode<IResource> getNode( IPath key )
        {
            return nodeCache.get( key );
        }

        public boolean cache( IPath key, TreeNode<IResource> node )
        {
            if( nodeCache.containsKey( key ) )
            {
                return false;
            }
            else
            {
                forceAdd( key, node );
                return true;
            }
        }

        public TreeNode<IResource> forceAdd( IPath key, TreeNode<IResource> node )
        {
            return nodeCache.put( key, node );
        }

        public TreeNode<IResource> remove( IPath key )
        {
            if( nodeCache.containsKey( key ) )
            {
                return nodeCache.remove( key );
            }
            else
            {
                return null;
            }
        }

        public void cache( TreeNode<IResource> treeNode )
        {
            cache( treeNode.getValue().getFullPath(), treeNode );
        }
    }

    public KittProjectNavigatorView()
    {
        kitt = Activator.instance();
    }

    public ProjectTreeContentProvider getContentProvider()
    {
        return contentProvider;
    }

    public ProjectTreeLabelProvider getLabelProvider()
    {
        return labelProvider;
    }

    @Override
    public void dispose()
    {
        super.dispose();
        kitt.unregisterProjectNavigator();
        menuMgr.dispose();
        contentProvider.dispose();
        labelProvider.dispose();
    }

    /**
     * Create control pane should initialize navigation view;
     * 
     * <pre>
     * 1. create project views from workspace(tree structure) 
     * 2. hook context menus 
     * 3. create viewer actions and contribute to bar
     *      1)None selection
     *          a. New Project
     *          b. Import Project
     *      2)Selection
     *          a. resource operation
     *              i. copy
     *              ii. paste
     *              iii. delete
     *          c. build configuration
     *          d. export
     *          e. refactor
     *      3) common
     *          a. refresh
     *          b. /new/ wizard
     * 
     * </pre>
     * */
    @Override
    public void createPartControl( Composite parent )
    {
        kitt.registerProjectNavigator( this );
        //create tree viewer, set content provider, label provider
        initViewer( parent );
        //set initialized tree data
        initTreeController();
        initInput();
        //create actions
        makeActions();
        contributeToActionBar();
        hookContextMenu();
        hookClickHandler();
        hookDefaultListener();
        hookKey();
        registerService();
    }

    private void hookKey()
    {
        // TODO Auto-generated method stub
        bindShortcut();
        bindIndex();
    }

    /**
     * Bind current index of expanded node to keyboard. the index is the first letter of the name of
     * IResource.
     * */
    private void bindIndex()
    {
        // TODO Auto-generated method stub

    }

    private void bindShortcut()
    {
        // TODO Auto-generated method stub

    }

    private void registerService()
    {
        //register tree viewer is the current ISelectionProvider
        getSite().setSelectionProvider( viewer );
    }

    private void initTreeController()
    {
        treeController = new TreeController( this );
        treeController.attachListener( new RefreshTreeListener( viewer ) );
    }

    private void hookDefaultListener()
    {
        // install default input change listener for content provider
        installInputChangeListener();
    }

    private void makeActions()
    {
        makeCollapseAction();
        makeLinkToEditorAction();
    }

    class LinkToEditorAction
        extends Action
    {
        void installToEditor( IEditorPart activeEditor )
        {
             //TODO : if edtorpart instanceof KittEdtor then get resource convert to tree node index.
        }

        public void uninstallToEditor( IEditorPart activeEditor )
        {
            // TODO : LinkToEditorAction#uninstallToEditor

        }
    }

    private void makeLinkToEditorAction()
    {
        linkToEditorAction = new LinkToEditorAction();
        linkToEditorAction.setToolTipText( SackConstant.ACTION_LINK_TO_EDITOR_TOOLTIP );
    }

    public void setLinkingEnable( boolean enable )
    {
        if( linkingEnable != enable )
        {
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( "Project navigator set linking enable action : " + enable );
            }
            linkingEnable = enable;
            linkToEditorAction.setChecked( enable );
            if( enable )
            {
                LinkToEditorAction.class.cast( linkToEditorAction ).installToEditor(
                    getSite().getPage().getActiveEditor() );
            }
            else
            {
                LinkToEditorAction.class.cast( linkToEditorAction ).uninstallToEditor(
                    getSite().getPage().getActiveEditor() );
            }
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( "Project navigator set linking enable action : activeEditor : "
                    + getSite().getPage().getActiveEditor().getTitle() );
            }
        }
    }

    public boolean isLinkingEnable()
    {
        return linkingEnable;
    }

    private void makeCollapseAction()
    {
        collapseAction = new Action()
        {
            @Override
            public void run()
            {
                if( Logger.isDebugEnable() )
                {
                    Logger.logDebug( "collapase action is running." );
                }
                treeController.collapseTree();
            }
        };

        collapseAction.setToolTipText( SackConstant.ACTION_COLLAPSE_TOOLTIP );
        collapseAction.setImageDescriptor( ImageHolder.instance().checkoutDescriptor(
            SackConstant.IMG_COLLAPSE_ACTION_IMG_S ) );
    }

    private void hookClickHandler()
    {
        hookDoubleClickListener();
    }

    private void hookDoubleClickListener()
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( getClass().getSimpleName(), "install NavigatorDoubleClickListener" );
        }
        viewer.addDoubleClickListener( new NavigatorDoubleClickListener() );
    }

    /**
     * Anyone want to listen to changes of tree, it should hook to tree.//TODO:AbstractTreeListener
     * can be more variable. so that tree could add more listener as needed.
     * 
     * */
    public void installListenerToTree( AbstractTreeListener listener )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "hook listener to tree : " + listener.getClass().getSimpleName() );
        }
        viewer.getTree().addSelectionListener( listener );
        viewer.getTree().addTreeListener( listener );
        //TOBE more flexible
    }

    /**
     * Install iselectionchangedlistener to treeviewer, so that when the selection changed, the
     * listener can info other observer.
     * */
    public void installSelectionChangedListenerToViewer( ISelectionChangedListener listener )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "hook selectionChanged listener to tree : " + listener.getClass().getSimpleName() );
        }
        viewer.addSelectionChangedListener( listener );
        //TOBE more flexible
    }

    private void hookContextMenu()
    {
        menuMgr = new NavigatorContextMenuManager( this );
        menuMgr.init();
    }

    private void contributeToActionBar()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), "contributeToActionBar" );
        }
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown( bars.getMenuManager() );
        fillLocalToolBar( bars.getToolBarManager() );
    }

    private void fillLocalPullDown( IMenuManager manager )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), "fillLocalPullDown" );
        }
        //        manager.add( new  );
        //        manager.add( new Separator() );
        //        manager.add( action2 );
    }

    private void fillLocalToolBar( IToolBarManager manager )
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), "fillLocalToolBar" );
        }
        manager.add( new LinkEditorToEditorAction( this, SackConstant.ACTION_LINK_TO_EDITOR_TEXT ) );
        manager.add( new CollapseAllAction( this, SackConstant.ACTION_COLLAPSE_ALL_TEXT ) );
        manager.add( new Separator() );
    }

    private void initInput()
    {
        viewer.setInput( findInputElement() );
    }

    /**
     * View is a ContentView which need to provide content input which will be used for refresh
     * defaultly(current i only find refresh need input). For this view, the input is all tree
     * content. </pre>
     * */
    private void installInputChangeListener()
    {
        //        installSelectionChangedListenerToViewer( new ISelectionChangedListener()
        //        {
        //            @Override
        //            public void selectionChanged( SelectionChangedEvent event )
        //            {
        //                viewer.setInput( TreeNode.class.cast( TreeSelection.class.cast( event.getSelection() )
        //                    .getFirstElement() ) );
        //            }
        //        } );
    }

    Object findInputElement()
    {
        return getViewSite();
    }

    /**
     * This method can be call after new project created. some thing like refresh.
     * */
    private void resetInput()
    {
        //        viewer.setInput( null);
    }

    public void refreshViewer()
    {
        // force content provider re-build whole tree based on current workspace, it will cost too much spent.
        // should be fixed with efficient way later. e.g. by resource delta.
        contentProvider.reinitializeRoot( null );
        treeRefresher.refresh();
    }

    private void initViewer( Composite parent )
    {
        viewer = new HashedTreeViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
        contentProvider = new ProjectTreeContentProvider( this );
        viewer.setContentProvider( contentProvider );
        labelProvider = new ProjectTreeLabelProvider();
        viewer.setLabelProvider( labelProvider );
        treeRefresher = new TreeRefresher( viewer );
    }

    public void expandAll()
    {
        viewer.expandAll();
    }

    public void expandCurrentNode( Object pathOrElement, int level )
    {
        viewer.expandToLevel( pathOrElement, level );
    }

    public void collapseAll()
    {
        try
        {
            viewer.getControl().setRedraw( false );
            viewer.collapseAll();
        }
        finally
        {
            viewer.getControl().setRedraw( true );
        }
    }

    @Override
    public void setFocus()
    {
    }

    public TreeController getTreeController()
    {
        return treeController;
    }

    public HashedTreeViewer getviewer()
    {
        return viewer;
    }
}
