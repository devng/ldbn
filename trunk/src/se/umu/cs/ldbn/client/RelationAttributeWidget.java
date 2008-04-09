package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.ui.Label;

/**
 * Class representing a relation attribute.
 * @author NGG
 */
public class RelationAttributeWidget extends Label  {
	
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
