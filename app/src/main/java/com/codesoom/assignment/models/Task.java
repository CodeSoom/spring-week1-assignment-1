package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private Long id;
    @JsonProperty(value = "title", required = true)
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void update(Task fromTask) {
        title = fromTask.getTitle();
    }
}
