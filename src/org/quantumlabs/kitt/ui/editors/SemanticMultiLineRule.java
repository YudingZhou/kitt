package org.quantumlabs.kitt.ui.editors;

import java.util.Arrays;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.ui.editors.partition.EndStrategy;
import org.quantumlabs.kitt.ui.editors.partition.SkipStrategy;
import org.quantumlabs.kitt.ui.editors.partition.TTCNDocumentPartitionScanner;

/**
 * Multiple lines partition is used for semantic block partitioning.For example:
 * type definition, test case definition, function definition. For the block, it
 * has specific structure which looks like,
 * 
 * <pre>
 * <strong> <key word declaration> <name>
 * [<additional declaration>< additional reference name>] block body </strong>
 * 
 * <pre>
 * Client code should define the allowed sequence token as the structure, if 
 * there are unexpected content, it could be allowed as any content.
 * <br>
 * For <strong>allowed as any content</strong>, there is ending strategy for 
 * ending current allowed content. It's designed to ending block body, since 
 * content of body is depends on user's input, but it should begin with {@link SackConstants.REGULAR_PATTEN_BLOCK_BEGIN} 
 * and ends with a
 * {@link SackConstants.REGULAR_PATTEN_BLOCK_END}.
 * Current implementation only use {@link OccurredTimesMatchEndStrategy}
 * to end block body.
 * */
@Deprecated
public class SemanticMultiLineRule extends MultiLineRule {

    final private String[] allowedSequences;
    private EndStrategy endStrategy;
    private SkipStrategy skipStrategy;
    private int optional;
    // Currently, i don't need to escape any character, since skip strategy
    // knows which character needs to be skipped.
    private static char escapeCharacter = ' ';

    /**
     * @param startSequence
     *            Start sequence of the token.
     * @param allowedSequnce
     * @param endSequence
     *            End sequence of the token.
     * @param token
     *            current token.
     * @param es
     * */
    public SemanticMultiLineRule(String startSequence,
	    String[] allowedSequences, String endSequence, IToken token,
	    SkipStrategy skipStrategy, EndStrategy endStrategy, int optional) {
	super(startSequence, endSequence, token, escapeCharacter, true);
	this.allowedSequences = allowedSequences;
	this.endStrategy = endStrategy;
	this.skipStrategy = skipStrategy;
	this.optional = optional;
    }

    public SemanticMultiLineRule(String[] strings, Token token,
	    SkipStrategy skipStrategy, EndStrategy endStrategy, int optinal) {
	this(strings[0], strings.length > 3 ? Arrays.copyOfRange(strings, 1,
		strings.length - 2) : new String[0],
		strings[strings.length - 1], token, skipStrategy, endStrategy,
		optinal);
    }

    @Override
    protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
	if (scanner instanceof TTCNDocumentPartitionScanner) {
	    TTCNDocumentPartitionScanner kScanner = (TTCNDocumentPartitionScanner) scanner;
	    if (resume) {
		if (endSequenceDetected(scanner))
		    return fToken;
	    } else {

		int c = scanner.read();
		if (KITTParameter.isBETA()) {
		    System.out.println((char) c);
		}
		if (c == fStartSequence[0]) {
		    if (sequenceDetected(scanner, fStartSequence, true)) {
			int currentTokenPositionIndex = 0;
			boolean allowed = false;
			do {
			    allowed = sequenceDetectedOnPosition(scanner,
				    currentTokenPositionIndex++, fBreaksOnEOF,
				    fBreaksOnEOL)
				    | 1 == ((optional >> currentTokenPositionIndex) & SackConstant.MASK_FIRST_BIT);
			} while (allowed
				&& currentTokenPositionIndex < allowedSequences.length);
			if (allowed) {
			    if (endSequenceDetected(scanner))
				return fToken;
			}
		    }
		}

	    }
	    /*
	     * If current rule failed, all the scanned characters should be
	     * unread.
	     */
	    return Token.UNDEFINED;
	} else {
	    return super.doEvaluate(scanner, resume);
	}
    }


    private void skipAllAllowedSequnce(ICharacterScanner scanner,
	    boolean endOfEOF) {
	char c;
	// do {
	// c = (char) scanner.read();
	// end on following condition
	// 1.reach end.
	// 2.current char is EOF and ending on EOF
	// } while (!endUntil(c));
	// && !(c == ICharacterScanner.EOF && endOfEOF));

	while (!endStrategy.end(scanner))
	    ;
	endStrategy.reset();
    }

    private boolean isAllAllowed(char[] sequence) {
	return Arrays.equals(sequence,
		SackConstant.REGULAR_PARTTEN_ANY_ONE.toCharArray());
    }

    private boolean sequenceDetectedOnPosition(ICharacterScanner scanner,
	    int currentTokenPositionIndex, boolean breakOnEOF,
	    boolean breakOnEOL) {
	char[] sequence = allowedSequences[currentTokenPositionIndex]
		.toCharArray();
	if (isAllAllowed(sequence)) {
	    skipAllAllowedSequnce(scanner, true);
	    return true;
	}

	// if (SKIP_WHILESPACE_) {
	// while (scanner.read() == ' ')
	// ;
	// scanner.unread();
	// }
	while (skipStrategy.skip((char) scanner.read()))
	    ;
	scanner.unread();

	if (scanner.read() == sequence[0]) {
	    return sequenceDetected(scanner, sequence, false);
	}
	return false;
    }

    @Override
    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
	// TODO Auto-generated method stub
	return super.evaluate(scanner, resume);
    }

    @Override
    public IToken getSuccessToken() {
	// TODO Auto-generated method stub
	return super.getSuccessToken();
    }
}
