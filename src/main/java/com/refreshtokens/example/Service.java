package com.refreshtokens.example;

import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;

public class Service {

    static Response<ResponseEntity> login (RequestContext requestContext) {
        System.out.println("Payload: " + requestContext.request().payload().get().utf8());
        String username = requestContext.request().parameter("username").get();
        String password = requestContext.request().parameter("password").get();

        if(username.equals("miguel") && password.equals("password")){
            return Response.forPayload(new ResponseEntity("Login Successful", 200))
                    .withHeader("Authorization", "Bearer " + JwtService.issueJwt(username))
                    .withHeader("Set-Cookie", "refresh-token=" + JwtService.issueRefreshToken(username)
                    + "; HttpOnly");
        }
        else {
            return Response.forPayload(new ResponseEntity("Login Failed", 401));
        }
    }

    static boolean verify (RequestContext request) {
        String bearer = request.request().header("access_token").get();

        return JwtService.verifyJwt(bearer);
    }
}
