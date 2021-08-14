package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;

/**
 *  SendResponseData 클래스는 HttpExchange sendResponseHeaders 메소드에 들어가는 매개변수를 담고 있는 클래스입니다.
 * @author HyoungUkJJAng(김형욱)
 */
public class SendResponseData {

    /**
     * HTTP 상태코드를 저장하는 변수입니다.
     */
    private int httpStatusCode;

    /**
     * 응답 데이터를 저장하는 변수입니다.
     */
    private String response;

    /**
     * 기본값을 설정할 수 있는 메소드입니다.
     */
    public void init() {
        this.httpStatusCode = HttpStatusCode.NOT_FOUND.getStatus();
        this.response = "[]";
    }

    /**
     * 기본 생성자입니다. 상태코드와 응답데이터를 입력받아 저장합니다.
     * @param httpStatusCode : 상태코드를 담는 변수
     * @param response : 응답 데이터를 저장하는 변수
     */
    public SendResponseData(int httpStatusCode, String response) {
        this.httpStatusCode = httpStatusCode;
        this.response = response;
    }

    /**
     * HTTP 상태코드 데이터를 꺼낼 수 있는 메소드입니다.
     * @return httpStatusCode
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * 응답 데이터를 꺼낼 수 있는 메소드입니다.
      * @return response
     */
    public String getResponse() {
        return response;
    }


}
