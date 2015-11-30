package se.umu.cs.ldbn.client.io;

import java.util.List;

import se.umu.cs.ldbn.client.ui.admin.UserListDialog;

public final class UserList extends AbstractRequestSender {

	private static UserList inst;

	public static UserList get() {
		if (inst == null) {
			inst = new UserList();
		}
		return inst;
	}

	private boolean toRemove;

	private UserList() {
		toRemove = false;
	}

	protected String getData() {
		String data = "";
		data = addSessionData(data);
		return data;
	}

	protected String getURL() {
		return Config.get().getListAllUsersScriptURL();
	}

	protected boolean handleResponse() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.user_list) {
			List<UserListEntry> data = p.getUserList();
			UserListDialog.get().loadUserList(data, toRemove);
			return true;
		}
		return false;
	}

	public void send(boolean toRemove) {
		super.send();
		this.toRemove = toRemove;
	}

}
