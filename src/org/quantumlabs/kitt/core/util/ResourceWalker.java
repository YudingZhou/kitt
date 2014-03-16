package org.quantumlabs.kitt.core.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;

public class ResourceWalker {
	private IResource root;
	private CallBackPro<Boolean> endPoint;
	private CallBackPro<Void> errorHandler;
	private CallBackPro<Void> visitor;

	public ResourceWalker(IResource root) {
		this.root = root;
		endPoint = new CallBackPro<Boolean>() {
			@Override
			public Boolean call(Object... args) {
				return false;
			}
		};
		visitor = new CallBackPro<Void>() {
			@Override
			public Void call(Object... args) {
				return null;
			}
		};
	}

	public void walk(CallBackPro<Boolean> end, CallBackPro<Void> visitor) {
		this.endPoint = end;
		this.visitor = visitor;
		internalWalk(root);
	}

	public void setErrorHandler(Callback errorHandler) {
	}

	private void internalWalk(IResource current) {
		try {
			visitor.call(current);
			if ((Boolean) endPoint.call(current)) {
				return;
			}
			if (current instanceof IContainer) {
				for (IResource child : ((IContainer) current).members()) {
					internalWalk(child);
				}
			}
		} catch (Exception e) {
			if (errorHandler != null) {
				errorHandler.call(e, current);
			}
		}
	}
}
