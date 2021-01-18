package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private final long id;
    private final String title;

    @JsonCreator
    public Task(@JsonProperty("id") long id, @JsonProperty("title") String title) {
        this.id = id;
        this.title = title;
    }

    public long id() {
        return this.id;
    }
    public String title() {
        return this.title;
    }
}
