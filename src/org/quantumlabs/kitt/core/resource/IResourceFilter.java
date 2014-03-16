package org.quantumlabs.kitt.core.resource;

import org.eclipse.core.resources.IResource;

public interface IResourceFilter
{
    public boolean accept( IResource resource );
}
