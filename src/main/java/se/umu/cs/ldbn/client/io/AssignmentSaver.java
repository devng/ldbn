package se.umu.cs.ldbn.client.io;

import java.util.List;

import se.umu.cs.ldbn.shared.core.Assignment;
import se.umu.cs.ldbn.shared.core.AttributeSet;
import se.umu.cs.ldbn.shared.core.DomainTable;
import se.umu.cs.ldbn.shared.core.FD;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public final class AssignmentSaver extends AbstractRequestSender {

	private static AssignmentSaver inst;

	public static AssignmentSaver get() {
		if (inst == null) {
			inst = new AssignmentSaver();
		}
		return inst;
	}

	private String data;

	private AssignmentSaver() {}

	public void sendToSaveScript(String xml, String name, Integer id) {
		data = "id="+id+"&name="+name+"&xml="+xml;
		data = addSessionData(data);
		send();
	}

	protected String getData() {
		return data;
	}

	protected String getURL() {
		return Config.get().getSaveScriptURL();
	}

	protected boolean handleResponse() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
			Window.alert(p.getMsgText());
			return true;
		}
		return false;
	}
}
