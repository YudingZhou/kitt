package org.quantumlabs.kitt.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The
 * page will only accept file name without the extension OR with the extension that matches the
 * expected one (.ttcn).
 */

public class NewTTCNFileWizardPage
    extends WizardPage
{
    private Text containerText;

    private Text fileText;

    private ISelection selection;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewTTCNFileWizardPage(ISelection selection)
    {
        super( "wizardPage" );
        setTitle( "Multi-page Editor File" );
        setDescription( "This wizard creates a new file with *.ttcn extension that can be opened by a multi-page editor." );
        this.selection = selection;
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl( Composite parent )
    {
        Composite container = new Composite( parent, SWT.NULL );
        GridLayout layout = new GridLayout();
        container.setLayout( layout );
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        Label label = new Label( container, SWT.NULL );
        label.setText( "&Container:" );

        containerText = new Text( container, SWT.BORDER | SWT.SINGLE );
        GridData gd = new GridData( GridData.FILL_HORIZONTAL );
        containerText.setLayoutData( gd );
        containerText.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                dialogChanged();
            }
        } );

        Button button = new Button( container, SWT.PUSH );
        button.setText( "Browse..." );
        button.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                handleBrowse();
            }
        } );
        label = new Label( container, SWT.NULL );
        label.setText( "&File name:" );

        fileText = new Text( container, SWT.BORDER | SWT.SINGLE );
        gd = new GridData( GridData.FILL_HORIZONTAL );
        fileText.setLayoutData( gd );
        fileText.setText( SackConstant.MESSAGE_NEW_TTCN_FILE_NAME  );
        fileText.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                dialogChanged();
            }
        } );
        initialize();
        dialogChanged();
        setControl( container );
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */

    private void initialize()
    {
        if( selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection )
        {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if( ssel.size() > 1 )
                return;
            Object obj = ssel.getFirstElement();
            if( obj instanceof TreeNode )
            {
                @SuppressWarnings( { "unchecked" } )
                IResource selectedResource = ( (TreeNode<IResource>) obj ).getValue();
                if( selectedResource instanceof IResource )
                {
                    IContainer container;
                    if( selectedResource instanceof IContainer )
                    {
                        container = (IContainer) selectedResource;
                    }
                    else
                    {
                        container = selectedResource.getParent();
                    }
                    containerText.setText( container.getFullPath().toString() );
                }
            }
            else
            {
                if( Logger.isDebugEnable() )
                {
                    Logger.logDebug( getClass().getSimpleName(), "unknown selection type", obj.getClass().toString() );
                }
            }

        }
        fileText.setText( SackConstant.MESSAGE_NEW_TTCN_FILE_NAME );
    }

    /**
     * Uses the standard container selection dialog to choose the new value for the container field.
     */

    private void handleBrowse()
    {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog( getShell(), ResourcesPlugin.getWorkspace()
            .getRoot(), false, "Select new file container" );
        if( dialog.open() == ContainerSelectionDialog.OK )
        {
            Object[] result = dialog.getResult();
            if( result.length == 1 )
            {
                containerText.setText( ( (Path) result[0] ).toString() );
            }
        }
    }

    /**
     * Ensures that both text fields are set.
     */
    private void dialogChanged()
    {
        IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember( new Path( getContainerName() ) );
        String fileName = getFileName();

        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( getClass().getSimpleName(), "dialogChanged", "file : " + getFileName(), "container : "
                + getContainerName() );
        }

        if( getContainerName().length() == 0 )
        {
            updateStatus( "File container must be specified" );
            return;
        }

        if( getFileName() != null
            && getFileName() != ""
            && ResourcesPlugin.getWorkspace().getRoot().getFile( new Path( getContainerName() + "/" + getFileName() ) )
                .exists() )
        {
            updateStatus( "File already exsiting" );
            return;
        }

        if( container == null || ( container.getType() & ( IResource.PROJECT | IResource.FOLDER ) ) == 0 )
        {
            updateStatus( "File container must exist" );
            return;
        }
        if( !container.isAccessible() )
        {
            updateStatus( "Project must be writable" );
            return;
        }
        if( fileName.length() == 0 )
        {
            updateStatus( "File name must be specified" );
            return;
        }
        if( fileName.replace( '\\', '/' ).indexOf( '/', 1 ) > 0 )
        {
            updateStatus( "File name must be valid" );
            return;
        }
        int dotLoc = fileName.lastIndexOf( '.' );
        if( dotLoc != -1 )
        {
            String ext = fileName.substring( dotLoc + 1 );
            if( ext.equalsIgnoreCase( "ttcn" ) == false )
            {
                updateStatus( "File extension must be \".ttcn\"" );
                return;
            }
        }
        updateStatus( null );
    }

    private void updateStatus( String message )
    {
        setErrorMessage( message );
        setPageComplete( message == null );
    }

    public String getContainerName()
    {
        return containerText.getText();
    }

    public String getFileName()
    {
        return fileText.getText();
    }
}
