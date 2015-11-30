package se.umu.cs.ldbn.client.io;

import java.util.ArrayList;
import java.util.List;

import se.umu.cs.ldbn.client.ui.user.UserData;

public final class Login extends AbstractRequestSender {

	private static Login inst;

	public static Login get() {
		if(inst == null) {
			inst = new Login();
		}
		return inst;
	}

	private List<LoginListener> listeners;
	private String url;
	private String data;
	private boolean isSendLogin;

	private Login() {
		listeners = new ArrayList<>();
		isSendLogin = false;
		url = null;
		data = null;
	}

	public void addListener(LoginListener l) {
		if(l != null) {
			listeners.add(l);
		}
	}

	public void removeListener(LoginListener l) {
		if(l != null) {
			listeners.remove(l);
		}
	}

	public void sendLogin(String userName, String userPassBase64) {
		url = Config.get().getUserLoginScriptURL();
		data = "user_name="+userName+"&user_pass="+userPassBase64;
		isSendLogin = true;
		send();
	}

	public void sendKillSession(String sessionID) {
		url = Config.get().getKillSessionScriptURL();
		data = "id_session="+sessionID;
		isSendLogin = false;
		send();
	}

	protected String getData() {
		return data;
	}

	protected String getURL() {
		return url;
	}

	protected boolean handleResponse() {
		if(isSendLogin) {
			return handleLoginResponce();
		} else {
			return handleKillSessionResponse();
		}
	}

	private boolean handleLoginResponce() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.session) {
			UserData ud = UserData.get();
			ud.setId(p.getUserId());
			ud.setSession(p.getSessionId());
			ud.setEmail(p.getEmail());
			ud.setAdmin(p.isAdmin());
			ud.setSuperUser(p.isSuperUser());
			for (LoginListener l : listeners) {
				if (l != null)
					l.onLoginSuccess();
			}
			return true;
		}
		return false;
	}

	private boolean handleKillSessionResponse() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
			if(p.getMsgType() == LdbnParser.MSG_TYPE.ok) {
				UserData.get().clear();
				for (LoginListener l : listeners) {
					l.onSessionKilled();
				}
				return true;
			}
		}
		return false;
	}
}
