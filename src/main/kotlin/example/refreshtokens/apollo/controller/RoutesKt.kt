package example.refreshtokens.apollo.controller

import com.spotify.apollo.route.AsyncHandler
import com.spotify.apollo.route.Route
import com.spotify.apollo.route.RouteProvider
import example.refreshtokens.apollo.controller.ControllerKt
import java.util.stream.Stream


class RoutesKt : RouteProvider {

    override fun routes(): Stream<out Route<out AsyncHandler<*>>> {
        return Stream.of(
                Route.sync("POST", "/login", { ControllerKt.login(it) }),
                Route.sync("POST", "/verify", { ControllerKt.verifyRefreshToken(it) }),
                Route.sync("GET", "/user", { ControllerKt.getUserDetails(it) }),
                Route.sync("GET", "/refresh", { ControllerKt.getAccessToken(it) }),
                Route.sync("GET", "/admin", { ControllerKt.adminOperation(it) }),
                Route.sync("OPTIONS", "/admin", { ControllerKt.adminOperation(it) }),
                Route.sync("GET", "/nonAdmin", { ControllerKt.nonAdminOperation(it) }),
                Route.sync("OPTIONS", "/nonAdmin", { ControllerKt.nonAdminOperation(it) }),
                Route.sync("POST", "/logout", { ControllerKt.logout(it) })
        )
    }
}