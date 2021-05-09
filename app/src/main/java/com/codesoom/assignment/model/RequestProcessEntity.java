package com.codesoom.assignment.model;

public class RequestProcessEntity {
    private String method;
    private String body;
    private String taskId;

    public RequestProcessEntity(RequestProcessEntityBuilder requestProcessEntityBuilder){
        this.method = requestProcessEntityBuilder.method;
        this.body = requestProcessEntityBuilder.body;
        this.taskId = requestProcessEntityBuilder.taskId;
    }
    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isBodyBlank(){
        if(body == null){
            return false;
        }
        return body.isBlank();
    }

    public static class  RequestProcessEntityBuilder{
        private String method;
        private String body;
        private String taskId;

        public RequestProcessEntityBuilder setMethod(String method){
            this.method = method;
            return this;
        }

        public RequestProcessEntityBuilder setBody(String body){
            this.body=body;
            return this;
        }

        public RequestProcessEntityBuilder setTaskId(String taskId){
            this.taskId = taskId;
            return this;
        }


        public RequestProcessEntity build(){
            return new RequestProcessEntity(this);
        }
    }
}
