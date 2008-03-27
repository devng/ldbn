package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.Widget;

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
