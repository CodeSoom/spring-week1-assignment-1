package com.codesoom.assignment.exceptions;

/**
 * 주어진 Id에 해당하는 할 일이 목록에 없을 경우 발생하는 예외입니다.
 *
 * @author DevRunner21
 * @version 1.0
 * @since 2021.05.07
 */
public class NotFoundTaskException extends RuntimeException{

    public NotFoundTaskException(Long taskId) {
        super("Not Found Task" + taskId);
    }

}
