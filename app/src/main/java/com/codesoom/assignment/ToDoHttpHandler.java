package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.network.HttpResponseCode;
import com.codesoom.assignment.network.HttpRouter;
import com.codesoom.assignment.network.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;

/**
 * HTTP exchanges를 통해 전달받은 Request를 분석해서 할일 목록을 관리하고 적절한 Response를 전달하는 객체
 */
public class ToDoHttpHandler implements HttpHandler {
    private final TaskMapper taskMapper = new TaskMapper();
    private final ToDoController controller = new ToDoController(new ToDoRepository());
    private final HttpRouter router;

    /**
     * ToDoHttpHandler를 생성합니다
     */
    public ToDoHttpHandler() {
        router = new HttpRouter();
        router.get("/tasks", (request, response) -> {
            this.sendGetResponseRoot(response);
        });
        router.get("/tasks/\\d*", (request, response) -> {
            this.sendGetResponseWithId(request.getPath(), response);
        });
        router.post("/tasks", (request, response) -> {
            this.sendPostResponse(response, request.getBody());
        });
        router.put("/tasks/\\d*", (request, response) -> {
            this.sendPutResponse(response, request.getPath(), request.getBody());
        });
        router.patch("/tasks/\\d*", (request, response) -> {
            this.sendPutResponse(response, request.getPath(), request.getBody());
        });
        router.delete("/tasks/\\d*", (request, response) -> {
            this.sendDeleteResponse(response, request.getPath());
        });
    }

    /**
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws IOException Response 전송시에 예외가 발생될 수 있습니다
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        router.route(exchange);
    }

    /**
     * [Get Method] 특정 Task Id로 요청 대한 응답을 전송합니다
     * @param path Http Request Path
     * @param response Http Response
     * @throws IOException
     */
    private void sendGetResponseWithId(String path, HttpResponse response) throws IOException {
        Long taskId;
        try {
            taskId = parseTaskIdFromPath(path);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        try {
            Task task = controller.getTaskById(taskId);
            response.send(HttpResponseCode.OK, taskMapper.writeTaskAsString(task));
        } catch (TaskNotFoundException e) {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        }
    }

    /**
     * [Get Method] Task 목록에 대한 응답을 전송합니다
     * @param response Http Response
     * @throws IOException
     */
    private void sendGetResponseRoot(HttpResponse response) throws IOException {
        try {
            List<Task> tasks = controller.getTasks();
            response.send(HttpResponseCode.OK, taskMapper.writeTasksAsString(tasks));
        } catch (JsonProcessingException e) {
            response.send(HttpResponseCode.InternalServerError, "Failed to convert tasks to JSON");
        }
    }

    /**
     * [Delete Method] Task 삭제에 대한 응답을 전송합니다
     * @param response Http Response
     * @param path Request path
     * @throws IOException
     */
    private void sendDeleteResponse(HttpResponse response, String path) throws IOException {
        Long taskId;
        try {
            taskId = parseTaskIdFromPath(path);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        try {
            controller.deleteTask(taskId);
            response.send(HttpResponseCode.NoContent, null);
        } catch (TaskNotFoundException e) {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        }
    }

    /**
     * [Put Method] Task 추가에 대한 응답을 전송합니다
     * @param response Http Response
     * @param path Request path
     * @param body Request body
     * @throws IOException
     */
    private void sendPutResponse(HttpResponse response, String path, String body) throws IOException {
        Long taskId;
        try {
            taskId = parseTaskIdFromPath(path);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        try {
            controller.updateTask(taskId, body);
            Task updatedTask = controller.getTaskById(taskId);
            response.send(HttpResponseCode.OK, taskMapper.writeTaskAsString(updatedTask));
        } catch (TaskNotFoundException e) {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        } catch (JsonProcessingException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse request body to Task");
        }
    }

    /**
     * [Post Method] Task 수정에 대한 응답을 전송합니다
     * @param response Http Response
     * @param body Request body
     * @throws IOException
     */
    private void sendPostResponse(HttpResponse response, String body) throws IOException {
        if (body == null || body.isBlank()) {
            response.send(HttpResponseCode.BadRequest, "Failed to convert request body to Task");
            return;
        }

        try {
            Task task = controller.addTask(body);
            response.send(HttpResponseCode.Created, taskMapper.writeTaskAsString(task));
        } catch (JsonProcessingException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to convert request body to Task");
        }
    }

    /**
     * Path에서 TaskId를 추출합니다
     * @param path Request path
     * @return Task Id
     * @throws NumberFormatException
     */
    private long parseTaskIdFromPath(String path) throws NumberFormatException {
        return Long.parseLong(path.split("/")[2]);
    }
}
