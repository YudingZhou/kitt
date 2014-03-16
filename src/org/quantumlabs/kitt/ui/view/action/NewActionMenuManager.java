package org.quantumlabs.kitt.ui.view.action;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.NewWizardMenu;

public class NewActionMenuManager
    extends NewWizardMenu
{

    //FIXME ： 6.27 : can't detect exsiting file when creating !!!
    public NewActionMenuManager(IWorkbenchWindow window)
    {
        super( window );
//        this.getContributionItems()[2];
    }
}
