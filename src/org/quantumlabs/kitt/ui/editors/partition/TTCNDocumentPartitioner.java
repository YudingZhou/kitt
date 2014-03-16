package org.quantumlabs.kitt.ui.editors.partition;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class TTCNDocumentPartitioner extends FastPartitioner {

    public TTCNDocumentPartitioner(IPartitionTokenScanner scanner,
	    String[] legalContentTypes) {
	super(scanner, legalContentTypes);
    }

    @Override
    public IRegion documentChanged2(DocumentEvent e) {
	return super.documentChanged2(e);
    }

    public Position[] getPartitionPositions()
	    throws BadPositionCategoryException {
	return super.getPositions();
    }
}
