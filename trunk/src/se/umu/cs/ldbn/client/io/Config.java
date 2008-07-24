package se.umu.cs.ldbn.client.io;

import com.google.gwt.core.client.GWT;

public final class Config {
	
	public static final String[] DEFAULT_VALUES = {
		"php/list.php",
		"php/load.php",
		"php/save.php",
		"php/userlogin.php",
		"php/userregister.php",
		"php/userchange.php",
		"php/killsession.php",
		"php/resendactivation.php",
		"php/sendnewpassword.php"
	};
	
	private static Config inst = null;
	
	public static Config get() {
		if (inst == null) {
			inst = new Config();
		}
		return inst;
	}
	
	private Config() {
	}
	
	public String getListScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[0];
	}
	
	public String getLoadScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[1];
	}
	
	public String getSaveScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[2];
	}
	
	public String getUserLoginScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[3];
	}
	
	public String getUserRegisterScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[4];
	}
	
	public String getUserChangeScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[5];
	}
	
	public String getKillSessionScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[6];
	}
	
	public String getResendActivationScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[7];
	}
	
	public String getSendNewPasswordScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[8];
	}
	
}
