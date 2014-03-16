package org.quantumlabs.kitt.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.quantumlabs.kitt.ui.text.UnionDetecor;

public class Helper {
	private static UnionDetecor ID_DETECTOR = new UnionDetecor();

	public static String[] asString(Object... o) {
		String[] strings = new String[o.length];
		for (int idx = 0; idx < o.length; idx++) {
			strings[idx] = o[idx].toString();
		}
		return strings;
	}

	public static boolean isWordPart(char c) {
		return ID_DETECTOR.isWordPart(c);
	}

	public static boolean isWordStart(char c) {
		return ID_DETECTOR.isWordStart(c);
	}

	public static String arrayToString(Object[] objs) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (Object o : objs) {
			// if(o.getClass().isArray()){
			// stringBuilder.append()
			// }
			stringBuilder.append(o.toString());
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * Convert collection to array in given type.<strong>T should be compatible
	 * to E</strong>, otherwise, run time exception will be thrown.
	 * 
	 * @param collection
	 *            Input collection.
	 * @param clazz
	 *            Return type of array.
	 * */
	@SuppressWarnings("unchecked")
	public static <E, T> E[] asArray(Collection<T> collection, Class<E> clazz) {
		E[] es = (E[]) Array.newInstance(clazz.getComponentType(), collection.size());
		int i = 0;
		for (T e : collection) {
			es[i++] = (E) e;
		}
		return es;
	}

	/**
	 * Get values of declared variables of given object.
	 * 
	 * @param clazz
	 *            The type of given object
	 * @param The
	 *            instance of the type
	 * */
	public static Object[] getDeclaredSVariables(Class<?> clazz, Object target) {
		Field[] fields = clazz.getDeclaredFields();
		List<Object> vars = new ArrayList<Object>(fields.length);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				vars.add(field.get(target));
			} catch (Exception e) {
				if (Logger.isErrorEnable()) {
					Logger.logError(e);
				}
			}
		}
		return vars.toArray();
	}

	public static <E> boolean isXInY(E x, Iterable<E> y) {
		for (E e : y) {
			if (x == e || e.equals(x)) {
				return true;
			}
		}
		return false;
	}

	public static <E> boolean isXInY(E x, E[] y) {
		for (int i = 0; i < y.length; i++) {
			if (x == y[i] || y[i].equals(x)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSpecific(String value) {
		return "\r".equals(value) || "\n".equals(value) || "\t".equals(value);
	}

	public static boolean isSpecific(char c) {
		return '\r' == c || '\n' == c || '\t' == c;
	}

	public static boolean isWhitespace(String word) {
		if (word == null) {
			return false;
		}
		char[] original = word.toCharArray();
		for (char c : original) {
			if (c != ' ') {
				return false;
			}
		}
		return true;
	}

	public static boolean isLettersInUpperCase(int c) {
		return c >= 65 && c <= 90;
	}

	public static boolean isLettersInLowerCase(int c) {
		return c >= 97 && c <= 122;
	}

	public static boolean isUnderscore(int c) {
		return c == 95;
	}

	public static boolean isNumber(int c) {
		return c >= 48 && c <= 57;
	}

	public static String generateColorKey(String key) {
		return new StringBuilder().append(key).append("_OVERVIEW_ANNOTATION_COLOR_KEY").toString();
	}

	public static String generateTextKey(String key) {
		return new StringBuilder().append(key).append("_OVERVIEW_ANNOTATION_TEXT_KEY").toString();
	}

	public static String generateRulerKey(String key) {
		return new StringBuilder().append(key).append("_OVERVIEW_ANNOTATION_RULER_KEY").toString();
	}

	public static String generatePresentLayerKey(String key) {
		return new StringBuilder().append(key).append("_OVERVIEW_ANNOTATION_PRESENT_LAYER_KEY").toString();
	}

	public static String generateAnnotationHighlightKey(String key) {
		return new StringBuilder().append(key).append("_OVERVIEW_ANNOTATION_HIGHLIGHT_KEY").toString();
	}

	public static void runJob(final Runnable runnable, String name, int priority) {
		Job job = new Job(name) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					runnable.run();
					return Status.OK_STATUS;
				} catch (Exception e) {
					// Ignore.
					return new Status(Status.ERROR, SackConstant.PLUGIN_ID, String.format("Exception happened : %s",
							e.getMessage()));
				}
			}
		};
		job.setPriority(priority);
		job.schedule();
	}

	public static void runSyncDisplayRun(final Runnable runnable) {
		Display d = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		d.syncExec(runnable);
	}

	public static void runAsyncDisplayRun(final Runnable runnable) {
		Display d = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		d.asyncExec(runnable);
	}
}
