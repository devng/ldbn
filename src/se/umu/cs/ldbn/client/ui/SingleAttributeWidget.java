package se.umu.cs.ldbn.client.ui;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Label;

/**
 * Class representing a relation attribute.
 * @author NGG
 */
public final class SingleAttributeWidget extends Label  implements HasName {
	
	/**
	 * name of the attribute.
	 */
	private String name;
	
	
	public SingleAttributeWidget(String name) {
		super(name);
		this.name = name;
		setStyleName("saw");
		LdbnDragCotroller.get().makeDraggable(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setText(name);
	}
}
