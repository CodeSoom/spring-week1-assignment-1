### Q. Map과 Hashmap의 차이

### ObjectMapper 관련내용 다시 수강

### ConnectionError 발생 이유

- title을 key로 받는데, Model에서 todo를 계속 보내고 있었다(key값으로)

### what is printstacktrace()?

### todostoJSON은 왜 IOException 처리를 꼭 해주는가.

```java
 // 여기서 private Long newId 처럼 선언만 하고 정의가 안돼있으면, 아무리 genereateId 해봤자 도루묵
private Long newId = 0L;


// Body 를 얻는 과정을 하나의 메서드로 만들자. 글고 바디는 post에서만 필요하니까 공통으로 뺄 필요가 없다.
InputStream inputStream=exchange.getRequestBody();
String body=new BufferedReader(new InputStreamReader(inputStream))
        .lines()
        .collect(Collectors.joining("\n"));


// .get() => .orElse(null)
private Task findTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
        .findFirst().orElse(null);
}


// (마지막 줄)tasks 넣으면 당연히 정확한 id의 body 못찾고, 전체 다 찾아옴
private void createTask(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);
        Task task = bodyToTask(body);
        task.setId(generateId());
        tasks.add(task);
        send(exchange, 201, tasksToJSON(task));
}


private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
        .lines() // 여러 줄이 나오는 것을
        .collect(Collectors.joining("\n")); // 제대로 줄 넘김 해서 받음
}


// path의 id값 분리
Long getId(String path) {
        String[] splitPath = path.split("/");
        return Long.parseLong(splitPath[2]);
        }
```