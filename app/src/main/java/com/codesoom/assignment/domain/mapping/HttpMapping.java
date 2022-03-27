package com.codesoom.assignment.domain.mapping;


import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.response.HttpResponse;


public interface HttpMapping {

    HttpResponse process(HttpRequest httpRequest);
}
