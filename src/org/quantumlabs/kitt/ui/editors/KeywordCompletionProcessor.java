package org.quantumlabs.kitt.ui.editors;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.Helper;
import org.quantumlabs.kitt.core.util.ITTCNKeyWords;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class KeywordCompletionProcessor implements IContentAssistProcessor {

    public static char[] DEFAULT_TRIGGER;
    static String[] KEYWORD;
    static {
	KEYWORD = Helper.asString(Helper.getDeclaredSVariables(
		ITTCNKeyWords.class, ITTCNKeyWords.class));
	Set<Character> trigger = new HashSet<Character>(

	KEYWORD.length > 24 ? 24 : KEYWORD.length);
	for (int i = 0; i < KEYWORD.length; i++) {
	    for (int j = 0; j < KEYWORD[i].length(); j++) {
		trigger.add(KEYWORD[i].charAt(j));
	    }
	}
	DEFAULT_TRIGGER = new char[trigger.size()];
	int i = 0;
	for (char c : trigger) {
	    DEFAULT_TRIGGER[i++] = c;
	}
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
	    int offset) {
	IDocument doc = viewer.getDocument();
	try {
	    IRegion lineInfo = doc.getLineInformationOfOffset(offset);
	    int charOffsetBefore = findCharBefore(lineInfo, offset, doc);
	    if (charOffsetBefore == offset) { // This means new line wouldn't
					      // trigger proposals.
		return null;
	    }
	    String prefix = doc
		    .get(charOffsetBefore, offset - charOffsetBefore);
	    List<String> rawProposals = collectProposals(prefix);
	    if (rawProposals.size() == 0) {
		return null;
	    }
	    ICompletionProposal[] proposals = new ICompletionProposal[rawProposals
		    .size()];
	    for (int i = 0; i < rawProposals.size(); i++) {
		String rawProposal = rawProposals.get(i);
		proposals[i] = new CompletionProposal(rawProposal, offset
			- prefix.length(), prefix.length(),
			rawProposal.length());
	    }
	    return proposals;

	} catch (BadLocationException e) {
	    throw new RuntimeException(e);
	}
    }

    private List<String> collectProposals(String prefix) {
	List<String> matchedProposals = new LinkedList<String>();
	for (String keyword : KEYWORD) {
	    int matchedLength = matchPrefix(prefix, keyword);
	    if (matchedLength > 0) {
		matchedProposals.add(keyword);
	    }
	}
	return matchedProposals;
    }

    private int matchPrefix(String target, String matcher) {
	if (KITTParameter.isStrictMatching() && matcher.startsWith(target)) {
	    return target.length();
	} else if (!(KITTParameter.isStrictMatching())
		&& target.length() <= matcher.length()
		&& matcher.substring(0, target.length()).equalsIgnoreCase(
			target)) {
	    return target.length();
	} else {
	    return 0;
	}
    }

    /**
     * Find the beginning offset of character of current identifier in reversed.
     * */
    private int findCharBefore(IRegion lineInfo, int offset, IDocument doc)
	    throws BadLocationException {
	int startOffset = lineInfo.getOffset();
	int currentOffset = offset;
	if (startOffset < currentOffset) {
	    while (currentOffset > startOffset
		    && Helper.isWordPart(doc.getChar(currentOffset - 1))) {
		currentOffset--;
	    }
	    return currentOffset;
	}
	return startOffset;
    }


    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer,
	    int offset) {
	if (Logger.isDebugEnable()) {
	    Logger.logDebug(this.toString(),
		    "computeContextInformation() return null : " + offset);
	}
	return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
	return KITTParameter.getKeywordCompletionProposalActivationChars();
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
	return new char[] { '#' };
    }

    @Override
    public String getErrorMessage() {
	return "This is dummy error message";
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
	if (Logger.isDebugEnable()) {
	    Logger.logDebug(this.toString(), "getContextInformationValidator()");
	}
	return new ContextInformationValidator(this);
    }
}
