package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

public class RequestPathTest {

	@Test
	public void taaskPathParsingTest() {
		String pattern = "/tasks/(\\d+)/?";
		String path = "/tasks/2";

		System.out.println(path.matches(pattern));
		String str1 = path.replaceAll(pattern, "$1");
		System.out.println(str1);
	}

	@Test
	public void pathParsingTest() {
		String pattern = "/(\\D+)/(\\d+)/?";
		String path = "/tasks/2";

		System.out.println(path.matches(pattern));
		String str1 = path.replaceAll(pattern, "$1");
		String str2 = path.replaceAll(pattern, "$2");
		System.out.println(str1 + " / " + str2);
	}
}
