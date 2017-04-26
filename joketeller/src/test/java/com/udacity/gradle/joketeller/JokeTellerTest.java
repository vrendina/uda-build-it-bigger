package com.udacity.gradle.joketeller;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JokeTellerTest {

    @Test
    public void testJokeIsReturnedCorrectlyWhenSuppliedToTeller() {
        String setup = "Did you hear about the guy who invented lifesavers?";
        String punchLine = "They said he made a mint!";

        ArrayList<Joke> jokes = new ArrayList<>();
        jokes.add(new Joke(setup, punchLine));

        JokeTeller jokeTeller = new JokeTeller(jokes);

        Joke testJoke = jokeTeller.tellJoke();

        assertEquals(setup, testJoke.setup);
        assertEquals(punchLine, testJoke.punchLine);
    }

    @Test
    public void testJokeIsReturnedWhenLoadingFromList() {
        JokeTeller jokeTeller = new JokeTeller();
        Joke joke = jokeTeller.tellJoke();

        assertNotNull(joke.setup);
        assertNotNull(joke.punchLine);
    }

}
