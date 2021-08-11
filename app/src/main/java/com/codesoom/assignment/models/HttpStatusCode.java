package com.codesoom.assignment.models;

public enum HttpStatusCode {
    Success(200), Created(201), NoContent(204), Forbidden(403),NotFound(404), InternalServerError(500);

    private int num;

    HttpStatusCode(int num){
        this.num = num;
    }

    public static HttpStatusCode valueOf(int num) {
        switch (num) {
            case 200 :
                return Success;
            case 201:
                return Created;
            case 403 :
                return Forbidden;
            case 404 :
                return NotFound;
            case 500:
                return InternalServerError;
            default :
                return null;
        }
    }

    public int getStatusCode(){
        return this.num;
    }
}
