package org.quantumlabs.kitt.ui.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;

public abstract class AbstractOpenWizardAction
    extends Action
{

    @Override
    public void run()
    {
        //TODO : AbstractOpenWizardAction -> do some initialized works for Open wizard action; reference to org.eclipse.jdt.ui.actions.AbstractOpenWizardAction
    }

    public abstract Wizard createWizard();
}
