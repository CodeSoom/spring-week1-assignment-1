package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Task {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("title")
    private String title;

    private final LocalDateTime regDate;
    private LocalDateTime modDate;

    public Task(Long id, String title, LocalDateTime regDate) {
        this.id = id;
        this.title = title;
        this.regDate = regDate;
    }

    public void changeTitle(String title) {
        this.title = title;
        modDate = LocalDateTime.now();
    }
}
