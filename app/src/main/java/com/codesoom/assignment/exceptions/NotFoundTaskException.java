package com.codesoom.assignment.exceptions;

/**
 * 할 일을 찾을 수 없는 경우에 던집니다.
 */
public class NotFoundTaskException extends RuntimeException{

    // 주어진 Id에 해당하는 할 일이 목록에 없을 경우 발생하는 예외입니다.
    public NotFoundTaskException(Long taskId) {
        super("Not Found Task" + taskId);
    }

}
