package org.quantumlabs.kitt.core.resource;

import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.quantumlabs.kitt.core.util.collection.Pair;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class ImageHolder {
    private static ImageHolder instance;

    static class InstanceHolder {
	final static ImageHolder holding;
	static {
	    holding = new ImageHolder();
	}
    }

    public static ImageHolder instance() {
	if (instance == null) {
	    // Lazy initialization.
	    instance = InstanceHolder.holding;
	}
	return instance;
    }

    private ImageHolder() {
	initialize();
    }

    private void initialize() {
	Iterator<Pair<String, String>> iterator = ResourcesManager.instance()
		.getImagePairs().iterator();
	while (iterator.hasNext()) {
	    Pair<String, String> pair = iterator.next();
	    FileImageDescriptor imageDescriptor = new FileImageDescriptor(
		    Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream(pair.getRight()));
	    JFaceResources.getImageRegistry().put(pair.getLeft(),
		    imageDescriptor);
	}
    }

    public Image checkout(String imageId) {
	return JFaceResources.getImageRegistry().get(imageId);
    }

    public ImageDescriptor checkoutDescriptor(String imageId) {
	return JFaceResources.getImageRegistry().getDescriptor(imageId);
    }
}
