package se.umu.cs.ldbn.client.io;

import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public abstract class AbstractRequestSender {
	
	protected abstract String getURL();
	protected abstract String getData();
	/* Always call this method after Common.checkResponse(response) */
	protected abstract boolean handleResponce();
	
	protected Method getMethod() {
		return RequestBuilder.POST;
	}
	
	protected String addSessionData(String data) {
		if (data == null) {
			data = new String();
		}
		if(data.length() > 0 && data.charAt(data.length()-1) != '&' ) {
			data = data+"&";
		} 
		UserData ud = UserData.get();
		data += "id_user="+ud.getId()+"&id_session="+ud.getSession();
		return data;
	}
	
	protected void send() {
		RequestBuilder rb = new RequestBuilder(getMethod(), getURL());
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		try {
			rb.sendRequest(getData(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (Common.checkResponse(response)) {
						boolean isOK = handleResponce();
						if(!isOK) {
							Log.warn("Invalid response by the server:\n");
							Log.warn(response.getText());
						} 
					}
				}
			});
		} catch (Exception e) {
			Log.error("Request failed", e);
		}
	}
}
