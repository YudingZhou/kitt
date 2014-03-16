package org.quantumlabs.kitt.ui.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.serivce.PlatformServiceProvider;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class NavigatorDoubleClickListener
    implements IDoubleClickListener
{
    /**
     * <pre>
     * Double click policy:
     *       click on :
     *          >>file
     *              >>> ttcn file : open in KITT editor
     *              >>> other file : open in corresponding editor 
     *          >>container
     *              >>> folder ://TODO
     *              >>> package ://TODO
     *              >>> project ://TODO
     *              >>> ...
     * </pre>
     * */
    @Override
    public void doubleClick( DoubleClickEvent event )
    {
        ITreeSelection selection = (ITreeSelection) event.getSelection();
        IResource selectedResource = (IResource) TreeNode.class.cast( selection.getFirstElement() ).getValue();
        if( selectedResource instanceof IFile )
        {
            doubleClickOnFile( IFile.class.cast( selectedResource ) );
        }
        else
        {
            doubleClickOnContainer( selectedResource );
        }
    }

    private void doubleClickOnFile( IFile selectedResource )
    {
        try
        {
            if( isTTCNFile( selectedResource ) )
            {
                openInKittEditor( selectedResource );
            }
            else
            {
                IWorkbenchPage activePage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage();
                // FIXME : how may i use the opened editorPart??? add to a editor list for retrieving it, like java editor does?
                IEditorPart editorPart = IDE.openEditor( activePage, selectedResource );
            }
        }
        catch( PartInitException e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( getClass().getSimpleName(), e,
                    "double click file to open it failed : " + e.getMessage() );
            }
        }
    }

    private void openInKittEditor( IResource selectedResource )
    {
        final IFile file = (IFile) selectedResource;
        PlatformServiceProvider.syncExecOnUI( new Runnable()
        {
            public void run()
            {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try
                {
                    IDE.openEditor( page, file, true );
                }
                catch( PartInitException e )
                {
                }
            }
        } );
    }

    //TODO : if double click on container, is there any suitable way for it.
    private void doubleClickOnContainer( IResource selectedResource )
    {
        if( selectedResource instanceof IFolder )
        {

        }
        else if( selectedResource instanceof IProject )
        {

        }
        else
        {

        }
    }

    private boolean isTTCNFile( IResource selectedResource )
    {
        return selectedResource.getName().endsWith( SackConstant.FILE_EXTENSION );
    }
}
