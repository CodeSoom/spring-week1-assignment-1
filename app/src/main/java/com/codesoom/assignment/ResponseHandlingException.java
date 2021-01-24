package com.codesoom.assignment;

/**
 *  define exception related with handling response contents
 */
public class ResponseHandlingException extends Exception {
    private final HttpStatusCode httpStatusCode;

    public ResponseHandlingException(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void printDescription() {
        switch (this.httpStatusCode) {
            case NOT_FOUND:
                System.out.println("Not Found.");

            case METHOD_NOT_ALLOWED:
                System.out.println("Method Not Allowed.");
        }
    }
}
