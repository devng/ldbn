package se.umu.cs.ldbn.client.ui.dialog;


import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public interface LoadAssignmentDialogCallback {
	void onLoaded(AssignmentDto entry);
	/**
	 * If this returns true then the LoadAssignmentDialog will check the model
	 * rights to see if it should show assignments only submitted by this model,
	 * if he is not an administrator, otherwise if he/she is an administrator,
	 * the LoadAssignmentDialog will show all assignments.
	 */
	boolean checkUserRights();
}
