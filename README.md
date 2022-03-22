# ToDo REST API 만들기

## 과제 목표

Java를 이용해서 ToDo REST API를 만들어봅니다.

![API](https://user-images.githubusercontent.com/14071105/103476206-0456f280-4df7-11eb-89c4-d61845ef45ec.png)

- ToDo 목록 얻기 - `GET /tasks`
- ToDo 상세 조회하기 - `GET /tasks/{id}`
- ToDo 생성하기 - `POST /tasks`
- ToDo 제목 수정하기 - `PUT/PATCH /tasks/{id}`
- ToDo 삭제하기 - `DELETE /tasks/{id}`

## 요구 사항

- 모든 API 테스트를 통과해야 합니다.

## 실행하기

```bash
./gradlew run
```

## 테스트

### 설치하기

```bash
$ cd tests
$ npm install
```

### 테스트 실행하기

테스트는 실제로 동작하는 서버에 테스트하므로 서버가 동작하고 있는 상태여야 올바르게 동작합니다.

```bash
$ npm run test
```

## 작업목록 및 순서
1. 초기 세팅
   1. Http server 만들기 : no connection -> 404 -> 500 넘어가는 흐름 파악
   2. handler 생성
   3. header, body response & log 세팅
   4. models package 생성 > Task model 생성
      1. id
      2. title 
   5. Jackson lib dependency 
   6. task to json / json to task 공통 매소드 만들기
2. ToDo 목록 얻기 & 테스트
   1. 경로 : GET /tasks
   2. 기능 : tasks 목록 응답
3. ToDo 상세 조회하기 & 테스트
   1. 경로 : GET /tasks/{id}
   2. 기능 : path 의 id에 해당하는 task 내용 응답 
   3. 예외처리
      1. id 가 0 이하일때 오류메시지 응답
4. ToDo 생성하기 & 테스트
   1. 경로 : POST /tasks
   2. 기능 : 마지막 id + 1에 해당하는 신규 task 추가
   3. 예외처리
      1. task title 형식이 안맞을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨
5. ToDo 제목 수정하기 & 테스트
   1. 경로 : PUT/PATCH /tasks/{id}
   2. 기능 : id에 맞는 task 찾아서 제목 수정하기
   3. 예외처리
      1. id 에 맞는 task 가 존재하지 않을 때 오류 응답
      2. 수정하려는 값이 적절하지 않을 때 오류 응답 : 문자가 아니거나 비어있으면 안됨ㄴ
6. ToDo 삭제하기 & 테스트
   1. 경로 : DELETE /tasks/{id}
   2. 기능 : id에 맞는 값을 찾아서 삭제한다. 
   3. 예외처리
      1. id 에 맞는 task 가 존재하지 않을 때 오류 응답
7. tests > 전체 테스트