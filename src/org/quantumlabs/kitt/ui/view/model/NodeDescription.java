package org.quantumlabs.kitt.ui.view.model;

import org.eclipse.swt.graphics.Image;

/**
 * NodeDescription is the detail of a tree node.
 * */
public class NodeDescription
    implements Cloneable
{
    public Image icon;
    public String realName;
    public NodeType type;
}
