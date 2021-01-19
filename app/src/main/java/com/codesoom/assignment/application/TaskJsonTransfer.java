package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;

import java.util.List;

public class TaskJsonTransfer {
    public Task jsonStringToTask(String jsonString) {
        return new Task(-1L, "Play Game");
    }
}
