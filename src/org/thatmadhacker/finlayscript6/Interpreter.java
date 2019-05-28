package org.thatmadhacker.finlayscript6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
	
	public static Program createProgram(List<String> lines) throws Exception {
		return new Program(lines,Environment.createDefaultEnv());
	}
	public static Program createProgram(List<String> lines, Environment env) throws Exception {
		return new Program(lines,env);
	}
	public static ProgramResult execProgram(Program p) throws Exception{
		return execMethod("", p);
	}
	public static ProgramResult execMethod(String method, Program p) throws Exception{
		
		for(Method m : p.getMethods()) {
			if(m.getName().equals(method)) {
				return execMethod(m,p);
			}
		}
		throw new Exception("Method not found!");
	}
	private static ProgramResult execMethod(Method method, Program program) {
		program.setReturned(false);
		Map<String,FS6Object> localVars = program.getLocalVars();
		program.setLocalVars(new HashMap<String,FS6Object>());
		for(int i = 0; i < method.getLines().size(); i++) {
			i += execLine(method.getLines().get(i),i,program,method);
			if(program.isReturned()) {
				break;
			}
		}
		ProgramResult result = new ProgramResult(program.getReturnVal(),program.getGlobalVars(),program.getLocalVars());
		program.setLocalVars(localVars);
		return result;
	}
	private static int execLine(String line, int index, Program program, Method method) {
		if(line.contains("=")) {
			String variable = line.split("=",2)[0].trim();
			boolean global = false;
			if(variable.split(" ",2).length > 1) {
				global = true;
			}
			String eval = line.split("=",2)[1].trim();
			FS6Object obj = eval(eval);
			if(!global) {
				program.getLocalVars().put(variable, obj);
			}else {
				program.getGlobalVars().put(variable, obj);
			}
			return obj.getSkip();
		}else {
			FS6Object obj = eval(line);
			return obj.getSkip();
		}
	}
	private static FS6Object eval(String eval) {
		
		String methodName = eval.split("\\(",2)[0].trim();
		String methodArgs = eval.split("\\(",2)[1].trim();
		methodArgs = methodArgs.substring(0, methodArgs.lastIndexOf("\\)"));
		
		if(methodName.equals("if")) {
			
		}else if(methodName.equals("for")) {
			
		}else if(methodName.equals("while")) {
			
		}
		
	}
	private static Map<String,FS6Object> parseArgs(String args, String methodArgs){
		Map returnVal = new HashMap<String,FS6Object>();
		
		
		
	}
	public static void setupProgram(Program p) throws Exception{
		p.getMethods().clear();
		List<String> nonMethodLines = new ArrayList<String>();
		int count = 0;
		for(int i = 0; i < p.getLines().size(); i++) {
			String line = p.getLines().get(i);
			if(count == 0 && line.contains("{")) {
				String name = line.split("\\(",2)[0].trim();
				String args = line.split("\\(",2)[1].trim();
				args = args.substring(0, args.lastIndexOf(")"));
				count = adjustCount(line,count);
				int endLine = -1;
				for(int i1 = i+1; i1 < p.getLines().size(); i1++) {
					count = adjustCount(p.getLines().get(i1), count);
					if(count == 0) {
						endLine = i1;
						break;
					}
				}
				if(endLine == -1) {
					throw new Exception("Closing bracket not found for method declaration on line "+(i+1));
				}
				List<String> lines = new ArrayList<String>();
				for(int i1 = i+1; i1 < endLine; i1++) {
					lines.add(p.getLines().get(i1));
				}
				Method method = new Method(lines,name,args);
				p.getMethods().add(method);
				continue;
			}else if(count == 0) {
				nonMethodLines.add(line);
			}
			count = adjustCount(line,count);
		}
		Method main = new Method(nonMethodLines,"","");
		p.getMethods().add(main);
	}
	private static int adjustCount(String line, int count) {
		int diff = 0;
		for(String s : line.split("")) {
			if(s.equals("{")) {
				diff++;
			}else if(s.equals("}")) {
				diff--;
			}
		}
		return count + diff;
	}
}