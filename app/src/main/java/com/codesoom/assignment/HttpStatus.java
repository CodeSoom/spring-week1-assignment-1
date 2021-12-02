package com.codesoom.assignment;



public enum HttpStatus {

    HTTP_OK(200),
    HTTP_CREATE(201),
    HTTP_NO_CONTENT(204),
    HTTP_NOT_FOUND(404);


    private int status;

    HttpStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

/* 여러 상수를 선언 할 때 열거형으로 편리하게 선언 할 수 있는 방법이다.
 * -자바의 정석 참고-
 *
 상태 코드별 정리
 * 200 : OK
 * 201 : Create
 * 204 : No Content
 * 404 : Not Found
 *  */