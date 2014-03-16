package org.quantumlabs.kitt.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.TTCNCore;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.editors.partition.ITTCNPartitions;

public class TTCNEditor extends TextEditor {

	private IDocumentProvider implicitDocumentPrvider;
	private EditorSelectionListener selectionListener;
	private TTCNOutlinePage kOutlinePage;

	public TTCNEditor() {
		super();
		implicitDocumentPrvider = new TTCNFileDocumentProvider();
		IPreferenceStore preferenceStore = Activator.instance().getPreferenceStore();
		ColorManager colorManager = Activator.instance().getTextTools().getColorManager();
		setSourceViewerConfiguration(new TTCNEditorSourceViewerConfiguration(preferenceStore, this, colorManager,
				ITTCNPartitions.TTCN_PARTITION_CATEGORY));
		setDocumentProvider(getDocumentProvider());
	}

	@Override
	public IVerticalRuler createVerticalRuler() {
		IVerticalRuler ruler = getVerticalRuler();
		if (ruler == null) {
			ruler = super.createVerticalRuler();
		}

		if (ruler instanceof CompositeRuler) {
			((CompositeRuler) ruler).addDecorator(0, new AnnotationRulerColumn(VERTICAL_RULER_WIDTH));
		}
		return ruler;
	}

	public ISourceViewer getViewer() {
		return super.getSourceViewer();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		IDocument doc =getDocumentProvider().getDocument(input);
		if(Logger.isDebugEnable()){
			Logger.logDebug(toString(), String.format("Init TTCNEditor, DOC : %s", doc));
		}
		TTCNCore.instance().connect(doc, ((IFileEditorInput) input).getFile());
	}

	@Override
	public IDocumentProvider getDocumentProvider() {
		return implicitDocumentPrvider;
	}

	@Override
	public void dispose() {
		IDocument doc = getDocumentProvider().getDocument(getEditorInput());
		if(Logger.isDebugEnable()){
			Logger.logDebug(toString(), String.format("Dispose TTCNEditor, DOC : %s", doc));
		}
		TTCNCore.instance().disconnect(doc, null);
		super.dispose();
	}

	@Override
	public void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		Activator.instance().getTextTools().handlePreferenceStoreChanged(event);
	}

	@Override
	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return Activator.instance().getTextTools().affectsTextPresentation(event);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		selectionListener = new EditorSelectionListener();
		selectionListener.install(this);
	}

	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		String[] keys = ((TTCNAnnotationModel) getDocumentProvider().getAnnotationModel(getEditorInput()))
				.getSupportedOverviewAnnotationTypes();
		for (String key : keys) {
			AnnotationPreference preference = new AnnotationPreference(key, Helper.generateColorKey(key),
					Helper.generateTextKey(key), Helper.generateRulerKey(key), getPreferenceStore().getInt(
							Helper.generatePresentLayerKey(key)));
			preference.setHighlightPreferenceKey(Helper.generateAnnotationHighlightKey(key));
			support.setAnnotationPreference(preference);
		}
		// support.install(getPreferenceStore());
	}

	public void highlightOccurrence(String value) {
		FindOccurrenceJob findingJob = new FindOccurrenceJob(value);
		if (findingJob.canFind()) {
			findingJob.start();
		} else {
			if (Logger.isDebugEnable()) {
				Logger.logDebug(this.toString(), String.format("%s is invalid! Can not hightlight occurrence! ", value));
			}
		}
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class clazz) {
		if (IContentOutlinePage.class.equals(clazz)) {
			if (kOutlinePage == null) {
				kOutlinePage = new TTCNOutlinePage(getDocumentProvider(), this);
				if (getEditorInput() != null) {
					kOutlinePage.setInput((IFileEditorInput) getEditorInput());
				}
			}
			return kOutlinePage;
		}
		return super.getAdapter(clazz);
	}

	public void goToLine(int offset, int length) {
		selectAndReveal(offset, 0);
	}

	class FindOccurrenceJob implements Runnable {
		String value;

		FindOccurrenceJob(String value) {
			this.value = value;
		}

		//TODO : Try to filter these word can be found.
		public boolean canFind() {
			// char [] chars = value.toCharArray();
			// for(char c : chars){
			// if(!Helper.isWordPart(c)){/*BUG:f_eNB_NS_002715_01_0007 is
			// invalid! Can not hightlight occurrence! -> needs to check*/
			// return false;
			// }
			// }
			return true;
		}

		@Override
		public void run() {
			IRegion[] findings = TTCNCore.instance().getDocumentIndex(getSourceViewer().getDocument()).find(value);
			updateNonPersistentAnnotation(findings);
			getSourceViewerDecorationSupport(getSourceViewer()).updateOverviewDecorations();
		}

		private void updateNonPersistentAnnotation(IRegion[] findings) {
			IAnnotationModel model = getDocumentProvider().getAnnotationModel(getEditorInput());
			Iterator<?> iterator = model.getAnnotationIterator();
			ArrayList<Annotation> annotations = new ArrayList<Annotation>();

			while (iterator.hasNext()) {
				Annotation removement = (Annotation) iterator.next();
				annotations.add(removement);
			}
			Map<Annotation, Position> replacementMap = new HashMap<Annotation, Position>();
			for (IRegion finding : findings) {
				Position p = new Position(finding.getOffset(), finding.getLength());
				Annotation insertion = new Annotation(SackConstant.ANNOTATION_TYPE_OCCURRENCE, false, null);
				replacementMap.put(insertion, p);
			}
			((IAnnotationModelExtension) model).replaceAnnotations(
					annotations.toArray(new Annotation[annotations.size()]), replacementMap);
		}

		public void start() {
			Display.getCurrent().syncExec(this);
		}
	}

	public class EditorSelectionListener implements ISelectionChangedListener {

		private TTCNEditor editor;

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			try {
				TextSelection selection = (TextSelection) event.getSelection();
				String selectedText = selection.getText();
				if (selectedText == null || selectedText.length() == 0) {
					selectedText = findSelected(selection.getOffset());
				}
				editor.highlightOccurrence(selectedText);
			} catch (BadLocationException ex) {
				if (Logger.isErrorEnable()) {
					Logger.logError(ex);
				}
			}
		}

		private String findSelected(int position) throws BadLocationException {
			IDocument doc = editor.getSourceViewer().getDocument();
			try {
				int backwardOfst = findGap(doc, position, false);
				int forwardOfst = findGap(doc, position, true);
				return doc.get(backwardOfst, forwardOfst - backwardOfst);
			} catch (Exception e) {
				return null;
			}
		}

		private int findGap(IDocument doc, int ofst, boolean forward) throws BadLocationException {
			int cursor = ofst;
			int len = doc.getLength();
			while (forward ? len - cursor > 0 : cursor > 0) {
				char c = doc.getChar(forward ? cursor++ : --cursor);
				if (Helper.isLettersInLowerCase(c) || Helper.isLettersInUpperCase(c) || Helper.isNumber(c)
						|| Helper.isUnderscore(c)) {
					continue;
				}
				return forward ? cursor - 1 : cursor + 1;
			}
			return forward ? len : 0;
		}

		public void install(TTCNEditor editor) {
			this.editor = editor;
			editor.getSelectionProvider().addSelectionChangedListener(this);
		}
	}
}
