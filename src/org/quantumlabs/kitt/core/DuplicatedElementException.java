package org.quantumlabs.kitt.core;

import org.quantumlabs.kitt.ui.util.exception.Warning;

public class DuplicatedElementException extends Warning {
    private static final long serialVersionUID = 405973035599712335L;

    public DuplicatedElementException(String message) {
	super(message);
    }
}
