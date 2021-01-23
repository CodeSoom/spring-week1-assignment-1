package com.codesoom.assignment;

public class ResponseHandlingException extends Exception {
    public enum ErrorCode {
        NOT_FOUND, METHOD_NOT_ALLOWED
    }

    private final ErrorCode errorCode;

    public ResponseHandlingException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void printDescription() {
        switch (this.errorCode) {
            case NOT_FOUND:
                System.out.println("Not Found.");

            case METHOD_NOT_ALLOWED:
                System.out.println("Method Not Allowed.");
        }
    }
}