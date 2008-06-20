package se.umu.cs.ldbn.client.ui.dialog;


public interface LoadAssignmentDialogCallback {
	void loadedIdAndName(String id, String name);
	void onLoadCanceled();
}
