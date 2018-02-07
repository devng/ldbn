package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.ClientMain;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Label;

/**
 * Class representing a relation attribute.
 */
public class SingleAttributeWidget extends Label implements HasName {

	/**
	 * name of the attribute.
	 */
	protected String name;


	public SingleAttributeWidget(String name) {
		super(name);
		this.name = name;
		setStyleName("saw");
		ClientMain.get().getDragController().makeDraggable(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setText(name);
	}
}
