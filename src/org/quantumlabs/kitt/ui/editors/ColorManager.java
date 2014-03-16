package org.quantumlabs.kitt.ui.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	private static ColorManager instance = new ColorManager();

	public static ColorManager instance() {
		if (instance == null) {
			synchronized (ColorManager.class) {
				instance = new ColorManager();
			}
		}
		return instance;
	}

	protected Map<String, RGB> kColorTable;
	protected Map<Display, Map<RGB, Color>> kDisplayTable;
	private boolean kAutoDisposeOnDisplayDispose;

	public ColorManager() {
		kColorTable = new HashMap<String, RGB>(10);
		kDisplayTable = new HashMap<Display, Map<RGB, Color>>();
	}

	public void dispose(Display display) {
		 Map<RGB, Color> colorsMap = kDisplayTable.get(display);
		 if (colorsMap != null){
			 Iterator<Color> colors = colorsMap.values().iterator();
				while (colors.hasNext()) {
					colors.next().dispose();
				}
		 }
	}

	public Color getColor(String colorKey) {
		RGB rgb = kColorTable.get(colorKey);
		if (rgb == null) {
			rgb = StringConverter.asRGB(colorKey);
			kColorTable.put(colorKey, rgb);
		}
		return getColor(rgb);
	}

	public void bind(String key, RGB rgb) {
		kColorTable.put(key, rgb);
	}

	public void unbind(String key) {
		kColorTable.remove(key);
	}

	public Color getColor(RGB rgb) {

		if (rgb == null)
			return null;

		final Display display = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		Map<RGB, Color> colorTable = kDisplayTable.get(display);
		if (colorTable == null) {
			colorTable = new HashMap<RGB, Color>(10);
			kDisplayTable.put(display, colorTable);
			if (kAutoDisposeOnDisplayDispose) {
				display.disposeExec(new Runnable() {
					public void run() {
						dispose(display);
					}
				});
			}
		}

		Color color = colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}

		return color;
	}

	public void dispose() {
		dispose(Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent());
	}
}
