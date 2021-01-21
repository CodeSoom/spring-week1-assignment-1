package com.codesoom.assignment.models;

import com.codesoom.assignment.service.TaskService;

import static com.codesoom.assignment.models.TasksHttpRequest.TASKS;

public enum TasksHttpRequestMethod implements HttpRequestProcessor {
    GET {
        @Override
        public HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService) {
            String path = httpRequest.getPath();
            if (path.equals(TASKS)) {
                return new HttpResponse(HttpStatus.OK, taskService.getTasks());
            }

            Long id = getIdFromPath(path);
            String content = taskService.getTask(id);

            if (content.isEmpty()) {
                return new HttpResponse(HttpStatus.NOT_FOUND);
            }

            return new HttpResponse(HttpStatus.OK, content);
        }
    },
    POST {
        @Override
        public HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService) {
            return new HttpResponse(HttpStatus.CREATED, taskService.addTask(httpRequest.getBody()));
        }
    },
    PATCH {
        @Override
        public HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService) {
            Long id = getIdFromPath(httpRequest.getPath());

            if (taskService.getTask(id).isEmpty()) {
                return new HttpResponse(HttpStatus.NOT_FOUND);
            }

            String content = taskService.updateTask(id, httpRequest.getBody());
            return new HttpResponse(HttpStatus.OK, content);
        }
    },
    PUT {
        @Override
        public HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService) {
            Long id = getIdFromPath(httpRequest.getPath());

            if (taskService.getTask(id).isEmpty()) {
                return new HttpResponse(HttpStatus.NOT_FOUND);
            }

            String content = taskService.updateTask(id, httpRequest.getBody());
            return new HttpResponse(HttpStatus.OK, content);
        }
    },
    DELETE {
        @Override
        public HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService) {
            Long id = getIdFromPath(httpRequest.getPath());

            if (taskService.getTask(id).isEmpty()) {
                return new HttpResponse(HttpStatus.NOT_FOUND);
            }

            taskService.deleteTask(id);
            return new HttpResponse(HttpStatus.NO_CONTENT);
        }
    };

    private static Long getIdFromPath(String path) {
        Long id = Long.valueOf(path.replace(TASKS + "/", ""));
        return id;
    }

}
