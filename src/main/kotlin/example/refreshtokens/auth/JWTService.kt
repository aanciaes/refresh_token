package example.refreshtokens.auth

import example.refreshtokens.apollo.model.User
import io.jsonwebtoken.*
import org.apache.commons.lang.time.DateUtils

import java.util.Date

object JWTService {

    private val key = "supersecurekey"
    private val refreshToken_key = "ev3nMoreS3cureK3yThanTheOtherOne!"

    fun issueJwt(username: String): String {

        return Jwts.builder()
                .setSubject(username)
                .claim("refresh_token", false)
                .claim("claimExample", "hello")
                .setExpiration(setExpirationDate(false))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact()
    }

    fun issueRefreshToken(user: User): String {

        return Jwts.builder()
                .setSubject(user.username)
                .claim("refresh_token", true)
                .claim("userId", user.id)
                .claim("admin", user.isAdmin)
                .setIssuedAt(Date())
                .setExpiration(setExpirationDate(false))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS512, refreshToken_key)
                .compact()
    }

    fun verifyRefreshToken(refreshToken: String?): Boolean {
        try {
            if (refreshToken == null || refreshToken == "")
                return false

            Jwts.parser().setSigningKey(refreshToken_key)
                    .require("refresh_token", true)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(refreshToken)
            return true

        } catch (e: ExpiredJwtException) {
            println("JWT has expired")
            return false
        } catch (e: SignatureException) {
            println("Signature Exception")
            return false
        }
    }

    fun verifyJwt(jwt: String): Boolean {
        try {
            Jwts.parser().setSigningKey(key)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(jwt)
            return true

        } catch (e: ExpiredJwtException) {
            println("JWT has expired")
            return false
        } catch (e: SignatureException) {
            println("Signature Exception")
            return false
        }
    }

    fun getJwtFromRefreshToken(refreshToken: String): Jws<Claims>? {
        try {
            return Jwts.parser().setSigningKey(refreshToken_key)
                    .require("refresh_token", true)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(refreshToken)

        } catch (e: ExpiredJwtException) {
            println("JWT has expired")
            return null
        } catch (e: SignatureException) {
            println("Signature Exception")
            return null
        }

    }

    private fun setExpirationDate(refreshToken: Boolean): Date {
        /*val current = Date()
        val c = Calendar.getInstance()
        c.time = current
        if (refreshToken) {
            c.add(Calendar.WEEK_OF_MONTH, 1)
        } else {
            c.add(Calendar.SECOND, 60)
        }
        return c.time*/
        return if (refreshToken) DateUtils.addWeeks(Date(), 1)
        else DateUtils.addSeconds(Date(), 60)
    }
}