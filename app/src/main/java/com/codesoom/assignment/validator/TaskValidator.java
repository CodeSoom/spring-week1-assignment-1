package com.codesoom.assignment.validator;

import com.codesoom.assignment.domain.Task;

import java.util.List;
import java.util.Optional;

public class TaskValidator {

    public boolean validTaskId(String id) {
        try {
            int n = Integer.parseInt(id);

            if (n < 1) {
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean vaildTaskTitle(String string) {
        if (string.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean vaildBody(String string) {
        if (string.isBlank()) {
            return false;
        }
        return true;
    }

}
