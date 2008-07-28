package se.umu.cs.ldbn.client.ui.ca;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import se.umu.cs.ldbn.client.core.DomainTable;
import se.umu.cs.ldbn.client.core.DomainTableListener;
import se.umu.cs.ldbn.client.ui.SingleAttributeWidget;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialog;
import se.umu.cs.ldbn.client.ui.dialog.RenameDialogCallback;
import se.umu.cs.ldbn.client.ui.sa.GivenAttributesWidget;
import se.umu.cs.ldbn.client.util.Common;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


//DO not inherit from GivenAttributesWidget it is a bug in GWT and 
//this.att wont be set, and it will still be null after the constructor 
//has been called.
public final class EditableGivenAttributesWidget extends GivenAttributesWidget  
	implements DomainTableListener, ClickListener, RenameDialogCallback {
	
	private Map<Image, SingleAttributeWidget> map;
	//used for renaming
	private SingleAttributeWidget currentSaw;
	
	public EditableGivenAttributesWidget() {
		super();
		att = new DomainTable();
		att.addListener(this);
		map = new HashMap<Image, SingleAttributeWidget>();
	}
	

	
	public void onDomainChange() {
		recalculateMainPanel();
	}
	
	public void setDomain(DomainTable att) {
		throw new UnsupportedOperationException("You cannot change the domains");
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
			Common.setCursorPointer(img);
			vp.add(saw);
			vp.add(img);
			DOM.setStyleAttribute(img.getElement(), "margin", "2px");
			vp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
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

	
	public void onRenameCanceled() {
		currentSaw = null;
	}
}
