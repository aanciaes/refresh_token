package example.refreshtokens.auth;

import example.refreshtokens.apollo.model.User;
import io.jsonwebtoken.*;

import java.util.Calendar;
import java.util.Date;

public class JwtService {

    private static final String key = "supersecurekey";
    private static final String refreshToken_key = "ev3nMoreS3cureK3yThanTheOtherOne!";

    public static String issueJwt (User user, String resource) {

        String compactJws = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh_token", false)
                .claim("userId", user.getId())
                .claim("admin", user.isAdmin())
                .claim("resource", resource)
                .setExpiration(setExpirationDate(false))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return compactJws;
    }

    public static String issueRefreshToken (User user) {

        String compactJws = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh_token", true)
                .claim("userId", user.getId())
                .claim("admin", user.isAdmin())
                .setIssuedAt(new Date())
                .setExpiration(setExpirationDate(false))
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

    public static Jws decodeJwt (String jwt) {
        try {
            return Jwts.parser().setSigningKey(key)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(jwt);

        }catch (ExpiredJwtException e){
            System.out.println("JWT has expired");
            return null;
        } catch (SignatureException e) {
            System.out.println("Signature Exception");
            return null;
        }
    }

    public static Jws<Claims> decodeRefreshToken (String refreshToken) {
        try {
            return Jwts.parser().setSigningKey(refreshToken_key)
                    .require("refresh_token", true)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(refreshToken);
        }catch (ExpiredJwtException e){
            System.out.println("JWT has expired");
            return null;
        } catch (SignatureException e) {
            System.out.println("Signature Exception");
            return null;
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