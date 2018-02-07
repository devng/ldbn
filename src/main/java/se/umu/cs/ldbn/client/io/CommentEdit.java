package se.umu.cs.ldbn.client.io;

import se.umu.cs.ldbn.client.model.UserModel;
import se.umu.cs.ldbn.client.utils.Common;

public final class CommentEdit extends AbstractRequestSender {

	private static CommentEdit inst;

	public static CommentEdit get() {
		if(inst == null) {
			inst = new CommentEdit();
		}
		return inst;
	}

	private boolean delete;
	private String text;
	private String commentId;
	private CommentEditCallback cec;

	public void send(CommentEditCallback cec, String commentId, String text, boolean delete) {
		this.commentId = commentId;
		this.text = text;
		this.delete = delete;
		this.cec = cec;
		send();
	}


	protected String getData() {
		String data = "";
		data += "&id_user="+ UserModel.get().getId();
		data += "&id_session=" + UserModel.get().getSession();
		data += "&id_comment=" + commentId;
		if (text != null && !text.equals("")) {
			data += "&comment=" + Common.base64encode(text);
		}
		if (delete) {
			data += "&delete=true";
		}

		return data;
	}

	protected String getURL() {
		return Config.get().getCommentEditScriptURL();
	}

	protected boolean handleResponse() {
		boolean result = false;
		LdbnParser p = LdbnParser.get();
		if (p.getLastLdbnType() == LdbnParser.LDBN_TYPE.msg) {
			result = p.getMsgType().equals(LdbnParser.MSG_TYPE.ok);
		}
		cec.onReseive(result);
		return result;
	}

}
