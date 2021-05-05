package com.codesoom.assignment.enums;

public enum HttpStatus {

    OK(200),
    CREARE(201),
    NO_CONTENT(204), // 요청이 성공했으나 클라이언트가 현재 페이지에서 벗어나지 않아도 된다는 것을 나타냄
    NOT_FOUND(404);

    private int codeNo;

    HttpStatus(int i) {
        this.codeNo = i;
    }

    public int getCodeNo() {
        return codeNo;
    }

}
