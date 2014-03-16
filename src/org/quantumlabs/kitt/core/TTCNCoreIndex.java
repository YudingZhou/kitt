package org.quantumlabs.kitt.core;

import java.io.IOException;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.parse.TesParser.ModuleContext;
import org.quantumlabs.kitt.core.resource.ResourcesManager;
import org.quantumlabs.kitt.core.util.collection.Pair;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.util.exception.StackTracableException;

/**
 * TTCNResourceIndex maintains IResources which belongs to ITTCNModulePath, maps
 * between TTCN resources and ITTCNElements.
 * <p>
 * 1. Storing the relationship between IResources and ITTCNElements.<br>
 * 2. Providing searching service to client code. 3. Setting up TTCN element
 * index.<br>
 * 3. Provide a URL-like protocol to identify the location of ITTCNElement. E.g.
 * kitt:///r
 * */
public class TTCNCoreIndex {
	private static final AbstractTTCNElement WORKSPACE_ROOT;
	static {
		WORKSPACE_ROOT = new AbstractTTCNElement() {
			@Override
			public int getElementType() {
				return ITTCNElement.WORKSPACE_ROOT;
			}
		};
	}
	/**
	 * IResource/ITTCNElement pairs are indexed by IPath of corresponding
	 * IResource location. <strong>For current implementation, only IResource
	 * level TTCN element can be indexed.</strong>
	 * */
	private final SortedMap<String, Pair<IResource, ITTCNElement>> index;
	private CoreParser parser;

	/**
	 * Construct TTCNCoreIndex, indexes will be sorted by comparator.
	 * 
	 * @param indexSorter
	 *            Comparator is used to present packages in given order. Like
	 *            java package-explorer view.
	 */
	public TTCNCoreIndex(Comparator<String> indexSorter) {
		if (indexSorter == null) {
			index = new TreeMap<String, Pair<IResource, ITTCNElement>>();
		} else {
			index = new TreeMap<String, Pair<IResource, ITTCNElement>>(indexSorter);
		}
		initializeIndex();
	}

	private void initializeIndex() {
		index.put(ResourcesManager.instance().getWorkspaceRoot().getLocation().toString(),
				new Pair<IResource, ITTCNElement>(ResourcesManager.instance().getWorkspaceRoot(), WORKSPACE_ROOT));
		parser = new CoreParser();
	}

	public TTCNCoreIndex() {
		this(null);
	}

	private boolean isIndexed(String absoluteLocation) {
		return index.containsKey(absoluteLocation);
	}

	private Pair<IResource, ITTCNElement> retrieveInfo(String absoluteLocation) {
		return index.get(absoluteLocation);
	}

	/**
	 * Try to find a TTCNElement by IResource, if no available TTCNElement,
	 * <code>null</code> will be returned.
	 * 
	 * @return TTCNElement or Null.
	 * @see #pull(IResource)
	 * */
	public ITTCNElement find(IResource ttcnRsrc) {
		String location = ttcnRsrc.getLocation().toString();
		if (isIndexed(location)) {
			return retrieveInfo(location).getRight();
		} else {
			if (Logger.isDebugEnable()) {
				Logger.logDebug(this.toString(), "Try to find %s, but I've indexed it yet! ;-)");
			}
			//R1.0.0, we don't use preinitializing. then the TTCNElement will be initialized will it is opened first time.
			setupIndex(ttcnRsrc);
			return retrieveInfo(location).getRight();
		}
	}

	private ITTCNElement initializeTTCNElement(IResource iResource) throws CoreException, IOException {
		ITTCNElement element = null;
		if (iResource instanceof IProject) {
			element = new TTCNProject(find(ResourcesManager.instance().getWorkspaceRoot()), iResource.getName());
		} else if (iResource instanceof IFile) {
			element = initializeCompilationUnit((IFile) iResource);
		} else {
			if (Logger.isDebugEnable()) {
				Logger.logDebug(this.toString(), "initializeTTCNElement() iresource : " + iResource);
			}
		}
		element.setCorrespondingResource(iResource);
		return element;
	}

