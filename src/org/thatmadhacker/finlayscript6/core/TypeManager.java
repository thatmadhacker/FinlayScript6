package org.thatmadhacker.finlayscript6.core;

import java.util.HashMap;
import java.util.Map;

public class TypeManager {
	public static final int TYPE_NONE = 0;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_INTEGER = 2;
	public static final int TYPE_BOOLEAN = 3;
	private Map<Integer,String> types = new HashMap<Integer,String>();
	private int curr = 4;
	public TypeManager() {
		types.put(0, "NONE");
		types.put(1, "STRING");
		types.put(2, "INT");
		types.put(3, "BOOL");
	}
	public Map<Integer, String> getTypes() {
		return types;
	}
	/*
	 * Dynamically allocate variable types
	 */
	public int allocType(String name) {
		types.put(curr, name);
		curr++;
		return curr-1;
	}
}