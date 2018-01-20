package example.refreshtokens.apollo.service;

import example.refreshtokens.apollo.model.User;
import example.refreshtokens.apollo.model.UserRepository;
import example.refreshtokens.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

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

    public User getUser (String refreshToken) {
        Jws<Claims> jws = JwtService.getJwtFromRefreshToken(refreshToken);
        if(jws==null)
            return null;

        int userId = jws.getBody().get("userId", Integer.class);

        User user = userRepository.getUserById(userId);
        if(user==null)
            return null;
        return user;
    }
}
