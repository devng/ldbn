package se.umu.cs.ldbn.client.events;

import com.google.gwt.event.shared.GwtEvent;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import java.util.List;

public class CommentsReceivedEvent extends GwtEvent<CommentsReceivedEventHandler> {

	public static final Type<CommentsReceivedEventHandler> TYPE = new Type<>();

	private final List<CommentDto> comments;

	public CommentsReceivedEvent(List<CommentDto> comments) {
		this.comments = comments;
	}

	@Override
	public Type<CommentsReceivedEventHandler> getAssociatedType() {
		return null;
	}

	@Override
	protected void dispatch(CommentsReceivedEventHandler commentsReceivedEventHandler) {
		commentsReceivedEventHandler.onCommentsReceived(comments);
	}
}
