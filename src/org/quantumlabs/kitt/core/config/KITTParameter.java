package org.quantumlabs.kitt.core.config;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.collection.Pair;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.editors.KeywordCompletionProcessor;
import org.quantumlabs.kitt.ui.text.ITTCNColorConstants;

public class KITTParameter {
	
	private static IPreferenceStore store;

	/**
	 * Whether show all project in navigator which are not only KITT project but
	 * also others.
	 * */
	private static boolean CONFIG_SHOW_ONLY_KITT_PROJECTS = false;

	/**
	 * This variable means that current software is beta release, enable it will
	 * cause different behavior , e.g. exception handling level etc..
	 * */
	private static String BETA = "kitt.beta_model";

	/**
	 * Logger Level.
	 * */
	private static String LOG_LEVEL = "kitt.loglevel";

	/**
	 * Left a interface for coming image adding, deleting operation. suppose it
	 * can be used for user customized image changing. <strong>If any image
	 * changed, this flag should be false, then image holder could load image
	 * again.</strong>
	 * */
	private static boolean imageUpdated = false;
	private static Set<Pair<String, String>> customizedImages = new HashSet<Pair<String, String>>();

	/**
	 * This parameter means whether the action running delegate is running is
	 * background or current thread.
	 * */
	private static boolean ACTION_RUNNING_IN_OTHER_THREAD;

	private static String TTCN_KEYWORD_DEFAULT_COLOR;

	private static String TTCN_BRACKET_DEFAULT_COLOR;

	private static String TTCN_OPERATOR_DEFAULT_COLOR;

	private static String TTCN_MULTIPLE_LINE_COMMENT_DEFAULT_COLOR;

	private static String TTCN_SINGLE_LINE_COMMENT_DEFAULT_COLOR;

	private static String TTCN_STRING_DEFAULT_COLOR;

	private static String OUTLINE_SELECTION_HIGHLIGHT_BACKGROUND_COLOR = "51,153,255";

	private static String OUTLINE_SELECTION_HIGHLIGHT_FRONT_COLOR = "255,255,255";

	private static String OUTLINE_FONT = JFaceResources.TEXT_FONT;

	/**
	 * Content assistant trigger
	 * */
	private static char[] DEFAULT_KEYWORD_ASSIST_TRIGGER = KeywordCompletionProcessor.DEFAULT_TRIGGER;

	private static boolean CONTENT_ASSISTANT_ENABLED = true;

	private static int CONTENT_ASSISTANT_ACTIVATION_DELAY = 100;// default is
	// 0.1s

	private static int CONTEXT_INFORMATION_POPUP_ORIENTATION = IContentAssistant.CONTEXT_INFO_ABOVE;

	private static int PROPOSAL_POPUP_ORIENTATION = IContentAssistant.PROPOSAL_STACKED;

	private static String CONTEXT_INFORMATION_POPUP_BACKGROUND = "75,120,230";

	private static boolean CONTEXT_ASSISTANT_STRICT_MATCHING;
	/**
	 * For presentation in overview ruler
	 * */
	private static String ANNOTATION_OCCURRENCE_COLOR_KEY;
	/**
	 * For presentation in overview ruler
	 * */
	private static String ANNOTATION_OCCURRENCE_OVERVIEW_RULER_KEY;
	/**
	 * For presentation in overview ruler
	 * */
	private static String ANNOTATION_OCCURRENCE_TEXT_KEY;
	/**
	 * For presentation in overview ruler
	 * */
	private static int ANNOTATION_OCCURRENCE_PRESENTATION_LAYER;

	private static boolean MODULE_LAZAY_LOADING;

	/**
	 * Use a shared parser to parse all input source. Default implementation is
	 * <code>true</code>.
	 * */
	private static boolean SHARED_PARSER = true;

	/**
	 * If value is true, any text change in TTCNEditor should be reflected on
	 * corresponding TTCNElement.Otherwise, TTCNElement will be updated after
	 * text change has been saved.
	 * */
	// asdasd
	private static boolean TTCN_ELEMENT_UPDATE_TIME_TO_TIME = false;

	public enum Theme {
		JAVA_LIKE("130,0,85",  "65,130,95","63,95,191", "0,0,0", "0,0,0", "42,0,255","0,64,0");
		Theme(String keyWord, String sComment, String mComment, String operator, String brace, String string,String overallAnnotation) {
			this.keyWord = keyWord;
			this.mComment = mComment;
			this.sComment = sComment;
			this.operator = operator;
			this.brace = brace;
			this.string = string;
			this.overallAnnotation = overallAnnotation;
		}

		public String keyWord;
		public String mComment;
		public String operator;
		public String brace;
		public String string;
		public String sComment;
public String overallAnnotation;
	}

	/**
	 * Indication of how elements will be sorted in content outline.
	 * */
	private static int OUTLINE_SORT_POLICY = -1;;

	/**
	 * For presentation in overview ruler
	 * */
	public static boolean isACTION_RUNNING_IN_OTHER_THREAD() {
		return ACTION_RUNNING_IN_OTHER_THREAD;
	}

