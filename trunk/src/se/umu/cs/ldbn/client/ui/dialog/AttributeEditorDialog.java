package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;

import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;

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
		DomainTable atts = CreateAssignmentWidget.get().getDomain();
		atts.setNewNames(names);
	}
	
	protected void onInfoButClicked() {
		HelpDialog.get().showInfo("example.html");
	}
	
	public void center() {
		super.center();
		setErrorMsg("");
		AttributeTextArea a = getAttributeTextArea();
		a.setText("");
		DomainTable domain = 
			CreateAssignmentWidget.get().getDomain();
		String[] names = domain.getAttNames();
		for (String s : names) {
			a.appendAttributes(s);
		}
	}
}