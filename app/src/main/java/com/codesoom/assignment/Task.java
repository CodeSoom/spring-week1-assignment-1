package com.codesoom.assignment;

public class Task {
    static private Long maxId = 1L;
    private final Long id;
    private final String title;

    Task(String title) {
        this.id = generateId();
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    private Long generateId() {
        Long generatedId = maxId;
        maxId++;
        return generatedId;
    }
}
