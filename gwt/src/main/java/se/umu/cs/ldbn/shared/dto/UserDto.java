package se.umu.cs.ldbn.shared.dto;

public class UserDto {

    private Integer id;
    private String name;
    private String email;
    private boolean active;
    private boolean admin;
    private boolean su;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSu() {
        return su;
    }

    public void setSu(boolean su) {
        this.su = su;
    }
}
