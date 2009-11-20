package se.umu.cs.ldbn.client.io;

import com.google.gwt.user.client.Window;

import se.umu.cs.ldbn.client.io.LdbnParser.LDBN_TYPE;
import se.umu.cs.ldbn.client.io.LdbnParser.MSG_TYPE;

public class UserAdminList extends AbstractRequestSender {
	
	private static UserAdminList inst;
	
	public static UserAdminList get() {
		if (inst == null) {
			inst = new UserAdminList();
		}
		return inst;
	}
	
	private String adminId;
	private boolean toRemove;

	private UserAdminList() {
		super();
	}
	
	protected String getData() {
		String data = "&admin_id="+adminId;
		data = addSessionData(data);
		return data;
	}

	protected String getURL() {
		if (toRemove) {
			return Config.get().getRemoveUserFromAdminListScriptURL();
		} else {
			return Config.get().getAddUserToAdminListScriptURL();
		}
	}
	
	protected boolean handleResponce() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LDBN_TYPE.msg &&
				p.getMsgType() == MSG_TYPE.ok) {
			Window.alert(p.getMsgText());
			return true;
		}
		return false;
	}
	
	public void send(String adminId, boolean toRemove) {
		this.adminId = adminId;
		this.toRemove = toRemove;
		send();
	}

}
