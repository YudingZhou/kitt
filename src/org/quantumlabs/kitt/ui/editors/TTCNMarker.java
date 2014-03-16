package org.quantumlabs.kitt.ui.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class TTCNMarker extends MarkerAnnotation {

    public static final String OCCURRENCE = "org.quantumlabs.opensource.kitt.marker.occurrence";

    public TTCNMarker(String annotationType, IMarker marker) {
	super(annotationType, marker);
    }

    public TTCNMarker(IMarker marker) {
	super(marker);
    }

    public static boolean isTTCNMarker(IMarker marker) {
	String type;
	try {
	    type = marker.getType();

	    if (OCCURRENCE.equals(type)) {
		return true;
	    }
	    return false;
	} catch (CoreException e) {
	    if (Logger.isErrorEnable()) {
		Logger.logError(TTCNMarker.class.toString(), e,
			"TTCNMarker.isTTCNMarker()");
	    }
	    return false;
	}
    }
}
