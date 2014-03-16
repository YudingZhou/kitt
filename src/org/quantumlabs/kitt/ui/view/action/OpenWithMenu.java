package org.quantumlabs.kitt.ui.view.action;

import java.awt.Dialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.team.core.history.IFileRevision;
//import org.eclipse.team.internal.ui.TeamUIMessages;
//import org.eclipse.team.internal.ui.history.FileRevisionEditorInput;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.dialogs.EditorSelectionDialog;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.IStatusAdapterConstants;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.serivce.PlatformServiceProvider;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class OpenWithMenu
    extends MenuManager
    implements ISelectionChangedListener
{
    private Action openInDefaultEditor;
    private Action openInTextEditor;
    private Action openInOtherEditor;
    private ITreeSelection currentSelection;

    public OpenWithMenu(String text, MenuManager parent)
    {
        super( text );
        setParent( parent );
        init();
    }

    private void init()
    {
        createInnerContextMenu();
        contribute();
    }

    private void contribute()
    {
    }

    private void createInnerContextMenu()
    {
        createInnerActions();
        contributeToSubMenu();
        addMenuListener( new IMenuListener()
        {
            @Override
            public void menuAboutToShow( IMenuManager event )
            {
                if( Logger.isDebugEnable() )
                {
                    Logger.logDebug( getClass().getSimpleName(), "open with menu about to show" );
                }
                if( !disableIfNull() )
                {
                    IResource selectedResource = (IResource) TreeNode.class.cast( currentSelection.getFirstElement() )
                        .getValue();
                    if( selectedResource instanceof IFile )
                    {
                        enableOpenFileAction( true );
                    }
                    else
                    {
                        // TODO : if selection is a IContainer, what will to do next
                        if( Logger.isSystemEnable() )
                        {
                            Logger
                                .logSystem( getClass().getSimpleName(), "open with menu about to show ",
                                    "if selection is a IContainer, set current tree action as disable, what will to do next " );
                        }
                        enableOpenFileAction( false );
                    }

                }
            }

            private boolean disableIfNull()
            {
                if( currentSelection == null || currentSelection.getFirstElement() == null
                    || (IResource) TreeNode.class.cast( currentSelection.getFirstElement() ).getValue() == null )
                {
                    if( Logger.isDebugEnable() )
                    {
                        Logger.logDebug( getClass().getSimpleName(), "disable menu since selection is null." );
                    }
                    enableOpenFileAction( false );
                    return true;
                }
                return false;
            }

            private void enableOpenFileAction( boolean enable )
            {
                openInDefaultEditor.setEnabled( enable );
                openInOtherEditor.setEnabled( enable );
                openInTextEditor.setEnabled( enable );
            }
        } );
    }

    private void contributeToSubMenu()
    {
        add( openInDefaultEditor );
        add( openInTextEditor );
        add( new Separator( SackConstant.SEPARATOR_COMMON ) );
        add( openInOtherEditor );
    }

    private void createInnerActions()
    {
        createOpenInDefault();
        createOpenInText();
        createOpenInOther();
    }

    private void createOpenInOther()
    {
        openInOtherEditor = new BaseSelectionListenerAction( SackConstant.ACTION_OPEN_WITH_INNER_OTHER_TEXT )
        {
            @Override
            public void run()
            {
                Display display = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
                EditorSelectionDialog dialog = new EditorSelectionDialog( display.getActiveShell() );
                if (dialog.open() == Window.OK) {
                    IEditorDescriptor editor = dialog.getSelectedEditor();
                    if (editor != null) {
//                        openEditor(editor, editor.isOpenExternal());
                    }
                }
                int returnCode = dialog.open();
                switch( returnCode )
                {
                    case Window.OK:
                        if( Logger.isTraceEnable() )
                        {
                            Logger.logTrace( getClass().getSimpleName(), "OpenWithOtherEditor", "Ok",
                                "current implementation is only logging, what's the next?" );
                        }
                        break;
                    case Window.CANCEL:
                        if( Logger.isTraceEnable() )
                        {
                            Logger.logTrace( getClass().getSimpleName(), "OpenWithOtherEditor", "CANCEL",
                                "current implementation is only logging, what's the next?" );
                        }
                        break;
                    default:
                        if( Logger.isDebugEnable() )
                        {
                            Logger.logTrace( getClass().getSimpleName(), "OpenWithOtherEditor", "UNKNOWN",
                                "current implementation is only logging, what's the next?" );
                        }
                        break;
                }
            }

//            protected void openEditor(IEditorDescriptor editorDescriptor,
//                boolean openUsingDescriptor) {
//            IFileRevision fileRevision = getFileRevision();
//            if (fileRevision == null) {
//                return;
//            }
//            try {
//                IProgressMonitor monitor = new NullProgressMonitor();
//                IStorage storage = fileRevision.getStorage(monitor);
//                boolean isFile = storage instanceof IFile;
//
//                if (openUsingDescriptor) {
//                    // discouraged access to open system editors
//                    ((WorkbenchPage) (page.getSite().getPage()))
//                            .openEditorFromDescriptor(isFile ? new FileEditorInput(
//                                    (IFile) storage)
//                                    : (IEditorInput) FileRevisionEditorInput
//                                            .createEditorInputFor(fileRevision,
//                                                    monitor), editorDescriptor,
//                                    true, null);
//                } else {
//                    String editorId = editorDescriptor == null ? IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID
//                            : editorDescriptor.getId();
//                    page.getSite().getPage().openEditor(
//                            isFile ? new FileEditorInput((IFile) storage)
//                                    : (IEditorInput) FileRevisionEditorInput
//                                            .createEditorInputFor(fileRevision,
//                                                    monitor), editorId, true,
//                            MATCH_BOTH);
//                }
//            } catch (PartInitException e) {
//                StatusAdapter statusAdapter = new StatusAdapter(e.getStatus());
//                statusAdapter.setProperty(IStatusAdapterConstants.TITLE_PROPERTY,
//                        TeamUIMessages.LocalHistoryPage_OpenEditorError);
//                StatusManager.getManager()
//                        .handle(statusAdapter, StatusManager.SHOW);
//            } catch (CoreException e) {
//                StatusAdapter statusAdapter = new StatusAdapter(e.getStatus());
//                statusAdapter.setProperty(IStatusAdapterConstants.TITLE_PROPERTY,
//                        TeamUIMessages.LocalHistoryPage_OpenEditorError);
//                StatusManager.getManager().handle(statusAdapter, StatusManager.LOG);
//            }
//        }
        };
        openInOtherEditor.setImageDescriptor( ImageHolder.instance().checkoutDescriptor(
            SackConstant.IMG_ACTION_OTHER_EDITOR_IMG_S ) );
    }

    private void createOpenInText()
    {
        openInTextEditor = new BaseSelectionListenerAction( SackConstant.ACTION_OPEN_WITH_INNER_TEXT_TEXT )
        {
            @Override
            public void run()
            {
                // TODO
                assert false : "FIX ME : OpenWithAction#openTextEditor no implementation";
            }
        };
        openInTextEditor.setImageDescriptor( ImageHolder.instance().checkoutDescriptor(
            SackConstant.IMG_ACTION_TEXT_EDITOR_IMG_S ) );
    }

    private void createOpenInDefault()
    {
        openInDefaultEditor = new BaseSelectionListenerAction( SackConstant.ACTION_OPEN_WITH_INNER_DEFAULT_TEXT )
        {
            @Override
            public void run()
            {
                // TODO
                assert false : "FIX ME : OpenWithAction#openDefaultEditor no implementation";
            }
        };
        openInDefaultEditor.setImageDescriptor( ImageHolder.instance().checkoutDescriptor(
            SackConstant.IMG_ACTION_DEFAULT_EDITOR_IMG_S ) );
    }

    @Override
    public void selectionChanged( SelectionChangedEvent event )
    {
        currentSelection = (ITreeSelection) event.getSelection();
    }
}
