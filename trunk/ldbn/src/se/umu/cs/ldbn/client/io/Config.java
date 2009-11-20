package se.umu.cs.ldbn.client.io;

import com.google.gwt.core.client.GWT;

public final class Config {
	
	public static final String[] DEFAULT_VALUES = {
		"../php/list.php",                     // 0
		"../php/load.php",                     // 1
		"../php/save.php",                     // 2
		"../php/userlogin.php",                // 3
		"../php/userregister.php",             // 4
		"../php/userchange.php",               // 5
		"../php/killsession.php",              // 6
		"../php/resendactivation.php",         // 7
		"../php/sendnewpassword.php",          // 8
		"../php/download.php",                 // 9
		"../php/upload.php",                   // 10
		"../php/comment.php",                  // 11
		"../php/commentedit.php",              // 12
		"../php/add_user_to_admin_list.php",   // 13
		"../php/remove_user_from_admin_list.php", // 14
		"../php/delete_assignment.php",        // 15
		"../php/list_all_users.php"            // 16
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
	
	public String getDownloadScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[9];
	}
	
	public String getUploadScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[10];
	}
	
	public String getCommentScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[11];
	}
	
	public String getCommentEditScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[12];
	}
	
	public String getAddUserToAdminListScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[13];
	}
	
	public String getRemoveUserFromAdminListScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[14];
	}
	
	public String getDeleteAssignmentScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[15];
	}
	
	public String getListAllUsersScriptURL() {
		return GWT.getModuleBaseURL()+DEFAULT_VALUES[16];
	}
	
}
