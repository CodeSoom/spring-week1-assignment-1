package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskJsonTransferTest {
    TaskJsonTransfer transfer = new TaskJsonTransfer();

    @Test
    void transferJsonToTask(){
        String jsonString = "{\"title\": \"Play Game\"}";
        Task task = transfer.jsonStringToTask(jsonString);

        assertNotNull(task);
        assertEquals("Play Game", task.getTitle());
    }
}
