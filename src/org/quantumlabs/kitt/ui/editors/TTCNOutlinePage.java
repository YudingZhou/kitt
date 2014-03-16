package org.quantumlabs.kitt.ui.editors;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.quantumlabs.kitt.core.GroupDeclaration;
import org.quantumlabs.kitt.core.IAltStepDeclaration;
import org.quantumlabs.kitt.core.ICompilationUnit;
import org.quantumlabs.kitt.core.IConstantDeclaration;
import org.quantumlabs.kitt.core.IControlPart;
import org.quantumlabs.kitt.core.IFunctionDeclaraion;
import org.quantumlabs.kitt.core.IImportDeclaration;
import org.quantumlabs.kitt.core.INamable;
import org.quantumlabs.kitt.core.IOffsetable;
import org.quantumlabs.kitt.core.ITTCNElement;
import org.quantumlabs.kitt.core.ITemplate;
import org.quantumlabs.kitt.core.ITestCase;
import org.quantumlabs.kitt.core.ITypeDeclaration;
import org.quantumlabs.kitt.core.TTCNCore;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.resource.ImageHolder;
import org.quantumlabs.kitt.core.util.Callback;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class TTCNOutlinePage extends ContentOutlinePage implements IAdaptable, IPostSelectionProvider {
	private final TTCNEditor kEditor;
	private Menu menu;
	private Action collapseAction;
	private final ISelectionChangedListener selectionChangedListener;
	private final ILabelProvider labelProvider;
	private final IContentProvider contentProvider;
	private IFileEditorInput input;
	private IDocument doc;

	// private IPositionUpdater positionUpdater;

	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public TTCNOutlinePage(IDocumentProvider documentProvider, TTCNEditor editor) {
		labelProvider = new LabelProviderImp();
		contentProvider = new ContentProviderImp();
		selectionChangedListener = new ISelectionChangedListenerImp();
		kEditor = editor;
	}

	private static Color getBackgroundColor() {
		return ColorManager.instance().getColor(
				StringConverter.asRGB(KITTParameter.getSelectionHighlightBackgroundStyle()));
	}

	public static Color getFrontColor() {
		return ColorManager.instance().getColor(StringConverter.asRGB(KITTParameter.getSelectionHighlightFrontStyle()));
	}

	class ISelectionChangedListenerImp implements ISelectionChangedListener {
		int count = 0;

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			@SuppressWarnings("unchecked")
			Iterator<Object> selections = ((TreeSelection) event.getSelection()).iterator();

			Object lastSelection = null;
			while (selections.hasNext()) {
				lastSelection = selections.next();
			}
			if (lastSelection instanceof IOffsetable) {
				int tokenStartIndex = ((IOffsetable) lastSelection).getOffset();
				int tokenEndIndex = lookAhead(tokenStartIndex);
				kEditor.selectAndReveal(tokenStartIndex, tokenEndIndex - tokenStartIndex);
			}
		}

		private int lookAhead(final int tokenStartIndex) {
			int idx = tokenStartIndex;
			try {
				boolean isPartOfWord = Helper.isWordPart(doc.getChar(idx++));
				while (isPartOfWord) {
					isPartOfWord = Helper.isWordPart(doc.getChar(idx++));
				}
			} catch (BadLocationException e) {
				if (Logger.isErrorEnable()) {
					Logger.logError(String.format("TTCNOutlinePage.lookAhead() : Ignore BadlocationException %s", idx));
				}
			}
			return --idx;
		}
	}

	@Override
	public void setFocus() {
		super.setFocus();
		Logger.logDebug("setfocus " + getControl().isFocusControl());
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.addSelectionChangedListener(selectionChangedListener);
		viewer.setInput(input);
		getSite().setSelectionProvider(viewer);
	}

	class NodeData {
		int offset;
		int length;
		String type;
		String name;
		String description;
	}

	class ContentProviderImp implements ITreeContentProvider, Callback {

		private Object oldInput;
		private Object newInput;

		@Override
		public void dispose() {

			if (oldInput != null) {
				TTCNCore.instance().getCoreIndex().pull(((IFileEditorInput) oldInput).getFile()).removeCallBack(this);
			}
			if (newInput != null) {
				TTCNCore.instance().getCoreIndex().pull(((IFileEditorInput) newInput).getFile()).removeCallBack(this);
			}
			oldInput = null;
			newInput = null;
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.oldInput = oldInput;
			this.newInput = newInput;
			if (newInput != null) {
				ITTCNElement element = TTCNCore.instance().getCoreIndex().find(((IFileEditorInput) newInput).getFile());
				if (element != null) {
					element.addCallBack(this);
				}
			}

			if (oldInput != null) {
				ITTCNElement element = TTCNCore.instance().getCoreIndex().find(((IFileEditorInput) oldInput).getFile());
				if (element != null) {
					element.removeCallBack(this);
				}
			}
		}

		@Override
		public Object[] getElements(Object input) {
			if (input instanceof IFileEditorInput) {
				IFile resource = ((IFileEditorInput) input).getFile();
				return getChildren(TTCNCore.instance().getCoreIndex().pull(resource));
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			Object[] returnData;
			if (parentElement instanceof ICompilationUnit) {
				switch (KITTParameter.getOutlineSortPolicy()) {
				case SackConstant.OUTLINE_SORT_POLICY_ALPHABETIC:
					returnData = sortByAlphabetic((ICompilationUnit) parentElement);
					break;
				case SackConstant.OUTLINE_SORT_POLICY_OFFSET:
					returnData = sortByOffset((ICompilationUnit) parentElement);
					break;
				default:
					return returnData = ((ICompilationUnit) parentElement).getChildren();
				}

				return returnData;
			}
			return new Object[0];
		}

		private Object[] sortByOffset(ICompilationUnit parentElement) {
			ITTCNElement[] elements = parentElement.getChildren();
			Arrays.sort(elements, new Comparator<ITTCNElement>() {
				@Override
				public int compare(ITTCNElement o1, ITTCNElement o2) {
				    	
					return ((IOffsetable)o1).getOffset() - ((IOffsetable)o2).getOffset();
				}
			});
			return elements;
		}

		private ITTCNElement[] sortByAlphabetic(ICompilationUnit compilationUnit) {
			ITTCNElement[] returnData = new ITTCNElement[compilationUnit.getChildren().length];
			ITTCNElement[] temp;
			int index = 0;
			temp = compilationUnit.getImportDeclarations();
			index = copy(returnData, temp, index);

			temp = compilationUnit.getConstants();
			index = sortAndCopy(returnData, temp, index);
			temp = compilationUnit.getFunctions();
			index = sortAndCopy(returnData, temp, index);
			temp = compilationUnit.getTemplates();
			index = sortAndCopy(returnData, temp, index);
			temp = compilationUnit.getTestCases();
			index = sortAndCopy(returnData, temp, index);
			temp = compilationUnit.getTypeDeclarations();
			index = sortAndCopy(returnData, temp, index);

			ITTCNElement controlPart = compilationUnit.getControlPart();
			if (controlPart != null) {
				returnData[index] = controlPart;
			}
			return returnData;
		}

		int sortAndCopy(ITTCNElement[] returnData, ITTCNElement[] temp, int index) {
			Arrays.sort(temp, getINamableComparator());
			return copy(returnData, temp, index);
		}

		int copy(ITTCNElement[] returnData, ITTCNElement[] temp, int index) {
			System.arraycopy(temp, 0, returnData, index, temp.length);
			index += temp.length;
			return index;
		}

		// Only INamble can be sort.
		private Comparator<Object> getINamableComparator() {
			return new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					if (o1 instanceof INamable) {
						return ((INamable) o1).getName().compareTo(((INamable) o1).getName());
					} else {
						return 0;
					}
				}
			};
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof ICompilationUnit;
		}

		@Override
		// Call while TTCNElement is updating. Try to refresh tree reviewer.
		public Object call(Object... args) {
			Helper.runAsyncDisplayRun(new Runnable() {
				@Override
				public void run() {
					getTreeViewer().refresh();
				}
			});
			return null;
		}
	}

	class LabelProviderImp extends LabelProvider
	/* implements IColorProvider, IFontProvider */{

		@Override
		public Image getImage(Object o) {
			ITTCNElement element = (ITTCNElement) o;
			try {
				if (element.getElementType() == ITTCNElement.COMPILATION_UNIT) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_MODULE_IMG_S);
				} else if (element.getElementType() == ITTCNElement.TYPE) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_TYPE_IMG_S);
				} else if (element.getElementType() == ITTCNElement.IMPORT_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_IMPORT_IMG_S);
				} else if (element.getElementType() == ITTCNElement.FUNCTION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_FUNCTION_IMG_S);
				} else if (element.getElementType() == ITTCNElement.CONTROL_PART) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_CONTROL_IMG_S);
				} else if (element.getElementType() == ITTCNElement.TEST_CASE) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_TESTCASE_IMG_S);
				} else if (element.getElementType() == ITTCNElement.GROUP_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_GROUP_IMG_S);
				} else if (element.getElementType() == ITTCNElement.ALTSTEP_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_ALTSTEP_IMG_S);
				} else if (element.getElementType() == ITTCNElement.ATTRIBUTE_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_ATTRIBUTE_IMG_S);
				} else if (element.getElementType() == ITTCNElement.SIGNATURE_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_SIGNATURE_IMG_S);
				} else if (element.getElementType() == ITTCNElement.MODULE_PAR_DECLARATION) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_MODULEPAR_IMG_S);
				}else if (element.getElementType() == ITTCNElement.TEMPLATE) {
					return ImageHolder.instance().checkout(SackConstant.IMG_TTCN_ELEMENT_TEMPLATE_IMG_S);
				}
				
				
			} catch (Exception e) {
				Logger.logError(toString(), e);
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof ICompilationUnit) {
				return ((ICompilationUnit) element).getID();
			} else if (element instanceof ITypeDeclaration) {
				return "Type : " + ((ITypeDeclaration) element).getID();
			} else if (element instanceof IImportDeclaration) {
				return "Import : " + ((IImportDeclaration) element).getSourceModule();
			} else if (element instanceof IFunctionDeclaraion) {
				IFunctionDeclaraion function = ((IFunctionDeclaraion) element);
				return "Function : " + function.getID();
			} else if (element instanceof IControlPart) {
				return "Control ->";
			} else if (element instanceof ITestCase) {
				return ((ITestCase) element).getID();
			} else if (element instanceof ITemplate) {
				return "Template : " + ((ITemplate) element).getType();
			} else if (element instanceof IConstantDeclaration) {
				return "Const : " + ((IConstantDeclaration) element).getID();
			} else if (element instanceof IAltStepDeclaration) {
				return "Altstep";
			} else if(element instanceof GroupDeclaration) {
				return "Group";
			}else{
				return super.getText(element);
			}
		}
		/*
		 * @Override public Color getForeground(Object element) { return
		 * ColorManager.instance().getColor("200,100,30"); }
		 * 
		 * @Override public Color getBackground(Object element) { return
		 * ColorManager.instance().getColor("0,0,0"); }
		 * 
		 * @Override public Font getFont(Object element) { return
		 * JFaceResources.getFontRegistry().get(KITTParameter.getOutlineFont());
		 * }
		 */
	}

	public void setInput(IFileEditorInput input) {
		this.input = input;
		doc = kEditor.getDocumentProvider().getDocument(input);
	}
}
