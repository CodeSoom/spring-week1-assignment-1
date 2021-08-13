package com.codesoom.assignment;

public class HttpRequest {
    final String method;
    final String path;

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public boolean isGetAllTasks(){
        return HttpMethod.GET.getMethod().equals(method) && isTasksPath(path);
    }

    public boolean isDeleteTask(String method, String path) {
        return HttpMethod.DELETE.getMethod().equals(method) && isTasksPathWithId(path);
    }

    public boolean isUpdateTask(String method, String path) {
        return HttpMethod.PUT.getMethod().equals(method) || HttpMethod.PATCH.equals(method) && isTasksPathWithId(path);
    }

    public boolean isCreateTask(String method, String path) {
        return HttpMethod.POST.getMethod().equals(method) && isTasksPath(path);
    }

    public boolean isGetOneTask(String method, String path) {
        return HttpMethod.GET.getMethod().equals(method) && isTasksPathWithId(path);
    }

    private boolean isTasksPath(String path) {
        if("/tasks".equals(path)){
            return true;
        }
        return false;
    }

    private boolean isTasksPathWithId(String path) {
        String id = checkPathGetId(path);

        if(("/tasks/"+id).equals(path)){
            return true;
        }
        return false;
    }

    private String checkPathGetId(String path) {
        if (path.indexOf("/tasks/") == 0) {
            return path.substring(7);
        }
        return "";
    }

}
