package com.codesoom.assignment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Task {
    private Long id;
    private String title;

    @JsonCreator
    public Task(@JsonProperty("id") Long id,
                @JsonProperty("title") String title)
    {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        this.id = id;
        this.title = title;
    }

    @JsonGetter
    public Long getId() {
        return id;
    }

    @JsonGetter
    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
