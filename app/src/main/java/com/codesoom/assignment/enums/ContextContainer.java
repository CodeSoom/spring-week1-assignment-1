package com.codesoom.assignment.enums;

import com.codesoom.assignment.models.Path;

public enum ContextContainer {

    TASKS("/tasks"),
    NOT_EXIST(""),
    ;

    private String context;

    public String getContextValue() {
        return context;
    }

    ContextContainer(String context) {
        this.context = context;
    }

    public static ContextContainer getContext(Path path) {
        String exceptPathVariable = Path.getPathWithRemovedLastPath(path);

        for (ContextContainer context : ContextContainer.values()) {
            if (context.getContextValue().equals(path.getPath())) {
                return context;
            }

            if (context.getContextValue().equals(exceptPathVariable)) { // (1)
                Path.setPathVariable(path); // (2)
                return context;
            }
        }

        return NOT_EXIST;
    }
    
/*      (1) 해당 조건이 만족한다는 것은 제일 끝 path가 pathVariable

        해당 로직 설계 이유
         만약 해당 경로들이 들어온다면..?
         - /tasks/2
         - /tasks/giibeom

         현재 context 빈에 등록되어 있는 경로들 (예시)
            /tasks              -> 얘를 반환해야됨
            /tasks/test
            /user/tasks/test

        맨 마지막 패스를 PathVariable이라고 가정하였을 때,
        그걸 빼고 일치하는 경로가 있으면 해당 context를 반환하도록 하였습니다
        또한 마지막 패스가 PathVariable이 맞다는 소리이므로 PathVariable 세팅도 진행하였습니다.


        (2)
        call by value이지만 같은 참조값을 바라보기 때문에, 값이 정상적으로 바뀔 것이라고 생각합니다.
*/
}
