package org.quantumlabs.kitt.ui.view.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.AbstractOpenConfirmAction;
import org.quantumlabs.kitt.ui.util.IMonitoredCallable;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class DeleteAction
    extends AbstractOpenConfirmAction
    implements ISelectionChangedListener
{

    private IStructuredSelection selection;
    private KittProjectNavigatorView view;
    private String text;
    private MessageDialog dialog;

    public DeleteAction(KittProjectNavigatorView view, String text)
    {
        super( view.getSite().getWorkbenchWindow() );
        this.view = view;
        this.text = text;
        init();
    }

    public DeleteAction(String text, NavigatorContextMenuManager menu)
    {
        this( menu.getView(), text );
    }

    private void init()
    {
        setText( text );
        //        setImageDescriptor( newImage )
        setToolTipText( SackConstant.ACTION_DELETE_TOOLTIP );
    }

    @Override
    public void selectionChanged( SelectionChangedEvent event )
    {
        selection = BasicSemanticValidator.validateInstanceOf( IStructuredSelection.class, event.getSelection() );
        if( verifyValidSelection( selection ) )
        {
            enableAction();
        }
        else
        {
            disableAcion();
        }
    }

    @Override
    protected void beforeRun()
    {
        super.beforeRun();
    }

    private void disableAcion()
    {
        setEnabled( false );
    }

    private void enableAction()
    {
        setEnabled( true );
    }

    private boolean verifyValidSelection( IStructuredSelection selection )
    {
        return selection != null && selection.size() > 0;
    }

    @Override
    protected MessageDialog createDialog()
    {
        if( verifyValidSelection( selection ) )
        {
            StringBuilder stringBuilder = new StringBuilder();
            @SuppressWarnings( "unchecked" )
            Iterator<TreeNode<IResource>> iterator = selection.iterator();
            stringBuilder.append( SackConstant.DIALOG_CONFIRM_DELETE_MESSAGE );
            stringBuilder.append( " " );
            stringBuilder.append( "'" );
            stringBuilder.append( iterator.next().getDescription().realName );
            stringBuilder.append( "'" );
            while( iterator.hasNext() )
            {
                stringBuilder.append( "," );
                stringBuilder.append( " " );
                stringBuilder.append( "'" );
                stringBuilder.append( iterator.next().getDescription().realName );
                stringBuilder.append( "'" );
            }
            stringBuilder.append( " ?" );
            dialog = new MessageDialog( window.getShell(), SackConstant.DIALOG_CONFIRM_DELETE_TITLE, ImageHolder
                .instance().checkout( SackConstant.IMG_ACTION_DELETE_IMG_S ), stringBuilder.toString(),
                MessageDialog.QUESTION,
                new String[] { SackConstant.LABEL_BUTTON_OK, SackConstant.LABEL_BUTTON_CANCEL },
                SackConstant.INDEX_BUTTON_LABEL_OK );
            return dialog;
        }
        else
        {
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( "No selection for delete action." );
            }
            return null;
        }
    }

    @Override
    protected void doAction()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "DeleteAction#doAction -> delete selection" );
        }
        Iterator<?> selectionIterator = selection.iterator();
        while( selectionIterator.hasNext() )
        {
            final IResource selectedResource = (IResource) BasicSemanticValidator.validateInstanceOf( TreeNode.class,
                selectionIterator.next() ).getValue();
            runInProgress( new IRunnableWithProgress()
            {
                @Override
                public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
                {
                    try
                    {
                        selectedResource.delete( true, monitor );
                        if( Logger.isTraceEnable() )
                        {
                            Logger.logTrace( getClass().getSimpleName(), "delete action succeed in resource level." );
                        }
                    }
                    catch( CoreException e )
                    {
                        throw new StackTracableException( "Wrappered CoreException,  " + e.getClass().getSimpleName()
                            + " : delete action failed in resource level, " + e.getMessage(), e );
                    }
                }
            } );
        }
    }

    private void runInProgress( IRunnableWithProgress target )
    {
        ProgressMonitorDialog dialog = new ProgressMonitorDialog( view.getSite().getShell() );
        try
        {
            dialog.run( KITTParameter.isACTION_RUNNING_IN_OTHER_THREAD(), true, target );
        }
        catch( Exception e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( getClass().getSimpleName(), e, "do action failed due to exception." );
            }
        }

    }

    private void scheduleDeleteJob( final IMonitoredCallable<Void> target )
    {
        Job job = new Job( SackConstant.JOB_DELETE_RESOURCE )
        {
            @Override
            protected IStatus run( IProgressMonitor monitor )
            {
                IStatus status = null;
                try
                {
                    target.setMonitor( monitor );
                    target.call();
                }
                catch( Exception e )
                {
                    if( Logger.isErrorEnable() )
                    {
                        Logger.logError( getClass().getSimpleName(), e, "do action failed due to exception." );
                    }
                    status = ( (CoreException) e ).getStatus();
                }
                status = new Status( IStatus.OK, SackConstant.PLUGIN_ID,
                    SackConstant.SYSTEM_MESSAGE.ACTION_DELETE_RESOURCE_FAILURE.getMessage() );
                return status;
            }
        };
        job.setPriority( Job.SHORT );
        job.schedule();
    }
}
