package com.codesoom.assignment.network;

import java.io.IOException;

/**
 * HttpRouter에서 패턴에 매칭되면 수행할 행동을 추상화한 인터페이스
 */
public interface HttpRouterExecutable {
    /**
     * HttpRouter에서 패턴에 매칭되면 실행됩니다
     * @param request Http 요청이 추상화된 객체
     * @param response Http 응답결과 전송을 위한 객체
     * @throws IOException Http 응답결과 전송 호출시에 에러가 발생할 수 있습니다
     */
    void execute(HttpRequest request, HttpResponse response) throws IOException;
}
