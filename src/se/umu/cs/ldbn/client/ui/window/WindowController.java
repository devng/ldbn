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

import com.google.gwt.user.client.ui.AbsolutePanel;

public final class WindowController {

  private final AbsolutePanel boundaryPanel;

  private WindowDragController pickupDragController;

  private ResizeDragController resizeDragController;

  public WindowController(AbsolutePanel boundaryPanel) {
    this.boundaryPanel = boundaryPanel;

    pickupDragController = new WindowDragController();
    pickupDragController.setBehaviorConstrainedToBoundaryPanel(false);
    pickupDragController.setBehaviorMultipleSelection(false);
    pickupDragController.setConstrainWidgetToBoundaryPanel(false);
    //pickupDragController.setBehaviorBoundaryPanelDrop(true);
    //pickupDragController.setBehaviorDragProxy(true);

    resizeDragController = new ResizeDragController(boundaryPanel);
    resizeDragController.setBehaviorConstrainedToBoundaryPanel(false);
    resizeDragController.setBehaviorMultipleSelection(false);
  }

  public AbsolutePanel getBoundaryPanel() {
    return boundaryPanel;
  }

  public WindowDragController getPickupDragController() {
    return pickupDragController;
  }

  public ResizeDragController getResizeDragController() {
    return resizeDragController;
  }
}
