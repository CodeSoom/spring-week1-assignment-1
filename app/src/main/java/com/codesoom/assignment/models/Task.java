package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Task {
    private final long id;
    private final String title;

    @JsonCreator
    public Task(
            @JsonProperty("id") long id,
            @JsonProperty("title") String title
    ) {
        this.id = id;
        this.title = title;
    }

    @JsonGetter
    public long id() {
        return this.id;
    }

    @JsonGetter
    public String title() {
        return this.title;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
