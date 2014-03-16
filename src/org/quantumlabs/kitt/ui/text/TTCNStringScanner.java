package org.quantumlabs.kitt.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.quantumlabs.kitt.ui.editors.ColorManager;

public class TTCNStringScanner extends AbstractTTCNScanner {
	private String[] properties = { ITTCNColorConstants._STRING };

	public TTCNStringScanner(ColorManager kColorManager,
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
		IRule[] rules = new IRule[] {
				new SingleLineRule("\"", "\"",
						getToken(ITTCNColorConstants._STRING)),
				new SingleLineRule("'", "'",
						getToken(ITTCNColorConstants._STRING)) };
		return rules;
	}
}
