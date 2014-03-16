package org.quantumlabs.kitt.ui.view.model;

import org.eclipse.core.resources.IResource;
import org.quantumlabs.kitt.core.resource.IResourceFilter;

/**
 * This is a IResource filter which used for filtering the files which not need to be presented in
 * navigator. //TODO: filter to be implemented.
 * */
public class NavigatorResourceFilter
    implements IResourceFilter
{

    @Override
    public boolean accept( IResource resource )
    {
        //TODO : File filter for construct navigator tree.
        return true;
    }
}
