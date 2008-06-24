package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.ui.sa.RelationAttributesWidget;

public class AttributesEditorDialog extends KeyEditorDialog {
	
	private static AttributesEditorDialog inst;
	
	public static AttributesEditorDialog get() {
		if(inst == null) {
			inst = new AttributesEditorDialog();
		}
		return inst;
	}
	
	private AttributesEditorDialog() {
		super("Add/Remove Attributes", "Separate the attributes by commas.<BR>" +
				"You can also use Drag'n'Drop.");
	}
	
	protected void handleAttributeSet(AttributeSet as) {
		raw.setAttributes(as);
	}
	
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		this.raw = raw;
		kta.setAttributeSet(raw.getAttributes());
	}
}
