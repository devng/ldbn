package se.umu.cs.ldbn.client.ui.window;

import java.util.HashMap;

import se.umu.cs.ldbn.client.ui.window.WindowPanel.DirectionConstant;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public final class ResizeDragController extends AbstractDragController {

  private static final int MIN_WIDGET_WIDTH  = 200;
  private static final int MIN_WIDGET_HEIGHT = 100;

  private HashMap<Widget, DirectionConstant> directionMap = new HashMap<>();

  private WindowPanel windowPanel = null;

  public ResizeDragController(AbsolutePanel boundaryPanel) {
    super(boundaryPanel);
  }

  public void dragMove() {
    int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
    if ((direction & WindowPanel.DIRECTION_NORTH) != 0) {
      int delta = context.draggable.getAbsoluteTop() - context.desiredDraggableY;
      if (delta != 0) {
        int contentHeight = windowPanel.getContentHeight();
        int newHeight = Math.max(contentHeight + delta, MIN_WIDGET_HEIGHT);
        if (newHeight != contentHeight) {
          windowPanel.moveBy(0, contentHeight - newHeight);
        }
        windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
      }
    } else if ((direction & WindowPanel.DIRECTION_SOUTH) != 0) {
      int delta = context.desiredDraggableY - context.draggable.getAbsoluteTop();
      int newHeight = windowPanel.getContentHeight() + delta;
      if (delta != 0 && newHeight > MIN_WIDGET_HEIGHT) {
        windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
      }
    }
    if ((direction & WindowPanel.DIRECTION_WEST) != 0) {
      int delta = context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
      if (delta != 0) {
        int contentWidth = windowPanel.getContentWidth();
        int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_WIDTH);
        if (newWidth != contentWidth) {
          windowPanel.moveBy(contentWidth - newWidth, 0);
        }
        windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
      }
    } else if ((direction & WindowPanel.DIRECTION_EAST) != 0) {
      int delta = context.desiredDraggableX - context.draggable.getAbsoluteLeft();
      int newWidth = windowPanel.getContentWidth() + delta;
      if (delta != 0 && newWidth > MIN_WIDGET_WIDTH) {
        windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
      }
    }
  }

  @Override
  public void dragStart() {
    super.dragStart();
    windowPanel = (WindowPanel) context.draggable.getParent().getParent();
  }

  public void makeDraggable(Widget widget, WindowPanel.DirectionConstant direction) {
    super.makeDraggable(widget);
    directionMap.put(widget, direction);
  }

  protected BoundaryDropController newBoundaryDropController(AbsolutePanel boundaryPanel,
      boolean allowDroppingOnBoundaryPanel) {
    if (allowDroppingOnBoundaryPanel) {
      throw new IllegalArgumentException();
    }
    return new BoundaryDropController(boundaryPanel, false);
  }

  private DirectionConstant getDirection(Widget draggable) {
    return directionMap.get(draggable);
  }
}
