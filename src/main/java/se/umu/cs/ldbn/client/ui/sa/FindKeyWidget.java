package se.umu.cs.ldbn.client.ui.sa;

import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.dialog.AttributesEditorDialog;
import se.umu.cs.ldbn.shared.core.AttributeSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FindKeyWidget extends Composite implements ClickHandler {
	
	private Button setKeyButton;
	private RelationAttributesWidget key;
	private VerticalPanel mainPanel;
	private HorizontalPanel keyHolderPanel;
	
	public FindKeyWidget() { 
		mainPanel = new VerticalPanel();
		keyHolderPanel = new HorizontalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setKeyButton = new Button("Set a Key");
		setKeyButton.setStyleName("min-cov-but");
		setKeyButton.addClickHandler(this);
		hp.add(setKeyButton);
		hp.add(new InfoButton("find-key"));
		
		mainPanel.add(hp);
		key = new RelationAttributesWidget();
		
		key.setVisible(false);
		keyHolderPanel.add(key);
		mainPanel.add(keyHolderPanel);
		initWidget(mainPanel);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if(sender == setKeyButton) {
			key.setVisible(true);
			AttributesEditorDialog dialog = AttributesEditorDialog.get();
			dialog.center();
			dialog.setCurrentRelationAttributesWidget(key, true);
		}
		
	}
	
	public AttributeSet getKey() {
		return key.getKey();
	}
	
	public void setKey(AttributeSet newKey) {
		key.setAttributes(newKey.clone());
		key.setKey(newKey.clone());
		key.setVisible(true);
	}
	
	public void clearData() {
		key.setVisible(false);
		key.removeFromParent();
		key = new RelationAttributesWidget();
		key.setVisible(false);
		keyHolderPanel.add(key);
	}

}
