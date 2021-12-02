package com.codesoom.assignment.validator;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidatorTest {

    TaskValidator taskValidator = new TaskValidator();

    @Test
    @DisplayName("Task ID 형식이 맞는지")
    void testValidTaskId() {
        String testNumber1 = "-1";
        String testNumber2 = "abd";
        String testNumber3 = "3";

        assertFalse(taskValidator.validTaskId(testNumber1));
        assertFalse(taskValidator.validTaskId(testNumber2));
        assertTrue(taskValidator.validTaskId(testNumber3));
    }

    @Test
    void testValidBody() {
        String testString1 = "";
        String testString2 = "asdf";

        assertFalse(taskValidator.vaildBody(testString1));
        assertTrue(taskValidator.vaildBody(testString2));
    }
}