package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;

/**
 * HttpExchange sendResponseHeaders 메소드에 들어가는 매개변수를 담고 있는 클래스
 */
public class SendResponseData {

    private int httpStatusCode;
    private String response;

    /**
     * 초기값 메소드
     */
    public void init() {
        this.httpStatusCode = HttpStatusCode.NOT_FOUND.getStatus();
        this.response = "[]";
    }

    /**
     * 기본 생성자
     * @param httpStatusCode : 상태코드
     * @param response : 응답 데이터
     */
    public SendResponseData(int httpStatusCode, String response) {
        this.httpStatusCode = httpStatusCode;
        this.response = response;
    }

    /**
     * HTTP 상태코드 반환
     * @return httpStatusCode
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * 응답 데이터 반환
      * @return response
     */
    public String getResponse() {
        return response;
    }


}
