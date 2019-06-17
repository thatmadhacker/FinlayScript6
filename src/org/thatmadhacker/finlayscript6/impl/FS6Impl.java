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
		Interpreter.close(program);
		System.out.println("Local vars: \n\n");
		for(String s : result.localVariables.keySet()) {
			System.out.println("Name: "+s+" Type: "+result.localVariables.get(s).type+" - "+
					program.env.typeManager.types.get(result.localVariables.get(s).type)+
					" Value: "+result.localVariables.get(s).value.toString());
		}
		System.out.println("\n\nGlobal vars: \n\n");
		for(String s : result.globalVariables.keySet()) {
			System.out.println("Name: "+s+" Type: "+result.globalVariables.get(s).type+" - "+
					program.env.typeManager.types.get(result.globalVariables.get(s).type)+
					" Value: "+result.globalVariables.get(s).value.toString());
		}
		System.out.println("\n\nLibrary methods: \n\n");
		for(String s : program.env.libMethods.keySet()) {
			System.out.println("Name: "+s+" Module: "+program.env.libMethods.get(s).f.getPath());
		}
	}
}
