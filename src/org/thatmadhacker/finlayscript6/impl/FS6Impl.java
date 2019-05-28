package org.thatmadhacker.finlayscript6.impl;

import java.io.File;
import java.util.List;

import org.thatmadhacker.finlayscript6.core.Interpreter;
import org.thatmadhacker.finlayscript6.core.Program;
import org.thatmadhacker.finlayscript6.core.ProgramResult;

public class FS6Impl {
	public static void main(String[] args) throws Exception{
		List<String> lines = Interpreter.loadProgram(new File("test.fscript"));
		Program program = Interpreter.createProgram(lines);
		Interpreter.setupProgram(program);
		ProgramResult result = Interpreter.execProgram(program);
		System.out.println("Local vars: \n\n");
		for(String s : result.getLocalVariables().keySet()) {
			System.out.println("Name: "+s+" Type: "+result.getLocalVariables().get(s).getType()+" - "+
					program.getEnv().getTypeManager().getTypes().get(result.getLocalVariables().get(s).getType())+
					" Value: "+result.getLocalVariables().get(s).getValue().toString());
		}
		System.out.println("\n\nGlobal vars: \n\n");
		for(String s : result.getGlobalVariables().keySet()) {
			System.out.println("Name: "+s+" Type: "+result.getGlobalVariables().get(s).getType()+" - "+
					program.getEnv().getTypeManager().getTypes().get(result.getGlobalVariables().get(s).getType())+
					" Value: "+result.getGlobalVariables().get(s).getValue().toString());
		}
	}
}
