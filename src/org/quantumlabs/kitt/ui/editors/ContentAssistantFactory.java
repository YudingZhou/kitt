package org.quantumlabs.kitt.ui.editors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.quantumlabs.kitt.core.config.KITTParameter;

public class ContentAssistantFactory {
    private final ContentAssistant assistant;

    public ContentAssistantFactory(String configuredDocumentPartitioning) {
	assistant = new ContentAssistant();
	assistant.setDocumentPartitioning(configuredDocumentPartitioning);
	assistant.enableAutoActivation(KITTParameter
		.isContentAssistantAutoActivationEnabled());
	assistant.setProposalPopupOrientation(KITTParameter
		.getProposalPopupOrientation());
	assistant.setContextInformationPopupOrientation(KITTParameter
		.getContextInformationPopupOrientation());
	assistant
		.setContextInformationPopupBackground(ColorManager
			.instance()
			.getColor(
				KITTParameter
					.getContextInformationPopupBackground()));
	assistant.setAutoActivationDelay(KITTParameter
		.getContentAssistantAutoActivationDelay());
    }

    public IContentAssistant newAssistant() {
	return assistant;
    }

    public IContentAssistant assemble(
	    Class<? extends IContentAssistProcessor> processor) {
	Assert.isNotNull(assistant);
	if (processor == KeywordCompletionProcessor.class) {
	    assistant.setContentAssistProcessor(
		    new KeywordCompletionProcessor(),
		    IDocument.DEFAULT_CONTENT_TYPE);
	}

	// TODO add more kind of processor to this assistant;

	return assistant;
    }
}
