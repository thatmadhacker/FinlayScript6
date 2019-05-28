package org.thatmadhacker.finlayscript6.core;

import java.util.Map;

public class ProgramResult {
	private int resultCode;
	private Map<String,FS6Object> globalVariables;
	private Map<String,FS6Object> localVariables;
	private FS6Object returnVal;
	public int getResultCode() {
		return resultCode;
	}
	public Map<String, FS6Object> getGlobalVariables() {
		return globalVariables;
	}

	public Map<String, FS6Object> getLocalVariables() {
		return localVariables;
	}
	public ProgramResult(int resultCode, Map<String, FS6Object> globalVariables, Map<String,FS6Object> localVariables, FS6Object returnVal) {
		super();
		this.resultCode = resultCode;
		this.globalVariables = globalVariables;
		this.localVariables = localVariables;
		this.returnVal = returnVal;
	}
	public FS6Object getReturnVal() {
		return returnVal;
	}
}
