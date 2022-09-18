package com.codesoom.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException() {
        super("존재하지 않는 Task Id입니다.");
    }
}
