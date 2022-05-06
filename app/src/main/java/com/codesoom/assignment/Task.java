package com.codesoom.assignment;

public class Task implements Comparable<Task> {
    private Long id;
    private String title;

    Task() {
    }

    Task(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, title = %s }", id, title);
    }

    @Override
    public int compareTo(Task task) {
        if (getId() == null || task.getId() == null) {
            return 0;
        }
        return getId().compareTo(task.getId());
    }
}
