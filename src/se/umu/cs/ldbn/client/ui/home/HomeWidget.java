package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.ui.HeaderWidget;
import se.umu.cs.ldbn.client.ui.user.UserData;
import se.umu.cs.ldbn.client.ui.user.UserLoginWidget;
import se.umu.cs.ldbn.client.ui.user.UserLogoutWidget;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class HomeWidget extends Composite implements LoginListener {
	
	private static HomeWidget inst;
	
	public static HomeWidget get() {
		if(inst == null) {
			inst = new HomeWidget();
		}
		return inst;
	}
	
	private AbsolutePanel mainPanel;
	private UserLoginWidget login;
	private UserLogoutWidget logout;
	private HeaderWidget hw;
	
	private HomeWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		initWidget(mainPanel);
		login = new UserLoginWidget();
		hw = new HeaderWidget();
		hw.add(login);
		mainPanel.add(hw);
		Login.get().addListener(this);
	}

	public void onLoginSuccess() {
		login.clear();
		login.removeFromParent();
		if(logout == null) {
			logout = new UserLogoutWidget();
		}
		hw.add(logout);
	}

	public void onSessionKilled() {
		logout.removeFromParent();
		hw.add(login);
	}
	
}
