package example.refreshtokens.apollo

import com.spotify.apollo.*
import com.spotify.apollo.httpservice.HttpService
import example.refreshtokens.apollo.controller.Routes

object ApplicationKotlin {

    @JvmStatic
    fun main(args: Array<String>) {
        HttpService.boot(AppInit { init(it) }, "refreshtokenexample", *args)
    }

    fun init(environment: Environment) {
        environment.routingEngine()
                .registerAutoRoutes(Routes())
    }
}

