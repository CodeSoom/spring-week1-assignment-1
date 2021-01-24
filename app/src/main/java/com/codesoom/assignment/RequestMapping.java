package com.codesoom.assignment;

import com.codesoom.assignment.models.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

    RequestMethod method() default RequestMethod.GET;

    String path();

}
