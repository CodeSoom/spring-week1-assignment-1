package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskDto {
    @JsonProperty("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
