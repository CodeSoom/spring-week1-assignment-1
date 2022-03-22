package com.codesoom.assignment.common.response;

public enum ErrorCode {
    NO_TASK("찾으시는 할일이 존재하지 않습니다");
    private final String errorMsg;

    ErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
