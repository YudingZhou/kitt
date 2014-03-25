package org.quantumlabs.kitt;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.quantumlabs.kitt.core.TTCNCore;
import org.quantumlabs.kitt.core.config.ConfigurationManager;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.config.KITTParameter.Theme;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.resource.ResourcesManager;
import org.quantumlabs.kitt.core.serivce.PluginService;
import org.quantumlabs.kitt.core.serivce.TransactionCache;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.ILogger;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.editors.ColorManager;
import org.quantumlabs.kitt.ui.text.ITTCNColorConstants;
import org.quantumlabs.kitt.ui.text.TTCNTextTools;
import org.quantumlabs.kitt.ui.view.KittProjectNavigatorView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * Kitt configuration entry.
	 * */
	private final ConfigurationManager configurationManager;
	/**
	 * Kitt project navigator entry.
	 * */
	private KittProjectNavigatorView projectNavigator;
	// The plug-in ID
	public static final String PLUGIN_ID = SackConstant.PLUGIN_ID;

	// The shared instance
	private static Activator kitt;

	private TTCNTextTools textTools;

	private final ColorManager colorManager;
	private final IPreferenceStore kPreferenceStore;

	public Activator() {
		configurationManager = new ConfigurationManager();
		kPreferenceStore = getPreferenceStore();
		colorManager = ColorManager.instance();
	}
	
	@Override
	public void start(BundleContext context ) throws Exception {
		super.start(context);
		preStart();
		kitt = this;
		Logger.logSystem("kitt startup  ");
		PluginService.instance().initialize();
		ResourcesManager.instance().initialize();
		TransactionCache.instance().initialize();
		initializePreferences();
	}

	private void initializeProjects() {
		Job job = new Job("Indexing workspace") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				TTCNCore.instance().indexInOrder(ResourcesManager.instance().getWorkspaceRoot().getProjects());
				return new Status(IStatus.OK, Activator.PLUGIN_ID, "Indexing complete");
			}
		};
		job.setPriority(Job.LONG);
		job.schedule();
	}

	private void initializePreferences() {
		// For syntax highlight
		kPreferenceStore.setValue(ITTCNColorConstants._TTCN_KEY_WORD, KITTParameter.getTTCNKeyWordDefaultColor());
		kPreferenceStore.getString(ITTCNColorConstants._TTCN_KEY_WORD);
		kPreferenceStore.setValue(ITTCNColorConstants._TTCN_BRACKET, KITTParameter.getTTCNBracketDefaultColor());
		kPreferenceStore.setValue(ITTCNColorConstants._TTCN_OPERATOR, KITTParameter.getTTCNOperatorDefaultColor());
		kPreferenceStore.setValue(ITTCNColorConstants._TTCN_MULTIPLE_LINE_COMMENT,
				KITTParameter.getTTCNMulCommDefaultColor());
		kPreferenceStore.setValue(ITTCNColorConstants._TTCN_SINGLE_LINE_COMMENT,
				KITTParameter.getTTCNSiglCommDefaultColor());
		kPreferenceStore.setValue(ITTCNColorConstants._STRING, KITTParameter.getTTCNStringDefaultColor());
		IPreferenceStore preferenceStore = EditorsPlugin.getDefault().getPreferenceStore();
		preferenceStore.putValue(Helper.generateColorKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE),
				KITTParameter.getAnnotationOccurrenceColorKey());
		preferenceStore.putValue(Helper.generateTextKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "Regular");
		preferenceStore.putValue(Helper.generateRulerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "rulerkey");
		preferenceStore.putValue(Helper.generateRulerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "true");
		preferenceStore.putValue(Helper.generatePresentLayerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "1");
		preferenceStore
				.putValue(Helper.generateAnnotationHighlightKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "true");
		KITTParameter.initialize(kPreferenceStore);
		Logger.initialize();
		try {
			Logger.setLogger(ILogger.DEFAULT_LOGGER);
		} catch (Exception e) {
			//ignore.
		}
	}

	/**
	 * Premain does basic setup works, e.g. setup logger, setup basic
	 * configurations.
	 * */
	private void preStart() {
		KITTParameter.setTTCNKeyWordDefaultColor(Theme.JAVA_LIKE.keyWord);
		KITTParameter.setTTCNBracketDefaultColor(Theme.JAVA_LIKE.brace);
		KITTParameter.setTTCNOperatorDefaultColor(Theme.JAVA_LIKE.operator);
		KITTParameter.setTTCNMulCommDefaultColor(Theme.JAVA_LIKE.mComment);
		KITTParameter.setTTCNSingleLineCommentDefaultColor(Theme.JAVA_LIKE.sComment);
		KITTParameter.setTTCNStringDefaultColor(Theme.JAVA_LIKE.string);
		KITTParameter.setAnnotationOccurrenceColorKey(Theme.JAVA_LIKE.overallAnnotation);
		KITTParameter.setOutlineSortPolicy(SackConstant.OUTLINE_SORT_POLICY_OFFSET);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		kitt = null;
		colorManager.dispose();
		Logger.logSystem("Kitt stop.");
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return kitt;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return ImageHolder.instance().checkoutDescriptor(path);
	}

	public ConfigurationManager getGlobalConfigurationManager() {
		return configurationManager;
	}

	public static Activator instance() {
		return kitt;
	}

	public void registerProjectNavigator(KittProjectNavigatorView kittProjectNavigatorView) {
		projectNavigator = kittProjectNavigatorView;

	}

	public void unregisterProjectNavigator() {
		projectNavigator = null;
	}

	public KittProjectNavigatorView getProjectNavigator() {
		if (Logger.isDebugEnable()) {
			Logger.logDebug(getClass().getSimpleName(),
					"Project navigator is available : " + projectNavigator == null ? "No" : "Yes");
		}
		return projectNavigator;
	}

	public TTCNTextTools getTextTools() {
		if (textTools == null) {
			textTools = new TTCNTextTools(kPreferenceStore, colorManager);
		}
		return textTools;
	}
}
