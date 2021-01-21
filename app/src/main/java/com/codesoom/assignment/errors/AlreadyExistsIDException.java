package com.codesoom.assignment.errors;

/**
 * The class {@code AlreadyExistsIDException} using when insert already exists id.
 */
public class AlreadyExistsIDException extends RuntimeException {
    /**
     * Constructs a new exception with default message.
     */
    public AlreadyExistsIDException() {
        super("Inserted id is already exists.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public AlreadyExistsIDException(String message) {
        super(message);
    }
}
