package com.codesoom.assignment.domain;

public interface TaskReader {
    Task findBy(String taskId);
    Task getById();
}
