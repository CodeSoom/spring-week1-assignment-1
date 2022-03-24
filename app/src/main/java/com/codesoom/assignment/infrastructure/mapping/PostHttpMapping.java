package com.codesoom.assignment.infrastructure.mapping;

import com.codesoom.assignment.common.logger.Log;
import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.common.response.HttpResponse;
import com.codesoom.assignment.domain.mapping.HttpMapping;
import com.codesoom.assignment.domain.todo.Todo;
import com.codesoom.assignment.domain.todo.TodoService;

import java.util.logging.Logger;

public class PostHttpMapping implements HttpMapping {
    private final TodoService todoService;
    private final Logger logger = Log.getInstance().getLog(PostHttpMapping.class);
    public PostHttpMapping(TodoService todoService){
        this.todoService=todoService;
    }
    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        String title;
        String path = httpRequest.getPath();
        logger.info("hello");
        if(path.equals("/tasks")){
            title = (String) httpRequest.getRequestBody().get("title");
            return save(title);
        }
        return HttpResponse.fail(404, ErrorCode.NOT_INCLUDE_PATH.getErrorMsg());
    }


    private HttpResponse save(String title){
        Todo todo = new Todo.TodoBuilder()
                            .title(title)
                            .builder();
        todo = todoService.save(todo);
        return HttpResponse.success(201,todo);
    }
}
