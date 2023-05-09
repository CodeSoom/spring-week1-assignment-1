package com.codesoom.assignment.util;

public class URIPatternMatcher {

	private final static String ROOT_PATH_PATTERN = "/(\\D+)/?";
	private final static String BASIC_RESOURCE_PATTERN = "/(\\D+)/(\\d+)/?";

	public static boolean requestRoot(String path) {
		return path.matches(ROOT_PATH_PATTERN);
	}

	public static String getRootPath(String path) {
		return path.replaceAll(BASIC_RESOURCE_PATTERN, "$1");
	}

	public static String getBasicResourceId(String path) {
		return path.replaceAll(BASIC_RESOURCE_PATTERN, "$2");
	}
}
