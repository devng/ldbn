package se.umu.cs.ldbn.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Don't inherit from HorizontalPanel, since we have to have width = 100%, and
 * this will make the HorizontalPanel's widgets to appear not close to each 
 * other. Instead extend the Composite class.  
 * @author NGG
 *
 */
public class HeaderWidget extends Composite {
	
	private Grid grid;
	private HorizontalPanel hp;
	
	public HeaderWidget() {
		super();
		grid = new Grid(1,1);
		hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		grid.addStyleName("hw");
		grid.setWidget(0, 0, hp);
		initWidget(grid);
	}
	

	public void add(Widget w) {
		hp.add(w);
	}
	
	public HorizontalPanel getHorizontalPanel() {
		return hp;
	}
}
