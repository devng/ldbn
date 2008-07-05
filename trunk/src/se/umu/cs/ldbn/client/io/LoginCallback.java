package se.umu.cs.ldbn.client.io;

public interface LoginCallback {
	void setSessionData(String userID, String sessionID);
	void setSessionKilled();
}
