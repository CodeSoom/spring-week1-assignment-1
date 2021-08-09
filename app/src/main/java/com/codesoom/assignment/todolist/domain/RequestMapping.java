package com.codesoom.assignment.todolist.domain;

import com.codesoom.assignment.todolist.util.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
    String value() default "";
    HttpMethod method() default HttpMethod.GET;
}
