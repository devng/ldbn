package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.i18n.I18N;
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
		super(I18N.constants().addRemoveAtt(), I18N.constants().separateAtt()+"<BR>" +
				I18N.constants().useDND());
		infoButton.setFileBase("addrematt");
	}
	
	protected void handleAttributeSet(AttributeSet as) {
		raw.setAttributes(as);
	}
	
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		this.raw = raw;
		kta.setText("");
		kta.setAttributeSet(raw.getAttributes());
	}
}
