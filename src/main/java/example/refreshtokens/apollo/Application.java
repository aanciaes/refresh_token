package example.refreshtokens.apollo;

import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;

public class Application {


    public static void main(String... args) throws LoadingException {
        HttpService.boot(Application::init, "refreshtokenexample", args);
    }

    static void init(Environment environment) {
        environment.routingEngine()
                .registerAutoRoutes(new Routes());
    }
}
