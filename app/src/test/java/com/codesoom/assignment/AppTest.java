package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }

    @Test
    void isValidPathTest() {
        TaskHttpHandler taskHttpHandler = new TaskHttpHandler();

        String path1 = "/tasks";
        String[] pathItems1 = path1.split("/");
        assertTrue(taskHttpHandler.isValidPath(pathItems1));

        String path2 = "/tasks/1";
        String[] pathItems2 = path2.split("/");
        assertTrue(taskHttpHandler.isValidPath(pathItems2));

        String path3 = "tasks/tasks/1";
        String[] pathItems3 = path3.split("/");
        assertFalse(taskHttpHandler.isValidPath(pathItems3));

        String path4 = "/tasks/1/2/3";
        String[] pathItems4 = path4.split("/");
        assertFalse(taskHttpHandler.isValidPath(pathItems4));

        String path5 = "/task/1";
        String[] pathItems5 = path5.split("/");
        assertFalse(taskHttpHandler.isValidPath(pathItems5));
    }
}
