package example.refreshtokens.apollo.service;

import example.refreshtokens.apollo.model.User;
import example.refreshtokens.apollo.model.UserRepository;
import example.refreshtokens.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class Service {

    private UserRepository userRepository = new UserRepository();

    public User login(String username, String password) {
        User user = userRepository.getUserByUsername(username);

        if (user != null) {
            if (user.getHashedPassword().equals(password))
                return user;
            else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User getUser(String refreshToken) {
        Jws<Claims> jws = JwtService.decodeRefreshToken(refreshToken);
        if (jws == null)
            return null;

        int userId = jws.getBody().get("userId", Integer.class);

        User user = userRepository.getUserById(userId);
        if (user == null)
            return null;
        return user;
    }

    public String getAccessToken(String refreshToken, String resource) {
        User user = getUser(refreshToken);

        if(user == null)
            return null;

        switch (resource) {
            case "ADMIN_RESOURCE":
                if (user.isAdmin()) {
                    return JwtService.issueJwt(user, resource);
                }
            default:
                return JwtService.issueJwt(user, resource);
        }
    }

    public boolean accessAdminResource (String authorization) {
        if(authorization.contains("Bearer")) {
            String token = authorization.split("\\s+")[1];
            Jws<Claims> claims = JwtService.decodeJwt(token);

            if (claims != null) {
                if (claims.getBody().get("resource").equals("ADMIN_RESOURCE")) {
                    User user = userRepository.getUserById(claims.getBody().get("userId", Integer.class));
                    if (user != null)
                        return user.isAdmin();
                }
            }
        }
        return false;
    }

    public boolean accessNonAdminResource (String authorization) {
        if(authorization.contains("Bearer")) {
            String token = authorization.split("\\s+")[1];
            Jws<Claims> claims = JwtService.decodeJwt(token);

            if (claims != null) {
                if (claims.getBody().get("resource").equals("NON_ADMIN_RESOURCE")) {
                    User user = userRepository.getUserById(claims.getBody().get("userId", Integer.class));
                    if (user != null)
                        return true;
                }
            }
        }
        return false;
    }
}
