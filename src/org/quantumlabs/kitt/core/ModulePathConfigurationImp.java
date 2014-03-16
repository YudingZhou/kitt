package org.quantumlabs.kitt.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.quantumlabs.kitt.core.util.CallBackPro;
import org.quantumlabs.kitt.core.util.ResourceWalker;

public class ModulePathConfigurationImp implements IModulePathConfiguration {

	private final IProject project;
	private IResource[] dependencies = new IResource[0];
	private IFile[] sources = new IFile[0];
	private IFile[] resources = new IFile[0];
	private String name;

	public ModulePathConfigurationImp(IProject project) throws CoreException {
		this.project = project;
		name = project.getName();
		initialize();
	}

	public void initialize() throws CoreException {
		final ArrayList<IFile> tempContainer = new ArrayList<IFile>();
		ResourceWalker walker = new ResourceWalker(project);
		walker.walk(new CallBackPro<Boolean>() {
			@Override
			public Boolean call(Object... args) {
				// if current resource is file, then return.
				return (args[0] instanceof IFile);
			}
		}, new CallBackPro<Void>() {
			public Void call(Object... args) {
				if (args[0] instanceof IFile && ((IFile) args[0]).getName().endsWith(".ttcn")) {
					tempContainer.add((IFile) args[0]);
				}
				return null;
			}
		});
		sources = tempContainer.toArray(new IFile[tempContainer.size()]);
		resources = new IFile[0];
		dependencies = new IResource[0];
	}

	private List<IContainer> collectPackage(IContainer container) {
		IResource[] members;
		boolean isPackage = false;
		List<IContainer> packages = new ArrayList<IContainer>();
		try {
			members = container.members();
			for (IResource member : members) {
				if (member instanceof IFolder) {
					packages.addAll(collectPackage((IContainer) member));
				} else if (!isPackage && member instanceof IFile && ((IFile) member).getName().endsWith(".ttcn")) {
					isPackage = true;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		if (isPackage) {
			packages.add(container);
		}

		return packages;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO, e.g. include, exclude...
	}

	@Override
	public IFile[] getSource() {
		return sources;
	}

	@Override
	public IResource[] getDependency() {
		return dependencies;
	}

	@Override
	public IFile[] getResource() {
		return resources;
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public String getName() {
		return name;
	}
}
