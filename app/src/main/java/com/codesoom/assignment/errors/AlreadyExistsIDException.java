package com.codesoom.assignment.errors;

/**
 * The class {@code AlreadyExistsIDException} using when insert already exists id.
 */
public class AlreadyExistsIDException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    AlreadyExistsIDException() {
        super("Inserted id is already exists.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    AlreadyExistsIDException(String message) {
        super(message);
    }
}
