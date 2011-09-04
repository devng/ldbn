package se.umu.cs.ldbn.client.ui.dialog;

import java.util.Collection;

public interface RenameDialogCallback {
	String getOldName();
	void setNewName(String s);
	
	@SuppressWarnings("rawtypes")
	/**
	 * The collection objects must implement the HasName interface.
	 * For some reason if a class A inherits B, and u have a collection
	 * Collection<A> You cannot return the same collection as Collection<B>.
	 * I have to look into that.  
	 */
	Collection getTakenNames(); 
	
	void onRenameCanceled();
}
