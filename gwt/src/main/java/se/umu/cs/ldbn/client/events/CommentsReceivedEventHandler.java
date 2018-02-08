package se.umu.cs.ldbn.client.events;

import com.google.gwt.event.shared.EventHandler;
import se.umu.cs.ldbn.shared.dto.CommentDto;

import java.util.List;

public interface CommentsReceivedEventHandler extends EventHandler {
	void onCommentsReceived(List<CommentDto> comments);
}
