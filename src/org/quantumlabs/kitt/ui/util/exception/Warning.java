package org.quantumlabs.kitt.ui.util.exception;

/**
 * Warning is used in DETA release to tracing any unclear problem or essential
 * bug.
 * */
public class Warning extends RuntimeException {
    private static final long serialVersionUID = 4668156765172709444L;
    protected StackTraceElement[] stackTrack;
    static final String MSG = "[WARNING IS TRACING A UNCLEAR PROBLEM IN DETA RELEASE]:";

    public Warning() {
	stackTrack = getStackTrace();
    }

    public Warning(Throwable cause) {
	super(cause);
	stackTrack = getStackTrace();
    }

    public Warning(String message) {
	super(MSG + message);
	stackTrack = getStackTrace();
    }

    public Warning(String message, Throwable cause) {
	super(message, cause);
	stackTrack = getStackTrace();
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(getClass().getName());
	builder.append(":");
	builder.append(getLocalizedMessage() == null ? ""
		: getLocalizedMessage());
	builder.append(stacksToString(getStackTrace()));
	return builder.toString();
    }

    public static String stacksToString(StackTraceElement... elements) {
	if (elements.length > 0) {
	    StringBuilder builder = new StringBuilder();
	    for (StackTraceElement element : elements) {
		builder.append("\n");
		builder.append(element);
	    }
	    return builder.toString();
	} else {
	    return "";
	}
    }
}
