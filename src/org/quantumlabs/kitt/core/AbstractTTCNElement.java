package org.quantumlabs.kitt.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.quantumlabs.kitt.core.util.Callback;
import org.quantumlabs.kitt.core.util.collection.Pair;
import org.quantumlabs.kitt.core.util.trace.Logger;

public abstract class AbstractTTCNElement implements ITTCNElement {

	protected ITTCNElement ancestor;
	protected String name;
	protected List<ITTCNElement> children2;
	protected URI location;
	protected List<Callback> callBacks;
	protected IResource correspondingResource;
	private ParserRuleContext correspondingParserRule;
	protected List<Token> errors;
	/**
	 * Status checking implements <strong>lazy initializing pattern</strong>, all needed
	 * information will be initialized until requested. <br>
	 * The idea is <br>
	 * 1.Dirty status should be <code>true</code> after RuleContext setting.<br>
	 * 2.Any information requests should check dirty status and parse RuleContext
	 * if it is dirty.<br>
	 * 3.After parsing, dirty status back to false until next RuleContext
	 * setting.
	 */
	private boolean dirty = true;

	@Deprecated
	// Since some element doesn't have identifier, so the name doesn't make
	// sense at all.
	public AbstractTTCNElement(ITTCNElement ancestor, String name) {
		this();
		this.name = name;
		setAncestor(ancestor);
	}

	public AbstractTTCNElement() {
		children2 = new ArrayList<ITTCNElement>();
		callBacks = new ArrayList<Callback>();
		errors = new ArrayList<Token>();
	}

	public AbstractTTCNElement(ITTCNElement ancestor) {
		this();
		setAncestor(ancestor);
	}

	@Override
	public void removeCallBack(Callback callBack) {
		callBacks.remove(callBack);
	}

	/**
	 * Return pairs with start and end index of error token, if any.
	 * */
	@Override
	public Pair<Integer, Integer>[] getErrors() {
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer>[] errors = new Pair[this.errors.size()];
		for (int i = 0; i < errors.length; i++) {
			Token error = this.errors.get(i);
			errors[i] = new Pair<Integer, Integer>(error.getTokenIndex(), error.getStopIndex());

		}
		return errors;
	}

	@Override
	public Callback getAncestorListener() {
		return new Callback() {
			@Override
			public Void call(Object... args) {
				if (Logger.isDebugEnable()) {
					Logger.logDebug(String.format("%s,Ancestor listener is called ", AbstractTTCNElement.this
							.getClass().toString()));
				}
				return null;
			}
		}; // Default ancestor listener.does nothing but logging.
	}

	@Override
	public void clear() {
		children2.clear();
	}

	@Override
	public void addCallBack(Callback call) {
		Assert.isNotNull(call);
		callBacks.add(call);
	}

	public ITTCNElement[] getChildren() {
		// Collection<ITTCNElement> values = children.values();
		// return values.toArray(new ITTCNElement[values.size()]);
		return children2.toArray(new ITTCNElement[children2.size()]);
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITTCNElement getAncestor(int ancestorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITTCNElement getAncestor() {
		Assert.isNotNull(ancestor, "Ancestor should not be null!");
		return ancestor;
	}

	public void setAncestor(ITTCNElement element) {
		Assert.isNotNull(element, "Ancestor should not be null!");
		ancestor = element;
		element.addChild(this);
	}

	@Override
	public String getAttachedTTCNdoc(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource getCorrespondingResource() {
		return correspondingResource == null ? getParent().getCorrespondingResource() : correspondingResource;
	}

	@Override
	public ParserRuleContext getCorrespondingParserRuleContext() {
		return correspondingParserRule;
	}

	/**
	 * Call me as soon as the element being instantiated. Especially, before
	 * {@link #addChild(ITTCNElement)}
	 * */
	final public void setCorrespondingParserRuleContext(ParserRuleContext context) {
		correspondingParserRule = context;
		dirty = true;
	}

	@Override
	public void setCorrespondingResource(IResource iResource) {
		correspondingResource = iResource;
	}

	@Override
	public String getElementName() {
		return name;
	}

	@Override
	public abstract int getElementType();

	@Override
	public String getHandleIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITTCNElement getTTCNModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITTCNElement getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITTCNElement getPrimaryElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISchedulingRule getSchedulingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource getUnderlyingResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStructureKnown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(ITTCNElement element) {
		return name.compareTo(element.getElementName());
	}

	@Override
	public void addChild(ITTCNElement element) {
		if (children2.contains(element)) {
			throw new DuplicatedElementException(String.format(
					"Try to add child element to %s, but there is %s already!", this, element));
		}
		children2.add(element);
	}

	@Override
	public void removeChild(ITTCNElement child) {
		Assert.isNotNull(children2.contains(child), String.format("% Should contains %s before removing.", this, child));
		child.removeChild(child);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < callBacks.size(); i++) {
			callBacks.get(i).call(this);
		}
	}

	/**
	 * Default implementation do nothing but binding ParserRuleContext.
	 * */
	@Override
	public void parse(ParserRuleContext context) {
		//throw new UnsupportedOperationException(String.format( "No parsing implementation for %s", getClass().getSimpleName()));
	}

	/**
	 * @param elementTypeId
	 *            TTCN element type ID
	 * @param clazzType
	 *            TTCN element array instance. eg.
	 *            <code>new ConstantDeclaration[0]</code>
	 * */
	protected <T extends ITTCNElement> T[] getChildren(int elementTypeId, T[] clazzType) {
		List<T> collection = new ArrayList<T>();
		ITTCNElement[] children = getChildren();
		collect(collection, children, elementTypeId);
		return collection.toArray(clazzType);
	}

	@SuppressWarnings("unchecked")
	// Iterate an array and collect what you want.
	private <T extends ITTCNElement> void collect(Collection<T> collection, ITTCNElement[] elements, int kind) {
		for (ITTCNElement element : elements) {
			if (element.getElementType() == kind) {
				collection.add((T) element);
			}
		}
	}
	
	//Do not over write!
	final protected void checkDirtyStatus() {
		if (dirty) {
			parse(getCorrespondingParserRuleContext());
			dirty = false;
		}
	}
	
	protected boolean isDirty(){
		return dirty;
	}
}