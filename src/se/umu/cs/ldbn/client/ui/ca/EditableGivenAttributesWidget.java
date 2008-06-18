package se.umu.cs.ldbn.client.ui.ca;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.core.AttributeNameTable;
import se.umu.cs.ldbn.client.core.AttributeNameTableListener;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialog;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialogCaller;
import se.umu.cs.ldbn.client.ui.sa.GivenAttributesWidget;

public final class EditableGivenAttributesWidget extends GivenAttributesWidget 
	implements AttributeNameTableListener, ClickListener, RenameDialogCaller {
	
	private Map<Image, SingleAttributeWidget> map;
	//used for renaming
	private SingleAttributeWidget currentSaw;
	
	
	public EditableGivenAttributesWidget() {
		super();
		att = new AttributeNameTable();
		att.registerListener(this);
		map = new HashMap<Image, SingleAttributeWidget>();
	}

	public void onDomainChange() {
		recalculateMainPanel();
	}
	
	protected void recalculateMainPanel() {
		mainPanel.clear();
		map.clear();
		for (String attName : att.getAttNames()) {
			SingleAttributeWidget saw = new SingleAttributeWidget(attName);
			VerticalPanel vp = new VerticalPanel();
			Image img = new Image("img/edit-name.png", 0, 0, 15, 15); 
			map.put(img, saw);
			img.addClickListener(this);
			CommonFunctions.setCursorPointer(img);
			vp.add(saw);
			vp.add(img);
			mainPanel.add(vp);
		}
	}
	
	public void onClick(Widget sender) {
		if (sender instanceof Image) {
			Image img = (Image) sender;
			SingleAttributeWidget saw = map.get(img);
			if (saw == null) {
				Log.warn("SingleAttributeWidget is null. Could not rename");
				return;
			}
			currentSaw = saw;
			RenameDialog.get().rename(this);
		}
	}
	
	public String getOldName() {
		return currentSaw.getName();
	}
	
	public void setNewName(String s) {
		att.renameAtt(currentSaw.getName(), s);
		currentSaw = null;
	}
	
	public Collection getTakenNames() {
		return map.values();
	}
}