	static {

	}

	static void addCustomizeImage(String name, String path) {
		if (customizedImages.add(new Pair<String, String>(name, path))) {
			imageUpdated = true;
		}
	}

	public static Set<Pair<String, String>> getCustomizedImages() {
		return customizedImages;
	}

	/**
	 * This method is called for some private call back operation. For instance,
	 * after {@code ResourcesManager} updated images, it calls this method for
	 * reseting {@code imageUpdated} flag.
	 * 
	 * @param adapter
	 *            a related class which can be adapted.
	 */
	public static void callBack(Class<?> adapter) {
		if (Logger.isTraceEnable()) {
			Logger.logTrace("KITTParameter#callback->adapter: " + adapter.getName());
		}
		if (adapter == ImageHolder.class) {
			imageUpdated = false;
		}
		// TODO: else if for other adapters...
		else {
			if (Logger.isDebugEnable()) {
				Logger.logDebug("Unkown adapter for KITTParameter#callback : " + adapter.getName());
			}
		}
	}

	public static void initialize(IPreferenceStore preferenceStore) {
		store = preferenceStore;
//		// for annotation view showing in overview rule. {@see
//		// TTCNEditor.hightlightOccurrence()}
//		preferenceStore.putValue(Helper.generateColorKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "100,250,125");
//		preferenceStore.putValue(Helper.generateTextKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "Regular");
//		preferenceStore.putValue(Helper.generateRulerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "rulerkey");
//		preferenceStore.putValue(Helper.generateRulerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "true");
//		preferenceStore.putValue(Helper.generatePresentLayerKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "1");
//		preferenceStore
//				.putValue(Helper.generateAnnotationHighlightKey(SackConstant.ANNOTATION_TYPE_OCCURRENCE), "true");
//		// For syntax highlight
//		preferenceStore.setValue(ITTCNColorConstants._TTCN_KEY_WORD, KITTParameter.getTTCNKeyWordDefaultColor());
//		preferenceStore.getString(ITTCNColorConstants._TTCN_KEY_WORD);
//		preferenceStore.setValue(ITTCNColorConstants._TTCN_BRACKET, KITTParameter.getTTCNBracketDefaultColor());
//		preferenceStore.setValue(ITTCNColorConstants._TTCN_OPERATOR, KITTParameter.getTTCNOperatorDefaultColor());
//		preferenceStore.setValue(ITTCNColorConstants._TTCN_MULTIPLE_LINE_COMMENT,
//				KITTParameter.getTTCNMulCommDefaultColor());
//		preferenceStore.setValue(ITTCNColorConstants._TTCN_SINGLE_LINE_COMMENT,
//				KITTParameter.getTTCNSiglCommDefaultColor());
//		preferenceStore.setValue(ITTCNColorConstants._STRING, KITTParameter.getTTCNStringDefaultColor());
	}

	public static String getTTCNStringDefaultColor() {
		return TTCN_STRING_DEFAULT_COLOR;
	}

	public static void setTTCNStringDefaultColor(String value) {
		TTCN_STRING_DEFAULT_COLOR = value;
	}

	public static boolean isImageUpdated() {
		return imageUpdated;
	}

	public static void setImageUpdated(boolean imageUpdated) {
		KITTParameter.imageUpdated = imageUpdated;
	}

	public static int getLogLevel() {
		return store.getInt(LOG_LEVEL);
	}

	public static void setLogLevel(int logLevel) {
		store.setValue(LOG_LEVEL, logLevel);
		Logger.initialize();
	}

	public static boolean isBETA() {
		return store.getBoolean(BETA);
	}

	public static void setBETA(boolean bETA) {
		store.setValue(BETA, bETA);
	}

	public static boolean isCONFIG_SHOW_ONLY_KITT_PROJECTS() {
		return CONFIG_SHOW_ONLY_KITT_PROJECTS;
	}

	public static void setCONFIG_SHOW_ONLY_KITT_PROJECTS(boolean cONFIG_SHOW_ONLY_KITT_PROJECTS) {
		CONFIG_SHOW_ONLY_KITT_PROJECTS = cONFIG_SHOW_ONLY_KITT_PROJECTS;
	}

	public static void setTTCNKeyWordDefaultColor(String color) {
		TTCN_KEYWORD_DEFAULT_COLOR = color;
	}

	public static void setTTCNBracketDefaultColor(String color) {
		TTCN_BRACKET_DEFAULT_COLOR = color;
	}

	public static void setTTCNOperatorDefaultColor(String color) {
		TTCN_OPERATOR_DEFAULT_COLOR = color;
	}

	public static String getTTCNKeyWordDefaultColor() {
		return TTCN_KEYWORD_DEFAULT_COLOR;
	}

	public static String getTTCNBracketDefaultColor() {
		// TODO Auto-generated method stub
		return TTCN_BRACKET_DEFAULT_COLOR;
	}

	public static String getTTCNOperatorDefaultColor() {
		// TODO Auto-generated method stub
		return TTCN_OPERATOR_DEFAULT_COLOR;
	}

