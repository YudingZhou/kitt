package org.quantumlabs.kitt.ui.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.TTCNCore;
import org.quantumlabs.kitt.core.TTCNCoreIndex;
import org.quantumlabs.kitt.ui.editors.partition.ITTCNPartitions;
import org.quantumlabs.kitt.ui.editors.partition.TTCNDocumentPartitionScanner;

/**
 * Setup participant is responsible for partition ttcn document .
 * */
public class TTCNDocumentSetupParticipant implements IDocumentSetupParticipant {

    @Override
    public void setup(IDocument document) {
	/*
	 * FastPartitioner,A standard implementation of a document partitioner.
	 * It uses an IPartitionTokenScanner to scan the document and to
	 * determine the document's partitioning. The tokens returned by the
	 * scanner must return the partition type as their data. The partitioner
	 * remembers the document's partitions in the document itself rather
	 * than maintaining its own data structure. To reduce array creations in
	 * IDocument.getPositions(String), the positions get cached. The cache
	 * is cleared after updating the positions in
	 * documentChanged2(DocumentEvent). Subclasses need to call
	 * clearPositionCache() after modifying the partitioner's positions. The
	 * cached positions may be accessed through getPositions().
	 */
	setupTTCNDocumentPartitioner(document,
		ITTCNPartitions.TTCN_PARTITION_CATEGORY);
	setupIndex(document);
    }

    private void setupIndex(IDocument document) {
	TTCNCore.instance().installDocumentIndex(document);
    }

    private FastPartitioner createDocumentPartitioner() {
	String[] contentTypes = TTCNDocumentPartitionScanner
		.getLegalContentType();
	IPartitionTokenScanner scanner = Activator.instance().getTextTools()
		.getTTCNBasicPartitionScanner();
	return new FastPartitioner(scanner, contentTypes);
    }

    // private FastPartitioner createDocumentPartitionerByTtcnCodeScanner() {
    // return new FastPartitioner(kTtcnCodeScanner,
    // kTtcnCodeScanner.getTokenProperties());
    // }

    public void setupTTCNDocumentPartitioner(IDocument document,
	    String partitioning) {
	IDocumentPartitioner partitioner = createDocumentPartitioner();
	if ((document instanceof IDocumentExtension3)) {
	    IDocumentExtension3 extension3 = (IDocumentExtension3) document;
	    extension3.setDocumentPartitioner(partitioning, partitioner);
	} else {
	    document.setDocumentPartitioner(partitioner);
	}
	partitioner.connect(document);
    }
}
