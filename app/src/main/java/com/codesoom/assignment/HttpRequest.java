package com.codesoom.assignment;

public class HttpRequest {
    private final String method;
    private final String path;

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public boolean isGetAllTasks(){
        return HttpMethod.GET.getMethod().equals(method) && isTasksPath();
    }

    public boolean isDeleteTask() {
        return HttpMethod.DELETE.getMethod().equals(method) && isTasksPathWithId();
    }

    public boolean isUpdateTask() {
        return HttpMethod.PUT.getMethod().equals(method) || HttpMethod.PATCH.equals(method) && isTasksPathWithId();
    }

    public boolean isCreateTask() {
        return HttpMethod.POST.getMethod().equals(method) && isTasksPath();
    }

    public boolean isGetOneTask() {
        return HttpMethod.GET.getMethod().equals(method) && isTasksPathWithId();
    }

    private boolean isTasksPath() {
        if("/tasks".equals(path)){
            return true;
        }
        return false;
    }

    private boolean isTasksPathWithId() {
        String id = checkPathGetId(this.path);

        if(("/tasks/"+id).equals(path)){
            return true;
        }
        return false;
    }

    private String checkPathGetId(String path) {
        if (path.indexOf("/tasks/") == 0) {
            return this.path.substring(7);
        }
        return "";
    }

}
