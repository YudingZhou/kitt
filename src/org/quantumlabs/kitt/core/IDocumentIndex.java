package org.quantumlabs.kitt.core;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public interface IDocumentIndex {
    void install(IDocument doc);

    IRegion[] find(Object element);
}
