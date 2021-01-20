package com.codesoom.assignment.errors;

/**
 * The class {@code NotExistsIDException} using when want to use not exists id.
 */
public class NotExistsIDException extends Exception {
    /**
     * Constructs a new exception with default message.
     */
    public NotExistsIDException() {
        super("Inserted id is not exists.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public NotExistsIDException(String message) {
        super(message);
    }
}
