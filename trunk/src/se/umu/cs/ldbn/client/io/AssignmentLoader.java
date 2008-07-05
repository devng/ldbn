package se.umu.cs.ldbn.client.io;

import java.util.Map;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public final class AssignmentLoader {
	
	private AssignmentLoader() {}
	
	private static AssignmentLoaderCallback currentAlc;
	
	public static void loadAssignmentList() {
		String url = Config.get().getListScriptURL();
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
			rb.setCallback(new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}
				
				public void onResponseReceived(Request request,
						Response response) {
					if (Common.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.assignment_list) {
							Map<String, String> list = p.getAssignmentList();
							LoadAssignmentDialog.get().loadAssigmentList(list);
						} else {
							Window.alert("The returned xml is not of type " +
									"<i>assignment_list</i><br>The list of " +
									"assigments cannot be displayed.");
						}
					}
				}
			});
				rb.send();
		} catch (Exception e) {
			Log.error("Request failed", e);
		}
	}
	
	public static void loadFromURL(String id, AssignmentLoaderCallback alc) {
		String url = Config.get().getLoadScriptURL();
		currentAlc = alc;
		String data = "assignment_id="+id;
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		try {
			rb.sendRequest(data, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}
	
				public void onResponseReceived(Request request, Response response) {
					if (Common.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if(p.getLastLdbnType() == LdbnParser.LDBN_TYPE.assignment) {
							currentAlc.onAssignmentLoaded(p.getAssignment());
						} else {
							Log.warn("Invalid response by the server:\n");
							Log.warn(response.getText());
							currentAlc.onAssignmentLoadError();
						}
					} else {
						currentAlc.onAssignmentLoadError();
					}
				}
			});
		} catch (RequestException e) {
			Log.error("Request failed", e);
		}
	}
}
