package org.quantumlabs.kitt.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.quantumlabs.kitt.core.util.SyntaxConstants;
import org.quantumlabs.kitt.ui.editors.ColorManager;

public class TTCNMultiLineCommentScanner extends AbstractTTCNScanner {

	private String[] properties = { ITTCNColorConstants._TTCN_MULTIPLE_LINE_COMMENT };

	public TTCNMultiLineCommentScanner(ColorManager kColorManager,
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
		IRule[] rules = new IRule[] { new MultiLineRule(
				SyntaxConstants.PARTITION_MULTIL_LINE_BEGIN,
				SyntaxConstants.PARTITION_MULTIL_LINE_END,
				getToken(ITTCNColorConstants._TTCN_MULTIPLE_LINE_COMMENT)) };
		return rules;
	}
}
