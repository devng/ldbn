package se.umu.cs.ldbn.client.ui;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.dialog.HelpDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class InfoButton extends Image implements ClickListener {
	
	private String fileBase;
	
	public InfoButton (String fileBase) {
		super("img/info.png");
		CommonFunctions.setCursorPointer(this);
		this.fileBase = fileBase;
		this.addClickListener(this);
	}
	
	public String getFileBase() {
		return fileBase;
	}
	
	public void setFileBase(String fileBase) {
		this.fileBase = fileBase;
	}
	
	public void onClick(Widget sender) {
		HelpDialog.get().showInfo(fileBase+".html");
//		WindowController windowController = new WindowController(RootPanel.get());
//	    windowController.getPickupDragController().addDragHandler(new DragHandler(){
//	    	
//			public void onDragEnd(DragEndEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void onDragStart(DragStartEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void onPreviewDragEnd(DragEndEvent event)
//					throws VetoDragException {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void onPreviewDragStart(DragStartEvent event)
//					throws VetoDragException {
//				// TODO Auto-generated method stub
//				
//			}
//	    	
//	    });
//	    
//	    HTML header1 = new HTML("An draggable &amp; resizable panel");
//	    HTML html1 = new HTML("Test 1<br> TEst 2<BR> TESt3");
//	    html1.addStyleName("demo-resize-html");
//	    WindowPanel windowPanel1 = new WindowPanel(windowController, header1, html1, true);
//	    RootPanel.get().add(windowPanel1, 20, 20);

	}

}
