package org.quantumlabs.kitt.core.config;

public class ConfigurationManager
{
    public ConfigurationManager()
    {

    }

    public void setLogLevel( int level )
    {
        KITTParameter.setLogLevel( level );
    }

    public void setImage( String name, String path )
    {
        //TODO: used to configure customized image.
    }

    public void setBeta( boolean beta )
    {
        KITTParameter.setBETA( beta );
    }
}
