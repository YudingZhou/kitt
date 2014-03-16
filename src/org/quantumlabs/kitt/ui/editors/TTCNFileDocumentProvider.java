package org.quantumlabs.kitt.ui.editors;

import org.eclipse.core.filebuffers.IAnnotationModelFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class TTCNFileDocumentProvider extends TextFileDocumentProvider
	implements IAnnotationModelFactory {

    @Override
    public IAnnotationModel getAnnotationModel(Object element) {
	// TODO Auto-generated method stub
	return super.getAnnotationModel(element);
    }

    public IAnnotationModel createAnnotationModel(IFile file) {
	try {
	    file.createMarker(IMarker.PROBLEM);
	} catch (CoreException e) {
	   Logger.logError(e);
	}
	return null;
    }

    @Override
    protected FileInfo createFileInfo(Object element) throws CoreException {
	// TODO Auto-generated method stub
	return super.createFileInfo(element);
    }

    @Override
    public IAnnotationModel createAnnotationModel(IPath location) {
	IResource file = ResourcesPlugin.getWorkspace().getRoot()
		.findMember(location);
	if (file instanceof IFile) {
	    return new TTCNAnnotationModel(file);
	}
	return null;
    }
}
