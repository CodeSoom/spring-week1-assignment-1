package com.codesoom.assignment.exception;

public class ControllerNotFoundException extends NullPointerException {
    
    public ControllerNotFoundException() {
        super("Controller not found");
    }
}
