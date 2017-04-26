package com.udacity.gradle.joketeller;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
public abstract class Joke implements Serializable {
    public abstract String setup();
    public abstract String punchLine();

    public static Joke create(String setup, String punchLine) {
        return new AutoValue_Joke(setup, punchLine);
    }
}