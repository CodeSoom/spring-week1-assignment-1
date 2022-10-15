package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("title")
    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
