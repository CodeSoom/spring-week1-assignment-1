package com.codesoom.assignment.handlers;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.codesoom.assignment.App.*;

/**
 * Task 도메인 리퀘스트를 처리합니다.
 */

public class TaskHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<Task>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private int responseCode;

    /**
     * 사용자가 Request 를 보내면 실행되는 메서드 입니다.
     * @param exchange Http 통신을 통해 클라이언트에게 전달 받은 데이터가 들어있습니다.
     */

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String requestURIPath = requestURI.getPath();
        String requestMethod = exchange.getRequestMethod();
        String result = "";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(requestURIPath == null){
            responseCode = HttpStatusCode.NOT_FOUND.getCode();
            result = ResultMessage.NOT_FOUND.getMessage();
        }else{
            switch (requestMethod) {
                case "GET":
                    result = getMapper(requestURIPath);
                    break;
                case "POST":
                    result = postMapper(body);
                    break;
                case "PUT":
                case "PATCH": {
                    result = putMapper(requestURIPath, body);
                    break;
                }
                case "DELETE": {
                    result = deleteMapper(requestURIPath);
                    break;
                }
                default:
                    result = ResultMessage.METHOD_NOT_ALLOWED.getMessage();
                    responseCode = HttpStatusCode.METHOD_NOT_ALLOWED.getCode();
                    break;
            }
        }
        System.out.println(responseCode);
        exchange.sendResponseHeaders(responseCode, result.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(result.getBytes());
        responseBody.flush();
        exchange.close();
    }

    /**
     * 사용자의 RequestMethod 가 GET 이 였을 경우 URIPath 에 따라 필요한 메서드를 실행해주는 분기 메서드 입니다.
     * @param requestURIPath 사용자가 어떤 URL 로 접속했는지를 나타내주는 String 인자 입니다.
     * @return 분기를 통해 결과값을 String 으로 리턴합니다.
     */

    private String getMapper(String requestURIPath) throws IOException {
        String result = "";
        responseCode = HttpStatusCode.OK.getCode();
        if(requestURIPath.equals("/tasks")){
            result = getTasks();
        }else if(requestURIPath.contains("/tasks")){
            String[] split = requestURIPath.split("/");
            if(split.length == 3){
                result = getTask(extractIDFromBody(requestURIPath));
            }else{
                result = ResultMessage.BAD_REQUEST.getMessage();
                responseCode = HttpStatusCode.BAD_REQUEST.getCode();
            }
        }
        return result;
    }

    /**
     * 사용자의 RequestMethod 가 POST 였을 경우 Body Parameter 의 여부에 따라 필요한 메서드를 실행해주는 분기 메서드 입니다.
     * @param body 사용자가 Request 로 보낸 Body Parameter 가 String 인자로 들어옵니다.
     * @return 분기를 통해 결과값을 String 으로 리턴합니다.
     */

    private String postMapper(String body) throws IOException {
        String result;
        if (body.isBlank()) {
            result = ResultMessage.BAD_REQUEST.getMessage();
            responseCode = HttpStatusCode.BAD_REQUEST.getCode();
        } else {
            String title = body;
            if(title.contains("=")){
                title = getParameterFromBody(body);
            }
            result = setTask(title);
        }

        return result;
    }

    /**
     * 사용자의 RequestMethod 가 PUT 였을 경우 Body Parameter 의 여부에 따라 필요한 메서드를 실행해주는 분기 메서드 입니다.
     * @param requestURIPath 사용자가 접속 요청을 보낸 URI 에 path 정보가 String 인자로 들어옵니다.
     * @param body 사용자가 Request 로 보낸 Body Parameter 가 String 인자로 들어옵니다.
     * @return 분기를 통해 결과값을 String 으로 리턴합니다.
     */

    private String putMapper(String requestURIPath, String body) throws IOException {
        String[] split = requestURIPath.split("/");
        String result = ResultMessage.BAD_REQUEST.getMessage();
        String title = body;
        if(title.contains("=")){
            title = getParameterFromBody(body);
        }
        responseCode = HttpStatusCode.BAD_REQUEST.getCode();
        if (!body.isBlank() && split.length > 0 && split.length != 1) {
            result = updateTask(title, extractIDFromBody(requestURIPath));
        }

        return result;
    }

    /**
     * 사용자의 RequestMethod 가 PUT 였을 경우 Body Parameter 의 여부에 따라 필요한 메서드를 실행해주는 분기 메서드 입니다.
     * @param requestURIPath 사용자가 접속 요청을 보낸 URI 에 path 정보가 String 인자로 들어옵니다.
     * @return 분기를 통해 결과값을 String 으로 리턴합니다.
     */

    private String deleteMapper(String requestURIPath) throws IOException {
        String result = ResultMessage.BAD_REQUEST.getMessage();
        responseCode = HttpStatusCode.BAD_REQUEST.getCode();
        String[] split = requestURIPath.split("/");
        if (split.length > 0 && split.length != 1) {
            result = deleteTask(extractIDFromBody(requestURIPath));
        }
        return result;
    }

    /**
     * 저장되어있는 전체 Task 를 조회합니다.
     * @return 전체 Task 를 String 으로 리턴합니다.
     */

    private String getTasks() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    /**
     * ID 에 해당하는 Task 를 조회합니다.
     * @param ID 조회할 Task 와 매칭될 ID 입니다.
     * @return ID 에 해당하는 Task 를 JSON 형태의 String 으로 리턴하고, 해당되는 ID가 없으면 NOT_FOUND 메세지를 리턴합니다.
     */

    private String getTask(long ID) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        if(tasks.size() > 0){
            for(Task task : tasks){
                System.out.println(task.getId());
                if(task.getId() == ID){
                    objectMapper.writeValue(outputStream, task);
                    responseCode = HttpStatusCode.OK.getCode();
                    return outputStream.toString();
                }
            }
        }

      responseCode = HttpStatusCode.NOT_FOUND.getCode();
      return ResultMessage.NOT_FOUND.getMessage();
    }

    /**
     * 새로운 Task 를 생성합니다.
     * @param title Task 데이터가 들어있는 JSON 형태의 String 입니다.
     * @return 전체 Task 를 리턴합니다.
     */

    private String setTask(String title) throws IOException {
        Task taskJson = toTask(title);
        if(tasks.size() > 0){
            taskJson.setId(tasks.get(tasks.size() - 1).getId() + 1L);
        }
        tasks.add(taskJson);
        responseCode = HttpStatusCode.CREATED.getCode();
        return getTasks();
    }

    /**
     * ID에 해당하는 Task 를 ArrayList 에서 찾아 title 을 업데이트합니다.
     * @param title 매칭된 Task 의 변경 될 title 제목입니다.
     * @param ID ArrayList 에 저장된 Task 를 매칭하기 위한 Key 값입니다.
     * @return 매칭되는 ID가 있으면 해당 task 를 리턴하고, 없으면 Not Found message 를 리턴합니다.
     */

    private String updateTask(String title, long ID) throws IOException {
        if(tasks.size() > 0){
            for(Task task : tasks){
                if(task.getId() == ID){
                    task.setTitle(title);
                    responseCode = HttpStatusCode.OK.getCode();
                    return getTask(ID);
                }
            }
        }
        responseCode = HttpStatusCode.NOT_FOUND.getCode();
        return ResultMessage.NOT_FOUND.getMessage();
    }

    /**
     * ID에 해당하는 Task 를 ArrayList 에서 제거합니다.
     * @param ID ArrayList 에 저장된 Task 를 매칭하기 위한 Key 값입니다.
     * @return 매치되는 ID가 있으면 해당 task를 지우고 empty string 를 리턴하고, 없으면 Not Found message 를 리턴합니다.
     */

    private String deleteTask(long ID) throws IOException {
        if(tasks.size() > 0){
            for(Task task : tasks){
                if(task.getId() == ID){
                    tasks.remove(task);
                    responseCode = HttpStatusCode.NO_CONTENT.getCode();
                    return "";
                }
            }
        }
        responseCode = HttpStatusCode.NOT_FOUND.getCode();
        return ResultMessage.NOT_FOUND.getMessage();
    }

    /**
     * 전달 받은 JSON 형태의 문자열을 Jackson 을 사용해 Task 형태로 변환합니다.
     * @param content JSON 형태의 String 을 인자로 받습니다.
     * @return 값을 읽어 Task 를 리턴합니다.
     */

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    /**
     * 전달 받은 JSON 형태의 문자열을 Jackson 을 사용해 Task 형태로 변환합니다.
     * @param body "key=value" 형태의 string 을 인자로 받습니다.
     * @return '=' 을 split 으로 나눠서 배열 인덱스 1번에 담긴 value 를 리턴합니다.
     */

    private String getParameterFromBody(String body) {
        String[] split = body.split("=");
        return split[1];
    }

    /**
     * Request 로 전달받은 URI 에서 숫자 ID의 값을 추출합니다.
     * @param uri URI 형태의 스트링을 인자로 받습니다.
     * @return 숫자를 찾으면 해당 숫자를 리턴, 못찾으면 -1 을 리턴합니다.
     */

    private long extractIDFromBody(String uri) {
        Pattern pattern = Pattern.compile("[^0-9]");
        String extractNumber = pattern.matcher(uri).replaceAll("");
        if(extractNumber.equals("")){
            return -1;
        }
        return Long.parseLong(extractNumber);
    }

}

