/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package se.umu.cs.ldbn.client.ui.window;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetArea;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * DragController used for drag-and-drop operations where a draggable widget or
 * drag proxy is temporarily picked up and dragged around the boundary panel. Be
 * sure to register a {@link DropController} for each drop target.
 * 
 * @see #registerDropController(DropController)
 */
public class WindowDragController extends AbstractDragController {

	/**
	 * Private implementation class to store widget information while dragging.
	 */
	private static class SavedWidgetInfo {

		/**
		 * The initial draggable index for indexed panel parents.
		 */
		int initialDraggableIndex;

		/**
		 * Initial draggable CSS margin.
		 */
		String initialDraggableMargin;

		/**
		 * Initial draggable parent widget.
		 */
		Widget initialDraggableParent;

		/**
		 * Initial location for absolute panel parents.
		 */
		Location initialDraggableParentLocation;
	}

	/**
	 * CSS style name applied to movable panels.
	 */
	private static final String PRIVATE_CSS_MOVABLE_PANEL = "dragdrop-movable-panel";

	/**
	 * CSS style name applied to drag proxies.
	 */
	private static final String PRIVATE_CSS_PROXY = "dragdrop-proxy";

	/**
	 * The implicit boundary drop controller.
	 */
	private final BoundaryDropController boundaryDropController;

	private int boundaryOffsetX;

	private int boundaryOffsetY;

	private boolean dragProxyEnabled = false;

//	private final MyDropControllerCollection dropControllerCollection;

	private final ArrayList<DropController> dropControllerList = new ArrayList<DropController>();

	private int dropTargetClientHeight;

	private int dropTargetClientWidth;

	private Widget movablePanel;

	private HashMap<Widget, SavedWidgetInfo> savedWidgetInfoMap;

	private MyDropController myDropController;

	private class MyDropController implements DropController {

		public Widget getDropTarget() {
			return RootPanel.get();
		}

		public void onDrop(DragContext context) {
			RootPanel.get().add(context.draggable, context.desiredDraggableX,
					context.desiredDraggableY);
		}

		public void onEnter(DragContext context) {
		}

		public void onLeave(DragContext context) {
		}

		public void onMove(DragContext context) {
		}

		public void onPreviewDrop(DragContext context) throws VetoDragException {
		}

	}

	/**
	 * Create a new pickup-and-move style drag controller. Allows widgets or a
	 * suitable proxy to be temporarily picked up and moved around the specified
	 * boundary panel.
	 * 
	 * <p>
	 * Note: An implicit {@link BoundaryDropController} is created and registered
	 * automatically.
	 * </p>
	 * 
	 * @param boundaryPanel the desired boundary panel or <code>RootPanel.get()</code>
	 *                      if entire document body is to be the boundary
	 * @param allowDroppingOnBoundaryPanel whether or not boundary panel should
	 *            allow dropping
	 */
	public WindowDragController() {
		super(RootPanel.get());
		AbsolutePanel boundaryPanel = RootPanel.get();
		boolean allowDroppingOnBoundaryPanel = true;
		boundaryDropController = newBoundaryDropController(boundaryPanel,
				allowDroppingOnBoundaryPanel);
		registerDropController(boundaryDropController);
		myDropController = new MyDropController();
	}

	private void checkGWTIssue1813(Widget child, AbsolutePanel parent) {
		if (!GWT.isScript()) {
			if (child.getElement().getOffsetParent() != parent.getElement()) {
				DOMUtil
						.reportFatalAndThrowRuntimeException("The boundary panel for this drag controller does not appear to have"
								+ " 'position: relative' CSS applied to it."
								+ " This may be due to custom CSS in your application, although this"
								+ " is often caused by using the result of RootPanel.get(\"some-unique-id\") as your boundary"
								+ " panel, as described in GWT issue 1813"
								+ " (http://code.google.com/p/google-web-toolkit/issues/detail?id=1813)."
								+ " Please star / vote for this issue if it has just affected your application."
								+ " You can often remedy this problem by adding one line of code to your application:"
								+ " boundaryPanel.getElement().getStyle().setProperty(\"position\", \"relative\");");
			}
		}
	}

