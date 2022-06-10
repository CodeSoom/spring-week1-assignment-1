package com.codesoom.assignment.todo.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestValidation {
  private static boolean isValid;
  private static String resultMsg;

  private static int responseStatus;
  private static final String SELECT_ONE_TASK_PATH_FORMAT = "^\\/tasks\\/\\d+\\/?$";
  private static final String SELECT_WHOLE_TASK_PATH_FORMAT = "^\\/tasks\\/?$";
  private static final ObjectMapper objectMapper = new ObjectMapper();
  public RequestValidation() {}

  public RequestValidation(boolean isValid, String resultMsg) {
    this.isValid = isValid;
    this.resultMsg = resultMsg;
  }

  public RequestValidation(boolean isValid, String resultMsg, int responseStatus) {
    this.isValid = isValid;
    this.resultMsg = resultMsg;
    this.responseStatus = responseStatus;
  }

  public RequestValidation validationCheck(
      String sRequestMethod, String sRequestPath, String sRequestBody, String sRequestQuery) {

    boolean isRequestBodyExist = !isItHasNoContent(sRequestBody);
    boolean isRequestQueryExist = !isItHasNoContent(sRequestQuery);

    // query 존재 허용하지 않음
    if(isRequestQueryExist) return new RequestValidation(false, "URI 에 Query 는 허용되지 않습니다");

    // path format 정합성 체크
    RequestValidation pathValidation = isPathFormatCorrect(sRequestMethod, sRequestPath);
    if(!pathValidation.getIsValid()) return pathValidation;

    switch (sRequestMethod) {
      case "GET", "DELETE" -> {
        if (isRequestBodyExist)
          return new RequestValidation(false, "GET Method 로 접근할 때는 URI 에 Body 를 허용하지 않습니다");
     }
      case "POST", "PUT", "PATCH"-> {
        if (isRequestBodyExist) {
          RequestValidation convertToTaskValidation = isItPossibleToConvertToTask(sRequestBody);
          if(!convertToTaskValidation.getIsValid()) return convertToTaskValidation;
        } else {
          return new RequestValidation(false, "저장할 데이터가 존재하지 않습니다");
        }
      }
      default -> {
        return new RequestValidation(false, "허용되지 않는 HTTP Method 입니다");
      }
    }
    return new RequestValidation(true, "Validation Check Passed");
  }

  public boolean getIsValid() {
    return this.isValid;
  }

  public String getResultMsg() {
    return this.resultMsg;
  }

  public boolean isItHasNoContent(String content){
    return content == null || content.isBlank();
  }

  public RequestValidation isPathFormatCorrect(String requestMethod, String requestPath){
    boolean isPathFormatSelectOneTask = requestPath.matches(SELECT_ONE_TASK_PATH_FORMAT);
    boolean isPathFormatSelectWholeTask = requestPath.matches(SELECT_WHOLE_TASK_PATH_FORMAT);

    boolean isPathFormatCorrect;

    switch (requestMethod) {
      case "GET" -> {
        isPathFormatCorrect = isPathFormatSelectOneTask || isPathFormatSelectWholeTask;
      }
      case "POST" -> {
        isPathFormatCorrect = isPathFormatSelectWholeTask;
      }
      case "PUT", "PATCH","DELETE" -> {
        isPathFormatCorrect = isPathFormatSelectOneTask;
      }
      default -> {
        return new RequestValidation(false, "허용되지 않는 HTTP Method 입니다");
      }
    }

    String pathValidMsg = isPathFormatCorrect ? "CORRECT" : "비 정상적인 경로입니다.";

    return new RequestValidation(isPathFormatCorrect, pathValidMsg);

  }
  public RequestValidation isItPossibleToConvertToTask(String content){

    try {
      Task changedTask;
      changedTask = objectMapper.readValue(content, Task.class);

      String parsedTaskId = String.valueOf(changedTask.getId());

      if (isItHasNoContent(parsedTaskId) || "null".equals(parsedTaskId)) {
        return new RequestValidation(true, "contentBody 가 Task Class 타입으로 정상적으로 변환됨");
      } else {
        return new RequestValidation(false, "taskId 값을 지정하지 마십시오");
      }
    } catch (Exception e) {
      return new RequestValidation(false, "contentBody 는 Task Class 타입으로 변환할 수 없습니다");
    }

  }
}

