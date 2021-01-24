package com.codesoom.assignment.web;

public class Mapping {
    ExpectRequestPattern pattern;
    HttpController controller;

    public Mapping(ExpectRequestPattern pattern, HttpController controller) {
        this.controller = controller;
        this.pattern = pattern;
    }
}
