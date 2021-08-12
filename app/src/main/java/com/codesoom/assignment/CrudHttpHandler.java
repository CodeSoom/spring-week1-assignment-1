package com.codesoom.assignment;

import com.codesoom.HttpEnum.HttpMethodCode;
import com.codesoom.HttpEnum.HttpStatusCode;
import com.codesoom.models.SendResponseData;
import com.codesoom.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CrudHttpHandler implements HttpHandler {

    private Map<Long, Task> tasks = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();
    private Long autoId = 1L; // 아이디를 자동으로 부여하기 위한 변수
    private SendResponseData response = new SendResponseData(HttpStatusCode.NOTFOUND.getStatus(), "[]");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

       response.init(); // 매번 새로운 요청이 들어올때마다 상태코드와 response값을 초기화 해줘야 올바른 상태코드와 메시지를 전달 할 수 있다.

        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final Optional<String> hasId = Optional.ofNullable(hasId(path));

        /*
         * method에 null이 들어온다면 비교주체자가 null이 되버리기 때문에 equals를 실행할 수 없어 NPE가 발생할 가능성이 생기는 것 같습니다.
         * 반대로 "GET".equals(method) 로 변경하면 비교하는 주체가 null이 발생할 일이 없어지기 때문에 NPE 방지가 되는 원리 같습니다.
         */
        if(HttpMethodCode.GET.getStatus().equals(method) && "/tasks".equals(path) && !hasId.isPresent() ) {

            response = getAll();

        } else if(HttpMethodCode.GET.getStatus().equals(method) && hasId.isPresent() ) {

            response = getOne(hasId.get());

        } else if(HttpMethodCode.POST.getStatus().equals(method) && "/tasks".equals(path)) {

            response = join(getContent(exchange));

        } else if( (HttpMethodCode.PUT.getStatus().equals(method) || HttpMethodCode.PATCH.getStatus().equals(method) ) && hasId.isPresent()) {

            response = edit(getContent(exchange), hasId.get());

        } else if(HttpMethodCode.DELETE.getStatus().equals(method) && hasId.isPresent() ) {

            response = delete(hasId.get());

        }
        
        exchange.sendResponseHeaders(response.getHttpStatusCode(),response.getResponse().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getResponse().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private SendResponseData getAll() throws IOException {

        if(!tasks.isEmpty()) {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),toTaskJson());
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),"[]");
        }

    }

    private SendResponseData getOne(String hasId) throws IOException {

        Task task = tasks.get(Long.parseLong(hasId));
        if(task==null) {
            return new SendResponseData(HttpStatusCode.NOTFOUND.getStatus(), "아이디가 없습니다.");
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(), toTaskJsonOne(task));
        }

    }

    private SendResponseData join(String content) throws IOException {

        Task task = toTask(content);
        task.setId(autoId++);
        tasks.put(task.getId(), task);

        return new SendResponseData(HttpStatusCode.CREATED.getStatus(), toTaskJsonOne(task));

    }

    private SendResponseData edit(String content, String findId) throws IOException {

        Task taskPut = toTask(content);

        if(tasks.get(Long.parseLong(findId)) == null){
            return new SendResponseData(HttpStatusCode.NOTFOUND.getStatus(), "아이디가 없습니다.");
        } else {
            Task task = tasks.get(Long.parseLong(findId));
            task.setTitle(taskPut.getTitle());
            return new SendResponseData(HttpStatusCode.OK.getStatus(),toTaskJsonOne(task));
        }

    }

    private SendResponseData delete(String findId) throws IOException {

        if(tasks.get(Long.parseLong(findId)) == null) {
           return new SendResponseData(HttpStatusCode.NOTFOUND.getStatus(), "아이디가 없습니다.");
        } else {
            tasks.remove(Long.parseLong(findId));
            return new SendResponseData(HttpStatusCode.NOCONTENT.getStatus(), "");
        }

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

    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    private String toTaskJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks.values());

        return outputStream.toString();
    }

    private String toTaskJsonOne(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,task);

        return outputStream.toString();
    }

}
