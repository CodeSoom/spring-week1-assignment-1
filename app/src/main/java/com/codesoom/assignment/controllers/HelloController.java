package com.codesoom.assignment.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 그냥 @Controller 대신,
@RequestMapping("/hello") // dispatcherServlet을 연결해주기 위해 사용하는 어노테이션(주소가 아무것도 없을때, 슬래시일때 아래를 연결해줘라~!) (어노테이션은 sayHello()메서드 바로 위에 붙여도 된다.)
public class HelloController {
    @RequestMapping("/say")
    public String sayHello(){
        return "hello world!";
    }
}
