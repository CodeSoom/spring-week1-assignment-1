package com.codesoom.assignment.httpHandlers.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task를 찾을 수 없습니다.");
    }
}
