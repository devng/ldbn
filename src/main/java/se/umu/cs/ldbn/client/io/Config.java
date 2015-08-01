package se.umu.cs.ldbn.client.io;

import com.google.gwt.core.client.GWT;

public final class Config {
	
	public static final String[] DEFAULT_VALUES = {
		"../php/assignment_list.php",     //  0
		"../php/assignment_load.php",     //  1
		"../php/assignment_save.php",     //  2
		"../php/user_login.php",          //  3
		"../php/user_register.php",       //  4
		"../php/user_edit.php",           //  5
		"../php/user_logout.php",         //  6
		"../php/email_activation.php",    //  7
		"../php/email_password.php",      //  8
		"../php/assignment_download.php", //  9
		"../php/assignment_upload.php",   // 10
		"../php/comment_list_add.php",    // 11
		"../php/comment_edit.php",        // 12
		"../php/admin_add.php",           // 13
		"../php/admin_remove.php",        // 14
		"../php/assignment_delete.php",   // 15
		"../php/user_list.php"            // 16
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
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[0];
	}

	public String getLoadScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[1];
	}

	public String getSaveScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[2];
	}

	public String getUserLoginScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[3];
	}

	public String getUserRegisterScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[4];
	}

	public String getUserChangeScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[5];
	}

	public String getKillSessionScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[6];
	}

	public String getResendActivationScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[7];
	}

	public String getSendNewPasswordScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[8];
	}

	public String getDownloadScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[9];
	}

	public String getUploadScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[10];
	}

	public String getCommentScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[11];
	}

	public String getCommentEditScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[12];
	}

	public String getAddUserToAdminListScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[13];
	}

	public String getRemoveUserFromAdminListScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[14];
	}

	public String getDeleteAssignmentScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[15];
	}

	public String getListAllUsersScriptURL() {
		return GWT.getModuleBaseURL() + DEFAULT_VALUES[16];
	}
	
}
