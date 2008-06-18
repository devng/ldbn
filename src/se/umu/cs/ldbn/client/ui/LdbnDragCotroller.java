package se.umu.cs.ldbn.client.ui;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.RootPanel;

public final class LdbnDragCotroller extends PickupDragController {

	private static LdbnDragCotroller inst;
	
	private LdbnDragCotroller() {
		super(RootPanel.get(), false);
		setBehaviorDragProxy(true);
	}
	
	public static LdbnDragCotroller get() {
		if (inst == null) {
			inst = new LdbnDragCotroller();
		}
		return inst;
	}

}
