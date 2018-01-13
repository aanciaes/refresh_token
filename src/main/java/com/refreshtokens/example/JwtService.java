package com.refreshtokens.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JwtService {

    private static final String key = "supersecurekey";

    static String issueJwt (String username) {
        String compactJws = Jwts.builder()
                .setSubject(username)
                .claim("Admin", true)
                .claim("claimExample", "hello")
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();


        return compactJws;
    }

    static boolean verifyJwt (String jwt) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
            return true;

        } catch (SignatureException e) {

            return false;
        }
    }
}