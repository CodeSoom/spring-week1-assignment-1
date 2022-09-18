package com.codesoom.exception;

public class MethodNotExistException extends RuntimeException{
    public MethodNotExistException() {
        super("존재하지 않는 Method입니다.");
    }
}
