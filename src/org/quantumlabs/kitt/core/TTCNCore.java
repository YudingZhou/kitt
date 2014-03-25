package org.quantumlabs.kitt.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.ResourcesManager;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;
import org.quantumlabs.kitt.ui.util.exception.Warning;

/**
 * 1. provide listening service to IResource and ITTCNElement.<br>
 * 2. provide IDocument indexing service.
 * */
public class TTCNCore {

	private HashMap<IProject, IModulePathConfiguration> configs;
	private static TTCNCore instance;
	private final Map<Object, Info> infos;// currently, there are two kinds of
	// keys:IPath, IDocument
	private final static Object lock = new Object();

	private final TTCNCoreIndex coreIndex;
	private final TTCNCoreService coreService;

	private final List<IResource> failures;

	public TTCNCore() {
		infos = new HashMap<Object, Info>();
		coreService = new TTCNCoreService();
		coreIndex = new TTCNCoreIndex();
		coreService.start();
		configs = new HashMap<IProject, IModulePathConfiguration>();
		failures = new ArrayList<IResource>();
	}

	class Info {
		IDocumentIndex tIndex;
		ITTCNElement model;
		IResource resource;
	}

	static {
		// DUMMY = new CompilationUnit(null, "dummy");
		// Type type = new Type(DUMMY, "DNSQuery type", false, IType.RECORD);
		// Function function = new Function(DUMMY, type, type,
		// "dummy function");
		// DUMMY.addType(type);
		// DUMMY.addFunctions(function);
	}

	public TTCNCoreIndex getCoreIndex() {
		return coreIndex;
	}

	private TTCNCoreService getCoreService() {
		return coreService;
	}

	/**
	 * Build whole resources of given project.
	 * */
	public void initializeProject(IModulePathConfiguration configuration) {
		IResource prjctRsrc = configuration.getProject();
		getCoreIndex().setupIndex(prjctRsrc);
		initializeSources(configuration.getSource());
		initializeResources(configuration.getResource());
		initializeDependency(configuration.getDependency());
	}

	private void initializeDependency(IResource[] dependency) {
		// TODO Auto-generated method stub

	}

	private void initializeResources(IFile[] ttcnResourceFolder) {
		// TODO Auto-generated method stub

	}

	private void initializeSources(IFile[] sources) {
		for (IFile member : sources) {
			try {
				if (member instanceof IFile && ((IFile) member).getName().endsWith(".ttcn")) {
					getCoreIndex().setupIndex(member);
				}
			} catch (Exception e) {
				if (Logger.isErrorEnable()) {
					Logger.logError(toString(), e, e.getMessage(),
							String.format("Fail to setup index %s", member.getLocation().toFile()));
				}
				handleError(e, member);
			}
		}
	}

	// Current error handling is recording failure and trying to re-index at the
	// end.
	private void handleError(Exception e, IResource source) {
		recordFailure(source);
	}

	private void recordFailure(IResource source) {
		failures.add(source);
	}

