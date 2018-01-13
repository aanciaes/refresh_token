package com.refreshtokens.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Calendar;
import java.util.Date;

public class JwtService {

    private static final String key = "supersecurekey";
    private static final String refreshToken_key = "ev3nMoreS3cureK3yThanTheOtherOne!";

    static String issueJwt (String username) {
        //TODO: expiration date
        String compactJws = Jwts.builder()
                .setSubject(username)
                .claim("refresh_token", false)
                .claim("claimExample", "hello")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();


        return compactJws;
    }

    static String issueRefreshToken (String username) {

        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.WEEK_OF_MONTH, 1);
        Date expirationDate = c.getTime();

        String compactJws = Jwts.builder()
                .setSubject(username)
                .claim("refresh_token", true)
                .claim("admin", true)
                .setIssuedAt(current)
                .setExpiration(expirationDate)
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return compactJws;
    }

    static boolean verifyJwt (String jwt) {
        //TODO: Verify expiration date
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
            return true;

        } catch (SignatureException e) {

            return false;
        }
    }
}