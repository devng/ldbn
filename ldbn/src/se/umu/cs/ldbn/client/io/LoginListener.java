package se.umu.cs.ldbn.client.io;

public interface LoginListener {
	void onLoginSuccess();
	void onSessionKilled();
}
