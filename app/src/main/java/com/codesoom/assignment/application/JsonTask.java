package com.codesoom.assignment.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTask {
    public Long id;
    public String title;

    @JsonCreator
    public JsonTask(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title
    ) {
        this.id = id;
        this.title = title;
    }
}
