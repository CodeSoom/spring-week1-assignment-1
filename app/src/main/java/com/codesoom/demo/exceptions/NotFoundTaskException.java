package com.codesoom.demo.exceptions;

public class NotFoundTaskException extends RuntimeException{

    public NotFoundTaskException(Long taskId) {
        super("Not Found Task" + taskId);
    }
}
