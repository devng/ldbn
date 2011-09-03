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

import se.umu.cs.ldbn.client.ui.visualization.VisualizationWindow;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.RootPanel;

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

		DOMUtil.fastSetElementPosition(context.draggable.getElement(), desiredLeft,
				desiredTop);
	}

	@Override
	public void dragStart() {
		super.dragStart();
		context.draggable.removeStyleName("dragdrop-dragging");
		context.draggable.getElement().getStyle()
			.setProperty("overflow", "visible");
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
		if(context.draggable instanceof VisualizationWindow && Common.isAgentIE()) {
			((VisualizationWindow) context.draggable).reDrawCanvas();
		}
	}
}
