package com.codesoom.assignment.enums;

/**
 * 프로젝트 내에서 사용되는 HttpStatus를 정의해놓은 Enum입니다.
 *
 * @author DevRunner21
 * @version 1.0
 * @since 2021.05.07
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6">HttpResponse Status Codes(RFC 7231)</a>
 */
public enum HttpStatus {

    OK(200), // 요청이 성공했음을 나타냄
    CREATE(201), // 요청이 성공하여 하나 이상의 리소스가 생성되었음을 나타냄
    NO_CONTENT(204), // 요청이 성공했으나 클라이언트가 현재 페이지에서 벗어나지 않아도 된다는 것을 나타냄
    NOT_FOUND(404); // 요청한 리소스를 찾을 수 없음을 나타냄

    private int codeNo;

    HttpStatus(int i) {
        this.codeNo = i;
    }

    public int getCodeNo() {
        return codeNo;
    }

}
