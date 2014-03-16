package org.quantumlabs.kitt.ui.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;

public abstract class AbstractOpenConfirmAction
    extends Action
{
    protected MessageDialog dialog;
    protected IWorkbenchWindow window;

    public AbstractOpenConfirmAction(IWorkbenchWindow window)
    {
        this.window = window;
    }

    abstract protected MessageDialog createDialog();

    abstract protected void doAction();

    protected void beforeRun()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "ConfirmAction before run." );
        }
    }

    public void run()
    {
        if( !isEnabled() )
        {
            return;
        }

        beforeRun();
        dialog = createDialog();
        if( dialog != null && dialog.open() == SackConstant.INDEX_BUTTON_LABEL_OK )
        {
            doAction();
        }
        else
        {
            if( Logger.isDebugEnable() )
            {
                Logger.logDebug( "ConfirmAction N-OK" );
            }
        }
        postRun();
    }

    protected void postRun()
    {
        if( Logger.isTraceEnable() )
        {
            Logger.logTrace( "ConfirmAction post run." );
        }
    }
}
