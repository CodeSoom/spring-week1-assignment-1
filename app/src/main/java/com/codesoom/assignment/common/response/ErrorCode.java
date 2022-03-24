package com.codesoom.assignment.common.response;

public enum ErrorCode {
    NO_TASK("NO TASK"),
    EMPTY_TITLE("Title does not allow empty");
    private final String errorMsg;

    ErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
