package se.umu.cs.ldbn.client.io;

import java.util.List;

import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.utils.Common;

public class Comment extends AbstractRequestSender {
	
	private static Comment inst;

	public static Comment get() {
		if(inst == null) {
			inst = new Comment();
		}
		return inst;
	}

	private CommentCallback callback;
	
	protected String getData() {
		String data = "aid="+callback.getAssignmentID();
		String c = callback.getComment();
		if(c != null && !c.equals("")) {
			data += "&comment="+Common.base64encode(c);
			data += "&id_user="+UserData.get().getId();
			data += "&id_session="+UserData.get().getSession();
		}
		
		return data;
	}
	
	public void send(CommentCallback callback) {
		this.callback = callback;
		send();
	}

	protected String getURL() {
		return Config.get().getCommentScriptURL();
	}

	protected boolean handleResponce() {
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.comment) {
			List<CommentListEntry> c = p.getComments();
			String id = p.getAssignmentIDComent();
			callback.onCommentsReceived(c, id);
			return true;
		}
		return false;
	}

}
