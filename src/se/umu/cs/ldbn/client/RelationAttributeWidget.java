package se.umu.cs.ldbn.client;

import com.google.gwt.user.client.ui.Label;

public class RelationAttributeWidget extends Label  {
	
	private String name;
	
	public RelationAttributeWidget(String name) {
		super(name);
		this.name = name;
		setStyleName("raw");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
