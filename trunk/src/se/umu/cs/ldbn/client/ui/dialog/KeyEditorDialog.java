package se.umu.cs.ldbn.client.ui.dialog;

import java.util.List;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.core.AttributeSet;
import se.umu.cs.ldbn.client.core.AttributeSetIterator;
import se.umu.cs.ldbn.client.ui.AttributeTextArea;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.client.ui.sa.RelationAttributesWidget;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.ui.Widget;

public class KeyEditorDialog  extends BaseAttributeEditorDialog  {
	
	protected final class MyTextArea extends AttributeTextArea {

		public void onDrop(DragContext context) {
			Widget w = context.draggable;
			if(w == null) return;
			
			if(w instanceof SingleAttributeWidget) {
				this.appendAttributes(((SingleAttributeWidget)w).getText());
			} else if (w instanceof FDWidget) {
				FDWidget fdw = (FDWidget) w;
				List<String> att = fdw.getFD().getLHS().getAttributeNames();
				for (String str : att) {
					this.appendAttributes(str);
				}
				att = fdw.getFD().getRHS().getAttributeNames();
				for (String str : att) {
					this.appendAttributes(str);
				}
			} else if (w instanceof RelationAttributesWidget) {
				RelationAttributesWidget rw = (RelationAttributesWidget) w;
				List<String> names =  rw.getAttributes().getAttributeNames();
				for (String str : names) {
					this.appendAttributes(str);
				}
			}
		}
		
		public void setAttributeSet(AttributeSet att) {
			AttributeSetIterator asi = att.iterator();
			for (; asi.hasNext();) {
				String name = asi.next();
				this.appendAttributes(name);
			}
		}
	}
	private static KeyEditorDialog inst = null;
	
	public static KeyEditorDialog get() {
		if (inst == null) {
			inst = new KeyEditorDialog();
		}
		return inst;
	}

	protected RelationAttributesWidget raw;
	
	protected MyTextArea kta;
	
	private KeyEditorDialog() {
		super("Key Editor");
		infoButton.setFileBase("keyeditor");
	}
	
	protected KeyEditorDialog(String title) {
		super(title);
	}
	
	public void hide() {
		super.hide();
		Main.get().getDragController().unregisterDropController(getAttributeTextArea());
	}
	
	public void setCurrentRelationAttributesWidget(RelationAttributesWidget raw) {
		this.raw = raw;
		kta.setText("");
		kta.setAttributeSet(raw.getKey());
	}
	
	public void show() {
		super.show();
		Main.get().getDragController().registerDropController(getAttributeTextArea());
	}
	
	protected AttributeTextArea getAttributeTextArea() {
		if(kta == null)
			kta = new MyTextArea();
		return kta;
	}
	
	protected void onAddButClicked() {
		if(raw == null) {
			kta.setText("");
			return;
		}
		setErrorMsg("");
		List<String> atts = kta.parseAttributes();
		if(kta.hasOmittedAttributes()) {
			setErrorMsg("Some attributes had invalid names.");
		}
		AttributeSet as = new AttributeSet(SolveAssignmentWidget.get().getDomainTable());
		for (String str : atts) {
			as.addAtt(str);
		}
		handleAttributeSet(as);
		kta.setText("");
		hide();
	}
	
	protected void handleAttributeSet(AttributeSet as) {
		raw.setKey(as);
	}
	
	protected String getHelpMessage() {
		return "Give a valid candidate key for the relation.<BR>" +
		 "You can use Drag'n'Drop.";
	}
}
