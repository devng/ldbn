package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.ui.sa.RelationAttributesWidget;

public class AttributesEditorDialog extends KeyEditorDialog {
	
	private static AttributesEditorDialog inst;
	
	private boolean setAllAttributesToKeyAttributes;
	
	public static AttributesEditorDialog get() {
		if(inst == null) {
			inst = new AttributesEditorDialog();
		}
		return inst;
	}
	
	private AttributesEditorDialog() {
		super(I18N.constants().addRemoveAtt());
		infoButton.setFileBase("addrematt");
		setAllAttributesToKeyAttributes = false;
	}
	
	@Override
	protected void handleAttributeSet(AttributeSet as) {
		raw.setAttributes(as);
		if(setAllAttributesToKeyAttributes) {
			raw.setKey(as.clone());
		}
	}
	
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw, boolean setAllAttributesToKeyAttributes) {
		this.raw = raw;
		kta.setText("");
		kta.setAttributeSet(raw.getAttributes());
		this.setAllAttributesToKeyAttributes = setAllAttributesToKeyAttributes;
	}
	
	@Override
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		this.setCurrentRelationAttributesWidget(raw, false);
	}
	
	@Override
	protected String getHelpMessage() {
		return I18N.constants().separateAtt()+"<BR>" +
			I18N.constants().useDND();
	}
}
