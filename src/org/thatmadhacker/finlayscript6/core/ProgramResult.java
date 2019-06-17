package org.thatmadhacker.finlayscript6.core;

import java.util.Map;

public class ProgramResult {
	public int resultCode;
	public Map<String,FS6Object> globalVariables;
	public Map<String,FS6Object> localVariables;
	public FS6Object returnVal;
	public ProgramResult(int resultCode, Map<String, FS6Object> globalVariables, Map<String,FS6Object> localVariables, FS6Object returnVal) {
		super();
		this.resultCode = resultCode;
		this.globalVariables = globalVariables;
		this.localVariables = localVariables;
		this.returnVal = returnVal;
	}
}
