package com.udacity.gradle.builditbigger.backend;

public class CloudJoke {

    private String setup;
    private String punchLine;

    public String getSetup() {
        return setup;
    }

    public String getPunchLine() {
        return punchLine;
    }

    public CloudJoke(String setup, String punchLine) {
        this.setup = setup;
        this.punchLine = punchLine;
    }
}
