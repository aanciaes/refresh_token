package example.refreshtokens.apollo.model;

public class User {

    private int id;
    private String username;
    private String hashedPassword;
    private boolean isAdmin;

    public User () {
    }

    public User(int id, String username, String hashedPassword, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
    }

    public int getId () {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
