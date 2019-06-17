package org.thatmadhacker.finlayscript6.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
	
	public Map<String,FS6Object> globalVars = new HashMap<String,FS6Object>();
	public int returnVal = 0;
	public Map<String,FS6Object> localVars = new HashMap<String,FS6Object>();
	public List<Method> methods = new ArrayList<Method>();
	public List<String> lines;
	public Environment env;
	public FS6Object currReturnVal;
	public boolean returned = false;
	public Program(List<String> lines, Environment env) throws Exception {
		super();
		this.lines = lines;
		this.env = env;
	}
	public Method getMethod(String name) throws Exception{
		for(Method m : methods) {
			if(m.name.equals(name)) {
				return m;
			}
		}
		throw new Exception("Method "+name+" not found!");
	}
	public boolean containsMethod(String name) {
		for(Method m : methods) {
			if(m.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
