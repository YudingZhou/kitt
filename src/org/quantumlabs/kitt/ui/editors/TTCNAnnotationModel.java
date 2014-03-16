package org.quantumlabs.kitt.ui.editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentPartitioningChangedEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IDocumentPartitioningListenerExtension2;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;
import org.quantumlabs.kitt.core.util.SackConstant;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class TTCNAnnotationModel extends ResourceMarkerAnnotationModel {
	private final IMarker[] kMarkers;
	private int kMarkerCount;
	private IResource kResource;
	private IWorkspace kWorkspace;
	private boolean listenToMarkerChange;
	private boolean kDocumentChanged;
	private final InternalUpdateDriver kInternalDriver;
	private Position[] kAnnotationPositions;
	private Map<Position, Annotation> kPositionAnnotationMap;
	private int positionCount;
	private String ttcnFastPartitionContentCategory;
	private final Map<Position, Annotation> reversedMap;
	// return supported annotation ids, which will be shown in overview ruler.
	private String[] kOverviewAnnotationTypes = new String[] { SackConstant.ANNOTATION_TYPE_OCCURRENCE };// For
	// debugging,
	// more
	// annotation
	// could
	// be
	// added,
	// e.g.
	// caller,
	// callee
	// etc.
	/**
	 * FIXME : workaround for getting positions which belongs to
	 * CONTENT_TYPE_CATEGORY {@link FastPartitioner}
	 * */
	private static final String CONTENT_TYPES_CATEGORY = "__content_types_category"; //$NON-NLS-1$

	private class PositionDelta {
		static final int ADD = 0;
		static final int DELETE = 1;
		final static int ADD_AND_DELETE = 2;
		public int kind;
		public Position[] addDelta = new Position[0];
		public Position[] deleteDelta = new Position[0];
	}

	public void setOverviewAnnotationTypes(String[] types) {
		kOverviewAnnotationTypes = types;
	}

	private class InternalUpdateDriver implements IResourceChangeListener, IDocumentPartitioningListener,
			IDocumentPartitioningListenerExtension2 {

		@Override
		public void documentPartitioningChanged(IDocument document) {
		}

		@Override
		public void documentPartitioningChanged(DocumentPartitioningChangedEvent event) {
			try {
				IDocument doc = event.getDocument();
				if (doc == null) {
					return;
				}
				Position[] positions = doc.getPositions(ttcnFastPartitionContentCategory);
				PositionDelta delta = computeDelta(kAnnotationPositions, positions);
				switch (delta.kind) {
				case PositionDelta.ADD:
					doAddAnnotation(delta);
					break;
				case PositionDelta.DELETE:
					doRemoveAnnotation(delta);
					break;
				case PositionDelta.ADD_AND_DELETE:
					doAddAnnotation(delta);
					doRemoveAnnotation(delta);
					break;
				default:
					break;
				}
			} catch (BadPositionCategoryException e) {
				logEX(e);
			}
		}

		private void doRemoveAnnotation(PositionDelta delta) {
			for (int i = 0; i < delta.addDelta.length; i++) {
				TypedPosition position = (TypedPosition) delta.deleteDelta[i];
				removeAnnotation((Annotation) getAnnotationMap().get(position));
			}
		}

		private void doAddAnnotation(PositionDelta delta) {
			for (int i = 0; i < delta.addDelta.length; i++) {
				TypedPosition position = (TypedPosition) delta.addDelta[i];
				addAnnotation(new Annotation(position.getType(), !position.isDeleted(), position.getType()), position);
			}
		}

		private void logEX(BadPositionCategoryException e) {
			if (Logger.isErrorEnable()) {
				Logger.logError(getClass().getSimpleName(), e, "bad position category");
			}
		}

		@Override
		public void resourceChanged(IResourceChangeEvent event) {

		}
	}

	private PositionDelta computeDelta(Position[] origin, Position[] current) {
		int align = Math.min(origin.length, current.length);
		LinkedList<Position> add = new LinkedList<Position>();
		LinkedList<Position> delete = new LinkedList<Position>();
		for (int index = 0; index < align; index++) {
			if (!origin[index].equals(current[index])) {
				int index2 = index;
				int index3 = index;
				int lastDifferenceIdx = index;
				shit_orgin_one_step: for (; index2 < align; index2++) {
					for (; index3 < align;) {
						if (!origin[index2].equals(current[index3])) {
							add.add(current[index3]);
							index = index3++;
						} else {
							if (index3 == lastDifferenceIdx) {
								delete.add(origin[index2]);
							} else {
								index3 = lastDifferenceIdx;
							}
							continue shit_orgin_one_step;
						}
					}
				}
			}
		}

		return doComputeDelta(origin, current, add, delete);
	}

	private PositionDelta doComputeDelta(Position[] origin, Position[] current, LinkedList<Position> add,
			LinkedList<Position> delete) {
		PositionDelta delta = new PositionDelta();
		if (origin.length == 0) {
			Collections.addAll(add, current);
		}

		if (current.length == 0) {
			Collections.addAll(delete, origin);
		}

		if (add.size() > 0) {
			delta.addDelta = new Position[add.size()];
			System.arraycopy(add.toArray(), 0, delta.addDelta, 0, add.size());
			delta.kind = PositionDelta.ADD;
		}
		if (delete.size() > 0) {
			delta.deleteDelta = new Position[delete.size()];
			System.arraycopy(delete.toArray(), 0, delta.deleteDelta, 0, delete.size());
			delta.kind = delta.kind == PositionDelta.ADD ? PositionDelta.ADD_AND_DELETE : PositionDelta.DELETE;
		}
		return delta;
	}

	public TTCNAnnotationModel(IResource file) {
		super(file);
		kMarkers = new IMarker[SackConstant.MARKER_ANNOTATION_INIT_MARKER_COUNT];
		kInternalDriver = new InternalUpdateDriver();
		reversedMap = new HashMap<Position, Annotation>();
	}

	@Override
	protected IMarker[] retrieveMarkers() throws CoreException {
		return Arrays.copyOfRange(kMarkers, 0, kMarkerCount);
	}

	@Override
	protected void deleteMarkers(IMarker[] markers) throws CoreException {
		for (IMarker marker : markers) {
			for (int kIdx = 0; kIdx < kMarkerCount; kIdx++) {
				if (kMarkers[kIdx] == marker | kMarkers[kIdx].equals(marker)) {
					int numMoved = kMarkerCount - kIdx - 1;
					if (numMoved > 0) {
						System.arraycopy(kMarkers, kIdx + 1, kMarkers, kIdx, numMoved);
					}
					kMarkers[--kMarkerCount] = null;
				}
			}
		}
	}

	@Override
	protected MarkerAnnotation createMarkerAnnotation(IMarker marker) {
		// if (TTCNMarker.isTTCNMarker(marker)) {
		// return new TTCNMarker(marker);
		// }
		return super.createMarkerAnnotation(marker);
	}

	@Override
	protected void listenToMarkerChanges(boolean listen) {
		listenToMarkerChange = listen;
	}

	@Override
	protected boolean isAcceptable(IMarker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connected() {
		super.connected();
		// try {
		// if (ttcnFastPartitionContentCategory == null) {
		// for (String category : fDocument.getPositionCategories()) {
		// if (category.startsWith(CONTENT_TYPES_CATEGORY)) {
		// ttcnFastPartitionContentCategory = category;
		// break;
		// }
		// }
		// }
		// kAnnotationPositions = fDocument
		// .getPositions(ttcnFastPartitionContentCategory);
		// TypedPosition position = null;
		// for (int i = 0; i < kAnnotationPositions.length; i++) {
		// position = (TypedPosition) kAnnotationPositions[i];
		// addAnnotation(
		// new Annotation(position.getType(),
		// !position.isDeleted(), position.getType()),
		// position);
		// }
		// } catch (BadPositionCategoryException e) {
		// throw new StackTracableException(e);
		// }
	}

	// private int contains(Position position) {
	// for (int i = 0; i < positionCount; i++) {
	// if (kAnnotationPositions[i].equals(position))
	// return i;
	// }
	// return -1;
	// }
	//
	// private void removePosition(int index) {
	// System.arraycopy(kAnnotationPositions, index + 1, kAnnotationPositions,
	// index, positionCount - 1 - index);
	// positionCount--;
	// }
	//
	// private void addPosition(int index, Position position) {
	// ensureCapacity();
	// System.arraycopy(kAnnotationPositions, index, kAnnotationPositions,
	// index + 1, kMarkerCount - index);
	// kAnnotationPositions[index] = position;
	// }
	//
	// private void ensureCapacity() {
	// if (positionCount + 1 > kAnnotationPositions.length) {
	// Position[] newArray = new Position[kAnnotationPositions.length + 5];
	// System.arraycopy(kAnnotationPositions, 0, newArray, 0,
	// kAnnotationPositions.length);
	// kAnnotationPositions = newArray;
	// }
	// }

	@Override
	public void connect(IDocument document) {
		super.connect(document);
		document.addDocumentPartitioningListener(kInternalDriver);
	}

	public InternalUpdateDriver getUpdateDriver() {
		return kInternalDriver;
	}

	@Override
	protected void addAnnotation(Annotation annotation, Position position, boolean fireModelChanged)
			throws BadLocationException {
		super.addAnnotation(annotation, position, fireModelChanged);
		reversedMap.put(position, annotation);
	}

	@Override
	protected void removeAllAnnotations(boolean fireModelChanged) {
		super.removeAllAnnotations(fireModelChanged);
		reversedMap.clear();
	}

	@Override
	protected void removeAnnotation(Annotation annotation, boolean fireModelChanged) {
		super.removeAnnotation(annotation, fireModelChanged);
		Position p = getPosition(annotation);
		// if (!(annotation.equals(reversedMap.remove(p)))) {
		// throw new IllegalStateException(
		// "try to remove annotation, but reversed map doesn't contain the position ： "
		// + p);
		// }
	}

	/**
	 * 
	 * Get all annotation ids which will be shown in overview ruler
	 */
	public String[] getSupportedOverviewAnnotationTypes() {
		return kOverviewAnnotationTypes;
	}
}
