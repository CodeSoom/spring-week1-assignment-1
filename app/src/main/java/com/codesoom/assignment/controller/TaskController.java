package com.codesoom.assignment.controller;

public class TaskController extends Controller {
    @Override
    public boolean handleResource(String path) {
        return path.matches("^/(tasks).*$");
    }
}
