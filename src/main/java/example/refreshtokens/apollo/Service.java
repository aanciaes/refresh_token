package com.refreshtokens.example;

import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;

import java.util.NoSuchElementException;

public class Service {

    static Response<ResponseEntity> login (RequestContext requestContext) {
        System.out.println("Payload: " + requestContext.request().payload().get().utf8());
        String username = requestContext.request().parameter("username").get();
        String password = requestContext.request().parameter("password").get();

        if(username.equals("miguel") && password.equals("password")){
            return Response.forPayload(new ResponseEntity("Login Successful", 200))
                    .withHeader("Access-Control-Allow-Origin", "*")
                    .withHeader("Set-Cookie", "refresh-token=" + JwtService.issueRefreshToken(username)
                    + "; Domain=http://127.0.0.1:63342; Path=/; HttpOnly");
        }
        else {
            return Response.forPayload(new ResponseEntity("Login Failed", 401))
                    .withHeader("Access-Control-Allow-Origin", "*");
        }
    }

    static Response<ResponseEntity> verify (RequestContext request) {
        try {
            String bearer = request.request().header("access_token").get();
            boolean isValid = JwtService.verifyJwt(bearer);

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
}
