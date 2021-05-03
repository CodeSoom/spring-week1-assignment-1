package com.codesoom.demo.handler;

import com.codesoom.demo.exceptions.NotFoundTaskException;
import com.codesoom.demo.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    final int HTTP_STATUS_CREATE = 201;
    final int HTTP_STATUS_OK = 200;
    final int HTTP_STATUS_FAIL = 400;

    private static Long taskIdSeq = 0L;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod(); // Method
        URI uri = httpExchange.getRequestURI(); // URI
        String path = uri.getPath(); // Path
        System.out.println(method + " " + path);

        Long pathValue = null; // PathValue로 넘겨받은 값
        int taskIndex = -1; // PathValue로 List에서 찾을 Task의 인덱스
        String content = null; // 최종 반환할 데이터
        int httpStatus = HTTP_STATUS_OK; // Http 상태코드

        try {

            Task rqTask = new Task(); // Request에서 받은 데이터를 담을 객체

            // 입력값 가져오기
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            if(!body.isBlank()){ // Request에서 받은 데이터가 있다면 Task 객체에 넣음
                rqTask = jsonStrToTask(body);
            }

            // PathVariable이 있는지 검사한다.
            String[] splitArr = path.split("/");
            if(splitArr.length > 2){  // PathValue가 있을 경우 값 세팅 및 PathValue에 해당하는 Task의 인덱스를 구함

                // 값 세팅
                path = "/" + splitArr[1]; // API 경로
                pathValue = Long.valueOf(splitArr[2]); // PathValue(taskId)

                // PathValue에 해당하는 Task의 인덱스를 구함
                taskIndex = findTaskIndex(tasks, pathValue);

                // 해당하는 Task가 없을 경우 예외 발생
                if(taskIndex < 0){
                    throw new NotFoundTaskException(pathValue);
                }

            }

            if(method.equals("GET") && path.equals("/tasks")){ // Task List 조회 & 단건조회

                if(pathValue == null){
                    content = toJsonStr(tasks);
                } else {
                    Task findTask = tasks.get(taskIndex);
                    content = toJsonStr(findTask);
                }

            } else if(method.equals("POST") && path.equals("/tasks")){ // Task 등록

                rqTask.setId(taskIdSeq++);
                tasks.add(rqTask);

                content = toJsonStr(rqTask);
                httpStatus = HTTP_STATUS_CREATE;

            } else if(method.equals("PUT") && path.equals("/tasks") ){ // Task 수정

                String newTitle = rqTask.getTitle();

                Task modifiedTask = tasks.get(taskIndex);
                modifiedTask.setTitle(newTitle);

                content = toJsonStr(modifiedTask);


            } else if(method.equals("DELETE") && path.equals("/tasks")){ // Task 삭제

                tasks.remove(taskIndex);

            } else {

                httpStatus = HTTP_STATUS_FAIL;
                content = "잘못된 요청입니다.";

            }

        } catch (NotFoundTaskException e) {

            httpStatus = HTTP_STATUS_FAIL;
            content = "해당하는 Task를 찾을 수 없습니다.";

        } catch (Exception e) {

            httpStatus = HTTP_STATUS_FAIL;
            content = "통신에 실패했습니다.";

        } finally {

            httpExchange.sendResponseHeaders(httpStatus, content.getBytes().length );

            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();

        }

    }

    /**
     * Task List에서 ID에 해당하는 Task의 Index를 찾음
     * @param tasks
     * @param taskId
     * @return
     */
    private int findTaskIndex(List<Task> tasks, Long taskId) {

        int findTaskIndex = -1;
        for(int i = 0 ; i < tasks.size() ; i++){
            if(taskId == tasks.get(i).getId()){
                findTaskIndex = i;
            }
        }

        return findTaskIndex;
    }

    /**
     * JsonString으로 변환한다.
     * @param obj
     * @return
     * @throws IOException
     */
    private String toJsonStr(Object obj) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);

        return outputStream.toString();

    }

    /**
     * JsonString을 Task 객체로 변환한다.
     * @param content
     * @return
     * @throws JsonProcessingException
     */
    private Task jsonStrToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


}
