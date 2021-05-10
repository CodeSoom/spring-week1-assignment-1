package com.codesoom.assignment.exception;

public class DoesNotExistException extends NullPointerException{

    public DoesNotExistException() {
        super("Does not Exist");
    }
}
