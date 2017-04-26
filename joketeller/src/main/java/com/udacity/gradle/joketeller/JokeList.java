package com.udacity.gradle.joketeller;

import java.util.ArrayList;

/**
 * Contains list of jokes told by the JokeTeller library. Exclusively populated with Dad jokes.
 */
public class JokeList {

    public static ArrayList<Joke> jokes = new ArrayList<>();

    static {
        jokes.add(Joke.create("Did you hear about the guy who invented lifesavers?",
                "They said he made a mint!"));

        jokes.add(Joke.create("What do you call a fake noodle?",
                "An impasta!"));

        jokes.add(Joke.create("I just watched a program about beavers.",
                "It was the best dam program I've seen."));

        jokes.add(Joke.create("Want to hear a joke about paper?",
                "It's tearable."));
    }
}
