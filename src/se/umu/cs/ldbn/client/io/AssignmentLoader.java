package se.umu.cs.ldbn.client.io;

import java.util.Map;

import se.umu.cs.ldbn.client.CommonFunctions;
import se.umu.cs.ldbn.client.ui.NewAssignmentDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class AssignmentLoader {
	
	private AssignmentLoader() {}
	
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
					if (CommonFunctions.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.assignment_list) {
							Map<String, String> list = p.getAssignmentList();
							NewAssignmentDialog.get().loadAssigmentList(list);
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

	public static void loadFromURL(String id) {
		String url = Config.get().getLoadScriptURL();
		String data = "assignment_id="+id;
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		try {
			rb.sendRequest(data, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}
	
				public void onResponseReceived(Request request, Response response) {
					if (CommonFunctions.checkResponse(response)) {
						//TODO Main.get() load new assignment
					}
				}
			});
		} catch (RequestException e) {
			Log.error("Request failed", e);
		}
	}
}
