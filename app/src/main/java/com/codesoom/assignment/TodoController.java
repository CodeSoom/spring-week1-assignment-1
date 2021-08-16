package com.codesoom.assignment;

import java.util.List;
import java.util.logging.Logger;

import static java.net.HttpURLConnection.*;

public class TodoController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final TodoRepository todoRepository;

    public TodoController() {
        todoRepository = TodoRepository.getInstance();
    }

    public Response findTasks() {
        List<Task> tasks = todoRepository.findAll();

        return Response.of(HTTP_OK, tasks);
    }

    public Response findOne(Long id) {
        try {
            final Task findTask = todoRepository.findById(id).orElseThrow(NotFoundEntityException::new);
            return Response.of(HTTP_OK, findTask);
        } catch (NotFoundEntityException ne) {
            return Response.from(HTTP_NOT_FOUND);
        }
    }

    public Response saveTask(Task task) {
        final Task savedTask = todoRepository.save(task);

        return Response.of(HTTP_CREATED, savedTask);
    }

    public Response updateTask(Long id, Task task) {
        try{
            final Task findTask = todoRepository.findById(id).orElseThrow(NotFoundEntityException::new);
            findTask.updateTitle(task.getTitle());

            return Response.of(HTTP_OK, findTask);
        }catch(NotFoundEntityException ne){
            logger.severe(ne.getMessage());
            return Response.from(HTTP_NOT_FOUND);
        }
    }

    public Response deleteTask(Long id) {
        try{
            todoRepository.deleteById(id);
            return Response.from(HTTP_NO_CONTENT);
        }catch(NotFoundEntityException ne){
            logger.severe(ne.getMessage());
            return Response.from(HTTP_NOT_FOUND);
        }
    }

}
