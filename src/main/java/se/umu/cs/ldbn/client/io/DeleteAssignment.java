package se.umu.cs.ldbn.client.io;

import se.umu.cs.ldbn.client.io.LdbnParser.LDBN_TYPE;
import se.umu.cs.ldbn.client.io.LdbnParser.MSG_TYPE;

import com.google.gwt.user.client.Window;

public final class DeleteAssignment extends AbstractRequestSender {

	private static DeleteAssignment inst;
	
	public static DeleteAssignment get() {
		if (inst == null) {
			inst = new DeleteAssignment();
		}
		return inst;
	}
	
	private DeleteAssignment() {
		super();
	}
	
	private String assignmentId;
	
	
	protected String getData() {
		String data = "&assignment_id="+assignmentId;
		data = addSessionData(data);
		return data;
	}

	protected String getURL() {
		return Config.get().getDeleteAssignmentScriptURL();
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
	
	public void send(String assignmentId) {
		this.assignmentId = assignmentId;
		send();
	}

}
