package example.refreshtokens.apollo.controller;

import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.Route;
import com.spotify.apollo.route.RouteProvider;

import java.util.stream.Stream;

public class Routes implements RouteProvider {

    @Override
    public Stream<? extends Route<? extends AsyncHandler<?>>> routes() {
        return Stream.of(
                Route.sync("POST", "/login", Controller::login),
                Route.sync("POST", "/verify", Controller::verifyRefreshToken),
                Route.sync("GET", "/user", Controller::getUserDetails),
                Route.sync("GET", "/refresh", Controller::getAccessToken),
                Route.sync("GET", "/admin", Controller::adminOperation),
                Route.sync("OPTIONS", "/admin", Controller::adminOperation),
                Route.sync("GET", "/nonAdmin", Controller::nonAdminOperation),
                Route.sync("OPTIONS", "/nonAdmin", Controller::nonAdminOperation),
                Route.sync("POST", "/logout", Controller::logout)
        );
    }
}
