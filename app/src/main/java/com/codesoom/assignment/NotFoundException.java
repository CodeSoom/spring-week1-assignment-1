package com.codesoom.assignment;

class NotFoundException extends Exception{// 에러 코드 값을 저장하기 위한 필드를 추가 했다.
    private final int ERR_CODE = 404;// 생성자를 통해 초기화 한다.
    NotFoundException(String msg){ //생성자
        super(msg);
    }

}

