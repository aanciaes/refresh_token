package example.refreshtokens.apollo;

import com.spotify.apollo.*;
import com.spotify.apollo.meta.model.Model;
import example.refreshtokens.auth.JwtService;

import java.util.NoSuchElementException;

public class Service {

    static Response<ResponseEntity> login (RequestContext requestContext) {
        System.out.println("Payload: " + requestContext.request().payload().get().utf8());
        String username = requestContext.request().parameter("username").get();
        String password = requestContext.request().parameter("password").get();

        if(username.equals("miguel") && password.equals("password")){
            return Response.forPayload(new ResponseEntity("Login Successful", 200))
                    .withHeader("Access-Control-Allow-Origin", "http://localhost:63342")
                    .withHeader("Access-Control-Allow-Credentials", "true")
                    .withHeader("Set-Cookie", "refresh-token=" + JwtService.issueRefreshToken(username)
                    + "; Domain=localhost; Path=/; HttpOnly");
        }
        else {
            return Response.forStatus(Status.UNAUTHORIZED.withReasonPhrase("Login Failed"))
                    .withPayload(new ResponseEntity("Login Failed", 401))
                    .withHeader("Access-Control-Allow-Origin", "http://localhost:63342")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response<ResponseEntity> verifyRefreshToken (RequestContext request) {
        try {
            String refreshToken = getRefreshTokenFromCookies(request.request().header("cookie").get());
            boolean isValid = JwtService.verifyRefreshToken(refreshToken);

            return Response.forPayload(new ResponseEntity("Token is valid: " + isValid,
                    isValid ? 200 : 401))
                    .withHeader("Access-Control-Allow-Origin", "http://localhost:63342")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }catch (NoSuchElementException e){
            return Response.forPayload(new ResponseEntity("No Such Element Exception",500))
                    .withHeader("Access-Control-Allow-Origin", "http://localhost:63342")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    private static String getRefreshTokenFromCookies (String allCookies) {
        String [] splited = allCookies.split("\\s|=|;");

        for (int i=0; i<splited.length; i++) {
            if (splited[i].equals("refresh-token")) {
                return splited[i + 1];
            }
        }
        return new String ();
    }
}
