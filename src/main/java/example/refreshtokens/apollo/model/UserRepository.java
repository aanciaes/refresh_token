package example.refreshtokens.apollo.model;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private int counter;
    private List<User> database;

    public UserRepository () {
        this.counter=0;
        this.database = new ArrayList<>();

        //Bootstraping a few users
        insertUser("miguel", "miguelpassword", true);
        insertUser("joao", "joaopassword", false);
    }

    public User insertUser (String username, String password, boolean isAdmin) {
        User user = new User(counter++, username, password, isAdmin);
        database.add(user);
        return user;
    }

    public User getUserById (int id) {
        return database.get(id);
    }

    public User getUserByUsername (String username) {
        for(User u : database){
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }
}
