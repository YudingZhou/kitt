package org.quantumlabs.kitt.ui.view;

import java.util.LinkedList;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.action.BuildPathAction;
import org.quantumlabs.kitt.ui.view.action.CopyResourceAction;
import org.quantumlabs.kitt.ui.view.action.DebugAsAction;
import org.quantumlabs.kitt.ui.view.action.DeleteAction;
import org.quantumlabs.kitt.ui.view.action.ExportAction;
import org.quantumlabs.kitt.ui.view.action.ImportAction;
import org.quantumlabs.kitt.ui.view.action.KRefreshAction;
import org.quantumlabs.kitt.ui.view.action.NewAction;
import org.quantumlabs.kitt.ui.view.action.OpenWithMenu;
import org.quantumlabs.kitt.ui.view.action.PasteAction;
import org.quantumlabs.kitt.ui.view.action.PropertiesAction;
import org.quantumlabs.kitt.ui.view.action.RunAsAction;

/**
 * This menu could evaluate whether there is item selected or not, so that it can show menu
 * properly.
 * */
public final class NavigatorContextMenuManager
    extends MenuManager
{
    boolean selectedTree = false;
    SelectionEvent event;
    final private KittProjectNavigatorView view;

    //actions
    //----------------
    ImportAction importAction;
    ExportAction exportAction;
    //----------------
    MenuManager openWithMenu;
    //----------------
    NewAction newAction;
    CopyResourceAction copyAction;
    PasteAction pasteAction;
    DeleteAction deleteAction;
    //----------------
    BuildPathAction buildPathAction;
    //----------------
    KRefreshAction refreshAction;
    //----------------
    RunAsAction runAsAction;
    DebugAsAction debugAsAction;
    //----------------
    PropertiesAction propertiesAction;
    private LinkedList<Object> menuItems;

    //listen to tree change, if selection is not none show some more menu items;
    //vise, show default menu.
    NavigatorContextMenuManager(KittProjectNavigatorView view)
    {
        super( SackConstant.MENU_POPUPMENU );
        this.view = view;
    }

    public void init()
    {
        makeMenuItems();
        installSelectionListener();
        hookMenuListener();
        registerToSite();
    }

    private void registerToSite()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "init#registerToSite" );
        }
        Menu menu = createContextMenu( view.getviewer().getControl() );
        view.getviewer().getControl().setMenu( menu );
        view.getviewer().getControl().setMenu( menu );
        view.getSite().registerContextMenu( this, view.getviewer() );
    }

    private void hookMenuListener()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "init#hookMenuListener" );
        }
        //        setRemoveAllWhenShown( true );
        fillContextMenu();
        addMenuListener( new IMenuListener()
        {
            public void menuAboutToShow( IMenuManager manager )
            {
                //Before show menu, it should reset its data, so that its flag could work properly, 
                // so that menu could know whether there is a node which was selected.
                //FIXME : Big risk, navigator context menu -> menuAboutToShow : 
                //not sure which one is invoked first : menuAboutToShow() or widgetSelected().
                if( Logger.isTraceEnable() )
                {
                    Logger.logTrace( "Navigator menu -> menu about to show." );
                }
            }
        } );
    }

    private void makeMenuItems()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "init#makeActions" );
        }

        menuItems = new LinkedList<Object>();
        add();
    }

    private void add()
    {
        makeImportExportActionGroup();
        addSeparator();
        makeFileOperationActionGroup();
        addSeparator();
        makeResourceOperationGroup();
        addSeparator();
        makeProjectBuildActionGroup();
        addSeparator();
        makeProjectOperationActionGroup();
        addSeparator();
        makeRunDebugActionGroup();
        addSeparator();
        makePropertiesActionGroup();
    }

    private void makePropertiesActionGroup()
    {
        // TODO Auto-generated method stub
        propertiesAction = new PropertiesAction( SackConstant.ACTION_PROPERTIES_TEXT, this );
        menuItems.add( propertiesAction );

    }

    private void makeRunDebugActionGroup()
    {
        // TODO Auto-generated method stub
        //----------------
        runAsAction = new RunAsAction( SackConstant.ACTION_RUN_AS_TEXT, this );
        debugAsAction = new DebugAsAction( SackConstant.ACTION_DEBUG_AS_TEXT, this );
        menuItems.add( runAsAction );
        menuItems.add( debugAsAction );
        //----------------

    }

    private void makeProjectOperationActionGroup()
    {
        // TODO Auto-generated method stub
        refreshAction = new KRefreshAction( view.getSite().getWorkbenchWindow().getShell(), this );
        menuItems.add( refreshAction );

    }

    private void makeProjectBuildActionGroup()
    {
        // TODO Auto-generated method stub
        //----------------
        buildPathAction = new BuildPathAction( SackConstant.ACTION_BUILD_PATH_TEXT, this );
        menuItems.add( buildPathAction );
    }

    private void makeFileOperationActionGroup()
    {
        openWithMenu = new OpenWithMenu( SackConstant.ACTION_OPEN_WITH_TEXT, this );
        menuItems.add( openWithMenu );
    }

    private void addSeparator()
    {
        Separator separator = new Separator( SackConstant.SEPARATOR_GROUP_IMPORT );
        menuItems.add( separator );
    }

    public KittProjectNavigatorView getView()
    {
        return view;
    }

    private void makeResourceOperationGroup()
    {
        newAction = new NewAction( SackConstant.ACTION_NEW_MENU_TEXT, this );
        copyAction = new CopyResourceAction( SackConstant.ACTION_COPY_TEXT, this );
        pasteAction = new PasteAction( SackConstant.ACTION_PASTE_TEXT, this );
        deleteAction = new DeleteAction( SackConstant.ACTION_DELETE_TEXT, this );
        menuItems.add( newAction );
        menuItems.add( copyAction );
        menuItems.add( pasteAction );
        menuItems.add( deleteAction );
    }

    private void makeImportExportActionGroup()
    {
        importAction = new ImportAction( view.getSite().getWorkbenchWindow(), this );
        exportAction = new ExportAction( view.getSite().getWorkbenchWindow(), this );
        menuItems.add( importAction );
        menuItems.add( exportAction );
    }

    public void fillContextMenu()
    {
        fillMenuItem( getExistingItem() );

        //TODO : what to except fill actions?
    }

    private Object[] getExistingItem()
    {
        if( menuItems == null )
        {
            synchronized( this )
            {
                if( menuItems == null )
                {
                    makeMenuItems();
                }
            }
        }
        return menuItems.toArray( new Object[menuItems.size()] );
    }

    private void fillMenuItem( Object... items )
    {
        for( Object item : items )
        {
            add( item );
        }
    }

    private void add( Object item )
    {
        if( BasicSemanticValidator.isInstanceOf( Separator.class, item ) )
        {
            add( (Separator) item );
        }
        else if( BasicSemanticValidator.isInstanceOf( IAction.class, item ) )
        {
            add( (IAction) item );
        }
        else if( BasicSemanticValidator.isInstanceOf( IContributionItem.class, item ) )
        {
            add( (IContributionItem) item );
        }
        else
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "NavigatorContextMenuManage#add()", "Unknown action type : "
                    + item.getClass().getSimpleName() );
            }
        }
    }

    private void installSelectionListener()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "init#installSelectionListener" );
        }
        view.installSelectionChangedListenerToViewer( importAction );
        view.installSelectionChangedListenerToViewer( exportAction );
        view.installSelectionChangedListenerToViewer( copyAction );
        view.installSelectionChangedListenerToViewer( pasteAction );
        view.installSelectionChangedListenerToViewer( deleteAction );
        view.installSelectionChangedListenerToViewer( buildPathAction );
        view.installSelectionChangedListenerToViewer( refreshAction );
        view.installSelectionChangedListenerToViewer( runAsAction );
        view.installSelectionChangedListenerToViewer( debugAsAction );
        view.installSelectionChangedListenerToViewer( propertiesAction );
        view.installSelectionChangedListenerToViewer( newAction );
        view.installSelectionChangedListenerToViewer( (OpenWithMenu) openWithMenu );
    }
}
