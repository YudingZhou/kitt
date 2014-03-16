package org.quantumlabs.kitt.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.quantumlabs.kitt.core.util.SyntaxConstants;
import org.quantumlabs.kitt.ui.editors.ColorManager;

public class TTCNSingleLineCommentScanner extends AbstractTTCNScanner {

	private String[] properties = { ITTCNColorConstants._TTCN_SINGLE_LINE_COMMENT };

	public TTCNSingleLineCommentScanner(ColorManager kColorManager,
			IPreferenceStore kPreferenceStore) {
		super(kColorManager, kPreferenceStore);
		initialize();
	}

	@Override
	protected String[] getTokenProperties() {
		return properties;
	}

	@Override
	protected IRule[] createRules() {
		IRule[] rules = new IRule[] { new EndOfLineRule(
				SyntaxConstants.PARTITION_SINGLE_LINE_COMMENT,
				getToken(ITTCNColorConstants._TTCN_SINGLE_LINE_COMMENT)) };
		return rules;
	}

}
