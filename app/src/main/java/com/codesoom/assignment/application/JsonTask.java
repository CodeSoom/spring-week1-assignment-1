package com.codesoom.assignment.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTask {
    public String title;

    @JsonCreator
    public JsonTask(@JsonProperty("title") String title) {
        this.title = title;
    }
}
