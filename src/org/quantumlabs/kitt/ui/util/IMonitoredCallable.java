package org.quantumlabs.kitt.ui.util;

import java.util.concurrent.Callable;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IMonitoredCallable<T> extends Callable<T>{

	void setMonitor(IProgressMonitor monitor);

}
