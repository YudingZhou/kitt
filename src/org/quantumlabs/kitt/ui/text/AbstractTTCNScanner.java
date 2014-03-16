package org.quantumlabs.kitt.ui.text;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.editors.ColorManager;

/**
 * AbstractTTCNScanner provides color management functionality, its subclass
 * focusing on token scanning.
 * */
public abstract class AbstractTTCNScanner extends RuleBasedScanner {
	private ColorManager kColorManager;
	private IPreferenceStore kPreferenceStore;
	private Map<String, IToken> kTokenMap;
	protected String[] tokenProperties;
	// current those property keys haven't been used, since all different kind
	// of front are using same font and color, it should be refactored later.
	private String[] kPropertyNamesBold;
	private String[] kPropertyNamesItalic;
	private String[] kPropertyNamesStrikethrough;
	private String[] kPropertyNamesUnderline;

	public AbstractTTCNScanner(ColorManager kColorManager,
			IPreferenceStore kPreferenceStore) {
		this.kColorManager = kColorManager;
		this.kPreferenceStore = kPreferenceStore;
		kTokenMap = new HashMap<String, IToken>();
	}

	public IToken getToken(String key) {
		IToken token = kTokenMap.get(key);
		return token;
	}

	protected abstract String[] getTokenProperties();

	public void initialize() {

		tokenProperties = getTokenProperties();
		int length = tokenProperties.length;
		kPropertyNamesBold = new String[length];
		kPropertyNamesItalic = new String[length];
		kPropertyNamesStrikethrough = new String[length];
		kPropertyNamesUnderline = new String[length];

		for (int i = 0; i < length; i++) {
			kPropertyNamesBold[i] = getBoldKey(tokenProperties[i]);
			kPropertyNamesItalic[i] = getItalicKey(tokenProperties[i]);
			kPropertyNamesStrikethrough[i] = getStrikethroughKey(tokenProperties[i]);
			kPropertyNamesUnderline[i] = getUnderlineKey(tokenProperties[i]);
		}

		// fNeedsLazyColorLoading = Display.getCurrent() == null;
		// for (int i = 0; i < length; i++) {
		// if (fNeedsLazyColorLoading)
		// addTokenWithProxyAttribute(fPropertyNamesColor[i],
		// fPropertyNamesBold[i], fPropertyNamesItalic[i],
		// fPropertyNamesStrikethrough[i],
		// fPropertyNamesUnderline[i]);
		// else
		// addToken(fPropertyNamesColor[i], fPropertyNamesBold[i],
		// fPropertyNamesItalic[i],
		// fPropertyNamesStrikethrough[i],
		// fPropertyNamesUnderline[i]);
		// }

		for (int i = 0; i < length; i++) {
			addToken(tokenProperties[i]);
		}

		initializeRules();
	}

	private void initializeRules() {
		setRules(createRules());
	}

	protected abstract IRule[] createRules();

	private IToken addToken(String key) {
		RGB rgb = PreferenceConverter.getColor(kPreferenceStore, key);
		kColorManager.unbind(key);
		kColorManager.bind(key, rgb);
		// TODO: for now, all different font style is belong to same key, it
		// should be refactored later for different font, different color.
		IToken token = new Token(createTextAttribute(key, getBoldKey(key),
				getItalicKey(key), getStrikethroughKey(key),
				getUnderlineKey(key)));

		return kTokenMap.put(key, token);
	}

	private TextAttribute createTextAttribute(String colorKey, String boldKey,
			String italicKey, String strikethroughKey, String underlineKey) {
		Color color = null;
		if (colorKey != null)
			color = kColorManager.getColor(colorKey);

		int style = kPreferenceStore.getBoolean(boldKey) ? SWT.BOLD
				: SWT.NORMAL;
		if (kPreferenceStore.getBoolean(italicKey))
			style |= SWT.ITALIC;

		if (kPreferenceStore.getBoolean(strikethroughKey))
			style |= TextAttribute.STRIKETHROUGH;

		if (kPreferenceStore.getBoolean(underlineKey))
			style |= TextAttribute.UNDERLINE;

		return new TextAttribute(color, null, style);
	}

	// TODO, TOBE refactored as a special key
	private String getBoldKey(String key) {
		return key;
	}

	// TODO, TOBE refactored as a special key
	private String getItalicKey(String key) {
		return key;
	}

	// TODO, TOBE refactored as a special key
	private String getStrikethroughKey(String key) {
		return key;
	}

	// TODO, TOBE refactored as a special key
	private String getUnderlineKey(String key) {
		return key;
	}

	private void addToken(String colorKey, String boldKey, String italicKey,
			String strikethroughKey, String underlineKey) {
		if (kColorManager != null && colorKey != null
				&& kColorManager.getColor(colorKey) == null) {
			RGB rgb = PreferenceConverter.getColor(kPreferenceStore, colorKey);

			// if (kColorManager instanceof IColorManagerExtension) {
			// IColorManagerExtension ext= (IColorManagerExtension)
			// fColorManager;
			// ext.unbindColor(colorKey);
			// ext.bindColor(colorKey, rgb);
			// }
		}
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		String key = event.getProperty();
		int index = indexOf(key);
		IToken token = null;
		if (index > -1) {
			token = getToken(tokenProperties[index]);
		}
		return token == null;
	}

	// get the index of given key
	private int indexOf(String key) {
		for (int i = 0; i < tokenProperties.length; i++) {
			if (key == tokenProperties[i] || key == kPropertyNamesBold[i]
					|| key == kPropertyNamesItalic[i]
					|| key == kPropertyNamesStrikethrough[i]
					|| key == kPropertyNamesUnderline[i]) {
				return i;
			}
		}
		return -1;
	}

	public void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		String key = event.getProperty();
		Token token = null;
		int index = indexOf(key);
		if (index > -1) {
			token = (Token) getToken(tokenProperties[index]);
			Object newValue = event.getNewValue();
			TextAttribute oldAttribute = (TextAttribute) token.getData();
			Color background = oldAttribute.getBackground();
			int style = oldAttribute.getStyle();
			Color color = null;
			if (newValue instanceof RGB) {
				kColorManager.unbind(key);
				kColorManager.bind(key, (RGB) newValue);
				color = kColorManager.getColor((RGB) newValue);
			} else if (newValue instanceof String) {
				color = kColorManager.getColor((String) newValue);
			} else {
				if (Logger.isWarningEnable()) {
					Logger.logWarning(getClass().getSimpleName(),
							"unknown PropertyChangeEvent!",
							event.getProperty(), event.getNewValue().toString());
				}
			}

			token.setData(new TextAttribute(color, background, style));
		}
	}
}
