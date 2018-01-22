package example.refreshtokens.apollo.controller

import com.spotify.apollo.*
import example.refreshtokens.apollo.model.AccessTokenKt
import example.refreshtokens.apollo.model.ResponseEntityKt
import example.refreshtokens.apollo.service.ServiceKt
import example.refreshtokens.auth.JwtServiceKt

import java.util.NoSuchElementException

object ControllerKt {

    private val FRONT_END_SERVER = "http://localhost:63342"

    private val service = ServiceKt()

    internal fun login(requestContext: RequestContext): Response<*> {
        val username = requestContext.request().parameter("username").get()
        val password = requestContext.request().parameter("password").get()

        val user = service.login(username, password)

        return if (user != null) {
            addCorsFilter(Response.forPayload(ResponseEntityKt("Login Successful", 200))
                    .withHeader("Set-Cookie", "refresh-token=" + JwtServiceKt.issueRefreshToken(user)
                            + "; Domain=localhost; Path=/; HttpOnly"))
        } else {
            addCorsFilter(Response.forStatus<Any>(Status.UNAUTHORIZED.withReasonPhrase("Login Failed"))
                    .withPayload(ResponseEntityKt("Login Failed", 401)))
        }
    }

    internal fun verifyRefreshToken(request: RequestContext): Response<*> {
        try {
            val refreshToken = getRefreshTokenFromCookies(request.request().header("cookie").get())
            val isValid = JwtServiceKt.verifyRefreshToken(refreshToken)

            return addCorsFilter(Response.forStatus<Any>(if (isValid) Status.OK else Status.UNAUTHORIZED)
                    .withPayload(ResponseEntityKt("Token is valid: " + isValid,
                            if (isValid) 200 else 401)))
        } catch (e: NoSuchElementException) {
            return addCorsFilter(Response.forPayload(ResponseEntityKt("No Such Element Exception", 500)))
        }

    }

    internal fun getUserDetails(request: RequestContext): Response<*> {
        val refresh_token = getRefreshTokenFromCookies(request.request().header("cookie").get())

        if (refresh_token == null) {
            return addCorsFilter(Response.forStatus<Any>(Status.UNAUTHORIZED)
                    .withPayload(ResponseEntityKt("Unauthorized", 401)))
        } else {
            val user = service.getUser(refresh_token)
            return if (user != null) {
                addCorsFilter(Response.forPayload(user))
            } else addCorsFilter(Response.forStatus<Any>(Status.FORBIDDEN.withReasonPhrase("Session Expired"))
                    .withPayload(ResponseEntityKt("Session Expired", 403)))
        }
    }

    internal fun getAccessToken(request: RequestContext): Response<*> {
        val refresh_token = getRefreshTokenFromCookies(request.request().header("cookie").get())
        val resource = request.request().parameter("resource").get()

        val accessToken = service.getAccessToken(refresh_token, resource)

        return if (accessToken != null) {
            addCorsFilter(Response.forPayload(AccessTokenKt(accessToken, "Bearer")))
        } else {
            addCorsFilter(Response.forStatus<Any>(Status.UNAUTHORIZED.withReasonPhrase("Refresh Token is not valid")))
        }
    }

    internal fun adminOperation(request: RequestContext): Response<*> {
        //handle preflight options request made by ajax
        if (request.request().method() == "OPTIONS") {
            return addCorsFilter(Response.ok<Any>()
                    .withHeader("Access-Control-Allow-Headers", "Authorization"))
        }

        return if (service.accessAdminResource(request.request().header("Authorization").get())) {
            addCorsFilter(Response.forStatus<Any>(Status.OK))
        } else {
            addCorsFilter(Response.forStatus<Any>(Status.FORBIDDEN
                    .withReasonPhrase("You do not have permissions to access this resource")))
        }
    }

    internal fun nonAdminOperation(request: RequestContext): Response<*> {
        //handle preflight options request made by ajax
        if (request.request().method() == "OPTIONS") {
            return addCorsFilter(Response.ok<Any>()
                    .withHeader("Access-Control-Allow-Headers", "Authorization"))
        }

        return if (service.accessNonAdminResource(request.request().header("Authorization").get())) {
            addCorsFilter(Response.forStatus<Any>(Status.OK))
        } else {
            addCorsFilter(Response.forStatus<Any>(Status.FORBIDDEN
                    .withReasonPhrase("You do not have permissions to access this resource")))
        }
    }

    internal fun logout(request: RequestContext): Response<*> {
        return addCorsFilter(Response.ok<Any>().withPayload(ResponseEntityKt("You have been logged out", 200))
                .withHeader("Set-Cookie", "refresh-token=null; Max-Age=-1"))
    }

    private fun getRefreshTokenFromCookies(allCookies: String): String? {
        val splited = allCookies.split("\\s|=|;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in splited.indices) {
            if (splited[i] == "refresh-token") {
                if (splited[i + 1] != null)
                    return splited[i + 1]
            }
        }
        return null
    }

    private fun addCorsFilter(response: Response<*>): Response<*> {
        return response
                .withHeader("Access-Control-Allow-Origin", FRONT_END_SERVER)
                .withHeader("Access-Control-Allow-Credentials", "true")
    }
}
