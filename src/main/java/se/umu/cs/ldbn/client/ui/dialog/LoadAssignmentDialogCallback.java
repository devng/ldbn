package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;


public interface LoadAssignmentDialogCallback {
	void onLoaded(AssignmentListEntry entry);
	void onLoadCanceled();
	/**
	 * If this returns true then the LoadAssignmentDialog will check the user
	 * rights to see if it should show assignments only submitted by this user,
	 * if he is not an administrator, otherwise if he/she is an administrator,
	 * the LoadAssignmentDialog will show all assignments.
	 */
	boolean checkUserRights();
}
