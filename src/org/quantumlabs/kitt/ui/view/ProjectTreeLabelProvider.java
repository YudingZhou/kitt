package org.quantumlabs.kitt.ui.view;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.BasicSemanticValidator;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.ui.view.model.NodeDescription;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class ProjectTreeLabelProvider
    extends LabelProvider
{

    @Override
    public Image getImage( Object element )
    {
        BasicSemanticValidator.validateNotNull( element );
        TreeNode<?> node = BasicSemanticValidator.validateInstanceOf( TreeNode.class, element );
        NodeDescription description = node.getDescription();
        Image image = null;
        switch( node.type )
        {
            case FILE:
                image = ImageHolder.instance().checkout( SackConstant.IMG_PROJECT_NAV_NODE_FILE_IMG_S );
                break;
            case FOLDER:
                image = ImageHolder.instance().checkout( SackConstant.IMG_PROJECT_NAV_NODE_FOLDER_IMG_S );
                break;
            case PROJECT:
                image = ImageHolder.instance().checkout( SackConstant.IMG_PROJECT_NAV_NODE_PROJECT_IMG_S );
                break;
            case ROOT:
                //ROOT is invisible
                image = null;
                break;
            case OTHER:
                image = ImageHolder.instance().checkout( SackConstant.IMG_PROJECT_NAV_NODE_OTHER_IMG_S );
            default:
                if( KITTParameter.isBETA() )
                {
                    BasicSemanticValidator.fail( "unknown element type : " + node.type );
                }
                else
                {
                    image = null;
                }
        }
        return image;
    }

    @Override
    public String getText( Object element )
    {
        BasicSemanticValidator.validateNotNull( element );
        return BasicSemanticValidator.validateInstanceOf( TreeNode.class, element ).toString();
    }
}
