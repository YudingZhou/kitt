package org.quantumlabs.kitt.ui.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;

public interface IRefresher
{
    void addViewer( Viewer viewer );

    void refresh( ISelection selection );

    void refresh();
}
