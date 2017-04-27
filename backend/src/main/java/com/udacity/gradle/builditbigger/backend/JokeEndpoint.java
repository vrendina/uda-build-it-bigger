package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.udacity.gradle.joketeller.Joke;
import com.udacity.gradle.joketeller.JokeTeller;

@Api(
        name = "jokeApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class JokeEndpoint {

    @ApiMethod(name = "tellJoke")
    public CloudJoke tellJoke() {
        JokeTeller jokeTeller = new JokeTeller();
        Joke joke = jokeTeller.tellJoke();

        return new CloudJoke(joke.setup(), joke.punchLine());
    }

}
