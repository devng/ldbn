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

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.RootPanel;
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
	 * CSS style name applied to movable panels.
	 *
	 */
	private int boundaryOffsetX;

	private int boundaryOffsetY;

	private int dropTargetClientHeight;

	private int dropTargetClientWidth;

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
//		AbsolutePanel boundaryPanel = RootPanel.get();
//		boolean allowDroppingOnBoundaryPanel = true;
//		boundaryDropController = newBoundaryDropController(boundaryPanel,
//				allowDroppingOnBoundaryPanel);
//		registerDropController(boundaryDropController);
		myDropController = new MyDropController();
	}

//	@Override
//	public void dragEnd() {
//		assert context.finalDropController == null == (context.vetoException != null);
//		if (context.vetoException != null) {
////			if (!getBehaviorDragProxy()) {
////				restoreSelectedWidgetsLocation();
////			}
//		} else {
//			//context.dropController.onDrop(context);
//		}
//		//context.dropController.onLeave(context);
//		context.dropController = null;
//
////		if (!getBehaviorDragProxy()) {
////			restoreSelectedWidgetsStyle();
////		}
//		//movablePanel.removeFromParent();
//		super.dragEnd();
//	}

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

		DOMUtil.fastSetElementPosition(context.draggable.getElement(), desiredLeft,
				desiredTop);
	}

	@Override
	public void dragStart() {
		super.dragStart();
		context.draggable.removeStyleName("dragdrop-dragging");
		context.dropController = myDropController;
		context.draggable.getElement().getStyle()
			.setProperty("overflow", "visible");
		
		//context.draggable.addStyleName(PRIVATE_CSS_MOVABLE_PANEL);

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
		
		//force on top
		int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
		int desiredTop = context.desiredDraggableY - boundaryOffsetY;
		RootPanel.get().add(context.draggable, desiredLeft, desiredTop);
	}

	/**
	 * Whether or not dropping on the boundary panel is permitted.
	 * 
	 * @return <code>true</code> if dropping on the boundary panel is allowed
	 */
//	public boolean getBehaviorBoundaryPanelDrop() {
//		return boundaryDropController.getBehaviorBoundaryPanelDrop();
//	}

	/**
	 * Create a new BoundaryDropController to manage our boundary panel as a drop
	 * target. To ensure that draggable widgets can only be dropped on registered
	 * drop targets, set <code>allowDroppingOnBoundaryPanel</code> to <code>false</code>.
	 *
	 * @param boundaryPanel the panel to which our drag-and-drop operations are constrained
	 * @param allowDroppingOnBoundaryPanel whether or not dropping is allowed on the boundary panel
	 * @return the new BoundaryDropController
	 */
//	protected BoundaryDropController newBoundaryDropController(
//			AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
//		return new BoundaryDropController(boundaryPanel,
//				allowDroppingOnBoundaryPanel);
//	}

	/**
	 * Register a new DropController, representing a new drop target, with this
	 * drag controller.
	 * 
	 * @see #unregisterDropController(DropController)
	 * 
	 * @param dropController the controller to register
	 */
//	public void registerDropController(DropController dropController) {
//		dropControllerList.add(dropController);
//	}

	/**
	 * Set whether or not widgets may be dropped anywhere on the boundary panel.
	 * Set to <code>false</code> when you only want explicitly registered drop
	 * controllers to accept drops. Defaults to <code>true</code>.
	 * 
	 * @param allowDroppingOnBoundaryPanel <code>true</code> to allow dropping
	 */
//	public void setBehaviorBoundaryPanelDrop(
//			boolean allowDroppingOnBoundaryPanel) {
//		boundaryDropController
//				.setBehaviorBoundaryPanelDrop(allowDroppingOnBoundaryPanel);
//	}

	/**
	 * Unregister a DropController from this drag controller.
	 * 
	 * @see #registerDropController(DropController)
	 * 
	 * @param dropController the controller to register
	 */
//	public void unregisterDropController(DropController dropController) {
//		dropControllerList.remove(dropController);
//	}
}
