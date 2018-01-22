package example.refreshtokens.apollo.controller;

import com.spotify.apollo.*;
import example.refreshtokens.apollo.model.AccessToken;
import example.refreshtokens.apollo.model.ResponseEntity;
import example.refreshtokens.apollo.model.User;
import example.refreshtokens.apollo.service.Service;
import example.refreshtokens.auth.JwtService;

import java.util.NoSuchElementException;

public class Controller {

    private static final String FRONT_END_SERVER = "http://localhost:63342";

    private static Service service = new Service ();

    static Response<ResponseEntity> login (RequestContext requestContext) {
        String username = requestContext.request().parameter("username").get();
        String password = requestContext.request().parameter("password").get();

        User user = service.login(username, password);

        if(user!=null){
            return Response.forPayload(new ResponseEntity("Login Successful", 200))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true")
                    .withHeader("Set-Cookie", "refresh-token=" + JwtService.issueRefreshToken(user)
                    + "; Domain=localhost; Path=/; HttpOnly");
        }
        else {
            return Response.forStatus(Status.UNAUTHORIZED.withReasonPhrase("Login Failed"))
                    .withPayload(new ResponseEntity("Login Failed", 401))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response<ResponseEntity> verifyRefreshToken (RequestContext request) {
        try {
            String refreshToken = getRefreshTokenFromCookies(request.request().header("cookie").get());
            boolean isValid = JwtService.verifyRefreshToken(refreshToken);

            return Response.forStatus(isValid ? Status.OK : Status.UNAUTHORIZED).withPayload(new ResponseEntity("Token is valid: " + isValid,
                    isValid ? 200 : 401))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }catch (NoSuchElementException e){
            return Response.forPayload(new ResponseEntity("No Such Element Exception",500))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response getUserDetails (RequestContext request) {
        String refresh_token = getRefreshTokenFromCookies((request.request().header("cookie").get()));

        if (refresh_token==null){
            return Response.forStatus(Status.UNAUTHORIZED)
                    .withPayload(new ResponseEntity("Unauthorized", 401))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        } else {
            User user = service.getUser(refresh_token);
            if(user != null){
                return Response.forPayload(user)
                        .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                        .withHeader("Access-Control-Allow-Credentials", "true");
            }
            return Response.forStatus(Status.FORBIDDEN.withReasonPhrase("Session Expired"))
                    .withPayload(new ResponseEntity("Session Expired", 403))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response getAccessToken (RequestContext request) {
        String refresh_token = getRefreshTokenFromCookies((request.request().header("cookie").get()));
        String resource = request.request().parameter("resource").get();

        String accessToken = service.getAccessToken (refresh_token, resource);

        if(accessToken!=null){
            return Response.forPayload(new AccessToken(accessToken, "Bearer"))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }else{
            return Response.forStatus(Status.FORBIDDEN.withReasonPhrase("Refresh Token is not valid"))
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response adminOperation (RequestContext request) {
        //handle preflight options request made by ajax
        if(request.request().method().equals("OPTIONS")) {
            return Response.ok()
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Headers", "Authorization")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
        if(service.accessAdminResource (request.request().header("Authorization").get())){
            return Response.forStatus(Status.OK)
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Headers", "*")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        } else{
            return Response.forStatus(Status.FORBIDDEN)
                    .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                    .withHeader("Access-Control-Allow-Headers", "*")
                    .withHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    static Response nonAdminOperation (RequestContext request) {
        //TODO: finish
        return Response.ok().withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                .withHeader("Access-Control-Allow-Headers", "Authorization")
                .withHeader("Access-Control-Allow-Credentials", "true");
    }

    static Response<ResponseEntity> logout (RequestContext request) {
        return Response.ok().withPayload(new ResponseEntity("You have been logged out", 200))
                .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                .withHeader("Access-Control-Allow-Credentials", "true")
                .withHeader("Set-Cookie", "refresh-token=null; Max-Age=-1");
    }

    private static String getRefreshTokenFromCookies (String allCookies) {
        String [] splited = allCookies.split("\\s|=|;");

        for (int i=0; i<splited.length; i++) {
            if (splited[i].equals("refresh-token")) {
                if(splited[i+1]!=null)
                    return splited[i + 1];
            }
        }
        return null;
    }
}
