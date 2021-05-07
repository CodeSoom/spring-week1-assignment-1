package com.codesoom.assignment.handler;

import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpStatus;
import com.codesoom.assignment.exceptions.NotFoundTaskException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 수신받은 Request에 대해 적절한 Response를 송신하는 클래스입니다.
 *
 * @author DevRunner21
 * @version 1.0
 * @since 2021.05.07
 */
public class DemoHttpHandler implements HttpHandler {

    private static Long taskIdSeq = 0L;

    private List<Task> tasks = new ArrayList<>();

    /**
     * Request가 들어 왔을 떄, HttpMethod와 URL에 따라 적절한 Response를 전송합니다.
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod(); // Method
        URI uri = httpExchange.getRequestURI(); // URI
        String path = uri.getPath(); // Path
        Long pathValue = null; // PathValue로 넘겨받은 값

        System.out.println(method + " " + path);

        try {

            Task rqBody = new Task(); // Request에서 받은 데이터를 담을 객체

            // 입력값 가져오기
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            if(!body.isBlank()){ // Request에서 받은 데이터가 있다면 Task 객체에 넣음
                rqBody = JsonUtil.jsonStrToTask(body);
            }

            // PathVariable이 있는지 검사한다.
            String[] splitArr = path.split("/");
            if(splitArr.length > 2){  // PathValue가 있을 경우 값 세팅 및 PathValue에 해당하는 Task의 인덱스를 구함

                // 값 세팅
                path = "/" + splitArr[1]; // API 경로
                pathValue = Long.valueOf(splitArr[2]); // PathValue(taskId)

            }

            // 각 HTTP Method 별로 로직 분기

            if(method.equals(HttpMethod.GET.name()) && path.equals("/tasks")){ // Task List 조회 & 단건조회

                if(pathValue == null){
                    tasksGET(httpExchange);
                } else {
                    tasksGET(httpExchange, pathValue);
                }
                return;

            }

            if(method.equals(HttpMethod.POST.name()) && path.equals("/tasks")){ // Task 등록

                tasksPOST(httpExchange, rqBody);
                return;

            }

            if( method.equals(HttpMethod.PUT.name()) && path.equals("/tasks") ){ // Task 수정

                tasksPUT(httpExchange, pathValue, rqBody.getTitle());
                return;

            }

            if( method.equals(HttpMethod.PATCH.name()) && path.equals("/tasks") ){ // Task 수정

                tasksPATCH(httpExchange, pathValue, rqBody);
                return;

            }

            if(method.equals(HttpMethod.DELETE.name()) && path.equals("/tasks")){ // Task 삭제

                tasksDELETE(httpExchange, pathValue);
                return;

            }

            sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCodeNo(), "잘못된 요청입니다.");

        } catch (NotFoundTaskException e) {

            e.printStackTrace();
            sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCodeNo(), "해당하는 Task를 찾을 수 없습니다.");

        } catch (Exception e) {

            e.printStackTrace();
            sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCodeNo(), "통신에 실패했습니다.");

        }

    }

    /**
     * 할 일 목록에서 주어진 할 일 id에 해당하는 할 일의 인덱스 넘버를 찾아 리턴합니다.
     * @param tasks 할 일 목록
     * @param taskId 찾으려 하는 할 일의 아이디
     * @return findTaskIndex
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
     * Response를 전송 메서드
     * @param httpExchange
     * @param httpStatus 전송할 Response의 Http 상태 코드
     * @param resBody Response에 담아서 전송할 데이터
     * @throws IOException
     */
    private void sendResponse(HttpExchange httpExchange, int httpStatus, String resBody) throws IOException {

        httpExchange.sendResponseHeaders(httpStatus, resBody.getBytes().length );
        System.out.println("response = " + resBody);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(resBody.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 할 일의 전체 목록을 조회합니다.
     * @param httpExchange
     * @throws IOException
     */
    private void tasksGET(HttpExchange httpExchange) throws IOException {

        String content = null;
        int httpStatus = HttpStatus.OK.getCodeNo();

        content = JsonUtil.toJsonStr(tasks);

        sendResponse(httpExchange, httpStatus, content);

    }

    /**
     * 주어진 할 일 id에 해당하는 할 일 데이터를 조회합니다.
     * @param httpExchange
     * @param taskId 조회할 할 일 Id
     * @throws IOException
     */
    private void tasksGET(HttpExchange httpExchange, Long taskId) throws IOException {

        String content = null;
        int httpStatus = HttpStatus.OK.getCodeNo();

        int taskIndex = findTaskIndex(tasks, taskId);// taskId에 해당하는 Task의 인덱스를 구함

        if(taskId == null){
            content = JsonUtil.toJsonStr(tasks);
        } else {
            Task findTask = tasks.get(taskIndex);
            content = JsonUtil.toJsonStr(findTask);
        }

        sendResponse(httpExchange, httpStatus, content);

    }

    /**
     * 새로운 할 일을 할 일 목록에 등록합니다.
     * @param httpExchange
     * @param newTask 새로 등록할 할 일
     * @throws IOException
     */
    private void tasksPOST(HttpExchange httpExchange, Task newTask) throws IOException {

        String content = null;
        int httpStatus = HttpStatus.CREATE.getCodeNo();

        newTask.setId(taskIdSeq++);
        tasks.add(newTask);

        content = JsonUtil.toJsonStr(newTask);

        sendResponse(httpExchange, httpStatus, content);

    }

    /**
     * 주어진 할 일 Id에 해당하는 할 일의 제목을 수정한다.
     * @param httpExchange
     * @param taskId 수정 대상인 할 일의 Id
     * @param newTitle 수정할 할 일 제목
     * @throws IOException
     */
    private void tasksPUT(HttpExchange httpExchange,  Long taskId, String newTitle) throws IOException {

        String content = null;
        int httpStatus = HttpStatus.OK.getCodeNo();

        int taskIndex = findTaskIndex(tasks, taskId);// PathValue에 해당하는 Task의 인덱스를 구함

        // 해당하는 Task가 없을 경우 예외 발생
        if(taskIndex < 0){
            throw new NotFoundTaskException(taskId);
        }

        Task modifiedTask = tasks.get(taskIndex);
        modifiedTask.setTitle(newTitle);

        content = JsonUtil.toJsonStr(modifiedTask);

        sendResponse(httpExchange, httpStatus, content);

    }

    /**
     * 주어진 할 일 Id에 해당하는 할 일을 수정합니다.
     * @param httpExchange
     * @param pathValue
     * @param rqTask
     * @throws IOException
     */
    private void tasksPATCH(HttpExchange httpExchange,  Long pathValue, Task rqTask) throws IOException {

        String content = null;
        int httpStatus = HttpStatus.OK.getCodeNo();

        int taskIndex = findTaskIndex(tasks, pathValue);// PathValue에 해당하는 Task의 인덱스를 구함

        // 해당하는 Task가 없을 경우 예외 발생
        if(taskIndex < 0){
            throw new NotFoundTaskException(pathValue);
        }

        String newTitle = rqTask.getTitle();

        Task modifiedTask = tasks.get(taskIndex);
        modifiedTask.setTitle(newTitle);

        content = JsonUtil.toJsonStr(modifiedTask);

        sendResponse(httpExchange, httpStatus, content);

    }


    /**
     * 주어진 할 일 Id에 해당하는 할 일을 목록에서 삭제합니다.
     * @param httpExchange
     * @param taskId 삭제할 할 일의 Id
     * @throws IOException
     */
    private void tasksDELETE(HttpExchange httpExchange, Long taskId) throws IOException {

        String content = "";
        int httpStatus = HttpStatus.NO_CONTENT.getCodeNo();

        int taskIndex = findTaskIndex(tasks, taskId);// PathValue에 해당하는 Task의 인덱스를 구함

        // 해당하는 Task가 없을 경우 예외 발생
        if(taskIndex < 0){
            throw new NotFoundTaskException(taskId);
        }

        tasks.remove(taskIndex);

        sendResponse(httpExchange, httpStatus, content);

    }


}
