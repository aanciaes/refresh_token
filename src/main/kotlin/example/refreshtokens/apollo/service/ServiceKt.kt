package example.refreshtokens.apollo.service

import example.refreshtokens.auth.JwtServiceKt
import example.refreshtokens.apollo.model.UserKt
import example.refreshtokens.apollo.model.UserRepositoryKt
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws

class ServiceKt {

    private val userRepository = UserRepositoryKt

    fun login(username: String, password: String): UserKt? {
        val user = userRepository.getUserByUsername(username)

        return if (user != null) {
            if (user.password == password)
                user
            else {
                null
            }
        } else {
            null
        }
    }

    fun getUser(refreshToken: String?): UserKt? {
        val jws = JwtServiceKt.decodeRefreshToken(refreshToken) ?: return null

        val userId = jws.body["userId"] as Int

        return userRepository.getUserById(userId)
    }

    fun getAccessToken(refreshToken: String?, resource: String): String? {
        val user = getUser(refreshToken) ?: return null

        return when (resource) {
            "ADMIN_RESOURCE" -> {
                if (user.isAdmin) {
                    JwtServiceKt.issueJwt(user, resource)
                } else JwtServiceKt.issueJwt(user, resource)
            }
            else -> JwtServiceKt.issueJwt(user, resource)
        }
    }

    fun accessAdminResource(authorization: String): Boolean {
        if (authorization.contains("Bearer")) {
            val token = authorization.split("\\s+".toRegex())[1]
            val claims : Jws<Claims>? = JwtServiceKt.decodeJwt(token) ?: return false

            if (claims?.body?.get("resource") == "ADMIN_RESOURCE") {
                val user = userRepository.getUserById(claims.body["userId"] as Int)
                if (user != null)
                    return user.isAdmin
            }
        }
        return false
    }

    fun accessNonAdminResource(authorization: String): Boolean {
        if (authorization.contains("Bearer")) {
            val token = authorization.split("\\s+".toRegex())[1]
            val claims = JwtServiceKt.decodeJwt(token)

            if (claims != null) {
                if (claims.body.get("resource") == "NON_ADMIN_RESOURCE") {
                    val user = userRepository.getUserById(claims.body.get("userId") as Int)
                    if (user != null)
                        return true
                }
            }
        }
        return false
    }
}
