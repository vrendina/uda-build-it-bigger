package com.udacity.gradle.joketeller;

import java.util.ArrayList;
import java.util.Random;

public class JokeTeller {

    private ArrayList<Joke> jokes = new ArrayList<>();
    private Random random;

    /**
     * Initialize a new JokeTeller and supply the list of jokes.
     *
     * @param jokes An ArrayList of Joke objects
     */
    public JokeTeller(ArrayList<Joke> jokes) {
        random = new Random();
        this.jokes = jokes;
    }

    /**
     * Initialize a new JokeTeller and load jokes from existing joke list.
     */
    public JokeTeller() {
        this(JokeList.jokes);
    }

    /**
     * Provides a random joke for your entertainment.
     *
     * @return Joke
     */
    public Joke tellJoke() {
        int jokeCount = jokes.size();

        // No jokes were loaded, we can't tell a joke
        if(jokeCount == 0) {
            return null;
        }

        return jokes.get(random.nextInt(jokeCount));
    }
}