	/**
	 * Re-parse IResource to old TTCNElement for some reason, e.g. unexpected
	 * error; fast setup old(indexed) TTCN resource. Without reconnect<br>
	 * 1.Re-parse IResource. 2. Replace corresponding RuleContext.
	 * */
	public void reSetupIndex(IFile source) {
		ITTCNElement element = find(source);
		Assert.isNotNull(element,
				String.format("Unindexed file can't be re-setup : %s", source.getLocation().toString()));
		try {
 			element.clear();
			element.parse(getParser().parse(CoreParser.openStream(source), ModuleContext.class));
		} catch (Exception e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e, e.getMessage(),
						String.format("Failed to re-setup compilation unit : %s", source.getLocation()));
			}
			// No further error handling, re-throw exception.
			throw new StackTracableException(e);
		}
	}

	public void reSetupIndexWithContent(IFile source, String newContent) {
		ITTCNElement element = find(source);
		Assert.isNotNull(element,
				String.format("Unindexed file can't be re-setup : %s", source.getLocation().toString()));
		try {
			element.clear();
			element.parse(getParser().parse(CoreParser.openStream(newContent), ModuleContext.class));
		} catch (Exception e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e, e.getMessage(),
						String.format("Failed to re-setup compilation unit : %s", source.getLocation()));
			}
			// No further error handling, re-throw exception.
			throw new StackTracableException(e);
		}
	}

	/**
	 * Setup IResource level index.
	 * */
	public void setupIndex(IResource ttcnRsrc) {
		try {
			if (ttcnRsrc instanceof IFile) {
				ITextFileBufferManager.DEFAULT.connect(ttcnRsrc.getLocation(), LocationKind.LOCATION,
						new NullProgressMonitor());
			}
			ITTCNElement elemt = initializeTTCNElement(ttcnRsrc);
			doIndex(ttcnRsrc, elemt);
			TTCNCore.instance().connect(ttcnRsrc);
		} catch (Exception e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e, e.getMessage(),
						String.format("Failed to initialize compilation unit : %s", ttcnRsrc.getLocation()));
			}
			// No further error handling, re-throw exception.
			throw new StackTracableException(e);
		}
	}

	private ICompilationUnit initializeCompilationUnit(IFile resource) throws CoreException, IOException {
		// :Jan.22.2014.TODO. Current idea there is two kind of initializing
		// policies, one is LAZAY ININTIALIZING, that TTCNCoreIndex only index
		// ttcn element to Module level, inside ttcn module, It will be indexed
		// until it be asked by any other part(e.g. editor will ask for info of
		// any element inside a module.).
		// The other one is initialize module immediately.
		CompilationUnit compilationUnit = null;
		if (KITTParameter.isModuleLazayInitializing()) {
			// TODO : to be initialize immediately.
		} else {
			// compilationUnit = new
			// CompilationUnit(find(resource.getParent()));
			compilationUnit = new CompilationUnit();
			compilationUnit.parse(getParser().parse(CoreParser.openStream(resource), ModuleContext.class));
			ITTCNElement ancestor = TTCNCore.instance().getCoreIndex().pull(resource.getProject());
			compilationUnit.setAncestor(ancestor);
		}
		return compilationUnit;
	}

	public CoreParser getParser() {
		return KITTParameter.isSharedParser() ? parser : new CoreParser();
	}

	private void doIndex(IResource ttcnRsrc, ITTCNElement elemt) {
		if (isIndexed(ttcnRsrc.getLocation().toString())) {
			throw new DuplicatedElementException(String.format("%s has been indexed already!", ttcnRsrc.getLocation()));
		}
		Pair<IResource, ITTCNElement> info = new Pair<IResource, ITTCNElement>(ttcnRsrc, elemt);
		index.put(ttcnRsrc.getLocation().toString(), info);
	}

	public void tearDownIndex(IResource ttcnRsrc) {
		ITTCNElement element = null;
		Assert.isNotNull(element = find(ttcnRsrc),
				String.format("%s should not be null before removed!", ttcnRsrc.getLocation()));
		TTCNCore.instance().disconnect(ttcnRsrc);
		element.getAncestor().removeChild(element);
		element.dispose();
		try {
			ITextFileBufferManager.DEFAULT.disconnect(ttcnRsrc.getLocation(), LocationKind.LOCATION,
					new NullProgressMonitor());
		} catch (CoreException e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(toString(), e, String.format("Failed to teardown index : %s", ttcnRsrc.getLocation()));
			}
		}
	}

	/**
	 * Convert from ITTCN type id to literal.
	 * */
	public static String getTypeLiteral(int typeId) {
		String type = null;
		switch (typeId) {
		case ITTCNElement.COMPILATION_UNIT:
			type = "COMPILATION_UNIT";
			break;
		case ITTCNElement.PACKAGE:
			type = "PACKAGE";
			break;
		case ITTCNElement.PROJECT:
			type = "PROJECT";
			break;
		case ITTCNElement.IMPORT_DECLARATION:
			type = "IMPORT_DECLARATION";
			break;
		case ITTCNElement.COMPONENT:
			type = "COMPONENT";
			break;
		case ITTCNElement.CONTROL_PART:
			type = "CONTROL_PART";
			break;
		case ITTCNElement.FIELD:
			type = "FIELD";
			break;
		case ITTCNElement.INITIALIZER:
			type = "INITIALIZER";
			break;
		case ITTCNElement.LOCAL_VARIABLE:
			type = "LOCAL_VARIABLE";
			break;
		case ITTCNElement.TEMPLATE:
			type = "TEMPLATE";
			break;
		case ITTCNElement.TEST_CASE:
			type = "TEST_CASE";
			break;
		case ITTCNElement.GROUP_DECLARATION:
			type = "GROUP_DECLARATION";
			break;
		case ITTCNElement.ALTSTEP_DECLARATION:
			type = "ALTSTEP_DECLARATION";
			break;
		case ITTCNElement.ATTRIBUTE_DECLARATION:
			type = "ATTRIBUTE_DECLARATION";
			break;
		default:
			throw new StackTracableException(String.format("Unsupported ttcn element type %s.", type));
		}
		return type;
	}

	/**
	 * Try to find a TTCNElement. If no available TTCNElement, index it
	 * immediately. No <code>null</code> will be returned.
	 * 
	 * @return TTCNElement.
	 * @throws Exception
	 * @see #find(IResource)
	 * */
	public ITTCNElement pull(IResource resource) {
		if (null == find(resource)) {
			setupIndex(resource);
		}
		return find(resource);
	}
}
