package example.refreshtokens.apollo.service;

import example.refreshtokens.apollo.model.User;
import example.refreshtokens.apollo.model.UserRepository;

public class Service {

    private UserRepository userRepository = new UserRepository();

    public User login (String username, String password) {
        User user = userRepository.getUserByUsername(username);

        if(user!=null){
            if(user.getHashedPassword().equals(password))
                return user;
            else {
                return null;
            }
        }else {
            return null;
        }
    }
}
