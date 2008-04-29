package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.Main;

import com.google.gwt.user.client.ui.Label;

/**
 * Class representing a relation attribute.
 * @author NGG
 */
public final class RelationAttributeWidget extends Label  {
	
	/**
	 * name of the attribute.
	 */
	private String name;
	
	
	public RelationAttributeWidget(String name) {
		super(name);
		this.name = name;
		setStyleName("raw");
		Main.get().getDragController().makeDraggable(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
