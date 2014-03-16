package org.quantumlabs.kitt.ui.text;

import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class NonRuleBasedDamagerRepairer extends DefaultDamagerRepairer {

	private TextAttribute textAttribute;

	public NonRuleBasedDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}

	public void setTextAttribute(TextAttribute attribute) {
		textAttribute = attribute;
	}

	@Override
	public void createPresentation(TextPresentation presentation,
			ITypedRegion region) {
		addRange(presentation, region.getOffset(), region.getLength(),
				textAttribute);
	}

}
