package se.umu.cs.ldbn.client.events;

import com.google.gwt.event.shared.EventHandler;
import se.umu.cs.ldbn.shared.core.Assignment;

public interface AssignmentLoadedEventHandler extends EventHandler {
	void onAssignmentLoaded(Assignment a);
}
