package se.umu.cs.ldbn.client.io;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.io.LdbnParser.LDBN_TYPE;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class Login {
	
	private static Login inst;
	
	public static Login get() {
		if(inst == null) {
			inst = new Login();
		}
		return inst;
	}
	
	private List<LoginCallback> listeners;
	
	private Login() {
		listeners = new ArrayList<LoginCallback>();
	}
	
	public void addListener(LoginCallback l) {
		if(l != null) {
			listeners.add(l);
		}
	}
	
	public void removeListener(LoginCallback l) {
		if(l != null) {
			listeners.remove(l);
		}
	}
	
	public void sendLogin(String userName, String userPassMD5) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
				Config.get().getUserLoginScriptURL());
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		String data = "user_name="+userName+"&user_pass="+userPassMD5;
		try {
			rb.sendRequest(data, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (Common.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.session) {
							String userID = LdbnParser.get().getUserId();
							String sessionID = LdbnParser.get().getSessionId();
							for (LoginCallback l : listeners) {
								l.setSessionData(userID, sessionID);
							}
						} else {
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
	
	public void sendLogout(String userID, String sessionID) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
				Config.get().getUserLogoutScriptURL());
		rb.setHeader("Content-type", "application/x-www-form-urlencoded");
		String data = "id_user="+userID+"&id_session="+sessionID;
		try {
			rb.sendRequest(data, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					Log.error("Request Callback Error", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (Common.checkResponse(response)) {
						LdbnParser p = LdbnParser.get();
						if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
							if(LdbnParser.get().getMsgType() == LdbnParser.MSG_TYPE.ok) {
								for (LoginCallback l : listeners) {
									l.setSessionKilled();
								}
							}
						} else {
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
