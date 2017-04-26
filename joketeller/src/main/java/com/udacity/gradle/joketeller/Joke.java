package com.udacity.gradle.joketeller;

import java.io.Serializable;

public class Joke implements Serializable {

    public String setup;
    public String punchLine;

    public Joke(String setup, String punchLine) {
        this.setup = setup;
        this.punchLine = punchLine;
    }

    @Override
    public String toString() {
        return "Setup: '" + setup + "' Punch-line: '" + punchLine + "'";
    }
}
