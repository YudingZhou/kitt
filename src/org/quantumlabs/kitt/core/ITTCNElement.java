package org.quantumlabs.kitt.core;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.quantumlabs.kitt.core.util.Callback;
import org.quantumlabs.kitt.core.util.collection.Pair;

public interface ITTCNElement extends Comparable<ITTCNElement> {

	public static class TTCNElementEvent {
		private final ITTCNElement parentElement; // only TTCN project could
		// have no parentElement.
		private final ITTCNElement[] origins;
		private final ITTCNElement[] news;

		public TTCNElementEvent(ITTCNElement parentElement, ITTCNElement[] orgins, ITTCNElement[] news) {
			this.parentElement = parentElement;
			this.origins = orgins;
			this.news = news;
		}

		public ITTCNElement getAncestor() {
			return parentElement;
		}

		public ITTCNElement[] getOrigin() {
			return origins;
		}

		public ITTCNElement[] getNew() {
			return news;
		}
	}

	int WORKSPACE_ROOT = (int) Math.pow(10, 2);
	int COMPILATION_UNIT = 0;
	int FIELD = 1;
	int IMPORT_DECLARATION = 2;
	int INITIALIZER = 3;
	int LOCAL_VARIABLE = 9;
	int FUNCTION = 10;
	int TYPE = 14;
	int TEMPLATE = 15;
	int COMPONENT = 16;
	int PACKAGE = 17;
	int PROJECT = 18;
	int TEST_CASE = 19;
	int CONTROL_PART = 20;
	int RETURN_EXPR = 21;
	int CONSTANT_DECLARATION = 23;
	int SIGNATURE_DECLARATION = 24;
	int MODULE_PAR_DECLARATION = 25;
	int GROUP_DECLARATION = 26;
	int ATTRIBUTE_DECLARATION = 27;
	int ALTSTEP_DECLARATION = 28;
	int ARGUMENT_DECLARATION_LIST = 29;
	int ARGUMENT_DECLARATION = 30;

	/** Returns whether this TTCN element exists in the model. */
	boolean exists();

	/** Returns the first ancestor of this TTCN element that has the given type. */
	ITTCNElement getAncestor(int ancestorType);

	ITTCNElement getAncestor();

	/**
	 * Returns the TTCNdoc as an html source if this element has an attached
	 * TTCNdoc, null otherwise.
	 */

	String getAttachedTTCNdoc(IProgressMonitor monitor);

	/**
	 * Returns the resource that corresponds directly to this element, or null
	 * if there is no resource that corresponds to this element.
	 */
	IResource getCorrespondingResource();

	/**
	 * Bind a IResource to a TTCN element, if it's capable. <strong>If any, bind
	 * resource to TTCNElement ASAP!</strong> Otherwise, there could be
	 * exception comes to you! - -
	 * */
	void setCorrespondingResource(IResource resource);
	
	void clear();

	/** Returns the name of this element, if any. */
	String getElementName();

	/** Returns this element's kind encoded as an integer. */
	int getElementType();

	/** Returns a string representation of this element handle. */

	String getHandleIdentifier();

	/** Returns the TTCN model. */

	ITTCNElement getTTCNModel();

	/**
	 * Returns the TTCN project this element is contained in, or null if this
	 * element is not contained in any TTCN project (for instance, the
	 * ITTCNModel is not contained in any TTCN project);.
	 */
	// ITTCNProject getTTCNProject();
	/** Returns the first openable parent. */
	// IOpenable getOpenable();
	/**
	 * Returns the element directly containing this element, or null if this
	 * element has no parent.
	 */
	ITTCNElement getParent();

	/** Returns the path to the innermost resource enclosing this element. */
	// IPath getPath();
	/**
	 * Returns the primary element (whose compilation unit is the primary
	 * compilation unit); this working copy element was created from, or this
	 * element if it is a descendant of a primary compilation unit or if it is
	 * not a descendant of a working copy
	 */
	ITTCNElement getPrimaryElement();

	/** Returns the scheduling rule associated with this TTCN element. */
	ISchedulingRule getSchedulingRule();

	/**
	 * Returns the smallest underlying resource that contains this element, or
	 * null if this element is not contained in a resource.
	 */
	IResource getUnderlyingResource();

	/** Returns whether this TTCN element is read-only. */
	boolean isReadOnly();

	/** Returns whether the structure of this element is known. */
	boolean isStructureKnown();

	void addChild(ITTCNElement child);

	ITTCNElement[] getChildren();

	void removeChild(ITTCNElement child);

	void addCallBack(Callback call);

	/**
	 * Notify listeners.
	 */
	void dispose();

	/**
	 * Get the ancestor listener, the listener will be installed to ancestor as
	 * soon as it is added as child to ancestor. Ancestor will fire event to
	 * listener when ancestor makes any change.<br>
	 * Subclass should implement the method to respond to changes of ancestor.
	 * */
	Callback getAncestorListener();

	ParserRuleContext getCorrespondingParserRuleContext();

	/**
	 * Parse a RuleContext into a TTCN element.
	 * */
	void parse(ParserRuleContext context);

	/**
	 * Return pairs with start and end index of error token, if any.
	 * */
	Pair<Integer, Integer>[] getErrors();

	void removeCallBack(Callback callBack);
}