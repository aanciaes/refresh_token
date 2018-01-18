package example.refreshtokens.apollo;

import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.Route;
import com.spotify.apollo.route.RouteProvider;

import java.util.stream.Stream;

public class Routes implements RouteProvider {

    @Override
    public Stream<? extends Route<? extends AsyncHandler<?>>> routes() {
        return Stream.of(
                Route.sync("POST", "/login", Service::login),
                Route.sync("POST", "/verify", Service::verifyRefreshToken)
        );
    }
}