	@Override
	public void dragEnd() {
		assert context.finalDropController == null == (context.vetoException != null);
		if (context.vetoException != null) {
			if (!getBehaviorDragProxy()) {
				restoreSelectedWidgetsLocation();
			}
		} else {
			context.dropController.onDrop(context);
		}
		context.dropController.onLeave(context);
		context.dropController = null;

		if (!getBehaviorDragProxy()) {
			restoreSelectedWidgetsStyle();
		}
		movablePanel.removeFromParent();
		movablePanel = null;
		super.dragEnd();
	}

	public void dragMove() {
		int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
		int desiredTop = context.desiredDraggableY - boundaryOffsetY;
		if (getBehaviorConstrainedToBoundaryPanel()) {
			desiredLeft = Math
					.max(0, Math.min(desiredLeft, dropTargetClientWidth
							- context.draggable.getOffsetWidth()));
			desiredTop = Math.max(0, Math.min(desiredTop,
					dropTargetClientHeight
							- context.draggable.getOffsetHeight()));
		}

		DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
				desiredTop);
	}

	@Override
	public void dragStart() {
		super.dragStart();
		context.draggable.removeStyleName("dragdrop-dragging");
		WidgetLocation currentDraggableLocation = new WidgetLocation(
				context.draggable, context.boundaryPanel);
		if (getBehaviorDragProxy()) {
			movablePanel = newDragProxy(context);
			context.boundaryPanel.add(movablePanel, currentDraggableLocation
					.getLeft(), currentDraggableLocation.getTop());
			checkGWTIssue1813(movablePanel, context.boundaryPanel);
		} else {
			saveSelectedWidgetsLocationAndStyle();
			AbsolutePanel container = new AbsolutePanel();
			container.getElement().getStyle()
					.setProperty("overflow", "visible");

			container.setPixelSize(context.draggable.getOffsetWidth(),
					context.draggable.getOffsetHeight());
			context.boundaryPanel.add(container, currentDraggableLocation
					.getLeft(), currentDraggableLocation.getTop());
			checkGWTIssue1813(container, context.boundaryPanel);

			int draggableAbsoluteLeft = context.draggable.getAbsoluteLeft();
			int draggableAbsoluteTop = context.draggable.getAbsoluteTop();
			HashMap<Widget, CoordinateLocation> widgetLocation = new HashMap<Widget, CoordinateLocation>();
			for (Widget widget : context.selectedWidgets) {
				widgetLocation.put(widget, new CoordinateLocation(widget
						.getAbsoluteLeft(), widget.getAbsoluteTop()));
			}

			context.dropController = getIntersectDropController(context.mouseX,
					context.mouseY);
			if (context.dropController != null) {
				context.dropController.onEnter(context);
			}

			for (Widget widget : context.selectedWidgets) {
				Location location = widgetLocation.get(widget);
				int relativeX = location.getLeft() - draggableAbsoluteLeft;
				int relativeY = location.getTop() - draggableAbsoluteTop;
				container.add(widget, relativeX, relativeY);
			}
			movablePanel = container;
		}
		movablePanel.addStyleName(PRIVATE_CSS_MOVABLE_PANEL);

		// one time calculation of boundary panel location for efficiency during
		// dragging
		Location widgetLocation = new WidgetLocation(context.boundaryPanel,
				null);
		boundaryOffsetX = widgetLocation.getLeft()
				+ DOMUtil.getBorderLeft(context.boundaryPanel.getElement());
		boundaryOffsetY = widgetLocation.getTop()
				+ DOMUtil.getBorderTop(context.boundaryPanel.getElement());

		dropTargetClientWidth = DOMUtil.getClientWidth(getBoundaryPanel()
				.getElement());
		dropTargetClientHeight = DOMUtil.getClientHeight(getBoundaryPanel()
				.getElement());
	}

	/**
	 * Whether or not dropping on the boundary panel is permitted.
	 * 
	 * @return <code>true</code> if dropping on the boundary panel is allowed
	 */
	public boolean getBehaviorBoundaryPanelDrop() {
		return boundaryDropController.getBehaviorBoundaryPanelDrop();
	}

	/**
	 * Determine whether or not this controller automatically creates a drag proxy
	 * for each drag operation.
	 * 
	 * @return <code>true</code> if drag proxy behavior is enabled
	 */
	public boolean getBehaviorDragProxy() {
		return dragProxyEnabled;
	}

	private DropController getIntersectDropController(int x, int y) {
		//DropController dropController = dropControllerCollection.getIntersectDropController(x, y);
		//return dropController != null ? dropController : boundaryDropController;
		return myDropController;
	}

	/**
	 * Create a new BoundaryDropController to manage our boundary panel as a drop
	 * target. To ensure that draggable widgets can only be dropped on registered
	 * drop targets, set <code>allowDroppingOnBoundaryPanel</code> to <code>false</code>.
	 *
	 * @param boundaryPanel the panel to which our drag-and-drop operations are constrained
	 * @param allowDroppingOnBoundaryPanel whether or not dropping is allowed on the boundary panel
	 * @return the new BoundaryDropController
	 */
	protected BoundaryDropController newBoundaryDropController(
			AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
		return new BoundaryDropController(boundaryPanel,
				allowDroppingOnBoundaryPanel);
	}

	/**
	 * Called by {@link PickupDragController#dragStart()} to allow subclasses to
	 * provide their own drag proxies.
	 * 
	 * @param context the current drag context
	 * @return a new drag proxy
	 */
	protected Widget newDragProxy(DragContext context) {
		AbsolutePanel container = new AbsolutePanel();
		container.getElement().getStyle().setProperty("overflow", "visible");

		WidgetArea draggableArea = new WidgetArea(context.draggable, null);
		for (Widget widget : context.selectedWidgets) {
			WidgetArea widgetArea = new WidgetArea(widget, null);
			Widget proxy = new SimplePanel();
			proxy.setPixelSize(widget.getOffsetWidth(), widget
					.getOffsetHeight());
			proxy.addStyleName(PRIVATE_CSS_PROXY);
			container.add(proxy,
					widgetArea.getLeft() - draggableArea.getLeft(), widgetArea
							.getTop()
							- draggableArea.getTop());
		}

		return container;
	}

	@Override
	public void previewDragEnd() throws VetoDragException {
		assert context.finalDropController == null;
		assert context.vetoException == null;
		try {
			try {
				// may throw VetoDragException
				context.dropController.onPreviewDrop(context);
				context.finalDropController = context.dropController;
			} finally {
				// may throw VetoDragException
				super.previewDragEnd();
			}
		} catch (VetoDragException ex) {
			context.finalDropController = null;
			throw ex;
		}
	}

	/**
	 * Register a new DropController, representing a new drop target, with this
	 * drag controller.
	 * 
	 * @see #unregisterDropController(DropController)
	 * 
	 * @param dropController the controller to register
	 */
	public void registerDropController(DropController dropController) {
		dropControllerList.add(dropController);
	}

	/**
	 * Restore the selected widgets to their original location.
	 * @see #saveSelectedWidgetsLocationAndStyle()
	 * @see #restoreSelectedWidgetsStyle()
	 */
	protected void restoreSelectedWidgetsLocation() {
		for (Widget widget : context.selectedWidgets) {
			SavedWidgetInfo info = savedWidgetInfoMap.get(widget);

			// TODO simplify after enhancement for issue 1112 provides InsertPanel
			// interface
			// http://code.google.com/p/google-web-toolkit/issues/detail?id=1112
			if (info.initialDraggableParent instanceof AbsolutePanel) {
				((AbsolutePanel) info.initialDraggableParent).add(widget,
						info.initialDraggableParentLocation.getLeft(),
						info.initialDraggableParentLocation.getTop());
			} else if (info.initialDraggableParent instanceof HorizontalPanel) {
				((HorizontalPanel) info.initialDraggableParent).insert(widget,
						info.initialDraggableIndex);
			} else if (info.initialDraggableParent instanceof VerticalPanel) {
				((VerticalPanel) info.initialDraggableParent).insert(widget,
						info.initialDraggableIndex);
			} else if (info.initialDraggableParent instanceof FlowPanel) {
				((FlowPanel) info.initialDraggableParent).insert(widget,
						info.initialDraggableIndex);
			} else if (info.initialDraggableParent instanceof SimplePanel) {
				((SimplePanel) info.initialDraggableParent).setWidget(widget);
			} else {
				throw new RuntimeException(
						"Unable to handle initialDraggableParent "
								+ info.initialDraggableParent.getClass()
										.getName());
			}
		}
	}

	/**
	 * Restore the selected widgets with their original style.
	 * @see #saveSelectedWidgetsLocationAndStyle()
	 * @see #restoreSelectedWidgetsLocation()
	 */
	protected void restoreSelectedWidgetsStyle() {
		for (Widget widget : context.selectedWidgets) {
			SavedWidgetInfo info = savedWidgetInfoMap.get(widget);
			widget.getElement().getStyle().setProperty("margin",
					info.initialDraggableMargin);
		}
	}

	/**
	 * Save the selected widgets' current location in case they much
	 * be restored due to a cancelled drop.
	 * @see #restoreSelectedWidgetsLocation()
	 */
	protected void saveSelectedWidgetsLocationAndStyle() {
		savedWidgetInfoMap = new HashMap<Widget, SavedWidgetInfo>();
		for (Widget widget : context.selectedWidgets) {
			SavedWidgetInfo info = new SavedWidgetInfo();
			info.initialDraggableParent = widget.getParent();

			// TODO simplify after enhancement for issue 1112 provides InsertPanel
			// interface
			// http://code.google.com/p/google-web-toolkit/issues/detail?id=1112
			if (info.initialDraggableParent instanceof AbsolutePanel) {
				info.initialDraggableParentLocation = new WidgetLocation(
						widget, info.initialDraggableParent);
			} else if (info.initialDraggableParent instanceof HorizontalPanel) {
				info.initialDraggableIndex = ((HorizontalPanel) info.initialDraggableParent)
						.getWidgetIndex(widget);
			} else if (info.initialDraggableParent instanceof VerticalPanel) {
				info.initialDraggableIndex = ((VerticalPanel) info.initialDraggableParent)
						.getWidgetIndex(widget);
			} else if (info.initialDraggableParent instanceof FlowPanel) {
				info.initialDraggableIndex = ((FlowPanel) info.initialDraggableParent)
						.getWidgetIndex(widget);
			} else if (info.initialDraggableParent instanceof SimplePanel) {
				// save nothing
			} else {
				throw new RuntimeException(
						"Unable to handle 'initialDraggableParent instanceof "
								+ info.initialDraggableParent.getClass()
										.getName()
								+ "'; Please create your own "
								+ PickupDragController.class.getName()
								+ " and override saveSelectedWidgetsLocationAndStyle(), restoreSelectedWidgetsLocation() and restoreSelectedWidgetsStyle()");
			}

			info.initialDraggableMargin = DOM.getStyleAttribute(widget
					.getElement(), "margin");
			widget.getElement().getStyle().setProperty("margin", "0px");
			savedWidgetInfoMap.put(widget, info);
		}
	}

	/**
	 * Set whether or not widgets may be dropped anywhere on the boundary panel.
	 * Set to <code>false</code> when you only want explicitly registered drop
	 * controllers to accept drops. Defaults to <code>true</code>.
	 * 
	 * @param allowDroppingOnBoundaryPanel <code>true</code> to allow dropping
	 */
	public void setBehaviorBoundaryPanelDrop(
			boolean allowDroppingOnBoundaryPanel) {
		boundaryDropController
				.setBehaviorBoundaryPanelDrop(allowDroppingOnBoundaryPanel);
	}

	/**
	 * Set whether or not this controller should automatically create a drag proxy
	 * for each drag operation.
	 * 
	 * @param dragProxyEnabled <code>true</code> to enable drag proxy behavior
	 */
	public void setBehaviorDragProxy(boolean dragProxyEnabled) {
		this.dragProxyEnabled = dragProxyEnabled;
	}

	/**
	 * Unregister a DropController from this drag controller.
	 * 
	 * @see #registerDropController(DropController)
	 * 
	 * @param dropController the controller to register
	 */
	public void unregisterDropController(DropController dropController) {
		dropControllerList.remove(dropController);
	}
}
