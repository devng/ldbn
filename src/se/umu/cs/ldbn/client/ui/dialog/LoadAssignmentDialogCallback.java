package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.io.AssignmentListEntry;


public interface LoadAssignmentDialogCallback {
	void onLoaded(AssignmentListEntry entry);
	void onLoadCanceled();
}
