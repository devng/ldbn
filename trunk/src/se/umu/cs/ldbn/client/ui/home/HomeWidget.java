package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.LoginCallback;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.ui.user.UserLoginWidget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class HomeWidget extends Composite implements LoginCallback {
	
	private static HomeWidget inst;
	
	public static HomeWidget get() {
		if(inst == null) {
			inst = new HomeWidget();
		}
		return inst;
	}
	
	private AbsolutePanel mainPanel;
	private UserLoginWidget ulw;
	
	private HomeWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		initWidget(mainPanel);
		ulw = new UserLoginWidget();
		HeaderWidget hw = new HeaderWidget();
		hw.add(ulw);
		mainPanel.add(hw);
		
	}

	public void setSessionData(String userID, String sessionID) {
		UserData ud = UserData.get();
		ud.setId(userID);
		ud.setSession(sessionID);
	}

	public void setSessionKilled() {
		// TODO Auto-generated method stub
		
	}
	
}
