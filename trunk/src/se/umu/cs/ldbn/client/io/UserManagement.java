package se.umu.cs.ldbn.client.io;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

import se.umu.cs.ldbn.client.ui.user.UserData;

public final class UserManagement extends AbstractRequestSender {

	private static UserManagement inst;
	
	public static UserManagement get() {
		if(inst == null) {
			inst = new UserManagement();
		}
		return inst;
	}
	
	private ArrayList<UserManagementListener> listeners;
	private String url;
	private String data;
	
	private UserManagement() {
		listeners = new ArrayList<UserManagementListener>();
	}
	
	public void addListener(UserManagementListener umc) {
		if (umc != null) {
			listeners.add(umc);
		}
	}
	
	public void removeListener(UserManagementListener umc) {
		if (umc != null) {
			listeners.remove(umc);
		}
	}
	
	public void sendUserRegistration(String newUserName, String newUserPassMD5, 
			String newUserEmail) {
		url = Config.get().getUserRegisterScriptURL();
		data = "user_name="+newUserName+"&user_pass="+newUserPassMD5+"&user_mail="+newUserEmail;
		send();
	}
	
	public void sendUserChange(String newUserName, String newUserPassMD5, String newUserEmail) {
		url = Config.get().getUserChangeScriptURL();
		data = "";
		if(newUserName != null && !newUserName.equals("")) {
			data +=  "user_name="+newUserName+"&";
		}
		if(newUserPassMD5 != null && !newUserPassMD5.equals("")) {
			data +=  "user_pass="+newUserPassMD5+"&";
		}
		if(newUserEmail != null && !newUserEmail.equals("")) {
			data +=  "user_mail="+newUserEmail+"&";
		}
		if(data.equals("")) {
			for (UserManagementListener l : listeners) {
				l.setOKStatus();
			}
			return;
		}
		data = addSessionData(data);
		send();
	}

	protected String getData() {
		return data;
	}

	protected String getURL() {
		return url;
	}

	protected boolean handleResponce() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
			if(p.getMsgType() == LdbnParser.MSG_TYPE.ok) {
				for (UserManagementListener l : listeners) {
						l.setOKStatus();
				}
				return true;	
			}
		}
		return false;
	}
}
