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
