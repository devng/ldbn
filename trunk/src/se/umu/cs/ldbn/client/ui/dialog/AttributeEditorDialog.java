package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;

import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.ui.ca.EditableGivenAttributesWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;

public final class AttributeEditorDialog extends BaseAttributeEditorDialog {

	//NO drag and drop here because it mackes no sence 
	private class EditTextArea extends AttributeTextArea {
		
		public void onDrop(DragContext context) {}
		
		public void onEnter(DragContext context) {}
		
		public void onLeave(DragContext context) {}
	}
	
	private EditTextArea eta;
	
	private static AttributeEditorDialog inst;
	
	private AttributeEditorDialog() {
		super("Add/Remove Attributes","Separate the attributes by commas.");
	}
	
	public static AttributeEditorDialog get() {
		if (inst == null) {
			inst = new AttributeEditorDialog();
		}
		return inst;
	}
	
	protected AttributeTextArea getAttributeTextArea() {
		if (eta == null) {
			eta = new EditTextArea();
		}
		return eta;
	}
	
	protected void onAddButClicked() {
		List<String> names = eta.parseAttributes();
		setErrorMsg("");
		if(eta.hasOmittedAttributes()) {
			setErrorMsg("Some attributes had invalid names.");
		}
		AttributeNameTable atts = CreateAssignmentWidget.get().getDomain();
		atts.setNewNames(names);
	}
	
	protected void onInfoButClicked() {
		// TODO Auto-generated method stub
	}
	
	public void center() {
		super.center();
		setErrorMsg("");
		AttributeTextArea a = getAttributeTextArea();
		a.setText("");
		AttributeNameTable domain = 
			CreateAssignmentWidget.get().getDomain();
		String[] names = domain.getAttNames();
		for (String s : names) {
			a.appendAttributes(s);
		}
	}
}
