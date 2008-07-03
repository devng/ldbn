package se.umu.cs.ldbn.client.ui.user;

public class UserData {
	
	private static UserData inst;
	
	public static UserData get() {
		if(inst == null) {
			inst = new UserData();
		}
		return inst;
	}
	
	private String name;
	private int id;
	private String pass;
	private String session;
	private String email;
	
	private UserData() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	public void clear() {
		pass = null;
		id = 0;
		session = null;
		pass = null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
