package org.thatmadhacker.finlayscript6.impl;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.thatmadhacker.finlayscript6.core.Interpreter;
import org.thatmadhacker.finlayscript6.core.Program;
import org.thatmadhacker.finlayscript6.core.ProgramResult;

public class FS6Impl {
	public static void main(String[] args) throws Exception {
		boolean verbose = false;
		String file = null;
		for (String s : args) {
			if (s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("--help")) {
				System.out.println("FinlayScript6 Help: \n" + "Usage: finlayscript6 [-v] <file>\n"
						+ "-v | --verbose - Enables verbose output from the FS6 interpreter\n");
				System.exit(0);
			} else if (s.equalsIgnoreCase("-v") || s.equalsIgnoreCase("--verbose")) {
				verbose = true;
			} else {
				file = s;
			}
		}
		Scanner in = null;
		if (System.console() == null) {
			in = new Scanner(System.in);
			while (true) {
				System.out.print("> ");
				String s = in.nextLine();
				if (s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("--help")) {
					System.out.println("FinlayScript6 Help: \n" + "Usage: finlayscript6 [-v] <file>\n"
							+ "-v | --verbose - Enables verbose output from the FS6 interpreter\n");
					System.exit(0);
				} else if (s.equalsIgnoreCase("-v") || s.equalsIgnoreCase("--verbose")) {
					verbose = true;
				} else {
					file = s;
					break;
				}
			}
		}
		List<String> lines = Interpreter.loadProgram(new File(file));
		Program program = Interpreter.createProgram(lines);
		Interpreter.setupProgram(program);
		ProgramResult result = Interpreter.execProgram(program);
		Interpreter.close(program);
		if (verbose) {
			System.out.println("Local vars: \n\n");
			for (String s : result.localVariables.keySet()) {
				Object value = result.localVariables.get(s).value;
				String val = "null";
				if(value != null)
					val = value.toString();
				System.out.println("Name: " + s + " Type: " + result.localVariables.get(s).type + " - "
						+ program.env.typeManager.types.get(result.localVariables.get(s).type) + " Value: "
						+ val);
			}
			System.out.println("\n\nGlobal vars: \n\n");
			for (String s : result.globalVariables.keySet()) {
				Object value = result.globalVariables.get(s).value;
				String val = "null";
				if(value != null)
					val = value.toString();
				System.out.println("Name: " + s + " Type: " + result.globalVariables.get(s).type + " - "
						+ program.env.typeManager.types.get(result.globalVariables.get(s).type) + " Value: "
						+ val);
			}
			System.out.println("\n\nLibrary methods: \n\n");
			for (String s : program.env.libMethods.keySet()) {
				System.out.println("Name: " + s + " Module: " + program.env.libMethods.get(s).f.getPath());
			}
		}
		if(in != null) {
			in.close();
		}
	}
}
