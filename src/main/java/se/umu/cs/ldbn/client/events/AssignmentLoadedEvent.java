package se.umu.cs.ldbn.client.events;

import com.google.gwt.event.shared.GwtEvent;
import se.umu.cs.ldbn.shared.core.Assignment;

public class AssignmentLoadedEvent extends GwtEvent<AssignmentLoadedEventHandler> {

    public static final Type<AssignmentLoadedEventHandler> TYPE = new Type<>();

    private final Assignment a;

    public AssignmentLoadedEvent(Assignment a) {
        this.a = a;
    }

    @Override
    public Type<AssignmentLoadedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AssignmentLoadedEventHandler assignmentLoadedEventHandler) {
        assignmentLoadedEventHandler.onAssignmentLoaded(a);
    }
}
