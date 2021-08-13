package com.codesoom.assignment;

import com.codesoom.HttpEnum.HttpMethodCode;
import com.codesoom.HttpEnum.HttpStatusCode;
import com.codesoom.models.SendResponseData;
import com.codesoom.models.TaskService;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CrudHttpHandler implements HttpHandler {

    private static final TaskService taskService = new TaskService();
    private SendResponseData response = new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

       response.init(); // 매번 새로운 요청이 들어올때마다 상태코드와 response값을 초기화 해줘야 올바른 상태코드와 메시지를 전달 할 수 있다.

        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();



        // 라이브러리 메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있는 곳에서 제한적으로 사용할 수 있는 메커니즘을 제공하는 것이 Optional을 만든 의도라고 나와았습니다.
        // 옵셔널의 사용이 어제가 처음이여서 그런지 많은 어색함이 있는것 같습니다.주의사항이 여러개 있었는데 그중 하나가 옵셔널을 필드에서 사용하면 좋지 않다는게 문지점인것 같습니다.
        // 하지만 수정에 대한 감이 잡히질 않아 옵셔널에 좀 더 익숙해질 필요가 있어 다양하게 적용해 보고 있습니다.
        final Optional<String> hasId = Optional.of(hasId(path));

        String hasNextId = Optional.ofNullable(hasId(path)).orElse("noId");

        /*
         * method에 null이 들어온다면 비교주체자가 null이 되버리기 때문에 equals를 실행할 수 없어 NPE가 발생할 가능성이 생기는 것 같습니다.
         * 반대로 "GET".equals(method) 로 변경하면 비교하는 주체가 null이 발생할 일이 없어지기 때문에 NPE 방지가 되는 원리 같습니다.
         */
        if(HttpMethodCode.GET.getStatus().equals(method) && "/tasks".equals(path) && !hasId.isPresent() ) {

            response = taskService.getAll();

        } else if(HttpMethodCode.GET.getStatus().equals(method) && hasId.isPresent() ) {

            response = taskService.getOne(hasId.get());

        } else if(HttpMethodCode.POST.getStatus().equals(method) && "/tasks".equals(path)) {

            response = taskService.join(getContent(exchange));

        } else if( (HttpMethodCode.PUT.getStatus().equals(method) || HttpMethodCode.PATCH.getStatus().equals(method) ) && hasId.isPresent()) {

            response = taskService.edit(getContent(exchange), hasId.get());

        } else if(HttpMethodCode.DELETE.getStatus().equals(method) && hasId.isPresent() ) {

            response = taskService.delete(hasId.get());

        }

        exchange.sendResponseHeaders(response.getHttpStatusCode(),response.getResponse().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getResponse().getBytes());
        outputStream.flush();
        outputStream.close();

    }


    /*
     * 서버에서 클라이언트의 요청을 수행할 수 없을 때 404에러를 내려주는데 중복이 많이 보여서 따로 메소드로 빼자는 의도로 작성하였습니다.
     * 소스코드를 보기 편하도록 만드는거에 너무 많은 신경을 썼나봐요...
     * 메소드 자체에 어떤 문제가 있을지 감이 잘 잡히질 않습니다.. 에러코드를 내리는 메소드가 핸들러 클래스에 있는것이 문제인 것 일까요?
     */
    private void NOT_FOUND() {
        response.setHttpStatusCode(HttpStatusCode.NOT_FOUND.getStatus());
        response.setResponse("[]");
    }

    private String hasId(String path) {

        if ( Pattern.matches("/tasks/[0-9]*$",path) ) {
            return path.replace("/tasks/", "");
        } else {
            return null;
        }

    }

    private String getContent(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }


}
