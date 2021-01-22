package com.codesoom.assignment;

public class ResponseHandlingException extends Exception {
    enum ErrorCode {
        WRONG_PATH, UNKNOWN_HTTP_METHOD
    }

    private final ErrorCode errorCode;

    public ResponseHandlingException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void printDescription() {
        switch (this.errorCode) {
            case WRONG_PATH:
                System.out.println("Wrong URI path");

            case UNKNOWN_HTTP_METHOD:
                System.out.println("Unknown HTTP method");
        }
    }
}