	public static String getTTCNMulCommDefaultColor() {
		return TTCN_MULTIPLE_LINE_COMMENT_DEFAULT_COLOR;
	}

	public static String getTTCNSiglCommDefaultColor() {
		return TTCN_SINGLE_LINE_COMMENT_DEFAULT_COLOR;
	}

	public static void setTTCNSingleLineCommentDefaultColor(String color) {
		TTCN_SINGLE_LINE_COMMENT_DEFAULT_COLOR = color;
	}

	public static void setTTCNMulCommDefaultColor(String color) {
		TTCN_MULTIPLE_LINE_COMMENT_DEFAULT_COLOR = color;
	}

	public static String getSelectionHighlightBackgroundStyle() {
		return OUTLINE_SELECTION_HIGHLIGHT_BACKGROUND_COLOR;
	}

	public static void setSelectionHighlightBackgroundStyle(String color) {
		OUTLINE_SELECTION_HIGHLIGHT_BACKGROUND_COLOR = color;
	}

	public static String getSelectionHighlightFrontStyle() {
		return OUTLINE_SELECTION_HIGHLIGHT_FRONT_COLOR;
	}

	public static void setSelectionHighlightFrontStyle(String color) {
		OUTLINE_SELECTION_HIGHLIGHT_FRONT_COLOR = color;
	}

	public static String getOutlineFont() {
		return OUTLINE_FONT;
	}

	public static void setOutlineFont(String font) {
		OUTLINE_FONT = font;
	}

	public static char[] getKeywordCompletionProposalActivationChars() {
		return DEFAULT_KEYWORD_ASSIST_TRIGGER;
	}

	public static void setKeywordCompletionProposalActivationChars(char... c) {
		DEFAULT_KEYWORD_ASSIST_TRIGGER = c;
	}

	public static boolean isContentAssistantAutoActivationEnabled() {
		return CONTENT_ASSISTANT_ENABLED;
	}

	public static void setContentAssistantAutoActivationEnabled(boolean enabled) {
		CONTENT_ASSISTANT_ENABLED = enabled;
	}

	public static int getProposalPopupOrientation() {
		return PROPOSAL_POPUP_ORIENTATION;
	}

	public static void setProposalPopupOrientation(int value) {
		PROPOSAL_POPUP_ORIENTATION = value;
	}

	public static void setContextInformationPopupOrientation(int value) {
		CONTEXT_INFORMATION_POPUP_ORIENTATION = value;
	}

	public static void setContextInformationPopupBackground(String value) {
		CONTEXT_INFORMATION_POPUP_BACKGROUND = value;
	}

	public static int getContextInformationPopupOrientation() {
		return CONTEXT_INFORMATION_POPUP_ORIENTATION;
	}

	public static String getContextInformationPopupBackground() {
		return CONTEXT_INFORMATION_POPUP_BACKGROUND;
	}

	public static boolean isStrictMatching() {
		return CONTEXT_ASSISTANT_STRICT_MATCHING;
	}

	public static void setStrictMatching(boolean value) {
		CONTEXT_ASSISTANT_STRICT_MATCHING = value;
	}

	public static String getAnnotationOccurrenceColorKey() {
		return ANNOTATION_OCCURRENCE_COLOR_KEY;
	}
	
	public static void setAnnotationOccurrenceColorKey(String value) {
		ANNOTATION_OCCURRENCE_COLOR_KEY = value;
	}

	public static String getAnnotationOccurrenceTextKey() {
		return ANNOTATION_OCCURRENCE_TEXT_KEY;
	}

	public static String getAnnotationOccurrenceOverviewRulerKey() {
		return ANNOTATION_OCCURRENCE_OVERVIEW_RULER_KEY;
	}

	public static int getAnnotationOccurrencePresentationLayer() {
		return ANNOTATION_OCCURRENCE_PRESENTATION_LAYER;
	}

	public static int getContentAssistantAutoActivationDelay() {
		return CONTENT_ASSISTANT_ACTIVATION_DELAY;
	}

	public static void setContentAssistantAutoActivationDelay(int millis) {
		CONTENT_ASSISTANT_ACTIVATION_DELAY = millis;
	}

	public static boolean isModuleLazayInitializing() {
		return MODULE_LAZAY_LOADING;
	}

	public static void setModuleLazayInitializing(boolean value) {
		MODULE_LAZAY_LOADING = value;
	}

	public static boolean isSharedParser() {
		return SHARED_PARSER;
	}

	public static void setSharedParser(boolean value) {
		SHARED_PARSER = value;
	}

	public static boolean isElementUpdateOnTime() {
		return TTCN_ELEMENT_UPDATE_TIME_TO_TIME;
	}

	public void setElementUpdateOnTime(boolean value) {
		TTCN_ELEMENT_UPDATE_TIME_TO_TIME = value;
	}

	public static int getOutlineSortPolicy() {
		return OUTLINE_SORT_POLICY;
	}

	public static void setOutlineSortPolicy(int value) {
		OUTLINE_SORT_POLICY = value;
	}

	public static void save() {
		// TODO Auto-generated method stub
		
	}
}
