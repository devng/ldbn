package se.umu.cs.ldbn.client.ui;

import java.util.Collection;

public interface FDHolderPanelListener {
	public void fdAdded(Collection<FDWidget> currentFDs);
	public void fdRemoved(Collection<FDWidget> currentFDs); 
	public void allFDsRemoved();
}
