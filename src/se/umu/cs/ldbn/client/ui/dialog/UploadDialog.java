package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.Assignment;
import se.umu.cs.ldbn.client.io.Config;
import se.umu.cs.ldbn.client.io.LdbnParser;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.util.Common;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Widget;

public class UploadDialog extends OkCancelDialog {
	
	private static UploadDialog inst;
	
	public static UploadDialog get() {
		if (inst == null) {
			inst = new UploadDialog();
		}
		return inst;
	}
	
	private FormPanel form;
	private FileUpload upload;

	private UploadDialog() {
		super("Upload LDBN XML");
	}

	protected Widget getContentWidget() {
	    upload = new FileUpload();
	    upload.setName("clientfile");
		form = new FormPanel();
	    form.setAction(Config.get().getUploadScriptURL());
	    // Because we're going to add a FileUpload widget, we'll need to set the
	    // form to use the POST method, and multipart MIME encoding.
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    // Create a panel to hold all of the form widgets.
	    form.setWidget(upload);
	    // Add an event handler to the form.
	    form.addFormHandler(new FormHandler() {
	      public void onSubmit(FormSubmitEvent event) {
	        // This event is fired just before the form is submitted. We can take
	        // this opportunity to perform validation.
	        String fileName = upload.getFilename();
	        if(fileName == null || fileName.length() < 5 || !fileName.endsWith(".xml")) {
	        	Window.alert("Only files with XML extension are supported.");
	        	event.setCancelled(true);
	        	return;
	        }
	      }

	      public void onSubmitComplete(FormSubmitCompleteEvent event) {
	  		hide();
	        // When the form submission is successfully completed, this event is
	        // fired. Assuming the service returned a response of type text/html,
	        // we can get the result text here (see the FormPanel documentation for
	        // further explanation).
	        String response = event.getResults().replaceFirst("@", "");
	        response = response.replaceAll("<PRE>", "");
	        response = response.replaceAll("</PRE>", "");
	        response = response.replaceAll("&gt;", ">");
	        response = response.replaceAll("&lt;", "<");
	        URL.decode(response);
	        
	        Common.checkResponceTextOnly(response);
	        Assignment a = LdbnParser.get().getAssignment();
	        if(a == null) {
	        	Window.alert("LDBN Type is invalid.");
	        	return;
	        } 
	        CreateAssignmentWidget.get().onAssignmentLoaded(a);
	      }
	    });
		return form;
	}

	protected void onOkClick() {
		form.submit();
	}
}
