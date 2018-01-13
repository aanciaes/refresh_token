package com.refreshtokens.example;

import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;

public class Service {

    static Response<String> login (RequestContext requestContext) {
        String s = "";
        String username = requestContext.request().parameter("username").get();
        String password = requestContext.request().parameter("password").get();

        if(username.equals("miguel") && password.equals("password")){
            s = "Login Successful";
            return Response.forPayload(s)
                    .withHeader("Authorization", "Bearer: " + JwtService.issueJwt(username));
        }
        else {
            s = "Login Failed";
            return Response.forPayload(s)
                    .withHeader("RefreshToken", String.valueOf(s.length()));
        }
    }

    static boolean verify (RequestContext request) {
        String bearer = request.request().header("access_token").get();

        return JwtService.verifyJwt(bearer);
    }
}
