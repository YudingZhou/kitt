package org.quantumlabs.kitt.ui.view.action;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.quantumlabs.kitt.ui.view.NavigatorContextMenuManager;

public class CopyResourceAction
    extends SelectionListenerAction
{
    public CopyResourceAction(String text)
    {
        super( text );
        // TODO Auto-generated constructor stub
    }

    public CopyResourceAction(String text, NavigatorContextMenuManager navigatorContextMenuManager)
    {
        this(text);
    }

    private Clipboard clipboard;

    public void run()
    {
        List<?> selectedResources = getSelectedResources();
        IResource[] resources = selectedResources.toArray( new IResource[selectedResources.size()] );
        //TODO : copy resource action
    }

}
