package org.quantumlabs.kitt.ui.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.ui.texteditor.ITextEditor;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.ui.text.AbstractTTCNScanner;
import org.quantumlabs.kitt.ui.text.TTCNTextTools;

public class TTCNEditorSourceViewerConfiguration extends
	SourceViewerConfiguration {

    private final String kPartitionType;

    private final TTCNTextTools kTextTools;

    private final ITextEditor kTtcnEditor;

    private AbstractTTCNScanner kTtcnCodeScanner;
    private AbstractTTCNScanner kMultiLineCommentScanner;
    private AbstractTTCNScanner kSingleLineCommentScanner;

    private IDoubleClickListener kDoubleClickListener;

    private final IPreferenceStore kPreferenceStore;
    private PresentationReconciler reconciler;

    public TTCNEditorSourceViewerConfiguration(
	    IPreferenceStore preferenceStore, ITextEditor editor,
	    ColorManager colorManager, String partitionType) {
	kPreferenceStore = preferenceStore;
	kTtcnEditor = editor;
	kPartitionType = partitionType;
	kTextTools = Activator.instance().getTextTools();
	initialize();
    }

    private void initialize() {
	kTtcnCodeScanner = kTextTools.getTtcnCodeScanner();
	kMultiLineCommentScanner = kTextTools.getTtcnMultilineCommentScanner();
	kSingleLineCommentScanner = kTextTools
		.getTtcnSinglelineCommentScanner();
    }

    @Override
    public IReconciler getReconciler(ISourceViewer sourceViewer) {
	// TODO Auto-generated method stub
	return super.getReconciler(sourceViewer);
    }

    @Override
    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
	// TODO Auto-generated method stub
	return super.getContentFormatter(sourceViewer);
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
	ContentAssistantFactory factory = new ContentAssistantFactory(getConfiguredDocumentPartitioning(sourceViewer));
	return factory.assemble(KeywordCompletionProcessor.class);
    }

    @Override
    public IQuickAssistAssistant getQuickAssistAssistant(
	    ISourceViewer sourceViewer) {
	// TODO Auto-generated method stub
	return super.getQuickAssistAssistant(sourceViewer);
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(
	    ISourceViewer sourceViewer, String contentType) {
	// TODO Auto-generated method stub
	return super.getAutoEditStrategies(sourceViewer, contentType);
    }

    @Override
    public ITextDoubleClickStrategy getDoubleClickStrategy(
	    ISourceViewer sourceViewer, String contentType) {
	// TODO Auto-generated method stub
	return super.getDoubleClickStrategy(sourceViewer, contentType);
    }

    @Override
    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
	// TODO Auto-generated method stub
	return super.getAnnotationHover(sourceViewer);
    }

    @Override
    public IAnnotationHover getOverviewRulerAnnotationHover(
	    ISourceViewer sourceViewer) {
	// TODO Auto-generated method stub
	return super.getOverviewRulerAnnotationHover(sourceViewer);
    }

    @Override
    public int[] getConfiguredTextHoverStateMasks(ISourceViewer sourceViewer,
	    String contentType) {
	// TODO Auto-generated method stub
	return super
		.getConfiguredTextHoverStateMasks(sourceViewer, contentType);
    }

    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
	// TODO return . ITTCN_CODE, TTCN_comment
	return super.getConfiguredContentTypes(sourceViewer);
    }

    @Override
    public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
	if (kPartitionType != null) {
	    return kPartitionType;
	}
	return super.getConfiguredDocumentPartitioning(sourceViewer);
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
	    ISourceViewer sourceViewer) {
	if (reconciler == null) {
	    reconciler = kTextTools.createPresentationReconciler(sourceViewer,
		    getConfiguredDocumentPartitioning(sourceViewer));
	}
	return reconciler;
    }

    private ITokenScanner getStringScanner() {
	// TODO Auto-generated method stub
	return null;
    }

    public AbstractTTCNScanner getCodeScanner() {
	return kTtcnCodeScanner;
    }

    public AbstractTTCNScanner getMultilineCommentScanner() {
	return kMultiLineCommentScanner;
    }

    public AbstractTTCNScanner getSinglelineCommentScanner() {
	return kSingleLineCommentScanner;
    }
}
