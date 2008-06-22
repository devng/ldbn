package se.umu.cs.ldbn.client.ui.dialog;


public interface LoadAssignmentDialogCallback {
	void onLoaded(String id, String name);
	void onLoadCanceled();
}
