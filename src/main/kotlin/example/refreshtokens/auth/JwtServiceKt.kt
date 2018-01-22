package example.refreshtokens.auth

import example.refreshtokens.apollo.model.UserKt
import io.jsonwebtoken.*
import org.apache.commons.lang.time.DateUtils

import java.util.Date

object JwtServiceKt {

    private val key = "supersecurekey"
    private val refreshToken_key = "ev3nMoreS3cureK3yThanTheOtherOne!"

    fun issueJwt(user: UserKt, resource: String): String {

        return Jwts.builder()
                .setSubject(user.username)
                .claim("refresh_token", false)
                .claim("userId", user.id)
                .claim("admin", user.isAdmin)
                .claim("resource", resource)
                .setExpiration(setExpirationDate(false))
                .setIssuer("Refresh Token example authentication server")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact()
    }

    fun issueRefreshToken(user: UserKt): String {

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

    fun decodeJwt(jwt: String?): Jws<Claims>? {
        try {
            return Jwts.parser().setSigningKey(key)
                    .requireIssuer("Refresh Token example authentication server")
                    .parseClaimsJws(jwt)

        } catch (e: ExpiredJwtException) {
            println("JWT has expired")
            return null
        } catch (e: SignatureException) {
            println("Signature Exception")
            return null
        }
    }

    fun decodeRefreshToken(refreshToken: String?): Jws<Claims>? {
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
        return if (refreshToken) DateUtils.addWeeks(Date(), 1)
        else DateUtils.addSeconds(Date(), 60)
    }
}