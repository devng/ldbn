package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.core.Assignment;
import se.umu.cs.ldbn.client.io.Config;
import se.umu.cs.ldbn.client.io.LdbnParser;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.utils.Common;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

public class UploadDialog extends OkCancelDialog implements SubmitHandler, SubmitCompleteHandler {

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

	protected Widget getDialogContentWidget() {
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
		form.addSubmitHandler(this);
		form.addSubmitCompleteHandler(this);

		return form;
	}

	protected void onOkClick() {
		form.submit();
	}

	@Override
	public void onSubmit(SubmitEvent event) {
		Object sender = event.getSource();
		if (sender == form) {
			String fileName = upload.getFilename();
			if (fileName == null || fileName.length() < 5 || !fileName.endsWith(".xml")) {
				Window.alert("Only files with XML extension are supported.");
				event.cancel();
			}
		}
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		Object sender = event.getSource();
		if (sender == form) {
			// When the form submission is successfully completed, this event is
			// fired. Assuming the service returned a response of type text/html,
			// we can get the result text here (see the FormPanel documentation for
			// further explanation).
			String response = event.getResults();
			response = event.getResults().replaceFirst("@", "");
			response = response.replaceAll("<PRE>", "");
			response = response.replaceAll("</PRE>", "");
			response = response.replaceAll("&gt;", ">");
			response = response.replaceAll("&lt;", "<");
			URL.decode(response);
			// IE bug - makes all tags to upper case??!!
			response = response.replaceAll("LDBN", "ldbn");
			response = response.replaceAll("ATT", "att");
			response = response.replaceAll("FD", "fd");
			response = response.replaceAll("LHS", "lhs");
			response = response.replaceAll("RHS", "rhs");
			response = response.replaceAll("FDATT", "fdatt");
			Common.checkResponceTextOnly(response);
			Assignment a = LdbnParser.get().getAssignment();
			if (a == null) {
				Window.alert("LDBN Type is invalid.");
				return;
			}
			CreateAssignmentWidget.get().onAssignmentLoaded(a);
			hide();
		}
	}
}
