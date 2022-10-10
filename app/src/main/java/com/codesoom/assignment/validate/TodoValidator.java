package com.codesoom.assignment.validate;

import com.codesoom.assignment.controller.TodoHttpController;

import java.util.ArrayList;
import java.util.List;

public class TodoValidator {
    final private TodoHttpController todoHttpController;
    final private List<String> exceptionIdList;

    public TodoValidator() {
        exceptionIdList = new ArrayList<>();
        exceptionIdList.add("null");
        exceptionIdList.add("undefined");
        todoHttpController = new TodoHttpController();
    }

    public void throwInvalidId(String path) {
        if (path.split("/").length <= 1) {
            return;
        }
        String id = path.split("/")[1];
        if (id.equals("null") || id.equals("undefined")) {
            throw new IllegalArgumentException();
        }
    }
}
