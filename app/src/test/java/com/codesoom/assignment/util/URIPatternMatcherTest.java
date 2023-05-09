package com.codesoom.assignment.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URIPatternMatcherTest {

	@Test
	public void isRootPathRequest() {
		Assertions.assertEquals(true, URIPatternMatcher.requestRoot("/tasks"));
		Assertions.assertEquals(true, URIPatternMatcher.requestRoot("/tasks/"));
		Assertions.assertEquals(false, URIPatternMatcher.requestRoot("/tasks/1"));
	}

	@Test
	public void taaskPathParsingTest() {
		String path = "/tasks/2";

		String rootPath = URIPatternMatcher.getRootPath(path);

		Assertions.assertEquals("tasks", rootPath);
	}

	@Test
	public void pathParsingResourceIdTest() {
		String path = "/tasks/2";

		String resourceId = URIPatternMatcher.getBasicResourceId(path);

		Assertions.assertEquals("2", resourceId);
	}

}