	public static TTCNCore instance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new TTCNCore();
			}
			return instance;
		}
	}

	/**
	 * Current, there is a very big risk, which these info might be out of data.
	 * I'm not quite sure that, let's see.
	 * */
	private Info retrieveInfo(Object key) {
		// TODO, there should be a better way to uniform the key!
		Info info = null;
		if (key instanceof IPath) {
			info = infos.get(key.toString());
		} else if (key instanceof IDocument) {
			info = infos.get(key);
			// It should be indexed already before fetching Info by IDocument.
		} else if (key instanceof IResource) {
			info = infos.get(key = ((IResource) key).getFullPath().toString());
		} else {
			throw new IllegalArgumentException(String.format(
					"TTCNCore.getAdaptedCache():no match infos found by the give key :%s !", key));
		}

		if (info == null) {
			info = new Info();
			infos.put(key, info);
		}

		return info;
	}

	/**
	 * Remove info should be used only internally, every removing key should be
	 * valid!
	 * 
	 * @throw IllegalArgumentException If key is not valid.
	 */
	private void removeInfo(Object key) {
		Object theKey = null;
		if (key instanceof IPath) {
			theKey = key.toString();
		} else if (key instanceof IResource) {
			theKey = ((IResource) key).getFullPath().toString();
		} else if (key instanceof String) {
			theKey = (String) key;
		} else if (key instanceof IDocument) {
			theKey = (IDocument) key;
		}

		if (infos.remove(theKey) == null) {
			throw new IllegalArgumentException(String.format("Invalid info key : %s", key));
		}
	}

	// TTCNCoreService provides a service that bind IResource and ITTCNElement.
	// Any resource or document level change will be notified.
	class TTCNCoreService {
		private final TTCNResourceListener ttcnRsrcListenSrvc;
		private HashMap<IDocument, List<IDocumentListener>> listeners;

		public TTCNCoreService() {
			ttcnRsrcListenSrvc = new TTCNResourceListener();
			listeners = new HashMap<IDocument, List<IDocumentListener>>();
		}

		/**
		 * Resource change will be notified after connecting.
		 * */
		public void connect(IResource iResource) {
			ttcnRsrcListenSrvc.listen(iResource);
		}

		/**
		 * Try to associate IDocument and ITTCNElement, any change of IDocument
		 * will be reflected on ITTCNElement. <br>
		 * <strong> Since reconciler can't work correctly, don't use this method
		 * at this moment. Use {@link #connect3(IDocument, ITTCNElement)}
		 * instead.</strong>
		 * */
		@SuppressWarnings("serial")
		@Deprecated
		// For this moment
		public void connect2(IDocument doc, ITTCNElement element) {
			final IDocumentListener reconciler = createElementReconciler(element);
			if (listeners.get(doc) == null) {
				listeners.put(doc, new ArrayList<IDocumentListener>() {
					{
						add(reconciler);
					}
				});
			}
			doc.addDocumentListener(reconciler);
		}

		/**
		 * Associate IDocument and ITTCNElement, any change of IDocument will
		 * trigger <strong>re-parse ITTCNElement of whole file</strong>. Rebuild
		 * whole content, it takes pretty much more time.
		 * {@link #connect2(IDocument, ITTCNElement)}
		 * */
		@SuppressWarnings("serial")
		public void connect3(IDocument doc, ITTCNElement element) {
			final IDocumentListener contentRebuilder = createContentRebuilder();
			doc.addDocumentListener(contentRebuilder);
			if (listeners.get(doc) == null) {
				listeners.put(doc, new ArrayList<IDocumentListener>() {
					{
						add(contentRebuilder);
					}
				});
			} else {
				listeners.get(doc).add(contentRebuilder);
			}
		}

		private IDocumentListener createContentRebuilder() {
			return new IDocumentListener() {
				@Override
				public void documentChanged(DocumentEvent event) {

				}

				@Override
				public void documentAboutToBeChanged(DocumentEvent event) {
					IFile resource = (IFile) association.get(event.getDocument());
					try {
						if (KITTParameter.isElementUpdateOnTime()) {
							getCoreIndex().reSetupIndexWithContent(resource,
									event.getDocument().get(0, event.getDocument().getLength()));
						}
					} catch (BadLocationException e) {
						throw new StackTracableException(e);
					}
				}
			};
		}

		protected IDocumentListener createElementReconciler(final ITTCNElement element) {
			return new TTCNElementReconciler(element);
		}

		class ReconcilerContext {
			ITTCNElement rootElement;
			ITTCNElement affectedParent;
			int damageStart;
			int damageLen;
		}

		/**
		 * Don't use for this moment. damage() method is not capable to compute
		 * proper damaged range.
		 */
		@Deprecated
		class TTCNElementReconciler implements IDocumentListener {
			ReconcilerContext context;

			TTCNElementReconciler(ITTCNElement element) {
				context = new ReconcilerContext();
				context.rootElement = element;
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
				damage(event, context);
			}

			@Override
			public void documentChanged(DocumentEvent event) {
				repair(event, context);
			}

			public void damage(DocumentEvent event, ReconcilerContext context) {
				int offset = event.getOffset();
				context.damageStart = offset;
				int damagedLen = event.getLength();
				context.damageLen = damagedLen;
				List<ITTCNElement> damagedElements = computeDamagedElements(context.rootElement, offset, damagedLen);
				context.affectedParent = findParent(damagedElements);
				if (context.affectedParent == null) {
					if (Logger.isWarningEnable()) {
						Logger.logWarning(getClass().toString(),
								"No damanged element found, return null. CoreParser is gonna to re-parse whole file.");
					}
					// Since can't find affected parent element. Parser has to
					// re-parse whole file. It may take time, i'm sorry. :-(
					// @See findParent(), computeDamagedElements().
					context.affectedParent = context.rootElement;
				}
			}

			private ITTCNElement findParent(List<ITTCNElement> damagedElements) {
				// It is possible that no damaged elements found, since the
				// damaged content contains no valid ttcn element.
				if (damagedElements.size() > 0) {
					// 1.If through, potentially, there are more than one
					// damaged
					// elements, but it is sure that those damaged elements
					// should
					// belong to one parent.
					return damagedElements.get(0).getParent();
				}
				return null;
			}

			private List<ITTCNElement> computeDamagedElements(ITTCNElement element, int offset, int damagedLen) {
				List<ITTCNElement> damagedElements = new ArrayList<ITTCNElement>();
				// FIXME BUG :: 3rd.MARCH.2014 :: If change arise in any top
				// level
				// tokens, how to handle?
				ITTCNElement[] children = element.getChildren();
				for (ITTCNElement child : children) {
					Assert.isTrue(child instanceof TextBasedTTCNElement,
							String.format("%s should be text based ttcn element!", child));
					TextBasedTTCNElement tElement = (TextBasedTTCNElement) child;
					int elemtOffset = tElement.getOffset();
					int elemtLen = tElement.getLen();
					// If there is any overlap between existing element and
					// damaged
					// range, the element should be treated as damaged.
					if ((elemtOffset + elemtLen >= offset && elemtOffset + elemtLen < offset + damagedLen)
							|| (elemtOffset > offset && elemtOffset <= offset + damagedLen)) {
						damagedElements.add(tElement);
					}
				}

				// If only one element is damaged, which points out there is a
				// potential that there is a specific child node damaged.
				// Try to find a deeper and precise child which is damaged.
				if (damagedElements.size() == 1 && damagedElements.get(0).getChildren().length > 1) {
					List<ITTCNElement> deeperDamagedElements = computeDamagedElements(damagedElements.get(0), offset,
							damagedLen);
					// Insure, there is deeper level elements can be found.
					damagedElements = deeperDamagedElements;
				}

				return damagedElements;
			}

			public void repair(DocumentEvent event, ReconcilerContext context) {
				String newContent = null;
				try {
					newContent = contentAboutToParse(event, context);
					if (newContent.length() > 0) {
						CharStream streamAboutToParse = CoreParser.openStream(newContent);
						int size = streamAboutToParse.size();
						while (streamAboutToParse.index() < size) {
							ParserRuleContext ruleContext = getCoreIndex().getParser().parse(streamAboutToParse,
									context.affectedParent.getCorrespondingParserRuleContext().getClass());
							AbstractTTCNElement newChild = (AbstractTTCNElement) getAdaptedTTCNElement(context.affectedParent, ruleContext);
							newChild.setCorrespondingParserRuleContext(ruleContext);
							context.affectedParent.addChild(newChild);
						}
					}
				} catch (IOException e) {
					handleError(e, newContent);
				} catch (BadLocationException e) {
					handleError(e, newContent);
				}
			}

			private void handleError(Exception e, String content) {
				if (Logger.isErrorEnable()) {
					Logger.logError(
							String.format(
									"Oops! I'm trying to open a CharStream from a String(%s)! I have to re-parse whole file, it may take time.",
									content), e);
				}
				// Discard old element, since it has been modified already and
				// can't be updated.
				TTCNCore.instance().getCoreIndex().tearDownIndex(context.rootElement.getCorrespondingResource());
				// Re-parse the resource.
				TTCNCore.instance().getCoreIndex().setupIndex(context.rootElement.getCorrespondingResource());
			}

			private ITTCNElement getAdaptedTTCNElement(ITTCNElement parent, RuleContext ruleContext) {
				Class<? extends ITTCNElement> adaptedElement = CoreParser.RULE_ELEMENT_MAPPTING.get(ruleContext
						.getClass());
				try {
					return adaptedElement.getConstructor(ITTCNElement.class).newInstance(parent);
				} catch (Exception e) {
					throw new UnsupportedOperationException("I'm trying to load new TTCN element! : (", e);
				}
			}

			private String contentAboutToParse(DocumentEvent event, ReconcilerContext context)
					throws BadLocationException {
				String newContext = event.getText();
				int addedLen = newContext.length();
				return addedLen == 0 ? "" : event.getDocument().get(context.damageStart, addedLen);
			}
		}

		/**
		 * Disconnect while Editor is closing.
		 * */
		public void disconnect(IDocument doc, ITTCNElement element) {
			if (listeners.containsKey(doc)) {
				for (IDocumentListener listener : listeners.remove(doc)) {
					listeners.remove(listener);
				}
				removeInfo(doc);
			}
		}

		/**
		 * Disconnect while resource becomes not available or is removed from
		 * TTCN sources.
		 * */
		public void disconnect(IResource ttcnRsrc) {
			ttcnRsrcListenSrvc.unListen(ttcnRsrc);
			removeInfo(ttcnRsrc);
		}

		public void start() {
			ttcnRsrcListenSrvc.start();
		}

	}

	// 1.register resources to the monitor, so any change of the resource
	// could be noticed by TTCN core.
	// 2.TTCN Core will be only interesting in PRE_DELETE, POST_CHANGE event
	// currently, so that TTCN could apply resource changes to TTCN Module
	// properly according to DELETE or POST_CHANGE(changing come from out of
	// eclipse).
	// 3. Listener collect changes of resources, buffer it. Then notify TTCN
	// core which resource changed.
	class TTCNResourceListener {
		private IResourceDeltaVisitor preDeleteVisitor;
		private IResourceDeltaVisitor postChangeVisitor;
		private Set<IResource> ttcnResources;

		public void start() {
			ttcnResources = new HashSet<IResource>();
			addListener(new IResourceChangeListener() {
				@Override
				public void resourceChanged(IResourceChangeEvent event) {
					accept(event, IResourceChangeEvent.PRE_DELETE);
				}
			}, IResourceChangeEvent.PRE_DELETE);
			addListener(new IResourceChangeListener() {
				@Override
				public void resourceChanged(IResourceChangeEvent event) {
					accept(event, IResourceChangeEvent.POST_CHANGE);
				}
			}, IResourceChangeEvent.POST_CHANGE);
		}

		// This method can be used by configuring module path dynamically.
		// E.g. "Include as resource"
		public void listen(IResource ttcnResource) {
			ttcnResources.add(ttcnResource);
		}

		// This method can be used by configuring module path dynamically.
		// E.g."Exclude as resource".
		public void unListen(IResource resource) {
			ttcnResources.remove(resource);
		}

		private void addListener(IResourceChangeListener listener, int category) {
			ResourcesManager.instance().addResourceChangeListener(listener, category);
		}

		private void accept(IResourceChangeEvent event, int category) {
			try {
				event.getDelta().accept(getVisitor(category));
			} catch (CoreException e) {
				if (KITTParameter.isBETA()) {
					throw new StackTracableException("Unhandled exception while accepting TTCN source change : "
							+ category, e);
				}
			}
		}

		// 1. If true returned from visitor.visit(), it will visit children
		// recursively.
		// 2. For current implementation, only false returned directly in first
		// invocation of visitor.visit(), in case the resource is a container,
		// all affected children should be returned immediately.
		private IResourceDeltaVisitor getVisitor(int category) {
			if (IResourceChangeEvent.PRE_DELETE == category) {
				return preDeleteVisitor == null ? preDeleteVisitor = createPreDeleteVisitor() : preDeleteVisitor;
			} else if (IResourceChangeEvent.POST_CHANGE == category) {
				return postChangeVisitor == null ? postChangeVisitor = createPostChangeVistor() : postChangeVisitor;
			} else {
				throw new StackTracableException("Unsupported TTCN resrouce change event! " + category);
			}
		}

		private IResourceDeltaVisitor createPostChangeVistor() {
			return new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					return handleResourceDelta(delta);
				}
			};
		}

		// 1. ADDED or REMOVED will be handled directly.
		// 2. CHANGED of IFile will be handled directly.
		// 3. CHANGED Of IContainer will be ignored.
		private boolean handleResourceDelta(IResourceDelta delta) {
			boolean retrn = false;
			IResource ttcnRsrc = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				doHandleResource(ttcnRsrc, IResourceDelta.ADDED);
				break;
			case IResourceDelta.REMOVED:
				doHandleResource(ttcnRsrc, IResourceDelta.REMOVED);
				break;
			case IResourceDelta.CHANGED:
				if (IResource.FILE == delta.getResource().getType()) {
					doHandleResource(ttcnRsrc, IResourceDelta.CHANGED);
				} else {
					// recursive to affected children.
					retrn = true;
				}
				break;
			default:
				throw new StackTracableException(String.format("Unsupported IResourceDelta kind : %s", delta.getKind()));
			}
			return retrn;
		}

		// Jan.9 2014.R.1.0.0:no more processing but add core even to pipe based
		// on changing type.
		private void doHandleResource(final IResource ttcnRsrc, int changeType) {
			if (!ttcnResources.contains(ttcnRsrc)) {
				return;
			}
			IPath fPath = ttcnRsrc.getFullPath();
			switch (changeType) {
			case IResourceDelta.ADDED:
				getCoreIndex().setupIndex(ttcnRsrc);
				break;
			case IResourceDelta.REMOVED:
				getCoreIndex().tearDownIndex(ttcnRsrc);
				break;
			case IResourceDelta.CHANGED:
				// Since rebuild may take time, run it in other thread
				// TODO : Try to consider interrupting build sequential user
				// operation. Basic idea will be interrupt current thread and
				// run the newest one.
				Helper.runJob(new Runnable() {
					@Override
					public void run() {
						getCoreIndex().reSetupIndex((IFile) ttcnRsrc);
					}
				}, String.format("index %s", ttcnRsrc.getFullPath().toString()), Job.DECORATE);

				break;
			default:
				break;
			}
		}

		// Recursively finding all affected children resources until no more
		// affected resource found.
		@Deprecated
		private List<IResourceDelta> getAffacedChildren(IResourceDelta delta) {
			IResourceDelta[] affecteds = delta.getAffectedChildren();
			if (affecteds == null || affecteds.length == 0) {
				return new ArrayList<IResourceDelta>(0);
			}
			List<IResourceDelta> affectedChildren = new ArrayList<IResourceDelta>();
			for (IResourceDelta affected : affecteds) {
				affectedChildren.addAll(getAffacedChildren(affected));
			}
			return affectedChildren;
		}

		private IResourceDeltaVisitor createPreDeleteVisitor() {
			return new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (KITTParameter.isBETA()) {
						throw new Warning(
								"This is PRE_DELETE resource change event, i don't know what's the trigger of it and how to handled it at this moment!");
					}
					return false;
				}
			};
		}
	}

	public void indexInOrder(IProject[] projects) {
		List<IProject> openingPrjcts = new LinkedList<IProject>();
		for (IProject project : projects) {
			if (project.isOpen()) {
				openingPrjcts.add(project);
			}
		}
		IProject[] sortedPrjcts = sortOnDependency(openingPrjcts);
		for (IProject project : sortedPrjcts) {
			try {
				initializeProject(getConfiguration(project));
			} catch (CoreException e) {
				if (Logger.isErrorEnable()) {
					Logger.logError(String.format("Failed to index project %s.", project), e);
				}
			}
		}
	}

	protected IModulePathConfiguration getConfiguration(IProject project) throws CoreException {
		IModulePathConfiguration config = null;
		if (configs.get(project) == null) {
			configs.put(project, config = new ModulePathConfigurationImp(project));
		}
		return config;
	}

	private IProject[] sortOnDependency(List<IProject> openingPrjcts) {
		// TODO : based on IModulePathConfiguration of each project.
		return openingPrjcts.toArray(new IProject[openingPrjcts.size()]);
	}

	public IDocumentIndex getDocumentIndex(IDocument infokey) {
		Info info = retrieveInfo(infokey);
		return info.tIndex;
	}
	
	public void installDocumentIndex(IDocument infoKey){
		Info info = retrieveInfo(infoKey);
		info.tIndex = new LineBasedDocumentIndex();
		info.tIndex.install(infoKey);
	}

	private Map<IDocument, IFile> association = new HashMap<IDocument, IFile>();

	/**
	 * Disconnect document and file, as well as the underlying ITTCNElement.
	 * Hence, the underlying ITTCNElement wouldn't be update by document
	 * changing.
	 * 
	 * @see com.quantumlabs.kitt.ui.editors.TTCNEditor#dispose()
	 * */
	public void disconnect(IDocument document, IFile ttcnSource) {
		if (Logger.isDebugEnable()) {
			Logger.logDebug(toString(), String.format("disconnect %s and %s", document, ttcnSource));
		}
		TTCNCore.instance().getCoreService().disconnect(document, null);
		association.remove(document);
	}

	/**
	 * Connect document and file, as well as the underlying ITTCNElement. Hence,
	 * change of document will be reflected on ITTCNElement.
	 * 
	 * @see com.quantumlabs.kitt.ui.editors.TTCNEditor#init(IEditorSite,IEditorInput)
	 * */
	public void connect(IDocument document, IFile ttcnSource) {
		if (Logger.isDebugEnable()) {
			Logger.logDebug(toString(), String.format("Connect %s and %s", document, ttcnSource));
		}
		TTCNCore.instance().getCoreService().connect3(document, getCoreIndex().find(ttcnSource));
		Info info = retrieveInfo(ttcnSource);
		info.resource = ttcnSource;
		association.put(document, ttcnSource);
	}

	/**
	 * Connect TTCN resource, any change of resource level will be notice by
	 * TTCN core.
	 * */
	public void connect(IResource ttcnRsrc) {
		Assert.isNotNull(ttcnRsrc, "No null IResource can be connected!");
		Info info = retrieveInfo(ttcnRsrc);
		info.resource = ttcnRsrc;
		getCoreService().connect(ttcnRsrc);
	}

	/**
	 * Disconnect TTCN resource, any change of resource level would not be
	 * notice by TTCN core.
	 * */
	public void disconnect(IResource ttcnRsrc) {
		infos.remove(ttcnRsrc);
		getCoreService().disconnect(ttcnRsrc);
	}
}
