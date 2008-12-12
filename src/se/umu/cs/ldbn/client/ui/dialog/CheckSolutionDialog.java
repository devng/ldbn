package se.umu.cs.ldbn.client.ui.dialog;

import se.umu.cs.ldbn.client.i18n.I18N;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class CheckSolutionDialog extends CloseDialog  {

	public enum MSG_TYPE {error, warn, ok, title};
	
	private VerticalPanel mainPanel;
	
	private static CheckSolutionDialog inst;
	
	private CheckSolutionDialog() {
		super(I18N.constants().csdTitle(), "", false);
	}
	
	public static CheckSolutionDialog get() {
		if (inst == null) {
			inst = new CheckSolutionDialog();
		}
		
		return inst;
	}
	
	public void msg(String msg, MSG_TYPE type) {
		if(msg == null) {
			Log.warn("CheckSolutionDialog: msg cannot be null.");
			return;
		}
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image img;
		switch (type) {
		case error:
			img = new Image("img/errMsg.png");
			break;
		case warn:
			img = new Image("img/warnMsg.png");
			break;
		case ok:
			img = new Image("img/okMsg.png");
			break;
		default:
			img = null;
			break;
		}
		if(img != null) {
			hp.add(img);
		}
		Label l = new Label(msg);
		if(type.equals(MSG_TYPE.title)) {
			l.setStyleName("csd-title");
		}
		l.setWordWrap(false);
		hp.add(l);
		mainPanel.add(hp);
	}
	
	public void clearMsgs() {
		mainPanel.clear();
	}
	
	public void msgOK(String msg) {
		this.msg(msg, MSG_TYPE.ok);
	}
	
	public void msgErr(String msg) {
		this.msg(msg, MSG_TYPE.error);
	}
	
	public void msgWarn(String msg) {
		this.msg(msg, MSG_TYPE.warn);
	}
	
	public void msgTitle(String msg) {
		this.msg(msg, MSG_TYPE.title);
	}
	
	protected Widget getContentWidget() {
		ScrollPanel sp = new ScrollPanel();
		mainPanel = new VerticalPanel();
		sp.setSize("360px", "240px");
		Grid grid = new Grid(1,1);
		grid.setSize("100%", "100%");
		DOM.setStyleAttribute(grid.getElement(), "background", "white");
		sp.add(grid);
		grid.setWidget(0, 0, mainPanel);
		//sp.add(mainPanel);
		return sp;
	}
}
