package se.umu.cs.ldbn.client.io;

import se.umu.cs.ldbn.client.Assignment;

public interface AssignmentLoaderCallback {
	void onAssignmentLoaded(Assignment a);
	void onAssignmentLoadError();
}
