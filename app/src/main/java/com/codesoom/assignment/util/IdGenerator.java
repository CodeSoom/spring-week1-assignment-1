package com.codesoom.assignment.util;

import java.util.HashMap;

public class IdGenerator {

	public static HashMap<IdType, Integer> idMap = new HashMap<>();

	public static int genId(IdType idType) {
		idMap.put(idType, idMap.computeIfAbsent(idType, key -> 0) + 1);
		return getMaxId(idType);
	}

	public static int getMaxId(IdType idType) {
		return idMap.get(idType);
	}
	public enum IdType {
		TASK
	}

}
