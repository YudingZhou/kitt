package org.quantumlabs.kitt.core.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public final class SackConstant {

	/**
	 * IMAGE_MAP which hold image name/path entries.
	 * */
	public static final Map<String, String> DEFAULT_IMAGE_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(IMG_PROJECT_NAV_NODE_FILE_IMG_S, "icons/kitt-file-icon_32x32px.png");
			put(IMG_PROJECT_NAV_NODE_FOLDER_IMG_S, "icons/kitt-folder-icon_32x32px.png");
			put(IMG_PROJECT_NAV_NODE_PROJECT_IMG_S, "icons/kitt-project-icon_32x32px.png");
			put(IMG_ACTION_COLLAPSE_ALL_IMG_S, "icons/navigator-collapseall.JPG");
			put(IMG_ACTION_LINK_TO_EDITOR_IMG_S, "icons/navigator-linkedtoeditor.JPG");
			put(IMG_TTCN_ELEMENT_CONTROL_IMG_S, "icons/elements/ttcn-element-control-48x48px.png");
			put(IMG_TTCN_ELEMENT_FUNCTION_IMG_S, "icons/elements/ttcn-element-function-48x48px.png");
			put(IMG_TTCN_ELEMENT_MODULE_IMG_S, "icons/elements/ttcn-element-module-48x48px.png");
			put(IMG_TTCN_ELEMENT_PORT_IMG_S, "icons/elements/ttcn-element-port-48x48px.png");
			put(IMG_TTCN_ELEMENT_TYPE_IMG_S, "icons/elements/ttcn-element-subtype-48x48px.png");
			put(IMG_TTCN_ELEMENT_IMPORT_IMG_S, "icons/elements/ttcn-element-import-48x48px.png");
			put(IMG_TTCN_ELEMENT_TESTCASE_IMG_S, "icons/elements/ttcn-element-testcase-48x48px.png");
			put( IMG_TTCN_ELEMENT_GROUP_IMG_S, "icons/elements/ttcn-element-group-48x48px.png");
			put(IMG_TTCN_ELEMENT_ALTSTEP_IMG_S , "icons/elements/ttcn-element-altstep-48x48px.png");
			put( IMG_TTCN_ELEMENT_SIGNATURE_IMG_S, "icons/elements/ttcn-element-signature-48x48px.png");
			put(IMG_TTCN_ELEMENT_ATTRIBUTE_IMG_S , "icons/elements/ttcn-element-attribute-48x48px.png");
			put(IMG_TTCN_ELEMENT_MODULEPAR_IMG_S , "icons/elements/ttcn-element-modulepar-48x48px.png");
			put(IMG_TTCN_ELEMENT_TEMPLATE_IMG_S , "icons/elements/ttcn-element-template-48x48px.png");
		}
	};

	/**
	 * Component ID
	 * */
	public static final String K_PROJECT_NAV_VIEW = "org.quantumlabs.kitt.projectnavigatorview";

	/**
	 * Image ID.
	 * 
	 * <pre>
	 *  1.add new IMG ID.
	 * 2.add new IMG file to {@link #DEFAULT_IMAGE_MAP} .
	 * </pre>
	 * */
	public static final String IMG_PROJECT_NAV_NODE_FILE_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.file.img";
	public static final String IMG_PROJECT_NAV_NODE_FOLDER_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.folder.img";
	public static final String IMG_PROJECT_NAV_NODE_PROJECT_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.project.img";
	public static final String IMG_PROJECT_NAV_NODE_OTHER_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.other.img";
	public static final String IMG_COLLAPSE_ACTION_IMG_S = "";
	public static final String IMG_ACTION_DELETE_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.delete.img";
	public static final String IMG_ACTION_DEFAULT_EDITOR_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.kitteditor.img";
	public static final String IMG_ACTION_TEXT_EDITOR_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.texteditor.img";
	public static final String IMG_ACTION_OTHER_EDITOR_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.othereditor.img";
	public static final String IMG_ACTION_LINK_TO_EDITOR_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.linktoeditor.img";
	public static final String IMG_ACTION_COLLAPSE_ALL_IMG_S = "org.quantumlabs.kitt.projectnavigatorview.action.collapseall.img";
	public static final String IMG_TTCN_ELEMENT_MODULE_IMG_S = "org.quantumlabs.kitt.ttcnelement.16x16.module";
	public static final String IMG_TTCN_ELEMENT_CONTROL_IMG_S = "org.quantumlabs.kitt.ttcnelement.16x16.control";
	public static final String IMG_TTCN_ELEMENT_FUNCTION_IMG_S = "org.quantumlabs.kitt.ttcnelement.16x16.function";
	public static final String IMG_TTCN_ELEMENT_PORT_IMG_S = "org.quantumlabs.kitt.ttcnelement.16x16.port";
	public static final String IMG_TTCN_ELEMENT_TYPE_IMG_S = "org.quantumlabs.kitt.ttcnelement.16x16.type";
	public static final String IMG_TTCN_ELEMENT_IMPORT_IMG_S = "org.quantumlabs.kitt.ttcnelement.import";
	public static final String IMG_TTCN_ELEMENT_TESTCASE_IMG_S = "org.quantumlabs.kitt.ttcnelement.testcase";
	public static final String IMG_TTCN_ELEMENT_GROUP_IMG_S = "org.quantumlabs.kitt.ttcnelement.group";
	public static final String IMG_TTCN_ELEMENT_ALTSTEP_IMG_S = "org.quantumlabs.kitt.ttcnelement.altstep";
	public static final String IMG_TTCN_ELEMENT_SIGNATURE_IMG_S = "org.quantumlabs.kitt.ttcnelement.signature";
	public static final String IMG_TTCN_ELEMENT_ATTRIBUTE_IMG_S = "org.quantumlabs.kitt.ttcnelement.attribute";
	public static final String IMG_TTCN_ELEMENT_MODULEPAR_IMG_S = "org.quantumlabs.kitt.ttcnelement.modulepar";
	public static final String IMG_TTCN_ELEMENT_TEMPLATE_IMG_S = "org.quantumlabs.kitt.ttcnelement.template";

	/**
	 * Workspace root name
	 * */
	public static final String VIEW_WORKSPACE_ROOT_C = "com.quitus.opensource.kitt.projectnavigatorview.workspace.root";

	/**
	 * Exception message
	 * */
	public static final String MESSAGE_PAIR_INIT_WITH_NULL_EXCEPTION = "Pair doesn't take null as its valeue!";
	public static final String MESSAGE_RESOURCE_LOAD_IMAGE_PAIR_SUCCESS = "Load image name/path pair success : ";
	public static final String MESSAGE_RESOURCE_LOAD_IMAGE_PAIR_FAIL = "Load image name/path pair fail : ";
	public static final String MESSAGE_NEW_TTCN_FILE_NAME = "new_file.ttcn";

	/**
	 * Log messages
	 * */
	public static final String LOG_RESOURCE_LOADING_IMAGE_PAIR = "ProjectTreeContentProvider : initalize tree root";

	/**
	 * <pre>
	 * DATE : %s PID : %s CATEGORY : %s MODEL : %s
	 * TEXT : %s
	 * </pre>
	 */
	public static final String LOG_TEMPLATE_WITHOUTEXCEPTION = "DATE : %s     PID : %s     CATEGORY : %s     MODEL : %s\nTEXT : %s\n\n";

	/**
	 * <pre>
	 * DATE : %s PID : %s CATEGORY : %s MODEL : %s 
	 * TEXT : %s 
	 * EXCEPTION : %s
	 * </pre>
	 * */
	public static final String LOG_TEMPLATE_WITHEXCEPTION = "DATE : %s     PID : %s     CATEGORY : %s     MODEL : %s\nTEXT : %s\nEXCEPTION : %s\n\n";

	/**
	 * Nature
	 * */
	public static final String NATRURE_KITT_PROJECT = "org.quantumlabs.kitt.build.kittnature";

	/**
	 * Other
	 * */
	public static final String C_IMAGE_SUFFIX = "_IMS_S";
	public static final String SEPARATOR_GROUP_IMPORT = "group.import";
	public static final String SEPARATOR_COMMON = "common.separator";
	public static String MENU_POPUPMENU = "#PopupMenu";
	public static final String MISC_DELIMITER = "'";
	public static final String MISC_COMMA = ",";
	public static final TreeNode<IResource> NULL = null;
	public static final String PLUGIN_ID = "org.quantumlabs.kitt";
	public static final String FILE_EXTENSION = ".ttcn";

	/**
	 * Actions
	 * */
	public static final String ACTION_COPY_TEXT = "Copy";
	public static final String ACTION_COLLAPSE_TEXT = "Collapse All";
	public static final String ACTION_COLLAPSE_TOOLTIP = "Collapse All Tooltip";

	/**
	 * navigator menu
	 * */
	public static final String ACTION_PASTE_TEXT = "Paste";
	public static final String ACTION_DELETE_TEXT = "Delete";
	public static final String ACTION_OPEN_WITH_TEXT = "Open with";
	public static final String ACTION_OPEN_WITH_INNER_DEFAULT_TEXT = "Kitt editor";
	public static final String ACTION_OPEN_WITH_INNER_OTHER_TEXT = "Other editor";
	public static final String ACTION_OPEN_WITH_INNER_TEXT_TEXT = "Text editor";
	public static final String ACTION_BUILD_PATH_TEXT = "Build path";
	public static final String ACTION_PROPERTIES_TEXT = "Properties";
	public static final String ACTION_RUN_AS_TEXT = "Run as";
	public static final String ACTION_DEBUG_AS_TEXT = "Debug as";
	public static final String ACTION_NEW_TEXT = "New File";
	public static final String ACTION_NEW_MENU_TEXT = "New";
	public static final String ACTION_LINK_TO_EDITOR_TEXT = "Link with editor";
	public static final String ACTION_LINK_TO_EDITOR_TOOLTIP = "Link to editor tooltip";
	public static final String ACTION_GROUP_IMPORT_REORGANIZE = "group.reorganize";
	public static final String ACTION_GROUP_IMPORT_ACTION = "group.import";
	public static final String ACTION_GROUP_EXPORT_ACTION = "group.export";
	public static final String ACTION_COLLAPSE_ALL_TEXT = "Collapse all";
	public static final String ACTION_COLLAPSE_ALL_TOOLTIP_TEXT = "Collapse all";

	/**
	 * Dialog
	 * */
	public static final Object DIALOG_CONFIRM_DELETE_MESSAGE = "Do you want to delete";
	public static final String DIALOG_CONFIRM_DELETE_TITLE = "Delete";
	public static final String LABEL_BUTTON_OK = "OK";
	public static final String LABEL_BUTTON_CANCEL = "Cancel";
	public static final int INDEX_BUTTON_LABEL_OK = 0;

	public static final String ACTION_DELETE_TOOLTIP = "Delete selected resources.";

	/**
	 * Jobs
	 * */
	public static final String JOB_DELETE_RESOURCE = "Deleting resources.";

	public enum SYSTEM_MESSAGE {
		ACTION_DELETE_RESOURCE_FAILURE("delete resource failed.");
		private final String message;

		private SYSTEM_MESSAGE(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	/**
	 * Editor
	 * */
	public static final String EDITOR_TTCN_ID_STRING = "org.quantumlabs.kitt.editor";

	/**
	 * Marker
	 * */
	public static final int MARKER_ANNOTATION_INIT_MARKER_COUNT = 10;

	public static final String DOCUMENT_PARTITIONER_ID = "___TTCN_PARTITIONING";

	public static final String REGULAR_PARTTEN_ANY_ONE = "*.";

	/**
	 * Masks
	 * */
	public static final int MASK_FIRST_BIT = 0X0000000F;

	public static final String ANNOTATION_TYPE_OCCURRENCE = "org.quantumlabs.editor.annotation.occurrence";

	public static final String CORE_LOCATION_PROTOCOL = "kitt:///";

	public static final int OUTLINE_SORT_POLICY_ALPHABETIC = 1;

	public static final int OUTLINE_SORT_POLICY_OFFSET = 2;

	public static final int RULE_CONTEXT_DEPTH_TLD = 3;

	public static final String PREFERENCE_LOG_LEVEL = "kitt.loglevel";
}
