package se.umu.cs.ldbn.client.io;

import java.util.List;
import java.util.Map;

import se.umu.cs.ldbn.client.ui.dialog.LoadAssignmentDialog;
import se.umu.cs.ldbn.client.util.Common;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public final class AssignmentLoader extends AbstractRequestSender {
	
	private static AssignmentLoader inst;
	
	public static AssignmentLoader get() {
		if (inst == null) {
			inst = new AssignmentLoader();
		}
		return inst;
	}
	
	private String data;
	private String url;
	private boolean isListRequest;
	private AssignmentLoaderCallback currentAlc;
	
	private AssignmentLoader() {
		data = null;
		url = null;
		isListRequest = false;
	}

	public void loadAssignmentList() {
		url = Config.get().getListScriptURL();
		data = "";
		isListRequest = true;
		send();
	}
	
	public void loadFromURL(String id, AssignmentLoaderCallback alc) {
		url = Config.get().getLoadScriptURL();
		currentAlc = alc;
		data = "assignment_id="+id;
		isListRequest = false;
		send();
	}

	protected String getData() {
		return data;
	}
	
	protected String getURL() {
		return url;
	}

	protected boolean handleResponce() {
		if(isListRequest) {
			LdbnParser p = LdbnParser.get();
			if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.assignment_list) {
				List<AssignmentListEntry> list = p.getAssignmentList();
				LoadAssignmentDialog.get().loadAssigmentList(list);
				return true;
			}
			return false;
		} else {
			LdbnParser p = LdbnParser.get();
			if(p.getLastLdbnType() == LdbnParser.LDBN_TYPE.assignment) {
				currentAlc.onAssignmentLoaded(p.getAssignment());
				return true;
			}
			return false;
		}
	}
}
