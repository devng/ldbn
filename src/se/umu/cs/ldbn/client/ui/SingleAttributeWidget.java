package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.ui.Label;

/**
 * Class representing a relation attribute.
 * @author NGG
 */
public final class SingleAttributeWidget extends Label  {
	
	/**
	 * name of the attribute.
	 */
	private String name;
	
	
	public SingleAttributeWidget(String name) {
		super(name);
		this.name = name;
		setStyleName("saw");
		Main.get().getDragController().makeDraggable(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
