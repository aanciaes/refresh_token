package example.refreshtokens.auth;

import example.refreshtokens.apollo.model.User;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;

public class JwtService {

    private static final String key = "supersecurekey";
    private static final String refreshToken_key = "ev3nMoreS3cureK3yThanTheOtherOne!";

    public static String issueJwt (String username) {
        String compactJws = Jwts.builder()
                .setSubject(username)
                .claim("refresh_token", false)
                .claim("claimExample", "hello")
                .setExpiration(setExpirationDate(false))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return compactJws;
    }

    public static String issueRefreshToken (User user) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.WEEK_OF_MONTH, 1);
        Date expirationDate = c.getTime();

        String compactJws = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh_token", true)
                .claim("userId", user.getId())
                .claim("admin", user.isAdmin())
                .setIssuedAt(current)
                .setExpiration(setExpirationDate(true))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS512, refreshToken_key)
                .compact();

        return compactJws;
    }

    public static boolean verifyRefreshToken (String refreshToken) {
        try {
            if(refreshToken==null || refreshToken.equals(""))
                return false;

            Jwts.parser().setSigningKey(refreshToken_key)
                    .require("refresh_token", true)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(refreshToken);
            return true;

        }catch (ExpiredJwtException e){
            System.out.println("JWT has expired");
            return false;
        } catch (SignatureException e) {
            System.out.println("Signature Exception");
            return false;
        }
    }

    public static boolean verifyJwt (String jwt) {
        try {
            Jwts.parser().setSigningKey(key)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(jwt);
            return true;

        }catch (ExpiredJwtException e){
            System.out.println("JWT has expired");
            return false;
        } catch (SignatureException e) {
            System.out.println("Signature Exception");
            return false;
        }
    }

    private static Date setExpirationDate (boolean refreshToken) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        if(refreshToken) {
            c.add(Calendar.WEEK_OF_MONTH, 1);
        }else{
            c.add(Calendar.SECOND, 60);
        }
        return c.getTime();
    }
}