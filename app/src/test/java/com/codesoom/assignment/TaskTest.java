package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {


    @Test
    void testTask(){
        Task task = new Task(1L, "title");
    }

    @Test
    void testGet(){
        Task task = new Task(1L, "title");
        assertEquals(task.getId(), 1L);
    }

    



}
