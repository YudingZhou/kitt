package org.quantumlabs.kitt.ui.util.exception;

@SuppressWarnings("serial")
public class StackTracableException extends Warning {
    public StackTracableException(Throwable cause) {
	super(cause);
    }

    public StackTracableException(String message) {
	super(message);
    }

    public StackTracableException(String message, Throwable cause) {
	super(message, cause);
    }
}
