package se.umu.cs.ldbn.shared.dto;

public class UserDto {

	private Integer user_id;
	private String name;
	private String pass_hash;	
	private String pass_salt;
	private String email;
	private Boolean is_active;
	private Boolean is_admin;
	private Boolean is_su;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass_hash() {
		return pass_hash;
	}
	public void setPass_hash(String pass_hash) {
		this.pass_hash = pass_hash;
	}
	public String getPass_salt() {
		return pass_salt;
	}
	public void setPass_salt(String pass_salt) {
		this.pass_salt = pass_salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public Boolean getIs_admin() {
		return is_admin;
	}
	public void setIs_admin(Boolean is_admin) {
		this.is_admin = is_admin;
	}
	public Boolean getIs_su() {
		return is_su;
	}
	public void setIs_su(Boolean is_su) {
		this.is_su = is_su;
	}
	
	
}
