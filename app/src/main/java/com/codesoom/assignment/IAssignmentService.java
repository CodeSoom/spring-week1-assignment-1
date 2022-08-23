package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName IAssignmentService
 * @Description TODO에 대한 로직이 구현이 되어 있는 클래스이다.
 */

public interface IAssignmentService {

    Optional<Task> create(String content) throws JsonProcessingException;

    List<Task> getAll();
}
