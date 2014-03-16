package org.quantumlabs.kitt.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

public class FileImageDescriptor
    extends ImageDescriptor
{

    private File file;
    private InputStream in;

    public FileImageDescriptor(File file)
    {

        this.file = file;
    }

    public FileImageDescriptor(InputStream in)
    {
        this.in = in;
    }

    @Override
    public ImageData getImageData()
    {
        FileInputStream inputStream = null;
        ImageData imageData = null;

        if( in != null )
        {
            imageData = new ImageData( in );
            return imageData;
        }

        try
        {
            inputStream = new FileInputStream( file );
            imageData = new ImageData( inputStream );
        }
        catch( FileNotFoundException e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "can't find file : " + file.getPath(), e );
            }
        }
        catch( SWTException e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( "create image failed due to exception", e );
            }
            throw e;
        }
        finally
        {
            if( null != inputStream )
            {
                try
                {
                    inputStream.close();
                }
                catch( IOException e )
                {
                    //Ignore
                }
            }
        }

        return imageData;
    }

}
