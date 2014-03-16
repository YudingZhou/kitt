package org.quantumlabs.kitt.core.resource;

import java.net.URI;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IPath;

public class KittProjectDescription
    implements IProjectDescription
{

    private String comment;
    private IProject[] dynamicReferences;
    private IPath path;
    private URI locationUri;
    private String name;
    private String[] natures;
    private IProject[] refernceProjects;
    private String buildConfig;
    private String[] buildConfigs;
    private ICommand[] buildSpec;

    @Override
    public IBuildConfiguration[] getBuildConfigReferences( String arg0 )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ICommand[] getBuildSpec()
    {
        return buildSpec;
    }

    @Override
    public String getComment()
    {
        return comment;
    }

    @Override
    public IProject[] getDynamicReferences()
    {
        return dynamicReferences;
    }

    @Override
    public IPath getLocation()
    {
        return path;
    }

    @Override
    public URI getLocationURI()
    {
        return locationUri;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String[] getNatureIds()
    {
        // TODO Auto-generated method stub
        return natures;
    }

    @Override
    public IProject[] getReferencedProjects()
    {
        return refernceProjects;
    }

    @Override
    public boolean hasNature( String nature )
    {
        boolean has = false;
        for( String n : natures )
        {
            if( n == nature )
            {
                has = true;
                break;
            }
        }
        return has;
    }

    @Override
    public ICommand newCommand()
    {
        // FIXME TODO :What's ICommand? what does it use for?
        return null;
    }

    @Override
    public void setActiveBuildConfig( String config )
    {
        buildConfig = config;
    }

    @Override
    public void setBuildConfigReferences( String configName, IBuildConfiguration[] references )
    {
        // FIXME TODO : What does it mean?

    }

    @Override
    public void setBuildConfigs( String[] configs )
    {
        buildConfigs = configs;
    }

    @Override
    public void setBuildSpec( ICommand[] command )
    {
        buildSpec = command;
    }

    @Override
    public void setComment( String comment )
    {
        this.comment = comment;
    }

    @Override
    public void setDynamicReferences( IProject[] dynamicReferences )
    {
        this.dynamicReferences = dynamicReferences;
    }

    @Override
    public void setLocation( IPath path )
    {
        this.path = path;
    }

    @Override
    public void setLocationURI( URI uri )
    {
        locationUri = uri;
    }

    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public void setNatureIds( String[] natures )
    {
        this.natures = natures;
    }

    @Override
    public void setReferencedProjects( IProject[] referenceProjecets )
    {
        this.refernceProjects = referenceProjecets;
    }

}
