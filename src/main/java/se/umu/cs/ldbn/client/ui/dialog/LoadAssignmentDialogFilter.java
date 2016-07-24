package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

public interface LoadAssignmentDialogFilter {
	/**
	 * This interface should be implemented together with LoadAssignmentDialogCaller
	 * if a class wishes to filter some entries in the list. This is useful for
	 * the editing an assignment, so that a user can edit only assignments created 
	 * by the same user. 
	 * @param entry a entry in the list of assignments. 
	 * @return returns true if the entry should be included in the dialog list,
	 * otherwise returns false.
	 */
	boolean filter(AssignmentDto entry);

}
