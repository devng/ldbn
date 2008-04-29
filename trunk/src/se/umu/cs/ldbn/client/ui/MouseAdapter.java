package se.umu.cs.ldbn.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.Widget;

/**
 * An abstract class for mouse events, so subclasses don't have to implement all
 * the methods from the interfaces but just the ones they need.
 *  
 * @author Nikolay Georgiev (ens07ngv@cs.umu.se)
 *
 */
public abstract class MouseAdapter implements MouseListener, MouseWheelListener, 
	ClickListener{

	public void onMouseDown(Widget sender, int x, int y) {}

	public void onMouseEnter(Widget sender) {}

	public void onMouseLeave(Widget sender) {}

	public void onMouseMove(Widget sender, int x, int y) {}

	public void onMouseUp(Widget sender, int x, int y) {}

	public void onMouseWheel(Widget sender, MouseWheelVelocity velocity) {}

	public void onClick(Widget sender) {}

}
