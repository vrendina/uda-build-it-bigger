package com.udacity.gradle.joketeller;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
abstract class Joke implements Serializable {
    abstract String setup();
    abstract String punchLine();

    static Joke create(String setup, String punchLine) {
        return new AutoValue_Joke(setup, punchLine);
    }

}