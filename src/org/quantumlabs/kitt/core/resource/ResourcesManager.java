package org.quantumlabs.kitt.core.resource;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.resourcechange.PostChangeListener;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.collection.Pair;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class ResourcesManager {
    private static ResourcesManager instance;
    private volatile boolean initialized;
    private IResourceChangeListener PRE_REFRESH;
    private IResourceChangeListener POST_CHANGE;
    private IResourceChangeListener PRE_BUILD;
    private IResourceChangeListener PRE_DELETE;
    private IResourceChangeListener POST_BUILD;
    private IResourceChangeListener PRE_CLOSE;

    /**
     * <strong>{file_name(file_path) : file_path}</strong>
     * */
    public Set<Pair<String, String>> getImagePairs() {
	// TODO : scan and return image resources, represented as pairs :
	// {file_name : file_path}
	return ImagePairInnerHolder.get();
    }

    static class ImagePairInnerHolder {
	static Set<Pair<String, String>> images;
	static {
	    reset();
	}

	static void reset() {
	    images = new HashSet<Pair<String, String>>();
	    load();
	}

	static Set<Pair<String, String>> get() {
	    return images;
	}

	private static void load() {

	    // Add default images
	    for (Entry<String, String> entry : SackConstant.DEFAULT_IMAGE_MAP
		    .entrySet()) {
		images.add(new Pair<String, String>(entry.getKey(), entry
			.getValue()));
		if (Logger.isDebugEnable()) {
		    Logger.logDebug(SackConstant.MESSAGE_RESOURCE_LOAD_IMAGE_PAIR_SUCCESS
			    + entry.getKey() + " = " + entry.getValue());
		}
	    }

	    // Add customized images
	    if (KITTParameter.isImageUpdated()) {
		images.addAll(KITTParameter.getCustomizedImages());
		// add customized image and reset flag.
		KITTParameter.callBack(ImageHolder.class);
	    }
	}
    }

    private ResourcesManager() {

    }

    private static final class Holder {
	final static ResourcesManager holdings;
	static {
	    holdings = new ResourcesManager();
	}
    }

    /**
     * Get the root of workspace structure.
     * 
     * @see {@link #getWorkspace()}
     * */
    public IWorkspaceRoot getWorkspaceRoot() {
	return ResourcesPlugin.getWorkspace().getRoot();
    }

    public static ResourcesManager instance() {
	if (instance == null) {
	    instance = Holder.holdings;
	}
	return instance;
    }

    public IProject getProjectByName(String name) {
	return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    }

    /**
     * Get current workspace
     * 
     * @see {@link #getWorkspaceRoot()}
     * */
    public IWorkspace getWorkspace() {
	return ResourcesPlugin.getWorkspace();
    }

    /**
     * Add post resource change listener to workspace.<strong> Never skip Kitt
     * service to handle resources directly.</strong>
     * */
    public void addResourceChangeListener(IResourceChangeListener listener,
	    int category) {
	if (Logger.isTraceEnable()) {
	    Logger.logTrace("ResourcesManager#addResourcesChangeListener : "
		    + category);
	}
	switch (category) {
	case IResourceChangeEvent.POST_CHANGE:
	    // TODO: add customized operation for adding
	    // resourceschangedlistener
	    break;
	case IResourceChangeEvent.POST_BUILD:
	    break;
	case IResourceChangeEvent.PRE_DELETE:
	    break;
	case IResourceChangeEvent.PRE_CLOSE:
	    break;
	case IResourceChangeEvent.PRE_REFRESH:
	    break;
	case IResourceChangeEvent.PRE_BUILD:
	    break;
	default:
	    if (Logger.isWarningEnable()) {
		Logger.logWarning(getClass().getSimpleName(),
			"addResourcesChangeListener : unkown listener category "
				+ category + "; " + listener);
	    }
	    break;
	}
	getWorkspace().addResourceChangeListener(listener, category);
    }

    public void initialize() {
	if (!initialized) {
	    if (Logger.isDebugEnable()) {
		Logger.logDebug("ResourcesManage#initialize()");
	    }
	    createResourceChangeListener();
	    createPreDeleteResourceListener();
	    initialized = true;
	}
    }

    private void createResourceChangeListener() {
	POST_CHANGE = new PostChangeListener(this);
    }

    private void createPreDeleteResourceListener() {
	// PRE_DELETE = new PreDeleteListener( this );
    }

    public void deleteResource(TreeNode<IResource> node) {
	IProject project = extractProject(node);
	// project.getFile( arg0 )
    }

    private IProject extractProject(TreeNode<IResource> node) {
	return node.getValue().getProject();
    }

    public void createResource(TreeNode<IResource> node) {

    }
}
