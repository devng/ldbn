package se.umu.cs.ldbn.client.io;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Window;

public final class UserHelpSender extends AbstractRequestSender {

	private static UserHelpSender inst;
	
	public static UserHelpSender get() {
		if(inst == null) {
			inst = new UserHelpSender();
		}
		return inst;
	}
	
	private String url;
	private String data;
	
	private UserHelpSender() {
		url = null;
		data = null;
	}
	
	public void resendActivationEmail(String email) {
		url = Config.get().getResendActivationScriptURL();
		data = "user_mail="+email;
		send();
	}
	
	public void sendNewPassword(String email) {
		url = Config.get().getSendNewPasswordScriptURL();
		data = "user_mail="+email;
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
				String msg = p.getMsgText();
				Window.alert(msg);
				return true;	
			}
		}
		return false;
	}
}
