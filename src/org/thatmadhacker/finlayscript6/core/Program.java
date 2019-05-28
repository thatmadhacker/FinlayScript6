package org.thatmadhacker.finlayscript6.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
	
	private Map<String,FS6Object> globalVars = new HashMap<String,FS6Object>();
	private int returnVal = 0;
	private Map<String,FS6Object> localVars = new HashMap<String,FS6Object>();
	private List<Method> methods = new ArrayList<Method>();
	private List<String> lines;
	private Environment env;
	private FS6Object currReturnVal;
	private boolean returned = false;
	public Program(List<String> lines, Environment env) throws Exception {
		super();
		this.lines = lines;
		this.env = env;
	}
	public Map<String, FS6Object> getGlobalVars() {
		return globalVars;
	}
	public void setGlobalVars(Map<String, FS6Object> globalVars) {
		this.globalVars = globalVars;
	}
	public int getReturnVal() {
		return returnVal;
	}
	public void setReturnVal(int returnVal) {
		this.returnVal = returnVal;
	}
	public Map<String, FS6Object> getLocalVars() {
		return localVars;
	}
	public void setLocalVars(Map<String, FS6Object> localVars) {
		this.localVars = localVars;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	public List<String> getLines() {
		return lines;
	}
	public Environment getEnv() {
		return env;
	}
	public void setEnv(Environment env) {
		this.env = env;
	}
	public boolean isReturned() {
		return returned;
	}
	public void setReturned(boolean returned) {
		this.returned = returned;
	}
	public Method getMethod(String name) throws Exception{
		for(Method m : methods) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		throw new Exception("Method "+name+" not found!");
	}
	public boolean containsMethod(String name) {
		for(Method m : methods) {
			if(m.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	public FS6Object getCurrReturnVal() {
		return currReturnVal;
	}
	public void setCurrReturnVal(FS6Object currReturnVal) {
		this.currReturnVal = currReturnVal;
	}
}
