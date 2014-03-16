package org.quantumlabs.kitt.ui.text;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.quantumlabs.kitt.ui.editors.ColorManager;
import org.quantumlabs.kitt.ui.editors.partition.ITTCNPartitions;
import org.quantumlabs.kitt.ui.editors.partition.TTCNDocumentPartitionScanner;

/**
 * Tools for TTCN editor SourceViewerConfiguration, all scanners and color
 * manager are presented and exsiting only one time, only on instance are shared
 * to all clients.
 * 
 * */
public class TTCNTextTools {

    private AbstractTTCNScanner kTtcnCodeScanner;
    private AbstractTTCNScanner kTtcnMultilineCommentScanner;
    private AbstractTTCNScanner kTtcnSinglelineCommentScanner;
    private AbstractTTCNScanner kTtcnStringScanner;
    private ColorManager kColorManager;
    private IPreferenceStore kPreferenceStore;

    // TODO : for later using.
    private IPropertyChangeListener internalDriver;
    private List<IPropertyChangeListener> propertyChangeListeners;

    public TTCNTextTools(IPreferenceStore preferenceStore,
	    ColorManager colorManager) {
	kPreferenceStore = preferenceStore;
	kColorManager = colorManager;
	propertyChangeListeners = new LinkedList<IPropertyChangeListener>();
	internalDriver = new IPropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent event) {
		for (int index = 0; index < propertyChangeListeners.size(); index++) {
		    propertyChangeListeners.get(index).propertyChange(event);
		}
	    }
	};
	initialize();
    }

    public AbstractTTCNScanner getTtcnCodeScanner() {
	return kTtcnCodeScanner;
    }

    public AbstractTTCNScanner createScanner(
	    Class<? extends ITokenScanner> adapter) {
	if (TTCNCodeScanner.class == adapter) {
	    return new TTCNCodeScanner(kColorManager, kPreferenceStore);
	} else if (TTCNSingleLineCommentScanner.class == adapter) {
	    return new TTCNSingleLineCommentScanner(kColorManager,
		    kPreferenceStore);
	} else if (TTCNMultiLineCommentScanner.class == adapter) {
	    return new TTCNMultiLineCommentScanner(kColorManager,
		    kPreferenceStore);
	} else if (TTCNStringScanner.class == adapter) {
	    return new TTCNStringScanner(kColorManager, kPreferenceStore);
	} else {
	    return null;
	}
    }

    public AbstractTTCNScanner getTtcnMultilineCommentScanner() {
	return kTtcnMultilineCommentScanner;
    }

    public AbstractTTCNScanner getTtcnSinglelineCommentScanner() {
	return kTtcnSinglelineCommentScanner;
    }

    public ColorManager getColorManager() {
	return kColorManager;
    }

    public void initialize() {
	kPreferenceStore.addPropertyChangeListener(internalDriver);
	kTtcnCodeScanner = createScanner(TTCNCodeScanner.class);
	kTtcnMultilineCommentScanner = createScanner(TTCNMultiLineCommentScanner.class);
	kTtcnSinglelineCommentScanner = createScanner(TTCNSingleLineCommentScanner.class);
	kTtcnStringScanner = createScanner(TTCNStringScanner.class);
    }

    public IPartitionTokenScanner getTTCNBasicPartitionScanner() {
	return new TTCNDocumentPartitionScanner();
    }

    public boolean affectsTextPresentation(PropertyChangeEvent event) {
	return kTtcnCodeScanner.affectsTextPresentation(event);
	// kTtcnMultilineCommentScanner.affectsTextPresentation(event);
	// kTtcnSinglelineCommentScanner.affectsTextPresentation(event);
	// return false;
    }

    public void handlePreferenceStoreChanged(PropertyChangeEvent event) {
	kTtcnCodeScanner.handlePreferenceStoreChanged(event);
    }

    public PresentationReconciler createPresentationReconciler(
	    ISourceViewer sourceViewer, String partitioning) {

	PresentationReconciler reconciler = new PresentationReconciler();
	reconciler.setDocumentPartitioning(partitioning);

	DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
		getTtcnCodeScanner());
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	dr = new DefaultDamagerRepairer(getTtcnSinglelineCommentScanner());
	reconciler.setDamager(dr, ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT);
	reconciler.setRepairer(dr, ITTCNPartitions.TTCN_SINGLE_LINE_COMMENT);

	dr = new DefaultDamagerRepairer(getTtcnStringScanner());
	reconciler.setDamager(dr, ITTCNPartitions.TTCN_STRING);
	reconciler.setRepairer(dr, ITTCNPartitions.TTCN_STRING);

	NonRuleBasedDamagerRepairer dr2 = new NonRuleBasedDamagerRepairer(
		getTtcnMultilineCommentScanner());
	dr2.setTextAttribute((TextAttribute) getTtcnMultilineCommentScanner()
		.getToken(ITTCNColorConstants._TTCN_MULTIPLE_LINE_COMMENT)
		.getData());
	reconciler.setDamager(dr2, ITTCNPartitions.TTCN_MULTI_LINE_COMMENT);
	reconciler.setRepairer(dr2, ITTCNPartitions.TTCN_MULTI_LINE_COMMENT);
	return reconciler;
    }

    private AbstractTTCNScanner getTtcnStringScanner() {
	return kTtcnStringScanner;
    }
}
